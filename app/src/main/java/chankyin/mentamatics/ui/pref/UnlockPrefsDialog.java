package chankyin.mentamatics.ui.pref;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.Lock;
import lombok.RequiredArgsConstructor;

public class UnlockPrefsDialog extends DialogFragment{
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View view = inflater.inflate(R.layout.pref_dialog_unlock, null);
		return builder.setView(view)
				.setPositiveButton(R.string.pref_dialog_unlock_positive, new PositiveListener(view))
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialogInterface, int i){
						getActivity().finish();
					}
				})
				.create();
	}

	@RequiredArgsConstructor
	private class PositiveListener implements DialogInterface.OnClickListener{
		private final View view;

		@Override
		public void onClick(DialogInterface dialogInterface, int i){
			if(Lock.getString(getActivity()).equals(((EditText) view.findViewById(R.id.pref_dialog_unlock_field)).getText().toString())){
				dismiss();
				((ConfigActivity) getActivity()).showPrefs();
				Lock.unlock(getActivity());
			}else{
				Toast.makeText(getActivity(), R.string.cfg_lock_error, Toast.LENGTH_LONG).show();
				getActivity().finish();
			}
		}
	}
}
