package chankyin.mentamatics.config;

import android.support.annotation.NonNull;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.R;

public class ConfigEntries extends ConfigGroup{
	public ConfigEntries(){
		super(null, R.string.config_root, null, false);
	}

	public ConfigEntry getEntry(@NonNull String key){
		String[] ids = key.split("\\.");
		ConfigEntry entry = recurseGetEntry(ids, 0);
		if(BuildConfig.DEBUG && !key.equals(entry.calcFullId())){
			throw new AssertionError();
		}
		return entry;
	}
}
