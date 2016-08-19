package chankyin.mentamatics;

import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import org.junit.Test;

import java.util.Random;

import static chankyin.mentamatics.LogUtils.debug;
import static org.junit.Assert.assertTrue;

public class FooBarTest{
	@Test
	public void add(){
		for(int i = 0; i < 10; i++){
			doAdd();
		}
	}

	public void doAdd(){
		long seed = new Random().nextLong();
		Random random = new Random(seed);
		FooBarRange fooRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		FooBarRange barRange = FooBarRange.autoSort(random.nextInt(100), random.nextInt(100));
		int sum = new FooBarRange(fooRange.min + barRange.min, fooRange.max + barRange.max).generateRandom(random);

		FooBar fooBar = FooBarFactory.sumLessThan(new Random(), fooRange, barRange, sum);

		assertTrue("add.matchFoo", fooRange.matches(fooBar.getFoo()));
		assertTrue("add.matchBar", barRange.matches(fooBar.getBar()));
		assertTrue("add.sum", fooBar.getFoo() + fooBar.getBar() <= sum);

		debug("===========");
	}
}
