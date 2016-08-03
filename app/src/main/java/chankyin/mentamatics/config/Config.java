package chankyin.mentamatics.config;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class Config{
	private final ConfigEntries entries;
	private final PreferenceFragment fragment;
	private final SharedPreferences prefs;

	public Config(ConfigEntries entries, PreferenceFragment fragment){
		this.entries = entries;
		this.fragment = fragment;
		prefs = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
	}

	public PreferenceScreen toPrefScreen(){
		PreferenceScreen screen = fragment.getPreferenceManager().createPreferenceScreen(fragment.getActivity());
		for(ConfigElement element : entries.getChildren().values()){
			screen.addPreference(element.toPreference(fragment));
		}
		return screen;
	}
}
