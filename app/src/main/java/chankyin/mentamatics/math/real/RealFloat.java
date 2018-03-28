package chankyin.mentamatics.math.real;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.Spanned;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.math.RealFloatUtils;
import chankyin.mentamatics.math.real.annotation.AscendingDigits;
import chankyin.mentamatics.math.real.annotation.DescendingDigits;
import chankyin.mentamatics.math.real.annotation.Immutable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chankyin.mentamatics.TestUtils.debug;
import static chankyin.mentamatics.TestUtils.validate;
import static chankyin.mentamatics.math.RealFloatUtils.flipIntArray;

/**
 * Arbitrary-base floating-point infinite-precision real number class.
 */
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class RealFloat extends Number implements Comparable<RealFloat>, Cloneable{
	public final static int DEFAULT_BASE = 10;

	public final static RealFloat ZERO = new RealFloat(new int[0], 0, DEFAULT_BASE, 0);
	private final static Pattern parseFormat = Pattern.compile("^(\\-)?([0-9a-z]+)(\\.([0-9a-z]+))?(e([\\+\\-])([0-9]+))?(_([0-9]+))?$", Pattern.CASE_INSENSITIVE);

	@Immutable @AscendingDigits int[] digits;
	int exp;
	@IntRange(from = 2) int base;
	@IntRange(from = -1, to = 1) int signum;

	private RealFloat(@AscendingDigits int[] digits, int exp, @IntRange(from = 2) int base, @IntRange(from = -1, to = 1) int signum){
		this.digits = digits;
		this.exp = exp;
		this.base = base;
		this.signum = RealFloatUtils.isZero(digits) ? 0 : signum;
	}

	public static RealFloat bigEndianDigits(int signum, int exp, @DescendingDigits int[] digits){
		return bigEndianDigits(DEFAULT_BASE, signum, exp, digits);
	}

	public static RealFloat bigEndianDigits(@IntRange(from = 2) int base, int signum, int exp, @Immutable @DescendingDigits int[] digits){
		return bigEndianDigits(base, signum, exp, true, digits);
	}

	public static RealFloat bigEndianDigits(@IntRange(from = 2) int base, int signum, int exp, boolean trim, @Immutable @DescendingDigits int[] digits){
		RealFloatUtils.validate(base, digits);

		int start = -1;
		int end = -1;
		if(trim){
			for(int i = 0; i < digits.length; i++){
				if(digits[i] != 0){
					start = i;
					break;
				}
			}
			if(start == -1){
				return ZERO;
			}

			for(int i = digits.length - 1; i >= 0; i--){
				if(digits[i] != 0){
					end = i + 1;
					break;
				}
			}
			validate(end > 0, new AssertionError());

			exp += digits.length - end;
		}else{
			start = 0;
			end = digits.length;
		}

		@AscendingDigits int[] newDigits = new int[end - start];

		for(int i = 0; i < end - start; i++){
			newDigits[i] = digits[end - i - 1]; // flip of range end till start
			// i = 0 => end - i - 1 = end - 1
			// i = end - start - 1 => end - i - 1 = start
		}

		return new RealFloat(newDigits, exp, base, signum);
	}

	public static RealFloat littleEndianDigits(int signum, int exp, @AscendingDigits int[] digits){
		return littleEndianDigits(DEFAULT_BASE, signum, exp, digits);
	}

	public static RealFloat littleEndianDigits(@IntRange(from = 2) int base, int signum, int exp, @Immutable @AscendingDigits int[] digits){
		digits = digits.clone();
		flipIntArray(digits);
		return bigEndianDigits(base, signum, exp, true, digits);
	}

	public static RealFloat parseString(String string) throws NumberFormatException{
		debug("Parsing: " + string);
		Matcher matcher = parseFormat.matcher(string);
		validate(matcher.matches(), new NumberFormatException("Does not match format"));
		String sigStr = matcher.group(1);
		String intPartStr = matcher.group(2);
		String decPartStr = matcher.group(4);
		String expSigStr = matcher.group(6);
		String expPartStr = matcher.group(7);
		String basePartStr = matcher.group(9);

		int signum = 1;
		if("-".equals(sigStr)){
			signum = -1;
		}

		int[] intPart = RealFloatUtils.charsToInts(intPartStr.toCharArray());
		int[] decPart = decPartStr == null ? new int[0] : RealFloatUtils.charsToInts(decPartStr.toCharArray());

		int exp = 0;
		if("+".equals(expSigStr)){
			exp = 1;
		}else if("-".equals(expSigStr)){
			exp = -1;
		}
		if(exp != 0 && expPartStr != null){
			exp *= Integer.parseInt(expPartStr); // TODO should this include radix as well?
		}

		int base = basePartStr != null ? Integer.parseInt(basePartStr) : DEFAULT_BASE;
		validate(base > 1, new NumberFormatException("Illegal base"));

		int[] joinedDigits = RealFloatUtils.joinArrays(intPart, decPart);
		exp -= decPart.length;

		RealFloatUtils.validate(base, NumberFormatException.class, joinedDigits);
		return bigEndianDigits(base, signum, exp, joinedDigits);
	}

	public static RealFloat fromDouble(double randomInt){
		return parseString(String.format("%s", randomInt));
	}

	public RealFloat round(int newExp){
		if(signum == 0){
			return new RealFloat(new int[0], newExp, base, 0);
		}
		if(exp == newExp){
			return this;
		}
		if(newExp < exp){
			int addDigits = exp - newExp;
			int[] newDigits = new int[digits.length + addDigits];
			System.arraycopy(digits, 0, newDigits, addDigits, digits.length);
//			Arrays.copyOf(digits, digits.length + addDigits);
			return new RealFloat(newDigits, newExp, base, signum);
		}

		int removeDigits = newExp - exp;
		if(digits.length <= removeDigits){
			return ZERO;
		}
		int[] newDigits = Arrays.copyOf(digits, digits.length - removeDigits);
		if(digits[newDigits.length] >= base / 2){
			newDigits[newDigits.length - 1]++;
			int[] extra = RealFloatUtils.carry(newDigits, base);
			if(extra.length > 0){ // we are rounding, so the number of digits doesn't matter
				if(BuildConfig.DEBUG && !(extra.length == 1 && extra[0] == 1)){
					throw new AssertionError();
				}
				int[] ret = new int[newDigits.length + extra.length];
				System.arraycopy(newDigits, 0, ret, 0, newDigits.length);
				System.arraycopy(extra, newDigits.length, ret, 0, extra.length);
				newDigits = ret;
			}
		}
		return new RealFloat(newDigits, newExp, base, signum);
	}

	public RealFloat negative(){
		return new RealFloat(digits, exp, base, -signum);
	}

	@SuppressWarnings("UnnecessaryThis")
	public RealFloat plus(RealFloat that){
		return prepareAddition(that).compute();
	}

	@SuppressWarnings("UnnecessaryThis")
	public Operation prepareAddition(RealFloat that){
		validate(this.base == that.base, new UnsupportedOperationException());

		int signum = 1;
		if(this.signum == 0){
			return new Operation.Literal(that);
		}
		if(that.signum == 0){
			return new Operation.Literal(this);
		}
		if(this.signum == 1 && that.signum == -1){
			return this.prepareSubtraction(that.negative());
		}
		if(this.signum == -1 && that.signum == 1){
			return that.prepareSubtraction(this.negative());
		}
		if(this.signum == -1 && that.signum == -1){
			signum = -1;
		}

		int[] thisDigits = this.digits;
		int[] thatDigits = that.digits;

		int exp = this.exp;
		if(this.exp > that.exp){
			thisDigits = RealFloatUtils.leftPadArray(thisDigits, this.exp - that.exp);
			exp = that.exp;
		}else if(that.exp > this.exp){
			thatDigits = RealFloatUtils.leftPadArray(thatDigits, that.exp - this.exp);
		}

		return new Operation.Arithmetic(true, exp, signum, base, thisDigits, thatDigits, false);
	}

	@SuppressWarnings("UnnecessaryThis")
	public RealFloat minus(RealFloat that){
		Operation operation = prepareSubtraction(that);
		debug(ToStringBuilder.reflectionToString(operation));
		return operation.compute();
	}

	public Operation prepareSubtraction(RealFloat that){
		if(base != that.base){
			throw new UnsupportedOperationException();
		}

		int signum = 1;
		if(this.signum == 0){
			return new Operation.Literal(that.negative());
		}
		if(that.signum == 0){
			return new Operation.Literal(this);
		}
		if(this.signum == 1 && that.signum == -1){
			return prepareAddition(that.negative());
		}
		if(this.signum == -1 && that.signum == 1){
			Operation prep = negative().prepareAddition(that);
			if(prep instanceof Operation.Arithmetic){
				((Operation.Arithmetic) prep).setPostNegation(true);
			}else if(prep instanceof Operation.Literal){
				((Operation.Literal) prep).setResult(((Operation.Literal) prep).getResult().negative());
			}
			return prep;
		}
		if(this.signum == -1 && that.signum == -1){
			signum = -1;
		}

		int cmp = RealFloatUtils.cmpAsc(digits, exp, that.digits, that.exp);
		if(cmp < 0){
			Operation prep = that.prepareSubtraction(this);
			if(prep instanceof Operation.Arithmetic){
				((Operation.Arithmetic) prep).setPostNegation(true);
			}else if(prep instanceof Operation.Literal){
				((Operation.Literal) prep).setResult(((Operation.Literal) prep).getResult().negative());
			}
			return prep;
		}
		if(cmp == 0){
			return new Operation.Literal(ZERO);
		}

		int[] thisDigits = digits;
		int[] thatDigits = that.digits;

		int exp = this.exp;
		if(this.exp > that.exp){
			thisDigits = RealFloatUtils.leftPadArray(thisDigits, this.exp - that.exp);
			exp = that.exp;
		}else if(that.exp > this.exp){
			thatDigits = RealFloatUtils.leftPadArray(thatDigits, that.exp - this.exp);
		}

		return new Operation.Arithmetic(false, exp, signum, base, thisDigits, thatDigits, false);
	}

	public interface Operation{
		RealFloat compute();

		@AllArgsConstructor
		@Getter
		@Setter
		class Literal implements Operation{
			RealFloat result;

			@Override
			public RealFloat compute(){
				return result;
			}
		}

		@AllArgsConstructor
		@Getter
		@Setter
		class Arithmetic implements Operation{
			boolean addition;
			int exp;
			int signum;
			int base;
			@lombok.NonNull int[] operand1;
			@lombok.NonNull int[] operand2;
			boolean postNegation;

			@Override
			public RealFloat compute(){
				int[] digits = addition ? RealFloatUtils.add(operand1, operand2, base) : RealFloatUtils.subtract(operand1, operand2, base);
				RealFloat f = new RealFloat(digits, exp, base, signum);
				return postNegation ? f.negative() : f;
			}
		}
	}

	@SuppressWarnings("UnnecessaryThis")
	public RealFloat times(RealFloat that){
		if(this.base != that.base){
			throw new UnsupportedOperationException();
		}

		int startExp = this.exp + that.exp;
		int[] multiply = new int[this.digits.length + that.digits.length];

		for(int i = 0; i < this.digits.length; i++){
			for(int j = 0; j < that.digits.length; j++){
				multiply[i + j] += digits[i] * digits[j];
			}
		}

		RealFloatUtils.carry(multiply, base);

		return bigEndianDigits(base, this.signum * that.signum, startExp, multiply);
	}

	@SuppressWarnings("UnnecessaryThis")
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof RealFloat)){
			return false;
		}
		RealFloat that = (RealFloat) obj;

		if(this.signum == 0){
			return that.signum == 0;
		}
		if(that.signum == 0){
			return false;
		}
		if(BuildConfig.DEBUG && (this.digits.length == 0 || that.digits.length == 0)){
			throw new AssertionError();
		}

		if(that.exp < this.exp){
			return that.equals(this);
		}
		int dExp = that.exp - this.exp;

		// dExp is number of zeroes in front of that.digits
