package chankyin.mentamatics.config;

import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import chankyin.mentamatics.config.range.DoubleRangeConstraint;
import chankyin.mentamatics.config.range.DupletRange;
import chankyin.mentamatics.config.range.QuartetRange;
import chankyin.mentamatics.config.ui.IntegerDoubleRangePreference;
import chankyin.mentamatics.config.ui.IntegerPreference;
import chankyin.mentamatics.config.ui.IntegerRangePreference;
import chankyin.mentamatics.config.ui.LockPreference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ConfigEntryType{
	bool{
		@Override
		public boolean validate(Object value){
			return value instanceof Boolean;
		}

		@Override
		public Object fromString(String string){
			return Boolean.parseBoolean(string);
		}

		@Override
		public String toString(Object value){
			return Boolean.toString((boolean) value);
		}

		@Override
		public Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue){
			CheckBoxPreference preference = new CheckBoxPreference(fragment.getActivity());
			if(prefs.contains(prefKey)){
				preference.setChecked(prefs.getBoolean(prefKey, false));
			}else{
				preference.setChecked((boolean) defaultValue);
			}
			return preference;
		}
	},

	integer{
		@Override
		public boolean validate(Object value){
			return value instanceof Integer;
		}

		@Override
		public Object fromString(String string){
			return Integer.parseInt(string);
		}

		@Override
		public String toString(Object value){
			return Integer.toString((int) value);
		}

		@Override
		public Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue){
			IntegerPreference preference = new IntegerPreference(fragment.getActivity());
			preference.setSigned(args.length > 0 && "signed".equals(args[0]));
			if(prefs.contains(prefKey)){
				preference.setValue(prefs.getInt(prefKey, 0));
			}else{
				preference.setValue((int) defaultValue);
			}
			return preference;
		}
	},

	integerRange{
		@Override
		public boolean validate(Object value){
			return value instanceof int[] && ((int[]) value).length == 2;
		}

		@Override
		public Object fromString(String string){
			String[] parts = string.split(",", 2);
			if(parts.length != 2){
				throw new NumberFormatException();
			}
			return new int[]{
					Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1]),
			};
		}

		@Override
		public String toString(Object value){
			int[] array = (int[]) value;
			return array[0] + "," + array[1];
		}

		@Override
		public Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue){
			IntegerRangePreference pref;
			if(args.length == 2){
				int min = Integer.parseInt(args[0]);
				int max = Integer.parseInt(args[1]);
				pref = new IntegerRangePreference(fragment.getActivity(), min, max);
			}else{
				pref = new IntegerRangePreference(fragment.getActivity());
			}
			if(prefs.contains(prefKey)){
				pref.setValue((int[]) fromString(prefs.getString(prefKey, null)));
			}else{
				pref.setValue((int[]) defaultValue);
			}
			return pref;
		}
	},

	integerDoubleRange{
		private final Pattern pattern = Pattern.compile("([0-9]+),([0-9]+);([0-9]+),([0-9]+)");

		@Override
		public boolean validate(Object value){
			return value instanceof QuartetRange;
		}

		@Override
		public Object fromString(String string){
			Matcher matcher = pattern.matcher(string);
			if(!matcher.matches()){
				throw new NumberFormatException();
			}
			return new QuartetRange(new int[]{
					Integer.parseInt(matcher.group(1)),
					Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)),
					Integer.parseInt(matcher.group(4)),
			});
		}

		@Override
		public String toString(Object value){
			QuartetRange array = (QuartetRange) value;
			return array.toString();
		}

		@Override
		public Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue){
			DupletRange hardLimit = null;
			if(args.length >= 4){
				int min = Integer.parseInt(args[0]);
				int max = Integer.parseInt(args[1]);
				hardLimit = new DupletRange(min, max);
			}
			DoubleRangeConstraint constraint = DoubleRangeConstraint.NONE;
			if(args.length == 1 || args.length == 5){
				String symbol = args[args.length - 1];
				constraint = DoubleRangeConstraint.bySymbol(symbol);
			}
			IntegerDoubleRangePreference pref = new IntegerDoubleRangePreference(fragment.getActivity(), hardLimit, constraint);
			if(prefs.contains(prefKey)){
				pref.setValue((QuartetRange) fromString(prefs.getString(prefKey, null)));
			}else{
				pref.setValue((QuartetRange) defaultValue);
			}
			return pref;
		}
	},

	string{
		@Override
		public boolean validate(Object value){
			return value instanceof String;
		}

		@Override
		public Object fromString(String string){
			return string;
		}

		@Override
		public String toString(Object value){
			return (String) value;
		}

		@Override
		public Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue){
			EditTextPreference pref = new EditTextPreference(fragment.getActivity());
			if(prefs.contains(prefKey)){
				pref.setText(prefs.getString(prefKey, null));
			}else{
				pref.setText((String) defaultValue);
			}
			return pref;
		}
	},

	password{
		@Override
		public boolean validate(Object value){
			return value instanceof Lock;
		}

		@Override
		public Object fromString(String string){
			return Lock.fromString(string);
		}

		@Override
		public String toString(Object value){
			return value.toString();
		}

		@Override
		public Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue){
			return new LockPreference(fragment.getActivity());
		}
	};

	public abstract boolean validate(Object value);

	public abstract Object fromString(String string);

	public abstract String toString(Object value);

	public abstract Preference createPreference(PreferenceFragment fragment, String[] args, SharedPreferences prefs, String prefKey, Object defaultValue);
}
