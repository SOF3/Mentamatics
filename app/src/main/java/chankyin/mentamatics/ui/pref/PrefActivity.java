package chankyin.mentamatics.ui.pref;

import android.os.Bundle;
import android.support.annotation.IdRes;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.Lock;
import chankyin.mentamatics.ui.BaseActivity;

public class PrefActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pref);

		Lock lock = Lock.get(this);
		if(lock.enabled){
			new UnlockPrefsDialog().show(getFragmentManager(), "QueryPassword");
		}else{
			showPrefs();
		}
	}

	@Override
	@IdRes
	public int getContentViewId(){
		return R.id.pref_content_view;
	}

	public void showPrefs(){
		getFragmentManager().beginTransaction()
				.replace(R.id.pref_pref, new PrefFragment())
				.commit();
	}
}
