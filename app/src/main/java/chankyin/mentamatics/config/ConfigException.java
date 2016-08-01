package chankyin.mentamatics.config;

import android.support.annotation.StringRes;
import lombok.Getter;

public class ConfigException extends Exception{
	@Getter @StringRes private int message;

	public ConfigException(@StringRes int message){
		super("ConfigException " + Integer.toHexString(message));
		this.message = message;
	}
}
