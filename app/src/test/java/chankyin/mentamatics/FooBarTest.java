package chankyin.mentamatics;

import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.foobar.FooBarRobotics;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

public class FooBarTest{
	/**
	 * Tests {@link FooBarRobotics#sumLessThan(Random, FooBarRange, FooBarRange, int)}, where
	 * {@code foo + bar <= sum}
	 */
	@Test
	public void sum(){
		FooBarRange fooRange = new FooBarRange(109, 231);
		FooBarRange barRange = new FooBarRange(-53, 16);

		Random random = new Random(1);
		for(int i = 0; i < 100; ++i){
			int sum = new FooBarRange(fooRange.min + barRange.min, fooRange.max + barRange.max).generateRandom(random);
			FooBar fooBar = FooBarFactory.getInstance().sumLessThan(random, fooRange, barRange, sum);
			assertTrue("sum.matchFoo", fooRange.matches(fooBar.getFoo()));
			assertTrue("sum.matchBar", barRange.matches(fooBar.getBar()));
			assertTrue("sum.sum", fooBar.getFoo() + fooBar.getBar() <= sum);
		}
	}

	/**
	 * Tests {@link FooBarRobotics#fooLteBarPlus(Random, FooBarRange, FooBarRange, int)}, where
	 * {@code foo - bar <=  n}
	 */
	@Test
	public void ltePlus(){
		FooBarRange fooRange = new FooBarRange(97, 245);
		FooBarRange barRange = new FooBarRange(109, 231);
		Random random = new Random(2);

		for(int i = 0; i < 100; ++i){
			int n = new FooBarRange(fooRange.min - barRange.max, fooRange.max - barRange.min + random.nextInt(50)).generateRandom(random);
			FooBar fooBar = new FooBarRobotics().fooLteBarPlus(new Random(random.nextLong()), fooRange, barRange, n);

			assertTrue("ltePlus.matchFoo", fooRange.matches(fooBar.getFoo()));
			assertTrue("ltePlus.matchBar", barRange.matches(fooBar.getBar()));
			assertTrue("ltePlus.ltePlus", fooBar.getFoo() - fooBar.getBar() <= n);
		}
	}

	/**
	 * Tests {@link FooBarRobotics#fooGtBar(Random, FooBarRange, FooBarRange)}, where
	 * {@code foo > bar}
	 */
	@Test
	public void gt(){
		Random random = new Random(2);

		for(int i = 0; i < 100; ++i){
			FooBarRange fooRange = FooBarRange.autoSort(random.nextInt(1000), random.nextInt(1000));
			FooBarRange barRange = FooBarRange.autoSort(fooRange.max - random.nextInt(2000) - 1, fooRange.min + random.nextInt(2000) + 1);
			FooBar fooBar = new FooBarRobotics().fooGtBar(new Random(random.nextLong()), fooRange, barRange);

			assertTrue("gt.matchFoo", fooRange.matches(fooBar.getFoo()));
			assertTrue("gt.matchBar", barRange.matches(fooBar.getBar()));
			assertTrue("gt.gt", fooBar.getFoo() > fooBar.getBar());
		}
	}
	/**
	 * Tests {@link FooBarRobotics#fooGtBar(Random, FooBarRange, FooBarRange)}, where
	 * {@code foo >= bar}
	 */
	@Test
	public void gte(){
		Random random = new Random(2);

		for(int i = 0; i < 100; ++i){
			FooBarRange fooRange = FooBarRange.autoSort(random.nextInt(1000), random.nextInt(1000));
			FooBarRange barRange = FooBarRange.autoSort(fooRange.max - random.nextInt(2000), fooRange.min + random.nextInt(2000));
			FooBar fooBar = FooBarFactory.getInstance().fooGteBar(new Random(random.nextLong()), fooRange, barRange);

			assertTrue("gte.matchFoo", fooRange.matches(fooBar.getFoo()));
			assertTrue("gte.matchBar", barRange.matches(fooBar.getBar()));
			assertTrue("gte.gte", fooBar.getFoo() >= fooBar.getBar());
		}
	}
}
