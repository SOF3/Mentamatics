package chankyin.mentamatics.config;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.TwoStatePreference;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class ConfigEntry extends ConfigElement{
	@StringRes final int summary, summaryPositive;
	final ConfigEntryType type;
	final String[] typeArgs;
	final Object defaultValue;

	public ConfigEntry(String id, @StringRes int name, @StringRes int summary, int summaryPositive,
	                   ConfigEntryType type, String[] typeArgs, String defaultValue, @NonNull ConfigGroup parent){
		super(id, name, parent);
		this.summary = summary;
		this.summaryPositive = summaryPositive == -1 ? summary : summaryPositive;
		this.type = type;
		this.typeArgs = typeArgs;
		this.defaultValue = type.fromString(defaultValue);
	}

	@NonNull
	@Override
	public Preference addPreferenceTo(PreferenceFragment fragment, PreferenceGroup group, SharedPreferences sharedPrefs){
		String prefKey = "Config:" + getFullId();
		Preference preference = type.createPreference(fragment, typeArgs, sharedPrefs, prefKey, defaultValue);
		group.addPreference(preference);
		preference.setTitle(name);
		preference.setSummary(summary);
		if(preference instanceof TwoStatePreference){
			((TwoStatePreference) preference).setSummaryOn(summaryPositive);
			((TwoStatePreference) preference).setSummaryOff(summary);
		}
		preference.setDefaultValue(defaultValue);
		preference.setKey(prefKey);
		return preference;
	}

	public static Builder build(String id, @StringRes int name, ConfigGroup parent){
		return new Builder(id, name, parent);
	}

	public static class Builder{
		private final String id;
		private final int name;
		private int summary, summaryPositive = -1;
		private ConfigEntryType type;
		private String[] typeArgs;
		private String defaultValue = "";
		private final ConfigGroup parent;
		private boolean groupToggle;

		Builder(String id, @StringRes int name, ConfigGroup parent){
			this.id = id;
			this.name = name;
			this.parent = parent;
		}

		public Builder summary(@StringRes int summary){
			this.summary = summary;
			return this;
		}

		public Builder summaryPositive(@StringRes int summary){
			summaryPositive = summary;
			return this;
		}

		public Builder type(ConfigEntryType type){
			this.type = type;
			return this;
		}

		public Builder typeArgs(String[] typeArgs){
			this.typeArgs = typeArgs;
			return this;
		}

		public Builder defaultValue(String defaultValue){
			this.defaultValue = defaultValue;
			return this;
		}

		public Builder groupToggle(boolean groupToggle){
			this.groupToggle = groupToggle;
			return this;
		}

		public ConfigEntry build(){
			ConfigEntry entry = new ConfigEntry(id, name, summary, summaryPositive, type, typeArgs, defaultValue, parent);
			if(groupToggle){
				if(type != ConfigEntryType.bool){
					throw new AssertionError();
				}

				parent.setGroupToggle(entry);
			}
			return entry;
		}
	}
}
