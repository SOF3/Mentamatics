package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;
import chankyin.mentamatics.math.MathUtils;

import java.util.Random;

public class FooBarRobotics extends FooBarFactory{
	@NonNull
	@Override
	public FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum){
		int p = fooRange.max - fooRange.min;
		int q = barRange.max - barRange.min;
		int a = sum - q;
		int b = sum - p;

		int sizeA = a * (q + 1);
		int sizeB = (p - a + 1) * b;
		int sizeC = (p - a + 1) * (p - a) / 2;

		System.err.println(sizeA);
		System.err.println(sizeB);
		System.err.println(sizeC);
		int k = random.nextInt(sizeA + sizeB + sizeC);
		int x, y;
		if(k < sizeA){
			x = k / (q + 1);
			y = k % (q + 1);
		}else if(k < sizeB){
			k -= sizeB;
			x = a + k / b;
			y = k % b;
		}else{
			int[] ij = MathUtils.offsetToPositionInRightIsosTriangle(k - sizeA - sizeB);
			x = p - ij[0];
			y = b + ij[1];
		}
		return new FooBar(x + fooRange.min, y + barRange.min);
	}

	@NonNull
	@Override
	public FooBar fooGtBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		return null;
	}

	@NonNull
	@Override
	public FooBar fooGteBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		return null;
	}
}
