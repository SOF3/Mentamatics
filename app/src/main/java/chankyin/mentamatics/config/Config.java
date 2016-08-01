package chankyin.mentamatics.config;

import android.content.Context;
import android.content.SharedPreferences;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.ConfigEntry.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Config{
	public final static String KEY_GUI_VERTICAL_ALIGN = "gui.verticalAlign";

	public final static String KEY_OPT_LOCK_ENABLED = "opt.lockEnabled";
	public final static String KEY_OPT_LOCK_VALUE = "opt.lockValue";

	public final static String KEY_GEN_ADDITION_ENABLED = "gen.addition.enabled";
	public final static String KEY_GEN_ADDITION_CARRY_ALLOWED = "gen.addition.carryAllowed";
	public final static String KEY_GEN_ADDITION_DIGITS_UPPER_MIN = "gen.addition.digits.upper.min";
	public final static String KEY_GEN_ADDITION_DIGITS_UPPER_MAX = "gen.addition.digits.upper.max";
	public final static String KEY_GEN_ADDITION_DIGITS_LOWER_MIN = "gen.addition.digits.lower.min";
	public final static String KEY_GEN_ADDITION_DIGITS_LOWER_MAX = "gen.addition.digits.lower.max";

	public final static String KEY_GEN_SUBTRACTION_ENABLED = "gen.subtraction.enabled";
	public final static String KEY_GEN_SUBTRACTION_BORROW_ALLOWED = "gen.subtraction.borrowAllowed";
	public final static String KEY_GEN_SUBTRACTION_NEGATIVE_ALLOWED = "gen.subtraction.negativeAllowed";
	public final static String KEY_GEN_SUBTRACTION_DIGITS_UPPER_MIN = "gen.subtraction.digits.upper.min";
	public final static String KEY_GEN_SUBTRACTION_DIGITS_UPPER_MAX = "gen.subtraction.digits.upper.max";
	public final static String KEY_GEN_SUBTRACTION_DIGITS_LOWER_MIN = "gen.subtraction.digits.lower.min";
	public final static String KEY_GEN_SUBTRACTION_DIGITS_LOWER_MAX = "gen.subtraction.digits.lower.max";

	public final static String KEY_GEN_MULTIPLICATION_ENABLED = "gen.multiplication.enabled";

	public final static String KEY_GEN_DIVISION_ENABLED = "gen.division.enabled";

	private final static ConfigEntry[] CONFIG_ENTRIES_ARRAY = {
			new ConfigEntry(Type.BOOL, KEY_GUI_VERTICAL_ALIGN, false),

			new ConfigEntry(Type.BOOL, KEY_OPT_LOCK_ENABLED, false),
			new ConfigEntry(Type.STRING, KEY_OPT_LOCK_VALUE, ""),

			new ConfigEntry(Type.BOOL, KEY_GEN_ADDITION_ENABLED, true),
			new ConfigEntry(Type.BOOL, KEY_GEN_ADDITION_CARRY_ALLOWED, true),
			new ConfigEntry(Type.INTEGER, KEY_GEN_ADDITION_DIGITS_UPPER_MIN, 2, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_ADDITION_DIGITS_UPPER_MAX, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_ADDITION_DIGITS_LOWER_MIN, 2, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_ADDITION_DIGITS_LOWER_MAX, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),

			new ConfigEntry(Type.BOOL, KEY_GEN_SUBTRACTION_ENABLED, true),
			new ConfigEntry(Type.BOOL, KEY_GEN_SUBTRACTION_BORROW_ALLOWED, true),
			new ConfigEntry(Type.BOOL, KEY_GEN_SUBTRACTION_NEGATIVE_ALLOWED, false, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_UPPER_MIN, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_UPPER_MAX, 4, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_LOWER_MIN, 2, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_LOWER_MAX, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),

			new ConfigEntry(Type.BOOL, KEY_GEN_MULTIPLICATION_ENABLED, false),

			new ConfigEntry(Type.BOOL, KEY_GEN_DIVISION_ENABLED, false),
	};
	private final static Map<String, ConfigEntry> CONFIG_ENTRIES;

	static{
		CONFIG_ENTRIES = new HashMap<>();
		for(ConfigEntry entry : CONFIG_ENTRIES_ARRAY){
			CONFIG_ENTRIES.put(entry.getName(), entry);
		}
	}

	SharedPreferences pref;
	Map<String, Object> values;

	public Config(Context context){
		pref = context.getSharedPreferences(Main.PREF_SETTINGS, Context.MODE_PRIVATE);
		for(ConfigEntry entry : CONFIG_ENTRIES.values()){
			values.put(entry.getName(), entry.readPref(pref));
		}
	}

	public boolean getBoolean(String key){
		if(BuildConfig.DEBUG){
			ConfigEntry entry = CONFIG_ENTRIES.get(key);
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType() != Type.BOOL){
				throw new AssertionError("Not boolean");
			}
		}
		return (boolean) values.get(key);
	}

	public int getInteger(String key){
		if(BuildConfig.DEBUG){
			ConfigEntry entry = CONFIG_ENTRIES.get(key);
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType() != Type.INTEGER){
				throw new AssertionError("Not integer");
			}
		}
		return (int) values.get(key);
	}

	public String getString(String key){
		if(BuildConfig.DEBUG){
			ConfigEntry entry = CONFIG_ENTRIES.get(key);
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType() != Type.STRING){
				throw new AssertionError("Not string");
			}
		}
		return (String) values.get(key);
	}

	public void setValue(String key, Object value) throws ConfigException{
		ConfigEntry entry = CONFIG_ENTRIES.get(key);
		if(BuildConfig.DEBUG){
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType().validate(value)){
				throw new AssertionError("Incorrect type");
			}
		}

		entry.getValidator().validate(this, value);

		values.put(key, value);

		entry.getType().writePref(pref.edit(), key, value).apply();
	}
}
