package chankyin.mentamatics.math.foobar;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Random;

@RequiredArgsConstructor
@ToString
public class FooBarRange{
	public static FooBarRange autoSort(int a, int b){
		return new FooBarRange(Math.min(a, b), Math.max(a, b));
	}

	/**
	 * inclusive minimum
	 */
	public final int min;
	/**
	 * inclusive maximum
	 */
	public final int max;

	public int generateRandom(Random random){
		return random.nextInt(max - min + 1) + min;
	}

	public boolean matches(int num){
		return min <= num && num <= max;
	}
}
