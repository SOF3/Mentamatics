package chankyin.mentamatics.config.ui;

import android.content.Context;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import lombok.AllArgsConstructor;

public class IntegerDoubleRangePreference extends DialogPreference{
	private Quadret hardLimit;
	private Quadret softLimit;
	private Quadret value;

	public IntegerDoubleRangePreference(Context context){
		super(context, null);
	}

	@AllArgsConstructor
	@Deprecated
	public enum Constraint{
		UPPER_GT_LOWER(">"){
			@Override
			protected boolean validate0(int[] quadret){
				return quadret[1] > quadret[3] && quadret[0] > quadret[2];
			}

			@Override
			protected void adaptRange(int[] quadret, boolean upperModified){
				// TODO
			}
		},
		UPPER_GE_LOWER(">="){
			@Override
			protected boolean validate0(int[] quadret){
				return quadret[1] >= quadret[3] && quadret[0] >= quadret[2];
			}

			@Override
			protected void adaptRange(int[] quadret, boolean upperModified){
				// TODO
			}
		},
		UPPER_LT_LOWER("<"){
			@Override
			protected boolean validate0(int[] quadret){
				return quadret[1] < quadret[3] && quadret[0] < quadret[2];
			}

			@Override
			protected void adaptRange(int[] quadret, boolean upperModified){
				// TODO
			}
		},
		UPPER_LE_LOWER("<="){
			@Override
			protected boolean validate0(int[] quadret){
				return quadret[1] >= quadret[3] && quadret[0] >= quadret[2];
			}

			@Override
			protected void adaptRange(int[] quadret, boolean upperModified){
				// TODO
			}
		},
		NONE(""){
			@Override
			protected boolean validate0(int[] quadret){
				return true;
			}

			@Override
			protected void adaptRange(int[] quadret, boolean upperModified){
			}
		};

		private final String symbol;

		public static Constraint bySymbol(@NonNull String symbol){
			for(Constraint constraint : values()){
				if(constraint.symbol.equals(symbol)){
					return constraint;
				}
			}
			return null;
		}

		/**
		 * Validate that the quadret matches the constraint.
		 *
		 * @param quadret <code>[0 = upperMin, 1 = upperMax, 2 = lowerMin, 3 = lowerMax]</code>
		 * @return whether the quadret is valid
		 */
		public final boolean validate(int[] quadret){
			return quadret[0] <= quadret[1] && quadret[2] <= quadret[3] && validate0(quadret);
		}

		protected abstract boolean validate0(int[] quadret);

		protected abstract void adaptRange(int[] quadret, boolean upperModified);
	}
}
