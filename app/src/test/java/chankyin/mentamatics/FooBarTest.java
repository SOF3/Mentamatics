package chankyin.mentamatics;

import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class FooBarTest{
	@Test
	public void sum0(){
		doSum(0); //    p = 12, q = 18, s = 6;      a = 0,  b = 0;      A = 0,      B = 0,      C = 28
	}

	@Test
	public void sum1(){
		doSum(1); //    p = 3,  q = 34, s = 25;     a = 0,  b = 22;     A = 0,      B = 88,     C = 10
	}

	@Test
	public void sum2(){
		doSum(2); //    p = 64, q = 27, s = 83;     a = 56, b = 19;     A = 1568,   B = 171,    C = 45
	}

	@Test
	public void sum3(){
		doSum(3); //    p = 26, q = 71, s = 39;     a = 0,  b = 13;     A = 0,      B = 351,    C = 378
	}

	@Test
	public void sum4(){
		doSum(4); //    p = 10, q = 55, s = 59;     a = 3,  b = 48;     A = 168,    B = 384,    C = 36
	}

	@Test
	public void sum5(){
		doSum(5); //    p = 5,  q = 50, s = 37;     a = 0,  b = 32;     A = 0,      B = 192,    C = 21
	}

	@Test
	public void sum6(){
		doSum(6); //    p = 65, q = 12, s = 4;      a = 0,  b = 0;      A = 0,      B = 0,      C = 21
		// special: s is so small such that the x- and y-intercepts of x+y=s is within p and q
	}

	@Test
	public void sum7(){
		doSum(7); //    p = 28, q = 41, s = 56;     a = 15, b = 28;     A = 630,    B = 392,    C = 105
	}

	@Test
	public void sum8(){
		doSum(8); //    p = 8,  q = 9,  s = 8;      a = 0,  b = 0;      A = 0,      B = 0,      C = 45
	}

	@Test
	public void sum9(){
		doSum(9); //    p = 7,  q = 13, s = 20;     a = 7,  b = 13;     A = 98,     B = 13      C = 1
	}

	@Test
	public void sumRandom10(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom11(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom12(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom13(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom14(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom15(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom16(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom17(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom18(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom19(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom20(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom21(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom22(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom23(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom24(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom25(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom26(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom27(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom28(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	@Test
	public void sumRandom29(){
		doSum(Double.doubleToLongBits(Math.random()));
	}

	private static int trialCount = 0;

	public void doSum(long trial){
		System.err.println("Round " + ++trialCount);

		long seed = new Random().nextLong();
		Random random = new Random(seed);
		FooBarRange fooRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		FooBarRange barRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		FooBarRange sumRange = new FooBarRange(fooRange.min + barRange.min, fooRange.max + barRange.max);
		int sum = sumRange.generateRandom(random);

		FooBar fooBar = FooBarFactory.getInstance().sumLessThan(new Random(random.nextLong()), fooRange, barRange, sum);

		assertTrue("sum.matchFoo", fooRange.matches(fooBar.getFoo()));
		assertTrue("sum.matchBar", barRange.matches(fooBar.getBar()));
		assertTrue("sum.sum", fooBar.getFoo() + fooBar.getBar() <= sum);
	}
}
