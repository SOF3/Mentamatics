package chankyin.mentamatics.config;

import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class ConfigEntry extends ConfigElement{
	@StringRes final int summary;
	final Type type;
	final String[] typeArgs;
	final Object defaultValue;

	public ConfigEntry(String id, @StringRes int name, @StringRes int summary, Type type, String[] typeArgs, Object defaultValue, @NonNull ConfigGroup parent){
		super(id, name, parent);
		this.summary = summary;
		this.type = type;
		this.typeArgs = typeArgs;
		this.defaultValue = defaultValue;
	}

	@Override
	public Preference toPreference(PreferenceFragment fragment){
		Preference preference = type.createPreference(fragment, typeArgs);
		preference.setTitle(name);
		preference.setSummary(summary);
		preference.setDefaultValue(defaultValue);
		preference.setKey(getFullId());
		return preference; // TODO
	}

	public static Builder build(String id, @StringRes int name, ConfigGroup parent){
		return new Builder(id, name, parent);
	}

	public static class Builder{
		private String id;
		private int name;
		private int summary;
		private Type type;
		private String[] typeArgs;
		private Object defaultValue;
		private ConfigGroup parent;

		Builder(String id, @StringRes int name, ConfigGroup parent){
			this.id = id;
			this.name = name;
			this.parent = parent;
		}

		public Builder summary(@StringRes int summary){
			this.summary = summary;
			return this;
		}

		public Builder type(Type type){
			this.type = type;
			return this;
		}

		public Builder typeArgs(String[] typeArgs){
			this.typeArgs = typeArgs;
			return this;
		}

		public Builder defaultValue(Object defaultValue){
			this.defaultValue = defaultValue;
			return this;
		}

		public ConfigEntry build(){
			return new ConfigEntry(id, name, summary, type, typeArgs, defaultValue, parent);
		}
	}
}