//		debug("Comparing %s against %s, dExp = %d", this, that, dExp);
		for(int i = 0; i < Math.max(this.digits.length, that.digits.length + dExp); i++){
			int thatIndex = i - dExp;
			int thisDigit = 0 <= i && i < this.digits.length ? this.digits[i] : 0;
			int thatDigit = 0 <= thatIndex && thatIndex < that.digits.length ? that.digits[thatIndex] : 0;
//			debug("Cmp this[%d] = %d vs that[%d] = %d", i, thisDigit, thatIndex, thatDigit);
			if(thisDigit != thatDigit){
				return false;
			}
		}
		return true;
	}

	@Override
	public int intValue(){
		int out = 0;
		int i = Math.max(exp, 0);
		int unit = (int) Math.pow(base, exp + i);
		for(; i < digits.length; i++, unit *= base){
			out += digits[i] * unit;
		}
		return out * signum;
	}

	@Override
	public long longValue(){
		long out = 0;
		int i = Math.max(exp, 0);
		long unit = (long) Math.pow(base, exp + i);
		for(; i < digits.length; i++, unit *= base){
			out += unit * (long) digits[i];
		}
		return out * signum;
	}

	@Override
	public float floatValue(){
		float out = 0;
		float unit = (float) Math.pow(base, exp);
		for(int i = 0; i < digits.length; i++, unit *= base){
			out += unit * digits[i];
		}
		return out * signum;
	}

	@Override
	public double doubleValue(){
		double out = 0;
		double unit = Math.pow(base, exp);
		for(int i = 0; i < digits.length; i++, unit *= base){
			out += unit * digits[i];
		}
		return out * signum;
	}

	@Override
	public int compareTo(@NonNull RealFloat that){
		return compareTo(that, false);
	}

	@SuppressWarnings("UnnecessaryThis")
	public int compareTo(@NonNull RealFloat that, boolean ignoreSignum){
		if(!ignoreSignum){
			if(this.signum < that.signum){
				return -1;
			}
			if(this.signum > that.signum){
				return 1;
			}
		}
		if(signum == 0){ // both zero
			return 0;
		}

		if(this.base != that.base){
			throw new UnsupportedOperationException();
		}

		int ret = RealFloatUtils.cmpAsc(this.digits, this.exp, that.digits, that.exp) * (ignoreSignum ? 1 : signum);
		if(ret == 0 && BuildConfig.DEBUG && !this.equals(that)){
			throw new AssertionError();
		}
		return ret;
	}

	@Override
	public String toString(){
		if(signum == 0){
			return "0";
		}
		String d = new String(RealFloatUtils.intsToChars(digits));
		d = new StringBuilder(d).reverse().toString();
		return (signum == -1 ? "-" : "") + d + " E" + exp + " _" + base;
	}

	public String toUserString(boolean html){
		if(signum == 0){
			return "0";
		}
		StringBuilder output = new StringBuilder();
		if(signum == -1){
			output.append('-');
		}
		for(int i = digits.length - 1; i >= 0; i--){
			output.append(RealFloatUtils.intToChar(digits[i]));
		}

		if(0 < exp && exp <= 4){
			for(int i = 0; i < exp; i++){
				output.append('0');
			}
		}else if(0 > exp && exp >= -4){
			output.insert(output.length() + exp, '.');
		}else if(exp != 0){
			output.append(html ? "<sub>E</sub>" : " E")
					.append(String.format(Locale.getDefault(), "%d", exp));
		}
		if(base != DEFAULT_BASE){
			output.append(' ');
			String baseStr = String.format(Locale.getDefault(), "%d", base);
			if(html){
				output.append("<sub>").append(baseStr).append("</sub>");
			}else{
				output.append(baseStr);
			}
		}
		return output.toString();
	}

	@SuppressWarnings("deprecation")
	public Spanned toUserStringHtml(){
		return Main.fromHtml(toUserString(true));
	}
}
