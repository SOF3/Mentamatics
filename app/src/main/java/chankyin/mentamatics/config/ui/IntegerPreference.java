package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import lombok.Getter;

import java.util.Locale;

import static android.text.InputType.*;

public class IntegerPreference extends DialogPreference{
	@Getter private boolean signed = false;
	@Getter private final EditText editText;

	@Getter private int value;

	public IntegerPreference(Context context){
		super(context, null);
		editText = new EditText(context);
		editText.setInputType(TYPE_CLASS_NUMBER | (signed ? TYPE_NUMBER_FLAG_SIGNED : 0));
	}

	public void setSigned(boolean signed){
		this.signed = signed;
		editText.setInputType(TYPE_CLASS_NUMBER | (signed ? TYPE_NUMBER_FLAG_SIGNED : 0));
	}

	public void setValue(int value){
		this.value = value;
		persistInt(value);
	}

	@Override
	public CharSequence getSummary(){
		return String.format(super.getSummary().toString(), value);
	}

	@Override
	protected void onBindDialogView(View view){
		super.onBindDialogView(view);

		editText.setText(String.format(Locale.ENGLISH, "%d", value));

		ViewParent oldParent = editText.getParent();
		if(oldParent != view){
			if(oldParent != null){
				((ViewGroup) oldParent).removeView(editText);
			}
			((ViewGroup) view).addView(editText);
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult){
		super.onDialogClosed(positiveResult);

		if(positiveResult){
			int newValue;
			try{
				newValue = Integer.parseInt(editText.getText().toString());
			}catch(NumberFormatException e){
				return;
			}
			if(callChangeListener(newValue)){
				setValue(newValue);
			}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index){
		return a.getInt(index, 0);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue){
		int defaultInt = (int) defaultValue;
		setValue(restorePersistedValue ? getPersistedInt(0) : defaultInt);
	}
}
