package chankyin.mentamatics;

import android.annotation.SuppressLint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@EqualsAndHashCode
public class Decimal implements Comparable<Decimal>{
	public final static int DIGIT_DECIMAL_POINT = -1;
	public final static int DEFAULT_BASE = 10;

	public final static Decimal ZERO = new Decimal(new int[0], 0, DEFAULT_BASE, true);

	int[] digits; // 0-trimmed
	int exponent;
	int base;
	boolean positive;

	private Decimal(int[] digits, int exponent, int base, boolean positive){
		this(digits, exponent, base, positive, false);
	}

	private Decimal(int[] digits, int exponent, int base, boolean positive, boolean noRightTrim){
		int start = -1;
		int end = 0;

		for(int i = 0; i < digits.length; i++){
			if(digits[i] != 0){
				start = i;
				break;
			}
		}
		if(start == -1){
			this.digits = new int[0];
			this.exponent = 0;
		}else{
			if(noRightTrim){
				end = digits.length;
			}
			for(int i = digits.length; i > 0; i--){
				if(digits[i - 1] != 0){
					end = i;
					break;
				}
			}
			if(end == 0 || end <= start){
				throw new AssertionError();
			}

			this.digits = new int[end - start];
			System.arraycopy(digits, start, this.digits, 0, end - start);

			this.exponent = exponent + digits.length - end;
		}

		this.base = base;
		this.positive = positive;
	}

	public static Decimal integer(boolean positive, int... digits){
		return integerBase(positive, DEFAULT_BASE, digits);
	}

	public static Decimal integerBase(boolean positive, int base, int... digits){
		for(int digit : digits){
			if(digit < 0 || digit >= base){
				throw new IndexOutOfBoundsException();
			}
		}
		return new Decimal(digits, 0, base, positive);
	}

	public static Decimal decimal(boolean positive, int... digits){
		return decimalBase(positive, DEFAULT_BASE, digits);
	}

	public static Decimal decimalBase(boolean positive, int base, int... digits){
		boolean exponentSet = false;
		int exponent = 0;
		int trim = -1;
		for(int i = 0; i < digits.length; i++){
			if(digits[i] != 0){
				trim = i;
				break;
			}
		}
		if(trim > 0){
			digits = Arrays.copyOfRange(digits, trim, digits.length);
		}
		for(int i = 0; i < digits.length; i++){
			if(digits[i] < 0 || digits[i] >= base){
				throw new IndexOutOfBoundsException();
			}
			if(digits[i] == DIGIT_DECIMAL_POINT){
				if(exponentSet){
					throw new IllegalArgumentException("Multiple decimal points");
				}
				exponent = i + 1 - digits.length;
				exponentSet = true;
			}
		}
		int[] plain;
		if(exponentSet){
			plain = new int[digits.length - 1];
			int j = 0;
			for(int digit : digits){
				if(digit != DIGIT_DECIMAL_POINT){
					plain[j++] = digit;
				}
			}
		}else{
			plain = digits;
		}
		return new Decimal(plain, exponent, base, positive);
	}

	@SuppressWarnings("UnnecessaryThis")
	public Decimal plus(@NonNull Decimal that){
		if(that.equalsIgnoreExponent(ZERO)){
			return this;
		}

		if(this.base != that.base){
			throw new UnsupportedOperationException();
//			int lcm = Main.lcm(this.base, that.base);
//			return (this.base == lcm ? this : this.changeBase(lcm)).plus(that.base == lcm ? that : that.changeBase(lcm));
		}
		if(that.exponent > this.exponent){
			return this.plus(that.shiftExponentTo(this.exponent));
		}else if(this.exponent > that.exponent){
			return that.plus(this.shiftExponentTo(that.exponent));
		}


		if(this.positive && !that.positive){ // a, -b
			return this.minus(that); // a + (-b) = a - b
		}
		if(!this.positive){ // -a
			return that.positive ? // -a, b
					that.minus(this.negative()) // (-a) + b = b - |a|
					: // -a, -b
					this.negative().plus(that.negative()).negative(); // (-a) + (-b) = -(|a| + |b|)
		}
		// a, b: a + b

		int[] digits;
		if(this.digits.length == that.digits.length && this.digits[0] + that.digits[0] > base){
			digits = new int[this.digits.length + 1];
		}else{
			digits = new int[Math.max(this.digits.length, that.digits.length)];
		}

		boolean carry = false;
		for(int i = 1; i <= Math.max(this.digits.length, that.digits.length); i++){
			int thisDigit = 0;
			if(this.digits.length >= i){
				thisDigit = this.digits[this.digits.length - i];
			}
			int thatDigit = 0;
			if(that.digits.length >= i){
				thatDigit = that.digits[that.digits.length - i];
			}
			int sumDigit = thisDigit + thatDigit + (carry ? 1 : 0);
			if(carry = sumDigit >= base){
				sumDigit -= base;
				carry = true;
			}
			digits[digits.length - i] = sumDigit;
		}

		if(carry){
			if(digits[0] != 0){
				throw new AssertionError();
			}
			digits[0] = 1;
		}

		return new Decimal(digits, exponent, base, true);
	}

