package chankyin.mentamatics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.ConfigEntries;
import chankyin.mentamatics.config.ConfigParser;
import chankyin.mentamatics.ui.BaseActivity;
import com.github.nantaphop.fluentview.FluentView;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.*;

public class Main extends Application{
	public final static String TAG = "AndroidRuntime";

	@Getter private static Main instance;
	private final static Field fluentViewField;

	public final static LayoutParams WC_WC = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

	public final static LayoutParams WC_MP = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
	public final static LayoutParams MP_WC = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
	public final static LayoutParams MP_MP = new LayoutParams(MATCH_PARENT, MATCH_PARENT);

	@Getter @Setter private BaseActivity currentActivity;

	static{
		try{
			fluentViewField = FluentView.class.getDeclaredField("view");
		}catch(NoSuchFieldException e){
			throw new AssertionError(e);
		}
		fluentViewField.setAccessible(true);
	}

	@Getter(lazy = true) private final Config config = reloadConfig();

	@Override
	public void onCreate(){
		super.onCreate();
		instance = this;
	}

	public static Main getInstance(Activity activity){
		return (Main) activity.getApplication();
	}

	public static View defluent(FluentView view){
		try{
			return (View) fluentViewField.get(view);
		}catch(IllegalAccessException e){
			throw new AssertionError(e);
		}
	}

	public static int randomRange(@NonNull Random random, int lower, int upper){
		if(lower > upper){
			Log.w(TAG, "Call to randomRange() with lower greater than upper");
			return randomRange(random, upper, lower);
		}
		if(lower == upper){
			return lower;
		}
		return random.nextInt(upper - lower + 1) + lower;
	}

	public static int gcd(int a, int b){
		while(b > 0){
			int temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}

	public static int gcd(int... input){
		int result = input[0];
		for(int i = 1; i < input.length; i++){
			result = gcd(result, input[i]);
		}
		return result;
	}

	public static int lcm(int a, int b){
		return a * b / gcd(a, b);
	}

	public static int lcm(int... input){
		int result = input[0];
		for(int i = 1; i < input.length; i++){
			result = lcm(result, input[i]);
		}
		return result;
	}

	public Config reloadConfig(){
		ConfigEntries entries = ConfigParser.parse(this);
		return new Config(entries, this);
	}

	public static int getStringIdentifier(String name){
		return getStringIdentifier(getInstance(), name);
	}

	public static int getStringIdentifier(Context context, String name){
		int identifier = context.getResources().getIdentifier(name, "string", context.getPackageName());
		if(identifier == 0){
			throw new Resources.NotFoundException(name);
		}
		return identifier;
	}

	public void saveRandom(Random random, String name){
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(name, serialize(random))
				.apply();
	}

	@NonNull
	public Random loadRandom(String name){
		return loadRandom(name, true);
	}

	public Random loadRandom(String name, boolean notNull){
		Object unserialize = unserialize(PreferenceManager.getDefaultSharedPreferences(this).getString(name, null));
		if(notNull && !(unserialize instanceof Random)){
			return new Random();
		}
		return (Random) unserialize;
	}

	@SneakyThrows(IOException.class)
	public static String serialize(Serializable serializable){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Base64OutputStream bos = new Base64OutputStream(baos, Base64.NO_WRAP);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(serializable);
		oos.close();
		return baos.toString();
	}

	@SneakyThrows({IOException.class, ClassNotFoundException.class})
	@Nullable
	public static Object unserialize(@Nullable String string){
		if(string == null){
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes());
		Base64InputStream bis = new Base64InputStream(bais, Base64.NO_WRAP | Base64.NO_PADDING);
		ObjectInputStream oos = new ObjectInputStream(bis);
		Object ret = oos.readObject();
		oos.close();
		return ret;
	}

	@SuppressWarnings("deprecation")
	public static Spanned fromHtml(String html){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			return fromHtmlN(html);
		}else{
			return Html.fromHtml(html);
		}
	}

	@TargetApi(Build.VERSION_CODES.N)
	private static Spanned fromHtmlN(String html){
		return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
	}
}
