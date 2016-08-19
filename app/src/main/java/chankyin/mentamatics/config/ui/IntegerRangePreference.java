package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Size;
import android.view.View;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import static chankyin.mentamatics.config.ConfigEntryType.integerRange;

public class IntegerRangePreference extends MDialogPreference{
	private int min, max;
	private int[] value;

	private RangeSeekBar<Integer> rangeSeekBar;

	public IntegerRangePreference(Context context){
		this(context, 1, 10);
	}

	public IntegerRangePreference(Context context, int min, int max){
		super(context);

		rangeSeekBar = new RangeSeekBar<>(context);
		setRange(min, max);
	}

	public void setRange(int min, int max){
		this.min = min;
		this.max = max;
		rangeSeekBar.setRangeValues(min, max);
	}

	public void setValue(@Size(2) int[] value){
		if(value.length != 2){
			throw new ArrayIndexOutOfBoundsException();
		}
		this.value = value;
		persistString(toString(value));
		setSummary(getSummary());
	}

	@Override
	public CharSequence getSummary(){
		return String.format(super.getSummary().toString(), value[0], value[1]);
	}

	@Override
	protected View createDialogView(){
		return rangeSeekBar;
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
