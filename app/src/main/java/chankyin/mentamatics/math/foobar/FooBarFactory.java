package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;

public class FooBarFactory{
	@NonNull
	public static FooBar sumLessThan(FooBarRange fooRange, FooBarRange barRange, int sum){
		// TODO
		return new FooBar(fooRange.getMin(), barRange.getMin());
	}

	@NonNull
	public static FooBar fooLessThanEqualBar(FooBarRange fooRange, FooBarRange barRange){
		// TODO
		return new FooBar(fooRange.getMin(), barRange.getMin());
	}
}
