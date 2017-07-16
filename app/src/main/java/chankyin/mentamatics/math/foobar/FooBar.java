package chankyin.mentamatics.math.foobar;

import lombok.Value;

@Value
public class FooBar{
	int foo;
	int bar;

	public FooBar swap(){
		return new FooBar(bar, foo);
	}
}
