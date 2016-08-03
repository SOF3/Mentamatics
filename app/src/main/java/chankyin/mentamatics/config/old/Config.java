package chankyin.mentamatics.config.old;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.xmlpull.v1.XmlPullParser.*;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Deprecated
public final class Config{
	public final static String KEY_GUI_VERTICAL_ALIGN = "gui.verticalAlign";

	public final static String KEY_OPT_LOCK_ENABLED = "opt.lock.enabled";
	public final static String KEY_OPT_LOCK_VALUE = "opt.lock.value";

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
			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GUI_VERTICAL_ALIGN, false),

			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_OPT_LOCK_ENABLED, false),
			new ConfigEntry(ConfigEntry.Type.STRING, KEY_OPT_LOCK_VALUE, ""),

			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_ADDITION_ENABLED, true),
			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_ADDITION_CARRY_ALLOWED, true),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_ADDITION_DIGITS_UPPER_MIN, 2, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_ADDITION_DIGITS_UPPER_MAX, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_ADDITION_DIGITS_LOWER_MIN, 2, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_ADDITION_DIGITS_LOWER_MAX, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),

			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_SUBTRACTION_ENABLED, true),
			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_SUBTRACTION_BORROW_ALLOWED, true),
			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_SUBTRACTION_NEGATIVE_ALLOWED, false, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_UPPER_MIN, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_UPPER_MAX, 4, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_LOWER_MIN, 2, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),
			new ConfigEntry(ConfigEntry.Type.INTEGER, KEY_GEN_SUBTRACTION_DIGITS_LOWER_MAX, 3, new ConfigEntry.Validator(){
				@Override
				public void validate(Config config, Object value) throws ConfigException{
					// TODO
				}
			}),

			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_MULTIPLICATION_ENABLED, false),

			new ConfigEntry(ConfigEntry.Type.BOOL, KEY_GEN_DIVISION_ENABLED, false),
	};
	public final static Map<String, ConfigEntry> CONFIG_ENTRIES;

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

	public boolean is(String key){
		return getBoolean(key);
	}

	public boolean getBoolean(String key){
		if(BuildConfig.DEBUG){
			ConfigEntry entry = CONFIG_ENTRIES.get(key);
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType() != ConfigEntry.Type.BOOL){
				throw new AssertionError("Not boolean");
			}
		}
		return (boolean) values.get(key);
	}

	public int how(String key){
		return getInteger(key);
	}

	public int getInteger(String key){
		if(BuildConfig.DEBUG){
			ConfigEntry entry = CONFIG_ENTRIES.get(key);
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType() != ConfigEntry.Type.INTEGER){
				throw new AssertionError("Not integer");
			}
		}
		return (int) values.get(key);
	}

	public String what(String key){
		return getString(key);
	}

	public String getString(String key){
		if(BuildConfig.DEBUG){
			ConfigEntry entry = CONFIG_ENTRIES.get(key);
			if(entry == null){
				throw new AssertionError("Unknown key");
			}
			if(entry.getType() != ConfigEntry.Type.STRING){
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

	public static void init(Context ctx) throws XmlPullParserException, IOException{
		XmlResourceParser parser = ctx.getResources().getXml(R.xml.config_entries);
		int eventType = parser.getEventType();

		ConfigEntries entries;

		eventLoop:
		while(true){
			switch(eventType){
				case START_DOCUMENT:
					break;
				case END_DOCUMENT:
					break eventLoop;
				case START_TAG:
					if("ConfigEntries".equals(parser.getName())){
						break;
					}
					if("ConfigGroup".equals(parser.getName())){

					}
					break;
				case END_TAG:

					break;

			}
			parser.next();
		}
		parser.close();
	}
}
