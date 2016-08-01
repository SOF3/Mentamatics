package chankyin.mentamatics.problems;

import android.support.annotation.StringRes;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Operator{
	ADDITION(R.string.operator_addition),
	SUBTRACTION(R.string.operator_subtraction),
	MULTIPLICATION(R.string.operator_multiplication),
	DIVISION(R.string.operator_division);

	private @StringRes int symbol;

	@Override
	public String toString(){
		return Main.getInstance().getString(symbol);
	}
}
