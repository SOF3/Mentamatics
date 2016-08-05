package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import static chankyin.mentamatics.config.ConfigElement.Type.integerRange;

public class IntegerRangePreference extends DialogPreference{
	private int min, max;
	private int[] value;

	private RangeSeekBar<Integer> rangeSeekBar;

	public IntegerRangePreference(Context context){
		this(context, 1, 10);
	}

	public IntegerRangePreference(Context context, int min, int max){
		super(context, null);

		rangeSeekBar = new RangeSeekBar<>(context);
		setRange(min, max);
	}

	public void setRange(int min, int max){
		this.min = min;
		this.max = max;
		rangeSeekBar.setRangeValues(min, max);
	}

	public void setValue(int[] value){
		if(value.length != 2){
			throw new ArrayIndexOutOfBoundsException();
		}
		this.value = value;
		persistString(toString(value));
	}

	@Override
	public CharSequence getSummary(){
		return String.format(super.getSummary().toString(), value[0], value[1]);
	}

	@Override
	protected void onBindDialogView(View view){
		super.onBindDialogView(view);

		rangeSeekBar.setSelectedMinValue(value[0]);
		rangeSeekBar.setSelectedMaxValue(value[1]);

		ViewParent oldParent = rangeSeekBar.getParent();
		if(oldParent != view){
			if(oldParent != null){
				((ViewGroup) oldParent).removeView(rangeSeekBar);
			}
			((ViewGroup) view).addView(rangeSeekBar);
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult){
		super.onDialogClosed(positiveResult);

		if(positiveResult){
			int[] newValue = new int[]{
					rangeSeekBar.getSelectedMinValue(),
					rangeSeekBar.getSelectedMaxValue()
			};
			if(callChangeListener(newValue)){
				setValue(newValue);
			}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index){
		return fromString(a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultObject){
		int[] defaultValue = (int[]) defaultObject;
		setValue(restorePersistedValue ? (int[]) fromString(getPersistedString("")) : defaultValue);
	}

	public static Object fromString(String string){
		return integerRange.fromString(string);
	}

	public static String toString(Object value){
		return integerRange.toString(value);
	}
}
