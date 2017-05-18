package chankyin.mentamatics;

import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.foobar.FooBarRobotics;
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
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom11(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom12(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom13(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom14(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom15(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom16(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom17(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom18(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom19(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom20(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom21(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom22(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom23(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom24(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom25(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom26(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom27(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom28(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void sumRandom29(){
		doSum(new Random(sumTrials).nextLong());
	}

	@Test
	public void lte0(){
		doLte(0);
	}

	@Test
	public void lte1(){
		doLte(1);
	}

	@Test
	public void lte2(){
		doLte(2);
	}

	@Test
	public void lte3(){
		doLte(3);
	}

	@Test
	public void lte4(){
		doLte(4);
	}

	@Test
	public void lte5(){
		doLte(5);
	}

	@Test
	public void lte6(){
		doLte(6);
	}

	@Test
	public void lte7(){
		doLte(7);
	}

	@Test
	public void lte8(){
		doLte(8);
	}

	@Test
	public void lte9(){
		doLte(9);
	}

	@Test
	public void lteRandom10(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom11(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom12(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom13(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom14(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom15(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom16(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom17(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom18(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom19(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom20(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom21(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom22(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom23(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom24(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom25(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom26(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom27(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom28(){
		doLte(new Random(lteTrials).nextLong());
	}

	@Test
	public void lteRandom29(){
		doLte(new Random(lteTrials).nextLong());
	}

	private static int sumTrials = 0;
	private static int lteTrials = 0;

	public void doSum(long trial){
		System.err.println("Round " + ++sumTrials);

		long seed = new Random(trial).nextLong();
		Random random = new Random(seed);
		FooBarRange fooRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		FooBarRange barRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		int sum = new FooBarRange(fooRange.min + barRange.min, fooRange.max + barRange.max).generateRandom(random);

		FooBar fooBar = FooBarFactory.getInstance().sumLessThan(new Random(random.nextLong()), fooRange, barRange, sum);

		assertTrue("sum.matchFoo", fooRange.matches(fooBar.getFoo()));
		assertTrue("sum.matchBar", barRange.matches(fooBar.getBar()));
		assertTrue("sum.sum", fooBar.getFoo() + fooBar.getBar() <= sum);
	}

	public void doLte(long trial){
		System.err.println("Round #" + ++lteTrials);

		long seed = new Random(trial).nextLong();
		Random random = new Random(seed);
		FooBarRange fooRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		FooBarRange barRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		int n = new FooBarRange(fooRange.min - barRange.max, fooRange.max - barRange.min + 10).generateRandom(random);

		FooBarRobotics robotics = new FooBarRobotics();
		FooBar fooBar = robotics.fooLteBarPlus(new Random(random.nextLong()), fooRange, barRange, n);

		assertTrue("lte.matchFoo", fooRange.matches(fooBar.getFoo()));
		assertTrue("lte.matchBar", barRange.matches(fooBar.getBar()));
		assertTrue("lte.lte", fooBar.getFoo() <= fooBar.getBar() + n);
	}
}
