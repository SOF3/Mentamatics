package chankyin.mentamatics.config.old;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import chankyin.mentamatics.BuildConfig;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
@Deprecated
public class ConfigEntry{
	public final static Validator DEFAULT_VALIDATOR = new Validator(){
		@Override
		public void validate(Config config, Object value){
		}
	};

	Type type;
	String name;
	Object defaultValue;
	Validator validator;

	public ConfigEntry(final Type type, String name, Object defaultValue){
		this(type, name, defaultValue, DEFAULT_VALIDATOR);
	}

	public Object readPref(SharedPreferences pref){
		if(BuildConfig.DEBUG && !type.validate(defaultValue)){
			throw new AssertionError(defaultValue.getClass().getName());
		}
		return type.fromPref(pref, name, defaultValue);
	}

	public static interface Validator{
		public void validate(Config config, Object value) throws ConfigException;
	}

	enum Type{
		BOOL{
			@Override
			public boolean validate(Object value){
				return value instanceof Boolean;
			}

			@Override
			public Object fromPref(SharedPreferences pref, String name, Object defaultValue){
				return pref.getBoolean(name, (boolean) defaultValue);
			}

			@Override
			public Editor writePref(Editor editor, String key, Object value){
				return editor.putBoolean(key, (boolean) value);
			}
		},

		INTEGER{
			@Override
			public boolean validate(Object value){
				return value instanceof Integer;
			}

			@Override
			public Object fromPref(SharedPreferences pref, String name, Object defaultValue){
				return pref.getInt(name, (int) defaultValue);
			}

			@Override
			public Editor writePref(Editor editor, String key, Object value){
				return editor.putInt(key, (int) value);
			}
		},

		STRING{
			@Override
			public boolean validate(Object value){
				return value instanceof String;
			}

			@Override
			public Object fromPref(SharedPreferences pref, String name, Object defaultValue){
				return pref.getString(name, (String) defaultValue);
			}

			@Override
			public Editor writePref(Editor editor, String key, Object value){
				return editor.putString(key, (String) value);
			}
		};

		public abstract boolean validate(Object value);

		public abstract Object fromPref(SharedPreferences pref, String name, Object defaultValue);

		public abstract Editor writePref(Editor editor, String key, Object value);
	}
}
