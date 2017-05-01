package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;

import java.util.Random;

import static chankyin.mentamatics.LogUtils.debug;

@SuppressWarnings({"unused", "ConstantConditions"})
@Deprecated
class dep_FooBarRobotics extends FooBarFactory{
	@Override
	@NonNull
	public FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum){
		debug("sumLessThan: fooRange = %s, barRange = %s, sum < %d", fooRange, barRange, sum);
		int p, q;
		int a = fooRange.max - fooRange.min;
		int b = barRange.max - barRange.min;
		// WANT:
		// 0 <= p <= a
		// 0 <= q <= b

		int r = sum - fooRange.min - barRange.min - 1;
		// WANT: p + q <= r
		int m = r - b;

		debug("sumLessThan: Want: 0 <= p <= %d, 0 <= q <= %d, p + q <= %d", a, b, r);

		int rectSize = r > b ? m * (b + 1) : 0;
		int gaussSize = (b + 2) * (b + 1) / 2;

		int k = random.nextInt(rectSize + gaussSize);
		debug("sumLessThan: m = %d, rectSize = %d, gaussSize = %d, k = %d",
				m, rectSize, gaussSize, k);
		if(k < rectSize){
			// generate rect
			debug("sumLessThan: generate rect");
			p = k / (b + 1);
			q = k % (b + 1);
		}else{
			// generate gauss
			k -= rectSize;
			debug("sumLessThan: generate gauss for k = %d", k);
//			double[] roots = MathUtils.solveQuadratic(1, 1, -2 * k);
//			int h = (int) Math.ceil(Math.max(roots[0], roots[1])); // number of rows below the row of k
//			int p0 = h + 1;
//			debug("sumLessThan: h = " + h + ", p0 = " + p0);
			int y = (int) Math.floor(1 / 2 * (Math.sqrt(8 * k + 1) - 1));
			debug("sumLessThan: y = %d", y);
			p = r - y;
			q = k - y * (y + 1) / 2;
		}

		debug("sumLessThan: p = %d, q = %d", p, q);
		return new FooBar(p + fooRange.min, q + barRange.min);
	}

	@Override
	@NonNull
	public FooBar fooGtBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		return null; // TODO
	}

	@Override
	@NonNull
	public FooBar fooGteBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		return null; // TODO
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

<!-- ISSUE START -->

SECTION GAUSS
m   | 0 1 2 ... r-(m+1) r-m  | b+1
m+1 | 0 1 2 ... r-(m+1)      | b
.   | .
:   | :
r   | 0                      | 1        <====== r > a can be true!
=============================
Size: (b+2) * (b+1) / 2

SECTION LINE
r+1 | 0                      | 1        <====== WTF! [p + q <= r] !!!!!
r+2 | 0                      | 1
.   | .
:   | :
a   | 0                      | 1
=============================
Size: a - r

 */

/*
SECTION GAUSS
y | q           | COUNT(q) | GT             | k(q[0])    | k(q[x])
--+-------------+----------+----------------+------------+--------------
0 | 0           | 1        | 1              | 0          | x
1 | 0 1         | 2        | 3              | 1          | x + 1
2 | 0 1 2       | 3        | 6              | 3          | x + 3
. | .           | .        | .              | .          | .
: | :           | :        | :              | :          | :
n | 0 1 2 ... n | n + 1    | (n+1)(n+2) / 2 | n(n+1) / 2 | n/2 (n+1) + x
. | .           | .        | .              | .          | .
: | :           | :        | :              | :          | :
b | 0 1 2 ... b | b+1      | (b+1)(b+2) / 2 | b(b+1) / 2 | b/2 (b+1) + x

Therefore:
k = y/2 (y+1) + q
 >= 1/2 y^2 + y/2
k - 1/2 y^2 - y/2 >= 0
floor(+y) for y^2/2 + y/2 - k = 0
y = floor(1/2 (sqrt(8k+1) - 1))
p = r - floor(1/2 (sqrt(8k+1) - 1))

// y=r-p
//If q = 0, p = 1/2 sqrt(8k+1) + r + 1/2 (r >= 0, therefore negative solution rejected)
//Coefficient of p^2 < 0, therefore p = floor(1/2 sqrt(8k+1) + r + 1/2)

<!-- ISSUE EXIST END -->
 */

/*
ISSUE discovered on 18/8/2016

REMOVE: SECTION LINE

CHANGE: SECTION GAUSS
- CAP the triangle at p <= a
- shall change the triangle into a trapezium
RECOMMENDED CHANGES:
#1 recalculate the algorithm: common difference = 1 ==> common difference = -1
#2 cut off left-bottom rectangle (m <= p <= a, 0 <= q <= COUNT(q[a]) - 1)

CORRECTION IMPLEMENTATION, adopting recommended change #2

p   | q               | COUNT(q)
====+=================+=========
0   | 0 1 2 … b       | b + 1     } This section wouldn't exist if r <= b, i.e. m <= 0
1   | 0 1 2 … b       | b + 1     }
…   | …               | …
m   | 0 1 2 … b       | b + 1
m+1 | 0 1 2 … b-2 b-1 | b
m+2 | 0 1 2 … b-2     | b - 1
…   | …               | …
a   | 0 … r-a
TODO!!!
 */
