package chankyin.mentamatics.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import chankyin.mentamatics.config.range.QuadretRange;

import static chankyin.mentamatics.LogUtils.debug;

public class Config{
	private final ConfigEntries entries;
	private final Context context;
	private final SharedPreferences prefs;

	public Config(ConfigEntries entries, Context context){
		this.entries = entries;
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public PreferenceScreen toPrefScreen(PreferenceFragment fragment){
		PreferenceScreen screen = fragment.getPreferenceManager().createPreferenceScreen(fragment.getActivity());
		fragment.setPreferenceScreen(screen);
		for(ConfigElement element : entries.getChildren().values()){
			element.addPreferenceTo(fragment, screen, prefs);
		}
		return screen;
	}

	public boolean getBoolean(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.bool){
			throw new ClassCastException();
		}
		return prefs.getBoolean("Config:" + key, (boolean) entry.defaultValue);
	}

	public int getInt(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.integer){
			throw new ClassCastException();
		}
		return prefs.getInt("Config:" + key, (int) entry.defaultValue);
	}

	public int[] getIntRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.integerRange){
			throw new ClassCastException();
		}
		String out = prefs.getString("Config:" + key, null);
		if(out == null){
			return (int[]) entry.defaultValue;
		}else{
			return (int[]) ConfigEntryType.integerRange.fromString(out);
		}
	}

	public QuadretRange getIntDoubleRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.integerDoubleRange){
			throw new ClassCastException("Expected integerDoubleRange for " + key + ", got " + entry.type.name());
		}
		String rangeString = prefs.getString("Config:" + key, null);
		debug("getIntDoubleRange: rangeString = %s", rangeString);
		if(rangeString == null){
			return (QuadretRange) entry.defaultValue;
		}else{
			return (QuadretRange) ConfigEntryType.integerDoubleRange.fromString(rangeString);
		}
	}
}
