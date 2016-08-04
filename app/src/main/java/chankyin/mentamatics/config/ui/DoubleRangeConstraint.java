package chankyin.mentamatics.config.ui;

import android.support.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DoubleRangeConstraint{
	NONE(""){
		@Override
		public void correctTripleLimit(DoubleRangeTriplet triplet, boolean upperModified){
		}

		@Override
		public void onHardLimitChange(DoubleRangeTriplet triplet){
			Duplet hardLimit = triplet.getHardLimit();
			Quadret softLimit = triplet.getSoftLimit();

			softLimit.lowerMin = hardLimit.min;
			softLimit.upperMin = hardLimit.min;
			softLimit.lowerMax = hardLimit.max;
			softLimit.upperMin = hardLimit.max;

			onSoftLimitChange(triplet);
		}
	},
	UPPER_GE_LOWER(">="){
		@Override
		public void correctTripleLimit(DoubleRangeTriplet triplet, boolean upperModified){
			Quadret softLimit = triplet.getSoftLimit();
			if(upperModified){
				softLimit.lowerMax = triplet.getValue().upperMax;
				softLimit.lowerMin = triplet.getHardLimit().min;
			}else{
				softLimit.upperMin = triplet.getValue().lowerMin;
				softLimit.upperMax = triplet.getHardLimit().max;
			}
		}

		@Override
		public void onHardLimitChange(DoubleRangeTriplet triplet){
			Duplet hardLimit = triplet.getHardLimit();
			Quadret softLimit = triplet.getSoftLimit();

			// TODO
		}
	},
	UPPER_GT_LOWER(">"){
		@Override
		public void correctTripleLimit(DoubleRangeTriplet triplet, boolean upperModified){
			if(upperModified){
				triplet.getSoftLimit().lowerMax = triplet.getValue().upperMax - 1;
				triplet.getSoftLimit().lowerMin = triplet.getHardLimit().min;
			}else{
				triplet.getSoftLimit().upperMin = triplet.getValue().lowerMin + 1;
				triplet.getSoftLimit().upperMax = triplet.getHardLimit().max;
			}
		}

		@Override
		public void onHardLimitChange(DoubleRangeTriplet triplet){
			// TODO
		}
	};

	@NonNull @Getter private final String symbol;

	public static DoubleRangeConstraint bySymbol(@NonNull String symbol){
		for(DoubleRangeConstraint constraint : values()){
			if(constraint.symbol.equals(symbol)){
				return constraint;
			}
		}
		return null;
	}

	public final static boolean UPPER = true, LOWER = false;

	public abstract void correctTripleLimit(DoubleRangeTriplet triplet, boolean upperModified);

	public abstract void onHardLimitChange(DoubleRangeTriplet triplet);

	public void onSoftLimitChange(DoubleRangeTriplet triplet){
		Quadret softLimit = triplet.getSoftLimit();
		Quadret value = triplet.getValue();

		if(value.lowerMin < softLimit.lowerMin){
			value.lowerMin = softLimit.lowerMin;
		}
		if(value.upperMin < softLimit.upperMin){
			value.upperMin = softLimit.upperMin;
		}
		if(value.lowerMax < softLimit.lowerMax){
			value.lowerMax = softLimit.lowerMax;
		}
		if(value.upperMax < softLimit.upperMax){
			value.upperMax = softLimit.upperMax;
		}
	}
}
