package chankyin.mentamatics;

import chankyin.mentamatics.math.real.RealNumber;
import org.junit.Test;

import static junit.framework.Assert.*;

public class RealNumberTest{
	@Test
	public void parseClean(){
		assertEquals("parseClean", 12345, RealNumber.parseString("12345").doubleValue(), 1e-3);
	}

	@Test
	public void parseCleanZeroesLeft(){
		assertEquals("parseCleanZeroesLeft", 12345, RealNumber.parseString("0012345").doubleValue(), 1e-3);
	}

	@Test
	public void parseCleanZeroesRight(){
		assertEquals("parseCleanZeroesRight", 1234500, RealNumber.parseString("1234500").doubleValue(), 1e-3);
	}

	@Test
	public void parseCleanZeroesBoth(){
		assertEquals("parseCleanZeroesBoth", 1234500, RealNumber.parseString("001234500").doubleValue(), 1e-3);
	}

	@Test
	public void parseDec(){
		assertEquals("parseDec", 1234.56, RealNumber.parseString("1234.56").doubleValue(), 1e-5);
	}

	@Test
	public void parseDecZeroesLeft(){
		assertEquals("parseDecZeroesLeft", 1234.56, RealNumber.parseString("1234.5600").doubleValue(), 1e-5);
	}

	@Test
	public void parseDecZeroesBoth(){
		assertEquals("parseDecZeroesLeft", 1234.56, RealNumber.parseString("001234.5600").doubleValue(), 1e-5);
	}

	@Test
	public void parseSigDec(){
		assertEquals("parseSigDec", -123.456, RealNumber.parseString("-123.456").doubleValue(), 1e-5);
	}

	@Test
	public void parseSigDecExp(){
		assertEquals("parseSigDecExp", -1234.56, RealNumber.parseString("-12.3456e+2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpZeroesPositive(){
		assertEquals("parseExpZeroesPositive", 1234.56, RealNumber.parseString("0012.345600e+2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpZeroesNegative(){
		assertEquals("parseExpZeroesNegative", 0.123456, RealNumber.parseString("0012.345600e-2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpNoDecPositive(){
		assertEquals("parseExpNoDecPositive", 12345600, RealNumber.parseString("123456e+2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpNoDecNegative(){
		assertEquals("parseExpNoDecNegative", 1234.56, RealNumber.parseString("123456e-2").doubleValue(), 1e-5);
	}

	@Test
	public void parseBase(){
		assertEquals("parseBase", 34 * 36 + 35 + 13d / 36d, RealNumber.parseString("YZDe-1_36").doubleValue(), 1e-5);
	}

	@Test
	public void equalsExp(){
		assertEqualsReflexive("equalsExp", RealNumber.parseString("00123400e+3"), RealNumber.parseString("00001234e+5"));
	}

	@Test
	public void addition(){
		assertEqualsReflexive("addition", RealNumber.parseString("012345").plus(RealNumber.parseString("678900e-1")), RealNumber.parseString("0802.35e+2"));
	}

	private static void assertEqualsReflexive(String message, Object foo, Object bar){
		assertTrue(message, foo.equals(bar));
		assertTrue(message, bar.equals(foo));
	}
}
