package chankyin.mentamatics.ui.pref;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import chankyin.mentamatics.Main;

public class PrefFragment extends PreferenceFragment{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		loadPrefScreen();
	}

	private PreferenceScreen loadPrefScreen(){
		return Main.getInstance().getConfig().toPrefScreen(this);
	}
}
