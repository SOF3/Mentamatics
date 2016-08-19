package chankyin.mentamatics;

import android.util.Log;

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
		if(args.length > 0){
			message = String.format(message, (Object[]) args);
		}
		if(IS_TEST){
			System.err.println(message);
		}else if(IS_ANDROID){
			debugAndroid(message);
		}
	}

	public static void debugAndroid(String message){
		Log.d(Main.TAG, message);
	}
}
