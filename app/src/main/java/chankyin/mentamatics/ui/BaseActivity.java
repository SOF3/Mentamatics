package chankyin.mentamatics.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import chankyin.mentamatics.Main;

public class BaseActivity extends AppCompatActivity{
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
}
