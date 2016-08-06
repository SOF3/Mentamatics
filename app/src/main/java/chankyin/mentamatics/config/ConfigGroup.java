package chankyin.mentamatics.config;

import android.preference.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import chankyin.mentamatics.Main;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigGroup extends ConfigElement{
	@Getter private final Map<String, ConfigElement> children = new LinkedHashMap<>();
	@Getter private final boolean header;
	@Getter private ConfigEntry groupToggle = null;

	public ConfigGroup(String id, @StringRes int name, @Nullable ConfigGroup parent, boolean header){
		super(id, name, parent);
		this.header = header;
	}

	public void addChild(ConfigElement child){
		children.put(child.id, child);
	}

	@NonNull
	@Override
	public Preference addPreferenceTo(PreferenceFragment fragment, PreferenceGroup group){
		PreferenceGroup out;
		if(header){
			out = fragment.getPreferenceManager().createPreferenceScreen(fragment.getActivity());
		}else{
			out = new PreferenceCategory(fragment.getActivity());
		}
		group.addPreference(out);
		out.setTitle(name);

		CheckBoxPreference checkBox = null;
		List<Preference> prefs = new ArrayList<>(children.values().size());
		for(ConfigElement element : children.values()){
			Preference preference = element.addPreferenceTo(fragment, out);
			if(element == groupToggle){
				checkBox = (CheckBoxPreference) preference;
			}else{
				prefs.add(preference);
			}
		}

		if(checkBox != null){
			for(Preference pref : prefs){
				Log.d(Main.TAG, "Adding dependency for " + pref.getKey());
//				pref.setDependency(checkBox.getKey());
			}
		}

		return out;
	}

	protected ConfigEntry recurseGetEntry(String[] ids, int level){
		ConfigElement el = children.get(ids[level]);
		if(el instanceof ConfigEntry){
			if(ids.length == level + 1){
				return (ConfigEntry) el;
			}else{
				throw new AssertionError("Cannot have grandchild");
			}
		}else if(el instanceof ConfigGroup){
			if(ids.length == level + 1){
				throw new AssertionError("Target is a group");
			}else{
				return ((ConfigGroup) el).recurseGetEntry(ids, level + 1);
			}
		}
		Log.d(Main.TAG, "Available children: " + children.keySet());
		throw new AssertionError("Nonexistent child " + ids[level]);
	}

	public void setGroupToggle(ConfigEntry entry){
		groupToggle = entry;
	}
}
