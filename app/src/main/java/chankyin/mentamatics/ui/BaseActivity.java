package chankyin.mentamatics.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.ui.main.KeyboardFragment;

public abstract class BaseActivity extends AppCompatActivity{
	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState){
		super.onCreate(savedInstanceState, persistentState);

		Main.getInstance(this).setCurrentActivity(this);
	}

	@Override
	protected void onResume(){
		super.onResume();

		Main.getInstance(this).setCurrentActivity(this);
	}

	@IdRes
	public abstract int getContentViewId();

	@Nullable
	public ViewGroup.LayoutParams getKeyboardParams(){
		return null;
	}

	public void onKeyboardButtonClick(View view){
		KeyboardFragment fragment = KeyboardFragment.get(this);
		if(fragment == null){
			Log.w(Main.TAG, "KeyboardFragment missing");
			return;
		}

		fragment.onInputButtonClick(view);
	}
}
