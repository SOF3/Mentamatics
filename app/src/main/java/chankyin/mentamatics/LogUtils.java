package chankyin.mentamatics;

import android.util.Log;
import org.apache.commons.lang3.ArrayUtils;

public class LogUtils{
	public static boolean IS_TEST = classExists("junit.framework.Test");
	public static boolean IS_ANDROID = classExists("android.app.Application");

	public static boolean classExists(String name){
		try{
			Class.forName(name);
			return true;
		}catch(ClassNotFoundException e){
			return false;
		}
	}

	public static void debug(String message, Object... args){
		for(int i = 0; i < args.length; i++){
			if(args[i] != null && args[i].getClass().isArray()){
				args[i] = ArrayUtils.toString(args[i]);
			}
		}
		if(args.length > 0){
			message = String.format(message, (Object[]) args);
		}
		if(IS_ANDROID){
			debugAndroid(message);
		}else if(IS_TEST){
			System.err.println(message);
		}
	}

	public static void debugAndroid(String message){
		if(BuildConfig.DEBUG){
			Log.d(Main.TAG, message);
		}
	}
}
