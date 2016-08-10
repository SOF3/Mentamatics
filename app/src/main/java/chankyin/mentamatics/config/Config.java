package chankyin.mentamatics.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import chankyin.mentamatics.config.range.QuadretRange;

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
		for(ConfigElement element : entries.getChildren().values()){
			element.addPreferenceTo(fragment, screen);
		}
		return screen;
	}

	public boolean getBoolean(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigElement.Type.bool){
			throw new ClassCastException();
		}
		return prefs.getBoolean(key, (boolean) entry.defaultValue);
	}

	public int getInt(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigElement.Type.integer){
			throw new ClassCastException();
		}
		return prefs.getInt(key, (int) entry.defaultValue);
	}

	public int[] getIntRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigElement.Type.integerRange){
			throw new ClassCastException();
		}
		String out = prefs.getString(key, null);
		if(out == null){
			return (int[]) entry.defaultValue;
		}else{
			return (int[]) ConfigElement.Type.integerRange.fromString(out);
		}
	}

	public QuadretRange getIntDoubleRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigElement.Type.integerDoubleRange){
			throw new ClassCastException();
		}
		String out = prefs.getString(key, null);
		if(out == null){
			return (QuadretRange) entry.defaultValue;
		}else{
			return (QuadretRange) ConfigElement.Type.integerDoubleRange.fromString(out);
		}
	}
}
