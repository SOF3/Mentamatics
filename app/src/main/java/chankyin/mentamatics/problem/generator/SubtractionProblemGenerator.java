package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.range.QuadretRange;
import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.math.real.annotation.DescendingDigits;
import chankyin.mentamatics.problem.Answer;
import chankyin.mentamatics.problem.Operator;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.SingleAnswer;
import chankyin.mentamatics.problem.question.Question;
import chankyin.mentamatics.problem.question.TripletQuestion;

import java.util.Random;

import static chankyin.mentamatics.TestUtils.debug;
import static chankyin.mentamatics.config.ConfigConstants.*;

public class SubtractionProblemGenerator extends ProblemGenerator{
	private static SubtractionProblemGenerator ourInstance = new SubtractionProblemGenerator();

	public static SubtractionProblemGenerator getInstance(){
		return ourInstance;
	}

	private SubtractionProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		QuadretRange range = config.getIntDoubleRange(KEY_GEN_SUBTRACTION_DIGITS);
		FooBarRange lowerDigitsRange = new FooBarRange(range.lowerMin, range.lowerMax);
		FooBarRange upperDigitsRange = new FooBarRange(range.upperMin, range.upperMax);
		boolean allowBorrow = config.getBoolean(KEY_GEN_SUBTRACTION_BORROW_ALLOWED);
		boolean allowNegative = config.getBoolean(KEY_GEN_SUBTRACTION_NEGATIVE_ALLOWED);

		int base = 10;
		RealFloat upper, lower;

		if(allowBorrow){
			if(allowNegative){
				upper = RealFloat.bigEndianDigits(base, 1, 0, generateNumber(random, upperDigitsRange.generateRandom(random), base));
				lower = RealFloat.bigEndianDigits(base, 1, 0, generateNumber(random, lowerDigitsRange.generateRandom(random), base));
			}else{
				// upperSize >= lowerSize
				FooBar sizes = FooBarFactory.getInstance().fooGteBar(random, upperDigitsRange, lowerDigitsRange);
				debug("sizes: %s, upperDigitsRange: %s, lowerDigitsRange: %s", sizes, upperDigitsRange, lowerDigitsRange);
				@DescendingDigits int[] upperDigits = generateNumber(random, sizes.getFoo(), base),
						lowerDigits = generateNumber(random, sizes.getBar(), base);
				upper = RealFloat.bigEndianDigits(base, 1, 0, upperDigits);
				lower = RealFloat.bigEndianDigits(base, 1, 0, lowerDigits);
				if(upper.compareTo(lower, true) == -1){
					if(upperDigits.length != lowerDigits.length){
						throw new AssertionError();
					}
					FooBar firstDigits = FooBarFactory.getInstance().fooGtBar(random, new FooBarRange(0, base - 1), new FooBarRange(0, base - 1));
					upperDigits[0] = firstDigits.getFoo();
					lowerDigits[0] = firstDigits.getBar();
					upper = RealFloat.bigEndianDigits(base, 1, 0, upperDigits);
					lower = RealFloat.bigEndianDigits(base, 1, 0, lowerDigits);
				}
			}
		}else{
			boolean isNegative = allowNegative && random.nextBoolean();

			@DescendingDigits int[] upperDigits, lowerDigits;

			FooBar sizes;
			if(isNegative){
				sizes = FooBarFactory.getInstance().fooGteBar(random, lowerDigitsRange, upperDigitsRange);
			}else{
				sizes = FooBarFactory.getInstance().fooGteBar(random, upperDigitsRange, lowerDigitsRange);
			}
			upperDigits = new int[sizes.getFoo()];
			lowerDigits = new int[sizes.getBar()];
			// upperDigits.length >= lowerDigits.length is always true
			for(int r = 0; r < upperDigits.length; ++r){
				boolean hasLower = r < lowerDigits.length;
				boolean isLowerLast = r + 1 == lowerDigits.length;
				boolean isUpperLast = r + 1 == upperDigits.length;

				if(hasLower){
					FooBar digits = FooBarFactory.getInstance().fooGteBar(random,
							new FooBarRange(isUpperLast ? 1 : 0, base - 1),
							new FooBarRange(isLowerLast ? 1 : 0, base - 1));
					upperDigits[upperDigits.length - 1 - r] = digits.getFoo();
					lowerDigits[lowerDigits.length - 1 - r] = digits.getBar();
				}else{
					upperDigits[upperDigits.length - 1 - r] = new FooBarRange(isUpperLast ? 1 : 0, base - 1).generateRandom(random);
				}
			}

			if(isNegative){
				upper = RealFloat.bigEndianDigits(base, 1, 0, lowerDigits);
				lower = RealFloat.bigEndianDigits(base, 1, 0, upperDigits);
			}else{
				upper = RealFloat.bigEndianDigits(base, 1, 0, upperDigits);
				lower = RealFloat.bigEndianDigits(base, 1, 0, lowerDigits);
			}
		}

		Question question = new TripletQuestion(upper, Operator.SUBTRACTION, lower, Question.TYPE_SUBTRACT, 0); // TODO implement flags
		Answer answer = new SingleAnswer(upper.minus(lower));
		return new Problem(question, answer);
	}
}
