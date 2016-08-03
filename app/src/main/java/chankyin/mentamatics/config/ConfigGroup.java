package chankyin.mentamatics.config;

import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ConfigGroup extends ConfigElement{
	@Getter private final Map<String, ConfigElement> children = new HashMap<>();
	@Getter private final boolean header;

	public ConfigGroup(String id, @StringRes int name, @Nullable ConfigGroup parent, boolean header){
		super(id, name, parent);
		this.header = header;
	}

	public void addChild(ConfigElement child){
		children.put(child.id, child);
	}

	@Override
	public Preference toPreference(PreferenceFragment fragment){
		PreferenceGroup out;
		if(header){
			out = fragment.getPreferenceManager().createPreferenceScreen(fragment.getActivity());
		}else{
			out = new PreferenceCategory(fragment.getActivity());
		}
		out.setTitle(name);
		for(ConfigElement element : children.values()){
			out.addPreference(element.toPreference(fragment));
		}
		return out;
	}

	protected ConfigEntry recurseGetElement(String[] ids, int level){
		ConfigElement el = children.get(ids[level]);
		if(el instanceof ConfigEntry){
			return ids.length == level + 1 ? (ConfigEntry) el : null;
		}else if(el instanceof ConfigGroup){
			return ids.length == level + 1 ? null : ((ConfigGroup) el).recurseGetElement(ids, level + 1);
		}
		return null;
	}
}
