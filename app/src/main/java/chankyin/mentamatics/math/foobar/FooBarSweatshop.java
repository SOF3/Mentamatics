package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class FooBarSweatshop extends FooBarFactory{
	@Override
	@NonNull
	public FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum){
		List<FooBar> list = new LinkedList<>(); // more efficient in add(E)

		for(int foo = fooRange.min; foo <= fooRange.max; foo++){
			// foo + bar < sum
			// bar < sum - foo
			for(int bar = barRange.min; bar < sum - foo && bar <= barRange.max; bar++){
				list.add(new FooBar(foo, bar));
			}
		}

		return randomFromList(random, list);
	}

	@Override
	@NonNull
	public FooBar fooGtBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		if(fooRange.max <= barRange.min){
			throw new IllegalArgumentException();
		}

		List<FooBar> list = new LinkedList<>();
		for(int foo = fooRange.min; foo <= fooRange.max; foo++){
			// bar < foo
			for(int bar = barRange.min; bar <= barRange.max && bar < foo; bar++){
				list.add(new FooBar(foo, bar));
			}
		}

		return randomFromList(random, list);
	}

	@Override
	@NonNull
	public FooBar fooGteBar(Random random, FooBarRange fooRange, FooBarRange barRange){
		if(fooRange.max < barRange.min){
			throw new IllegalArgumentException();
		}

		List<FooBar> list = new LinkedList<>();
		for(int foo = fooRange.min; foo <= fooRange.max; foo++){
			// bar < foo
			for(int bar = barRange.min; bar <= barRange.max && bar <= foo; bar++){
				list.add(new FooBar(foo, bar));
			}
		}

		return randomFromList(random, list);
	}

	private static FooBar randomFromList(Random random, List<FooBar> fooBars){
		return fooBars.get(random.nextInt(fooBars.size()));
	}
}
