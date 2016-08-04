package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.preference.DialogPreference;
import lombok.Getter;

public class IntegerDoubleRangePreference extends DialogPreference implements DoubleRangeTriplet{
	@Getter private Duplet hardLimit;
	@Getter private Quadret softLimit;
	@Getter private Quadret value;

	@Getter private DoubleRangeConstraint constraint;

	public IntegerDoubleRangePreference(Context context, Duplet hardLimit, DoubleRangeConstraint constraint){
		super(context, null);
		this.hardLimit = hardLimit;
		setConstraint(constraint);
	}

	public void setConstraint(DoubleRangeConstraint constraint){
		this.constraint = constraint;

		constraint.onHardLimitChange(this);
	}

	public void setHardLimit(Duplet hardLimit){
		this.hardLimit = hardLimit;

		constraint.onHardLimitChange(this);
	}
}
