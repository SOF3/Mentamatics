package chankyin.mentamatics.config.range;

import android.support.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DoubleRangeConstraint{
	NONE(""){
		@Override
		@Deprecated
		public void updateSoftLimitForValue(DoubleRangeTriplet triplet, boolean upperModified){
		}

		@Override
		public void updateValueForValue(DoubleRangeTriplet triplet, boolean upperModified){
		}

		@Override
		public void updateValueForHardLimit(DoubleRangeTriplet triplet){
//			DupletRange hardLimit = triplet.getHardLimit();
//			OctetRange softLimit = triplet.getSoftLimit();
//			QuadretRange value = triplet.getValue();
//
//			softLimit.lowerMinMin = hardLimit.min;
//			softLimit.lowerMinMax = value.lowerMax;
//			softLimit.lowerMaxMin = value.lowerMin;
//			softLimit.lowerMaxMax = hardLimit.max;
//			softLimit.upperMinMin = hardLimit.min;
//			softLimit.upperMinMax = value.lowerMax;
//			softLimit.upperMaxMin = value.lowerMin;
//			softLimit.upperMaxMax = hardLimit.max;
//
//			onSoftLimitChange(triplet);
		}
	},

	UPPER_GE_LOWER(">="){
		@Override
		@Deprecated
		public void updateSoftLimitForValue(DoubleRangeTriplet triplet, boolean upperModified){
			QuadretRange value = triplet.getValue();
			DupletRange hardLimit = triplet.getHardLimit();
			OctetRange softLimit = triplet.getSoftLimit();

			// legend:
			// || value: min, max
			// \\// soft limit: min.min, max.min, min.max, max.max
			// \X/ soft limit: min.min, max.min | min.max, max.max
			// [] hard limit: min, max
			// - value range or hard limit range
			// _ soft limit range

			// [---------]    hard limit
			//   \___\_/_/    upper soft limit
			//     |---|      upper value
			//   |---|        lower value
			// \_\_/___/      lower soft limit
			// [---------]    hard limit

			softLimit.upperMinMin = value.lowerMin;
			softLimit.upperMaxMin = value.lowerMax;
			softLimit.upperMinMax = value.upperMax;
			softLimit.upperMaxMax = hardLimit.max;

			softLimit.lowerMinMin = hardLimit.min;
			softLimit.lowerMinMax = value.upperMin;
			softLimit.lowerMaxMin = value.upperMin;
			softLimit.lowerMaxMax = value.upperMax;
		}

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

	/**
	 * Do not call {@link #updateValueForValue(DoubleRangeTriplet, boolean)} if this method is called.
	 * Choose one of these two to call.
	 *
	 * @param triplet
	 * @param upperModified
	 */
	@Deprecated
	public abstract void updateSoftLimitForValue(DoubleRangeTriplet triplet, boolean upperModified);

	/**
	 * Do not call {@link #updateSoftLimitForValue(DoubleRangeTriplet, boolean)} if this method is called.
	 * Choose one of these two to call.
	 *
	 * @param triplet
	 * @param upperModified
	 */
	public abstract void updateValueForValue(DoubleRangeTriplet triplet, boolean upperModified);

	public abstract void updateValueForHardLimit(DoubleRangeTriplet triplet);

	@Deprecated
	public void onSoftLimitChange(DoubleRangeTriplet triplet){
//		QuadretRange softLimit = triplet.getSoftLimit();
//		QuadretRange value = triplet.getValue();
//
//		if(value.lowerMin < softLimit.lowerMin){
//			value.lowerMin = softLimit.lowerMin;
//		}
//		if(value.upperMin < softLimit.upperMin){
//			value.upperMin = softLimit.upperMin;
//		}
//		if(value.lowerMax < softLimit.lowerMax){
//			value.lowerMax = softLimit.lowerMax;
//		}
//		if(value.upperMax < softLimit.upperMax){
//			value.upperMax = softLimit.upperMax;
//		}
	}
}
