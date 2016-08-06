package chankyin.mentamatics.config;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Lock{
	public boolean enabled;

	public String value;

	public static Lock fromString(@NonNull String string){
		return new Lock(!string.isEmpty(), string);
	}

	@Override
	public String toString(){
		return enabled ? value : "";
	}

	public static Lock get(Context context){
		return fromString(getString(context));
	}

	@NonNull
	public static String getString(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString(ConfigConstants.KEY_LOCK, "");
	}

	public static void unlock(Context context){
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putString(ConfigConstants.KEY_LOCK, "")
				.apply();
	}
}
