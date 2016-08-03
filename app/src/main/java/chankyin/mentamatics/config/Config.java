package chankyin.mentamatics.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import chankyin.mentamatics.config.ui.Quadret;

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
			screen.addPreference(element.toPreference(fragment));
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

	public Quadret getIntDoubleRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigElement.Type.integerRange){
			throw new ClassCastException();
		}
		String out = prefs.getString(key, null);
		if(out == null){
			return new Quadret((int[]) entry.defaultValue);
		}else{
			return new Quadret((int[]) ConfigElement.Type.integerRange.fromString(out));
		}
	}
}
