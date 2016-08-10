package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;
import chankyin.mentamatics.math.MathUtils;

import java.util.Random;

public class FooBarFactory{
	@NonNull
	public static FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum){
		int p, q;
		int a = fooRange.max - fooRange.min;
		int b = barRange.max - barRange.min;
		// WANT:
		// 0 <= p <= a
		// 0 <= q <= b

		int r = sum - fooRange.min - barRange.min - 1;
		int m = r - b;

		int rectSize = r > b ? m * (b + 1) : 0;
		int gaussSize = (b + 2) * (b + 1) / 2;
		int lineSize = a > r ? a - r : 0;

		int k = random.nextInt(rectSize + gaussSize + lineSize);
		if(k < rectSize){
			// generate rect
			p = k / (b + 1);
			q = k % (b + 1);
		}else if(k >= rectSize + gaussSize){
			k -= rectSize + gaussSize;
			// generate line
			p = r + 1 + k; // choose row from line
			q = 0; // there is only one column in line
		}else{
			// generate gauss
			k -= rectSize;
			double[] roots = MathUtils.solveQuadratic(1, 1, -2 * k);
			int h = (int) Math.ceil(Math.max(roots[0], roots[1])); // number of rows below the row of k
			int p0 = h + 1;
			p = r - p0;
			q = k - h * (h + 1) / 2;
		}

		return new FooBar(p + fooRange.min, q + barRange.min);
	}
}

/*
p   | q                      | COUNT(q)
----+------------------------+---------

SECTION RECT
0   | 0 1 2 ... b            | b+1
1   | 0 1 2 ... b            | b+1
.   | .
:   | :
m-1 | 0 1 2 ... b            | b+1
=============================
Size: m * (b+1)

SECTION TRANSITIONAL (not evaluated)
m | 0 1 2 ... b, where b = r - m

SECTION GAUSS
m   | 0 1 2 ... r-(m+1) r-m  | b+1
m+1 | 0 1 2 ... r-(m+1)      | b
.   | .
:   | :
r   | 0                      | 1
=============================
Size: (b+2) * (b+1) / 2

SECTION LINE
r+1 | 0                      | 1
r+2 | 0                      | 1
.   | .
:   | :
a   | 0                      | 1
=============================
Size: a - r
 */
