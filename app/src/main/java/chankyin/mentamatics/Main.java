package chankyin.mentamatics;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import chankyin.mentamatics.config.old.Config;
import com.github.nantaphop.fluentview.FluentView;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.*;

public class Main extends Application{
	@Getter private static Main instance;
	private final static Field fluentViewField;

	public final static LayoutParams WC_WC = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
	public final static LayoutParams WC_MP = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
	public final static LayoutParams MP_WC = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
	public final static LayoutParams MP_MP = new LayoutParams(MATCH_PARENT, MATCH_PARENT);

	@Deprecated public final static String PREF_SETTINGS = "chankyin.mentamatics.PREF_SETTINGS";

	static{
		try{
			fluentViewField = FluentView.class.getDeclaredField("view");
		}catch(NoSuchFieldException e){
			throw new AssertionError(e);
		}
		fluentViewField.setAccessible(true);
	}

	@Deprecated @Getter(lazy = true) private final Config config = reloadConfig();

	@Override
	public void onCreate(){
		super.onCreate();
		instance = this;
	}

	@Deprecated private Config reloadConfig(){
		return new Config(this);
	}

	public static Main getInstance(Activity ctx){
		return (Main) ctx.getApplication();
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
			throw new IndexOutOfBoundsException();
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
}
