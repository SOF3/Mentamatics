package chankyin.mentamatics;

import chankyin.mentamatics.math.real.RealFloat;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class RealFloatTest{
	@Test
	public void parseClean(){
		assertEquals("parseClean", 12345, RealFloat.parseString("12345").doubleValue(), 1e-3);
	}

	@Test
	public void parseVsLittleEndian(){
		assertEqualsSymmetric("parseVsLittleEndian", RealFloat.parseString("12345"), RealFloat.littleEndianDigits(1, 0, new int[]{5, 4, 3, 2, 1}));
	}

	@Test
	public void parseCleanZeroesLeft(){
		assertEquals("parseCleanZeroesLeft", 12345, RealFloat.parseString("0012345").doubleValue(), 1e-3);
	}

	@Test
	public void parseCleanZeroesRight(){
		assertEquals("parseCleanZeroesRight", 1234500, RealFloat.parseString("1234500").doubleValue(), 1e-3);
	}

	@Test
	public void parseCleanZeroesBoth(){
		assertEquals("parseCleanZeroesBoth", 1234500, RealFloat.parseString("001234500").doubleValue(), 1e-3);
	}

	@Test
	public void parseDec(){
		assertEquals("parseDec", 1234.56, RealFloat.parseString("1234.56").doubleValue(), 1e-5);
	}

	@Test
	public void parseDecZeroesLeft(){
		assertEquals("parseDecZeroesLeft", 1234.56, RealFloat.parseString("1234.5600").doubleValue(), 1e-5);
	}

	@Test
	public void parseDecZeroesBoth(){
		assertEquals("parseDecZeroesLeft", 1234.56, RealFloat.parseString("001234.5600").doubleValue(), 1e-5);
	}

	@Test
	public void parseSigDec(){
		assertEquals("parseSigDec", -123.456, RealFloat.parseString("-123.456").doubleValue(), 1e-5);
	}

	@Test
	public void parseSigDecExp(){
		assertEquals("parseSigDecExp", -1234.56, RealFloat.parseString("-12.3456e+2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpZeroesPositive(){
		assertEquals("parseExpZeroesPositive", 1234.56, RealFloat.parseString("0012.345600e+2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpZeroesNegative(){
		assertEquals("parseExpZeroesNegative", 0.123456, RealFloat.parseString("0012.345600e-2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpNoDecPositive(){
		assertEquals("parseExpNoDecPositive", 12345600, RealFloat.parseString("123456e+2").doubleValue(), 1e-5);
	}

	@Test
	public void parseExpNoDecNegative(){
		assertEquals("parseExpNoDecNegative", 1234.56, RealFloat.parseString("123456e-2").doubleValue(), 1e-5);
	}

	@Test
	public void parseBase(){
		assertEquals("parseBase", 34 * 36 + 35 + 13d / 36d, RealFloat.parseString("YZDe-1_36").doubleValue(), 1e-5);
	}

	@Test
	public void equalsExp(){
		assertEqualsSymmetric("equalsExp", RealFloat.parseString("00123400e+3"), RealFloat.parseString("00001234e+5"));
	}

	@Test
	public void addition(){
		assertEqualsSymmetric("addition", RealFloat.parseString("012345").plus(RealFloat.parseString("678900e-1")), RealFloat.parseString("0802.35e+2"));
	}

	@Test
	public void addition0(){
		assertEqualsSymmetric("addition0", RealFloat.parseString("47").plus(RealFloat.parseString("163")), RealFloat.parseString("210"));
	}

	@Test
	public void addition00(){
		assertEqualsSymmetric("addition00", RealFloat.parseString("37").plus(RealFloat.parseString("163")), RealFloat.parseString("200"));
	}

	@Test
	public void additionZero(){
		assertEqualsSymmetric("additionZero", RealFloat.parseString("-83").plus(RealFloat.ZERO), RealFloat.parseString("-83"));
	}

	@Test
	public void additionZeroZero(){
		assertEqualsSymmetric("additionZero", RealFloat.parseString("0").plus(RealFloat.ZERO), RealFloat.ZERO);
	}

	@Test
	public void subtractionNoBorrow0(){
		assertEqualsSymmetric("subtractionNoBorrow", RealFloat.parseString("1267").minus(RealFloat.parseString("247")), RealFloat.parseString("1020"));
	}

	@Test
	public void subtractionNoBorrow(){
		assertEqualsSymmetric("subtractionNoBorrow", RealFloat.parseString("167").minus(RealFloat.parseString("43")), RealFloat.parseString("124"));
	}

	@Test
	public void subtractionBorrow(){
		assertEqualsSymmetric("subtractionBorrow", RealFloat.parseString("143").minus(RealFloat.parseString("67")), RealFloat.parseString("76"));
	}

	@Test
	public void subtractionNegativeNegative(){
		assertEqualsSymmetric("subtractionNegativeNegative", RealFloat.parseString("-143").minus(RealFloat.parseString("-67")), RealFloat.parseString("-76"));
	}

	@Test
	public void xubtractionZero(){
		assertEqualsSymmetric("additionZero", RealFloat.parseString("-83").minus(RealFloat.ZERO), RealFloat.parseString("-83"));
	}

	@Test
	public void subtractionZeroZero(){
		assertEqualsSymmetric("additionZero", RealFloat.parseString("0").minus(RealFloat.ZERO), RealFloat.ZERO);
	}

	private static void assertEqualsSymmetric(String message, Object foo, Object bar){
		assertTrue(message, foo.equals(bar));
		assertTrue(message, bar.equals(foo));
	}
}
