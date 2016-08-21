package chankyin.mentamatics.config.range;

import android.support.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DoubleRangeConstraint{
	NONE(""){
		@Override
		public void updateValueForValue(DoubleRangeTriplet triplet, boolean upperModified){
		}

		@Override
		public void updateValueForHardLimit(DoubleRangeTriplet triplet){
		}
	},

	UPPER_GE_LOWER(">="){
		@Override
		public void updateValueForValue(DoubleRangeTriplet triplet, boolean upperModified){
			QuadretRange value = triplet.getValue();
			if(value.upperMax < value.lowerMax){
				if(upperModified){
					value.lowerMax = value.upperMax;
				}else{
					value.upperMax = value.lowerMax;
				}
			}
			if(value.upperMin < value.lowerMin){
				if(upperModified){
					value.lowerMin = value.upperMin;
				}else{
					value.upperMin = value.lowerMin;
				}
			}
		}

		@Override
		public void updateValueForHardLimit(DoubleRangeTriplet triplet){
			updateValueForValue(triplet, UPPER);
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

	public abstract void updateValueForValue(DoubleRangeTriplet triplet, boolean upperModified);

	public abstract void updateValueForHardLimit(DoubleRangeTriplet triplet);
}
