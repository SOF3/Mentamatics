package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.EditText;
import lombok.Getter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;

public class IntegerPreference extends MDialogPreference{
	@Getter private boolean signed = false;
	@Getter private final EditText editText;

	@Getter private int value;

	public IntegerPreference(Context context){
		super(context);
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
		setSummary(getSummary());
	}

	@Override
	public CharSequence getSummary(){
		return String.format(super.getSummary().toString(), value);
	}

	@Override
	protected View createDialogView(){
		return editText;
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

// TODO: 8/6/2016 save instance state
}
