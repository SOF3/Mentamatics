package chankyin.mentamatics.config.old;

import android.support.annotation.StringRes;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@Deprecated
public class ConfigGroup extends ConfigEntries{
	String id;
	@StringRes int name;

	public ConfigGroup(String id, @StringRes int name){
		this.id = id;
		this.name = name;
	}
}
