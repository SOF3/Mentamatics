package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.LinearLayout;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.range.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import static chankyin.mentamatics.Main.MP_WC;
import static chankyin.mentamatics.config.ConfigEntryType.integerDoubleRange;

public class IntegerDoubleRangePreference extends MDialogPreference implements DoubleRangeTriplet{
	@Getter private DupletRange hardLimit;
	@Deprecated @Getter private OctetRange softLimit;
	@Getter private QuartetRange value;

	@Getter private DoubleRangeConstraint constraint;

	private LinearLayout layout;
	private RangeSeekBar<Integer> upperBar, lowerBar;

	public IntegerDoubleRangePreference(Context context, DupletRange hardLimit, final DoubleRangeConstraint constraint){
		super(context);
		upperBar = new RangeSeekBar<>(context);
		upperBar.setLayoutParams(MP_WC);
		upperBar.setTextAboveThumbsColorResource(R.color.colorPrefThumb);
		lowerBar = new RangeSeekBar<>(context);
		lowerBar.setLayoutParams(MP_WC);
		lowerBar.setTextAboveThumbsColorResource(R.color.colorPrefThumb);
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(upperBar);
		layout.addView(lowerBar);

		setHardLimit(hardLimit);
		setConstraint(constraint);

		upperBar.setOnRangeSeekBarChangeListener(new ConstraintOnRangeSeekBarChangeListener(constraint, DoubleRangeConstraint.UPPER));
		lowerBar.setOnRangeSeekBarChangeListener(new ConstraintOnRangeSeekBarChangeListener(constraint, DoubleRangeConstraint.LOWER));
	}

	public void setConstraint(DoubleRangeConstraint constraint){
		this.constraint = constraint;

		if(hardLimit != null && value != null){
			constraint.updateValueForHardLimit(this);
		}
	}

	public void setHardLimit(DupletRange hardLimit){
		this.hardLimit = hardLimit;

		if(upperBar != null){
			upperBar.setRangeValues(hardLimit.min, hardLimit.max);
		}
		if(lowerBar != null){
			lowerBar.setRangeValues(hardLimit.min, hardLimit.max);
		}
		if(constraint != null && value != null){
			constraint.updateValueForHardLimit(this);
		}
	}

	public void setValue(QuartetRange value){
		this.value = value;
		persistString(value.toString());
		if(super.getSummary() != null){
			setSummary(getSummary());
		}
	}

	@Override
	public CharSequence getSummary(){
		if(value == null){
			return super.getSummary();
		}
		return String.format(super.getSummary().toString(), value.upperMin, value.upperMax, value.lowerMin, value.lowerMax);
	}

	@Override
	protected View createDialogView(){
		upperBar.setSelectedMinValue(value.upperMin);
		upperBar.setSelectedMaxValue(value.upperMax);
		lowerBar.setSelectedMinValue(value.lowerMin);
		lowerBar.setSelectedMaxValue(value.lowerMax);

		return layout;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult){
		super.onDialogClosed(positiveResult);

		if(positiveResult){
			QuartetRange newValue = new QuartetRange(new int[]{
					upperBar.getSelectedMinValue(),
					upperBar.getSelectedMaxValue(),
					lowerBar.getSelectedMinValue(),
					lowerBar.getSelectedMaxValue(),
			});
			if(callChangeListener(newValue)){
				setValue(newValue);
			}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index){
		return integerDoubleRange.fromString(a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue){
		setValue(restorePersistedValue ? (QuartetRange) integerDoubleRange.fromString(getPersistedString("")) : (QuartetRange) defaultValue);
	}

	@RequiredArgsConstructor
	private class ConstraintOnRangeSeekBarChangeListener implements RangeSeekBar.OnRangeSeekBarChangeListener<Integer>{
		private final DoubleRangeConstraint constraint;
		private final boolean whichSeekBar;

		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue){
			constraint.updateValueForValue(IntegerDoubleRangePreference.this, whichSeekBar);
			if(whichSeekBar == DoubleRangeConstraint.UPPER){
				lowerBar.setSelectedMinValue(value.lowerMin);
				lowerBar.setSelectedMaxValue(value.lowerMax);
			}else{
				upperBar.setSelectedMinValue(value.upperMin);
				upperBar.setSelectedMaxValue(value.upperMax);
			}
		}
	}
}
