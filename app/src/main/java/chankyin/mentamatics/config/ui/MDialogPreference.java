package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.preference.DialogPreference;
import android.view.View;
import android.view.ViewGroup;

public abstract class MDialogPreference extends DialogPreference{
	public MDialogPreference(Context context){
		super(context, null);
	}

	@Override
	protected final View onCreateDialogView(){
		View dialogView = createDialogView();
		if(dialogView.getParent() != null){
			((ViewGroup) dialogView.getParent()).removeView(dialogView);
		}
		return dialogView;
	}

	protected abstract View createDialogView();
}
