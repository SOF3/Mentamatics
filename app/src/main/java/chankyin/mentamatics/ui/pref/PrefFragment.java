package chankyin.mentamatics.ui.pref;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.ConfigParser;
import org.xmlpull.v1.XmlPullParserException;

public class PrefFragment extends PreferenceFragment{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setPreferenceScreen(loadPrefScreen());
	}

	private PreferenceScreen loadPrefScreen(){
		try{
			return new Config(new ConfigParser(getActivity()).parse(), this).toPrefScreen();
		}catch(XmlPullParserException e){
			throw new AssertionError(e.getMessage());
		}
	}
}
