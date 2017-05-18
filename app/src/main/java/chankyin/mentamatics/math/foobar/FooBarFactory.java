package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;

import java.util.Random;

public abstract class FooBarFactory{
	private static FooBarFactory instance = null;

	public static FooBarFactory getInstance(){
		if(instance == null){
			instance = new FooBarRobotics();
		}
		return instance;
	}

	@NonNull
	public abstract FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum);

	@NonNull
	public abstract FooBar fooGtBar(Random random, FooBarRange fooRange, FooBarRange barRange);

	@NonNull
	public abstract FooBar fooGteBar(Random random, FooBarRange fooRange, FooBarRange barRange);
}
