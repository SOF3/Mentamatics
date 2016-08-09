package chankyin.mentamatics.math.foobar;

import lombok.Value;

import java.util.Random;

@Value
public class FooBarRange{
	/**
	 * inclusive minimum
	 */
	int min;
	/**
	 * exclusive maximum
	 */
	int max;

	public int generateRandom(Random random){
		return random.nextInt(max - min) + min;
	}
}
