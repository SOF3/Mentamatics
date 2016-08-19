package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.range.QuadretRange;
import chankyin.mentamatics.math.MathUtils;
import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Operator;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.SingleAnswer;
import chankyin.mentamatics.problem.question.TripletQuestion;

import java.util.Random;

import static chankyin.mentamatics.LogUtils.debug;
import static chankyin.mentamatics.config.ConfigConstants.*;

public class AdditionProblemGenerator extends ProblemGenerator{
	private static AdditionProblemGenerator ourInstance = new AdditionProblemGenerator();
	private final static Operator OPERATOR = Operator.ADDITION;

	public static AdditionProblemGenerator getInstance(){
		return ourInstance;
	}

	private AdditionProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		QuadretRange digits = config.getIntDoubleRange(KEY_GEN_ADDITION_DIGITS);
		int upperDigitCount = Main.randomRange(random, digits.upperMin, digits.upperMax);
		int lowerDigitCount = Main.randomRange(random, digits.lowerMin, digits.lowerMax);
		debug("generateProblem: digits = %s, upperDigitCount = %d, lowerDigitCount = %d", digits, upperDigitCount, lowerDigitCount);
		int base = RealFloat.DEFAULT_BASE;

		@Size(2) RealFloat[] operands = config.getBoolean(KEY_GEN_ADDITION_CARRY_ALLOWED) ?
				generateCarry(random, base, upperDigitCount, lowerDigitCount) :
				generateNoCarry(random, base, upperDigitCount, lowerDigitCount);


		RealFloat answer = operands[0].plus(operands[1]);
		if(BuildConfig.DEBUG){
			RealFloat answer2 = operands[1].plus(operands[0]);
			if(!answer.equals(answer2)){
				throw new AssertionError(operands[0].toString() + " + " + operands[1].toString() + " = " + answer.toString() + " OR " + answer2.toString());
			}
		}

		return new Problem(new TripletQuestion(operands[0], OPERATOR, operands[1]), new SingleAnswer(answer));
	}

	@Size(2)
	private RealFloat[] generateCarry(Random random, int base, int upperDigitCount, int lowerDigitCount){
		return new RealFloat[]{
				RealFloat.bigEndianDigits(base, 1, 0, MathUtils.randomRangeArray(random, upperDigitCount, 0, base)),
				RealFloat.bigEndianDigits(base, 1, 0, MathUtils.randomRangeArray(random, lowerDigitCount, 0, base))
		};
	}

	@Size(2)
	private RealFloat[] generateNoCarry(Random random, int base, int upperDigitCount, int lowerDigitCount){
		boolean swap = upperDigitCount < lowerDigitCount;
		int[] upperDigits = new int[swap ? lowerDigitCount : upperDigitCount];
		int[] lowerDigits = new int[swap ? upperDigitCount : lowerDigitCount];

		for(int i = 0; i < upperDigits.length; i++){
			FooBarRange fooRange = new FooBarRange(i + 1 == upperDigits.length ? 1 : 0, base - 1);
			if(i >= lowerDigits.length){
				upperDigits[i] = fooRange.generateRandom(random) + 1;
				continue;
			}
			FooBarRange barRange;
			if(i + 1 == lowerDigits.length){
				barRange = new FooBarRange(1, base - 1);
			}else{
				barRange = fooRange;
			}

			FooBar fooBar = FooBarFactory.sumLessThan(random, fooRange, barRange, base);
			upperDigits[i] = fooBar.getFoo();
			lowerDigits[i] = fooBar.getBar();
		}

		return new RealFloat[]{
				RealFloat.smallEndianDigits(base, 1, 0, swap ? lowerDigits : upperDigits),
				RealFloat.smallEndianDigits(base, 1, 0, swap ? upperDigits : lowerDigits)
		};
	}
}
