package chankyin.mentamatics.config;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public abstract class ConfigElement{
	@Nullable String id;
	@StringRes int name;
	@Nullable ConfigGroup parent;
	@NonFinal @Getter(lazy = true) private final String fullId = calcFullId();

	public ConfigElement(@Nullable String id, @StringRes int name, @Nullable ConfigGroup parent){
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	@Nullable
	public String calcFullId(){
		String parentFullId;
		if(parent != null && (parentFullId = parent.getFullId()) != null){
			return parentFullId + "." + id;
		}else{
			return id;
		}
	}

	@NonNull
	public abstract Preference addPreferenceTo(PreferenceFragment fragment, PreferenceGroup group, SharedPreferences sharedPrefs);

}
