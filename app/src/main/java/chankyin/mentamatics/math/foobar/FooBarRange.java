package chankyin.mentamatics.math.foobar;

import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class FooBarRange{
	/**
	 * inclusive minimum
	 */
	public final int min;
	/**
	 * exclusive maximum
	 */
	public final int max;

	public int generateRandom(Random random){
		return random.nextInt(max - min) + min;
	}
}
