package chankyin.mentamatics.ui.pref;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import chankyin.mentamatics.R;

public class PrefActivity extends AppCompatActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pref);
		getFragmentManager().beginTransaction()
				.replace(R.id.pref_pref, new PrefFragment())
				.commit();
	}
}
