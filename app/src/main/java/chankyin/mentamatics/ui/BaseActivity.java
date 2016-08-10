package chankyin.mentamatics.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.ui.main.KeyboardFragment;

public abstract class BaseActivity extends AppCompatActivity{
	@Override
	@CallSuper
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Main.getInstance(this).setCurrentActivity(this);
	}

	@Override
	@CallSuper
	protected void onResume(){
		super.onResume();
		Main.getInstance(this).setCurrentActivity(this);
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
