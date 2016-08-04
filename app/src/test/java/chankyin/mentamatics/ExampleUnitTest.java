package chankyin.mentamatics;

import chankyin.mentamatics.math.Decimal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest{
	@Test
	public void addition_isCorrect() throws Exception{
		assertEquals(Decimal.decimal(true, 4).toDouble(), Decimal.decimal(true, 2).toDouble() + Decimal.integer(true, 2).toDouble(), 1e-5);
	}
}