	@SuppressWarnings("UnnecessaryThis")
	public Decimal minus(@NonNull Decimal that){
		if(that.equalsIgnoreExponent(ZERO)){
			return this;
		}

		if(this.base != that.base){
			throw new UnsupportedOperationException();
			// TODO change base
		}
		if(that.exponent > this.exponent){
			return this.minus(that.shiftExponentTo(this.exponent));
		}else if(this.exponent > that.exponent){
			return that.minus(this.shiftExponentTo(that.exponent));
		}

		if(this.positive && !that.positive){ // a, -b
			return this.plus(that.negative()); // a - (-b) = a + |b|
		}
		if(!this.positive){ // -a
			return that.positive ? // -a, b
					this.negative().plus(that).negative() // (-a) - b = -(|a| + b)
					: // -a, -b
					that.negative().minus(this.negative()); // (-a) - (-b) = |b| - |a|
		}
		// a, b: a - b
		if(this.signumAgainst(that) == -1){
			return that.minus(this).negative();
		}

		if(this.digits.length < that.digits.length){
			throw new AssertionError();
		}

		int[] digits = new int[this.digits.length];

		boolean borrow = false;
		for(int i = 1; i <= digits.length; i++){
			@SuppressWarnings("UnusedAssignment")
			int thisDigit = 0;
			if(this.digits.length >= i){
				thisDigit = this.digits[this.digits.length - i];
			}else{
				throw new AssertionError(); // subtraction on smaller number?
			}
			int thatDigit = 0;
			if(this.digits.length >= i){
				thatDigit = this.digits[this.digits.length - i];
			}
			int newDigit = thisDigit - thatDigit;
			if(borrow){
				newDigit--;
			}
			if(borrow = newDigit < 0){
				newDigit += base;
			}
			digits[digits.length - i] = newDigit;
		}
		if(borrow){
			throw new AssertionError(); // subtraction on smaller number?
		}

		return new Decimal(digits, exponent, base, true);
	}

	public Decimal changeBase(int base){
		return null; // TODO
	}

	public Decimal shiftExponentTo(int exponent){
		if(exponent >= this.exponent){
			return cutBehind(exponent - this.exponent);
		}
		int[] digits = new int[this.digits.length + this.exponent - exponent];
		System.arraycopy(this.digits, 0, digits, 0, this.digits.length);
		return new Decimal(digits, exponent, base, positive, true);
	}

	private Decimal cutBehind(@IntRange(from = 0) int i){
		if(i == 0){
			return this;
		}
		int[] digits;
		if(this.digits.length <= i){
			return ZERO;
		}
		int newExponent = exponent + i;
		digits = new int[this.digits.length - i];
		System.arraycopy(this.digits, 0, digits, 0, digits.length);
		return new Decimal(digits, newExponent, base, positive);
	}

	public Decimal negative(){
		return new Decimal(digits, exponent, base, !positive);
	}

	public double toDouble(){
		double output = 0d;
		for(int i = digits.length - 1, j = 0; i >= 0; i--, j++){
			output += digits[i] * Math.pow(base, j + exponent);
		}
		return output * (positive ? 1 : -1);
	}

	public int signum(){
		return isZero() ? 0 : positive ? 1 : -1;
	}

	public boolean equalsIgnoreExponent(Decimal that){
		if(that == ZERO){
			return isZero();
		}
		return signumAgainst(that) == 0;
	}

	public boolean isZero(){
		return digits.length == 0;
	}

	@SuppressWarnings("UnnecessaryThis")
	public int signumAgainst(Decimal that){
		int thisSignum = this.signum();
		int thatSignum = that.signum();
		if(thisSignum > thatSignum){
			return 1;
		}
		if(thisSignum < thatSignum){
			return -1;
		}
		if(isZero()){
			return -that.signum();
		}
		if(that.isZero()){
			return this.signum();
		}
		return absSignumAgainst(that) * (positive ? 1 : -1);
	}

	@SuppressLint("Assert")
	@SuppressWarnings("UnnecessaryThis")
	public int absSignumAgainst(Decimal that){
		int thisExp0 = this.getOffsetExponent(0);
		int thatExp0 = that.getOffsetExponent(0);
		if(thisExp0 > thatExp0){
			return 1;
		}
		if(thisExp0 < thatExp0){
			return -1;
		}

		for(int i = 0; i < Math.max(this.digits.length, that.digits.length); i++){
			if(i >= this.digits.length){
				assert i < that.digits.length; // according to for condition
				// this: 1.111, that: 1.111xxx; this < that
				return -1;
			}
			if(i >= that.digits.length){
				assert i > that.digits.length;
				// this: 1.111xxx, that: 1.111; this > that
				return 1;
			}
			int thisDigit = this.digits[i];
			int thatDigit = that.digits[i];
			if(thisDigit > thatDigit){
				return 1;
			}
			if(thisDigit < thatDigit){
				return -1;
			}
		}

		return 0;
	}

	@IntRange(from = 0)
	public int getOffsetExponent(@IntRange(from = 0) int i){
		if(i < 0 || i >= digits.length){
			throw new IndexOutOfBoundsException();
		}
		return exponent + digits.length - 1 - i;
	}

	@Override
	public int compareTo(@NonNull Decimal other){
		return signumAgainst(other);
	}
}
