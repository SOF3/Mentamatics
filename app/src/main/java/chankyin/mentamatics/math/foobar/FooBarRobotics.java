package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;
import android.support.annotation.Size;

import java.util.Random;

import static chankyin.mentamatics.math.MathUtils.*;
import static java.lang.Math.*;

public class FooBarRobotics extends FooBarFactory{
	@NonNull
	@Override
	public FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum){
		int p = fooRange.max - fooRange.min;
		int q = barRange.max - barRange.min;
		int s = sum - fooRange.min - barRange.min;
		int a = min(p, max(0, s - q));
		int b = min(q, max(0, s - p));

//		if(p + q <= s){
//			int k = random.nextInt((p + 1) * (q + 1));
//			System.err.println("k = " + k);
//			int[] xy = MathUtils.offsetToPositionInRectangle(k, p + 1);
//			return new FooBar(fooRange.min + xy[0], barRange.min + xy[1]);
//		}

		int sizeA = a * (q + 1);
		int sizeB = (p - a + 1) * b; // min(p, s) is not necessary because b == 0 if s < p
		int sizeC = (min(p, s) - a + 1) * (min(p, s) - a + 2) / 2; // min(p, s) is necessary to ensure only the innermost triangle is evaluated

		int k = random.nextInt(sizeA + sizeB + sizeC);
		int x, y;
		int[] ij;
		if(k < sizeA){
			ij = offsetToPositionInRectangle(k, q + 1);
		}else if(k < sizeA + sizeB){
			ij = offsetToPositionInRectangle(k - sizeA, b);
			ij[0] += a;
		}else{
			ij = offsetToPositionInRightIsosTriangle(k - sizeA - sizeB);
			ij[0] = min(p, s) - ij[0];
			ij[1] += b;
		}
		return new FooBar(ij[0] + fooRange.min, ij[1] + barRange.min);
	}

	@NonNull
	@Override
	public FooBar fooGtBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		return fooLteBarPlus(random, barRange, fooRange, -1);
	}

	@NonNull
	@Override
	public FooBar fooGteBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		return fooLteBarPlus(random, barRange, fooRange, 0);
	}

	public FooBar fooLteBarPlus(Random random, FooBarRange fooRange, FooBarRange barRange, int n){
		// a <= x <= b
		// c <= y <= d
		// x <= y + n

		int p = fooRange.max - fooRange.min;
		int q = barRange.max - barRange.min;
		int t = barRange.min + n - fooRange.min;

		int f = min(max(t, 0), p);
		int g = max(min(p - t, q), 0);
		int h = max(-t, 0);
		int k = min(q + t, p);

		int sizeA = (k + 1) * (q - g);
		int sizeB = f * (g - h + 1);
		int sizeC = (k - f + 1) * (k - f + 2) / 2;

		int rand = random.nextInt(sizeA + sizeB + sizeC);
		@Size(2) int[] ij;
		if(rand < sizeA){
			ij = offsetToPositionInRectangle(rand, q - g);
			ij[1] += g + 1;
		}else if((rand -= sizeA) < sizeB){
			ij = offsetToPositionInRectangle(rand, g - h + 1);
		}else{
			rand -= sizeB;
			if(rand >= sizeC){
				throw new AssertionError();
			}
			ij = offsetToPositionInRightIsosTriangle(rand);
			ij[0] = k - ij[0];
			ij[1] = g - ij[1];
		}

		return new FooBar(ij[0] + fooRange.min, ij[1] + barRange.min);
	}
}
