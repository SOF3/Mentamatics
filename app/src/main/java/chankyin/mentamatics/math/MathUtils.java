package chankyin.mentamatics.math;

import android.support.annotation.IntRange;
import android.support.annotation.Size;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.math.real.annotation.AscendingDigits;
import chankyin.mentamatics.math.real.annotation.DescendingDigits;
import chankyin.mentamatics.math.real.annotation.Immutable;
import chankyin.mentamatics.math.real.annotation.Mutable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class MathUtils{
	@AscendingDigits
	public static int[] carry(@Mutable @AscendingDigits int[] digits, @IntRange(from = 2) int base){
		int carry = 0;
		for(int i = 0; i < digits.length; i++){
			int newDigit = digits[i] + carry;
			carry = newDigit / base;
			digits[i] = newDigit % base;
		}
		if(carry > 0){
			int[] extra = new int[(int) (Math.log(carry) / Math.log(base)) + 1];
			for(int n = 0; n < extra.length; n++){
				extra[n] = carry % base;
				carry /= base;
			}
			if(carry > 0){
				throw new AssertionError();
			}
			return extra;
		}else{
			return new int[0];
		}
	}

	@AscendingDigits
	public static void borrow(@Mutable @AscendingDigits int[] digits, @IntRange(from = 2) int base){
		int borrow = 0;
		for(int i = 0; i < digits.length; i++){
			int digit = digits[i];
			digit -= borrow;
			borrow = 0;
			while(digit < 0){
				borrow++;
				digit += base;
			}
			digits[i] = digit;
		}
		if(borrow > 0){
			throw new IllegalArgumentException();
		}
	}

	@AscendingDigits
	public static int[] add(@Immutable @AscendingDigits int[] foo, @Immutable @AscendingDigits int[] bar, @IntRange(from = 2) int base){
		if(foo.length < bar.length){
			return add(bar, foo, base);
		}

		int[] add = foo.clone();
		for(int i = 0; i < bar.length; i++){
			add[i] += bar[i];
		}

		int[] carry = carry(add, base);
		if(carry.length > 0){
			add = joinArrays(add, carry);
		}

		return add;
	}

	@AscendingDigits
	public static int[] subtract(@Immutable @AscendingDigits int[] left, @Immutable @AscendingDigits int[] right, @IntRange(from = 2) int base){
		if(BuildConfig.DEBUG && cmp(left, right) <= 0){
			throw new IllegalArgumentException();
		}

		int[] subtract = left.clone();
		for(int i = 0; i < right.length; i++){
			subtract[i] -= right[i];
		}

		borrow(subtract, base);

		int length = 0;
		for(int i = subtract.length - 1; i >= 0; i--){
			if(subtract[i] != 0){
				length = i + 1;
			}
		}

		return length == subtract.length ? subtract : Arrays.copyOf(subtract, length);
	}

	public static void validate(int base, @Immutable int[]... subjects){
		validate(base, IndexOutOfBoundsException.class, subjects);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> void validate(int base, Class<T> t, @Immutable int[]... subjects) throws T{
		if(BuildConfig.DEBUG){
			for(int[] subject : subjects){
				for(int digit : subject){
					if(digit < 0 || digit >= base){
						String message = String.format(Locale.ENGLISH, "0 <= %d < %d is false", digit, base);
						try{
							throw t.getConstructor(String.class).newInstance(message);
						}catch(Exception e){
							if(e.getClass() == t){
								throw (T) e;
							}else{
								throw new RuntimeException(e);
							}
						}
					}
				}
			}
		}
	}

	public static boolean isZero(int[] digits){
		for(int digit : digits){
			if(digit != 0){
				return false;
			}
		}
		return true;
	}

	public static int cmp(@Immutable @AscendingDigits int[] foo0, int fooExp, @Immutable @AscendingDigits int[] bar0, int barExp){
		@DescendingDigits int[] foo = foo0.clone();
		flipIntArray(foo);
		@DescendingDigits int[] bar = bar0.clone();
		flipIntArray(bar);

		if(fooExp > barExp){
			foo = Arrays.copyOf(foo, foo.length + fooExp - barExp);
		}else if(fooExp < barExp){
			bar = Arrays.copyOf(bar, bar.length + barExp - fooExp);
		}
		// now both are of the same exponent

		return cmp(foo, bar);
	}

	private static int cmp(@Immutable @DescendingDigits int[] foo, @Immutable @DescendingDigits int[] bar){
		if(foo.length > bar.length){
			return 1;
		}
		if(foo.length < bar.length){
			return -1;
		}
		for(int i = 0; i < foo.length; i++){
			if(foo[i] > bar[i]){
				return 1;
			}
			if(foo[i] < bar[i]){
				return -1;
			}
		}
		return 0;
	}

	public static int charToInt(char c) throws NumberFormatException{
		if('0' <= c && c <= '9'){
			return c - '0';
		}else if('a' <= c && c <= 'z'){
			return c - 'a' + 10;
		}else if('A' <= c && c <= 'Z'){
			return c - 'A' + 10;
		}
		throw new NumberFormatException("Unknown digit");
	}

	public static int[] charsToInts(char[] input) throws NumberFormatException{
		int[] output = new int[input.length];
		for(int i = 0; i < input.length; i++){
			output[i] = charToInt(input[i]);
		}
		return output;
	}

	public static char intToChar(int i) throws IndexOutOfBoundsException{
		char c = Character.forDigit(i, Character.MAX_RADIX);
		if(c == '\0'){
			throw new IndexOutOfBoundsException();
		}
		return c;
	}

	public static char[] intsToChars(int[] input) throws IndexOutOfBoundsException{
		char[] output = new char[input.length];
		for(int i = 0; i < input.length; i++){
			output[i] = intToChar(input[i]);
		}
		return output;
	}

	public static void flipIntArray(@Mutable int[] array){
		for(int i = 0; i < array.length / 2; i++){
			int opposite = array.length - 1 - i;
			int swap = array[i];
			array[i] = array[opposite];
			array[opposite] = swap;
		}
	}

	public static int[] joinArrays(@Immutable int[]... arrays){
		int sum = 0;
		for(int[] array : arrays){
			sum += array.length;
		}
		int[] out = new int[sum];
		int offset = 0;

		for(int[] array : arrays){
			System.arraycopy(array, 0, out, offset, array.length);
			offset += array.length;
		}
		return out;
	}

	public static int[] leftPadArray(@Immutable int[] array, @IntRange(from = 1) int diff){
		int[] out = new int[array.length + diff];
		System.arraycopy(array, 0, out, diff, array.length);
		return out;
	}

	/**
	 * @param random random object to create pseudorandomness with
	 * @param length length of array to generate
	 * @param min    inclusive minimum value in array
	 * @param max    exclusive maximum value in array
	 * @return an array of length {@code length} filled with integers where {@code min <= i < max}
	 */
	public static int[] randomRangeArray(Random random, int length, int min, int max){
		int[] array = new int[length];
		for(int i = 0; i < length; i++){
			array[i] = random.nextInt(max - min) + min;
		}
		return array;
	}

	@Size(2)
	public static double[] solveQuadratic(int a, int b, int c){
		int d = b * b - 4 * a * c;
		if(d < 0){
			throw new IndexOutOfBoundsException("Cannot solve imaginary problem");
		}
		double root = Math.sqrt(d);
		return new double[]{
				(-b + root) / (2 * a),
				(-b - root) / (2 * a),
		};
	}

	@IntRange(from = 0)
	public static int positionToOffsetInRightIsosTriangle(@IntRange(from = 0) int i, @IntRange(from = 0) int j){
		// row above is the ith row
		// (j+1)th column in row

		return (i + 1) * i / 2 + j;
	}

	@Size(2)
	public static int[] offsetToPositionInRightIsosTriangle(@IntRange(from = 0) int offset){
		int i = (int) Math.ceil((-1 + Math.sqrt(8 * (offset + 1))) / 2) - 1;
		int j = offset - (i + 1) * i / 2;
		return new int[]{i, j};
	}

	/**
	 * @param offset offset counting in vertical then horizontal order
	 * @param height  vertical length
	 * @return {horizontal index, vertical index}
	 */
	@Size(2)
	public static int[] offsetToPositionInRectangle(int offset, int height){
		return new int[]{
				offset / height,
				offset % height
		};
	}
}
