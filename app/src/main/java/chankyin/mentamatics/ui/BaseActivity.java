package chankyin.mentamatics.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.ui.main.KeyboardFragment;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseActivity extends AppCompatActivity{
	@Getter @Setter private boolean keyboardOpened = false;

	@Override
	@CallSuper
	protected void onCreate(@Nullable Bundle savedInstanceState){
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
