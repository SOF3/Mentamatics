package chankyin.mentamatics.config;

import android.support.annotation.NonNull;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.R;

public class ConfigEntries extends ConfigGroup{
	public ConfigEntries(){
		super(null, R.string.config_root, null, false);
	}

	public ConfigElement getElement(@NonNull String key){
		String[] ids = key.split("\\.");
		ConfigEntry entry = recurseGetElement(ids, 0);
		if(BuildConfig.DEBUG && !key.equals(entry.calcFullId())){
			throw new AssertionError();
		}
		return entry;
	}
}
