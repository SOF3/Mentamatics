package chankyin.mentamatics.config;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import chankyin.mentamatics.config.range.DoubleRangeConstraint;
import chankyin.mentamatics.config.range.DupletRange;
import chankyin.mentamatics.config.ui.IntegerDoubleRangePreference;
import chankyin.mentamatics.config.ui.IntegerPreference;
import chankyin.mentamatics.config.ui.IntegerRangePreference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public abstract class ConfigElement{
	@Nullable String id;
	@StringRes int name;
	@Nullable ConfigGroup parent;
	@NonFinal @Getter(lazy = true) private final String fullId = calcFullId();

	public ConfigElement(@Nullable String id, @StringRes int name, @Nullable ConfigGroup parent){
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	@Nullable
	public String calcFullId(){
		if(parent != null){
			return parent.getFullId() + "." + id;
		}else{
			return id;
		}
	}

	public abstract Preference toPreference(PreferenceFragment fragment);

	public enum Type{
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
			public Preference createPreference(PreferenceFragment fragment, String[] args){
				return new CheckBoxPreference(fragment.getActivity());
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
			public Preference createPreference(PreferenceFragment fragment, String[] args){
				IntegerPreference preference = new IntegerPreference(fragment.getActivity());
				preference.setSigned(args.length > 0 && "signed".equals(args[0]));
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
			public Preference createPreference(PreferenceFragment fragment, String[] args){
				if(args.length == 2){
					int min = Integer.parseInt(args[0]);
					int max = Integer.parseInt(args[1]);
					return new IntegerRangePreference(fragment.getActivity(), min, max);
				}
				return new IntegerRangePreference(fragment.getActivity());
			}
		},

		integerDoubleRange{
			private final Pattern pattern = Pattern.compile("([0-9]+),([0-9]+);([0-9]+),([0-9]+)");

			@Override
			public boolean validate(Object value){
				return value instanceof int[] && ((int[]) value).length == 4;
			}

			@Override
			public Object fromString(String string){
				Matcher matcher = pattern.matcher(string);
				if(!matcher.matches()){
					throw new NumberFormatException();
				}
				return new int[]{
						Integer.parseInt(matcher.group(1)),
						Integer.parseInt(matcher.group(2)),
						Integer.parseInt(matcher.group(3)),
						Integer.parseInt(matcher.group(4)),
				};
			}

			@Override
			public String toString(Object value){
				int[] array = (int[]) value;
				return array[0] + "," + array[1] + ";" + array[2] + "," + array[3];
			}

			@Override
			public Preference createPreference(PreferenceFragment fragment, String[] args){
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
				return new IntegerDoubleRangePreference(fragment.getActivity(), hardLimit, constraint);
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
			public Preference createPreference(PreferenceFragment fragment, String[] args){
				return new EditTextPreference(fragment.getActivity());
			}
		};

		public abstract boolean validate(Object value);

		public abstract Object fromString(String string);

		public abstract String toString(Object value);

		public abstract Preference createPreference(PreferenceFragment fragment, String[] args);
	}
}
