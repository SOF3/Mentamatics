package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.InputType;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.Lock;
import chankyin.mentamatics.ui.BaseActivity;
import chankyin.mentamatics.ui.pref.PrefActivity;

public class LockPreference extends EditTextPreference{
	private Lock lock;

	public LockPreference(Context context){
		super(context, null);

		getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}

	@Override
	public void setText(String text){
		super.setText(text);

		lock = text.isEmpty() ? null : Lock.fromString(text);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult){
		super.onDialogClosed(positiveResult);

		BaseActivity currentActivity = Main.getInstance().getCurrentActivity();
		if(currentActivity instanceof PrefActivity){
			currentActivity.finish();
		}
	}
}
