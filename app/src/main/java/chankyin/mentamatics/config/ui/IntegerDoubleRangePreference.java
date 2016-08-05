package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.preference.DialogPreference;
import chankyin.mentamatics.config.range.*;
import lombok.Getter;

public class IntegerDoubleRangePreference extends DialogPreference implements DoubleRangeTriplet{
	@Getter private DupletRange hardLimit;
	@Deprecated @Getter private OctetRange softLimit;
	@Getter private QuadretRange value;

	@Getter private DoubleRangeConstraint constraint;

	public IntegerDoubleRangePreference(Context context, DupletRange hardLimit, DoubleRangeConstraint constraint){
		super(context, null);
		this.hardLimit = hardLimit;
		setConstraint(constraint);
	}

	public void setConstraint(DoubleRangeConstraint constraint){
		this.constraint = constraint;

		constraint.updateValueForHardLimit(this);
	}

	public void setHardLimit(DupletRange hardLimit){
		this.hardLimit = hardLimit;

		constraint.updateValueForHardLimit(this);
	}

	// TODO
}
