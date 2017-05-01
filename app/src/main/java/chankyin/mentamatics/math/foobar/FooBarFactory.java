package chankyin.mentamatics.math.foobar;

import android.support.annotation.NonNull;
import lombok.Getter;

import java.util.Random;

public abstract class FooBarFactory{
	@Getter(lazy = true)
	private final static FooBarFactory instance = new FooBarRobotics();

	@NonNull
	public abstract FooBar sumLessThan(Random random, FooBarRange fooRange, FooBarRange barRange, int sum);

	@NonNull
	public abstract FooBar fooGtBar(Random random, FooBarRange fooRange, FooBarRange barRange);

	@NonNull
	public abstract FooBar fooGteBar(Random random, FooBarRange fooRange, FooBarRange barRange);
}
