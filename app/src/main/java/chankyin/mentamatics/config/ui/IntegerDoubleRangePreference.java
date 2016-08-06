package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import chankyin.mentamatics.config.range.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import static chankyin.mentamatics.config.ConfigElement.Type.integerDoubleRange;

public class IntegerDoubleRangePreference extends DialogPreference implements DoubleRangeTriplet{
	@Getter private DupletRange hardLimit;
	@Deprecated @Getter private OctetRange softLimit;
	@Getter private QuadretRange value;

	@Getter private DoubleRangeConstraint constraint;

	private LinearLayout layout;
	private RangeSeekBar<Integer> upperBar, lowerBar;

	public IntegerDoubleRangePreference(Context context, DupletRange hardLimit, final DoubleRangeConstraint constraint){
		super(context, null);
		upperBar = new RangeSeekBar<>(context);
		lowerBar = new RangeSeekBar<>(context);
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

	public void setValue(QuadretRange value){
		this.value = value;

		persistString(value.toString());
	}

	@Override
	public CharSequence getSummary(){
		if(value == null){
			return super.getSummary();
		}
		return String.format(super.getSummary().toString(), value.upperMin, value.upperMax, value.lowerMin, value.lowerMax);
	}

	@Override
	protected void onBindDialogView(View view){
		super.onBindDialogView(view);

		upperBar.setSelectedMinValue(value.upperMin);
		upperBar.setSelectedMaxValue(value.upperMax);
		lowerBar.setSelectedMinValue(value.lowerMin);
		lowerBar.setSelectedMaxValue(value.lowerMax);

		ViewParent oldParent = layout.getParent();
		if(oldParent!=view){
			if(oldParent != null){
				((ViewGroup) oldParent).removeView(layout);
			}
			((ViewGroup) view).addView(layout);
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult){
		super.onDialogClosed(positiveResult);

		if(positiveResult){
			QuadretRange newValue = new QuadretRange(new int[]{
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
		setValue(restorePersistedValue ? (QuadretRange) integerDoubleRange.fromString(getPersistedString("")) : (QuadretRange) defaultValue);
	}

	@RequiredArgsConstructor
	private class ConstraintOnRangeSeekBarChangeListener implements RangeSeekBar.OnRangeSeekBarChangeListener<Integer>{
		private final DoubleRangeConstraint constraint;
		private final boolean whichSeekBar;

		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue){
			constraint.updateValueForValue(IntegerDoubleRangePreference.this, whichSeekBar);
		}
	}
}
