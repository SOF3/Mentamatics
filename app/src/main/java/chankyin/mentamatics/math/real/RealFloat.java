package chankyin.mentamatics.math.real;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.math.MathUtils;
import chankyin.mentamatics.math.real.annotation.AscendingDigits;
import chankyin.mentamatics.math.real.annotation.DescendingDigits;
import chankyin.mentamatics.math.real.annotation.Immutable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chankyin.mentamatics.math.MathUtils.*;

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
		this.signum = MathUtils.isZero(digits) ? 0 : signum;

		debug("Constructed RealFloat: " + toString());
	}

	public static RealFloat bigEndianDigits(int signum, int exp, @DescendingDigits int[] digits){
		return bigEndianDigits(DEFAULT_BASE, signum, exp, digits);
	}

	public static RealFloat smallEndianDigits(int signum, int exp, @DescendingDigits int[] digits){
		return smallEndianDigits(DEFAULT_BASE, signum, exp, digits);
	}

	public static RealFloat bigEndianDigits(@IntRange(from = 2) int base, int signum, int exp, @Immutable @DescendingDigits int[] digits){
		return bigEndianDigits(base, signum, exp, true, digits);
	}

	public static RealFloat smallEndianDigits(@IntRange(from = 2) int base, int signum, int exp, @Immutable @AscendingDigits int[] digits){
		digits = digits.clone();
		flipIntArray(digits);
		return bigEndianDigits(base, signum, exp, true, digits);
	}

	public static RealFloat bigEndianDigits(@IntRange(from = 2) int base, int signum, int exp, boolean trim, @Immutable @DescendingDigits int[] digits){
		MathUtils.validate(base, digits);

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
			if(end <= 0){
				throw new AssertionError();
			}

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

	public static RealFloat parseString(String string) throws NumberFormatException{
		debug("Parsing: " + string);
		Matcher matcher = parseFormat.matcher(string);
		if(!matcher.matches()){
			throw new NumberFormatException("Does not match format");
		}
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

		int[] intPart = MathUtils.charsToInts(intPartStr.toCharArray());
		int[] decPart = decPartStr == null ? new int[0] : MathUtils.charsToInts(decPartStr.toCharArray());

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
		if(base < 2){
			throw new NumberFormatException("Illegal base");
		}

		int[] joinedDigits = MathUtils.joinArrays(intPart, decPart);
		exp -= decPart.length;

		MathUtils.validate(base, NumberFormatException.class, joinedDigits);
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
			int[] extra = MathUtils.carry(newDigits, base);
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
//		@formatter:off
		if(this.base != that.base) throw new UnsupportedOperationException();

		int signum = 1;
		if(this.signum == 0) return that;
		if(that.signum == 0) return this;
		if(this.signum == 1 && that.signum == -1) return this.minus(that.negative());
		if(this.signum == -1 && that.signum == 1) return that.minus(this.negative());
		if(this.signum == -1 && that.signum == -1) signum = -1;
//		@formatter:on

		int[] thisDigits = this.digits;
		int[] thatDigits = that.digits;

		int exp = this.exp;
		if(this.exp > that.exp){
			thisDigits = MathUtils.leftPadArray(thisDigits, this.exp - that.exp);
			exp = that.exp;
		}else if(that.exp > this.exp){
			thatDigits = MathUtils.leftPadArray(thatDigits, that.exp - this.exp);
		}

		int[] add = MathUtils.add(thisDigits, thatDigits, base);
		return new RealFloat(add, exp, base, signum);
	}

	@SuppressWarnings("UnnecessaryThis")
	public RealFloat minus(RealFloat that){
//		@formatter:off
		if(this.base != that.base) throw new UnsupportedOperationException();

		int signum = 1;
		if(this.signum == 0) return that.negative();
		if(that.signum == 0) return this;
		if(this.signum == 1 && that.signum == -1) return this.plus(that.negative());
		if(this.signum == -1 && that.signum == 1) return this.negative().plus(that).negative();
		if(this.signum == -1 && that.signum == -1) signum = -1;
//		@formatter:on

		int cmp = MathUtils.cmp(this.digits, this.exp, that.digits, that.exp);
		if(cmp < 0){
			return that.minus(this).negative();
		}
		if(cmp == 0){
			return ZERO;
		}

		int[] thisDigits = this.digits;
		int[] thatDigits = that.digits;

		int exp = this.exp;
		if(this.exp > that.exp){
			thisDigits = MathUtils.leftPadArray(thisDigits, this.exp - that.exp);
			exp = that.exp;
		}else if(that.exp > this.exp){
			thatDigits = MathUtils.leftPadArray(thatDigits, that.exp - this.exp);
		}

		int[] subtract = MathUtils.subtract(thisDigits, thatDigits, base);
		return new RealFloat(subtract, exp, base, signum);
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

		MathUtils.carry(multiply, base);

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

		for(int i = 0; i < Math.max(this.digits.length, that.digits.length + dExp); i++){
			int thatIndex = dExp + i;
			int thisDigit = i < this.digits.length ? this.digits[i] : 0;
			int thatDigit = thatIndex < that.digits.length ? that.digits[thatIndex] : 0;
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
		if(this.signum < that.signum){
			return -1;
		}
		if(this.signum > that.signum){
			return 1;
		}
		if(signum == 0){
			return 0;
		}

		if(this.base != that.base){
			throw new UnsupportedOperationException();
		}

		return MathUtils.cmp(this.digits, this.exp, that.digits, that.exp) * (ignoreSignum ? 1 : signum);
	}

	@Override
	public String toString(){
		if(signum == 0){
			return "0";
		}
		String d = new String(MathUtils.intsToChars(digits));
		d = new StringBuilder(d).reverse().toString();
		return (signum == -1 ? "-" : "") + d + " E" + exp + " _" + base;
	}
}