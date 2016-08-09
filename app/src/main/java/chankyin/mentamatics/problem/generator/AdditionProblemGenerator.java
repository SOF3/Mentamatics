package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.range.QuadretRange;
import chankyin.mentamatics.math.NumberUtils;
import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.real.RealNumber;
import chankyin.mentamatics.problem.Operator;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.SingleAnswer;
import chankyin.mentamatics.problem.TripletQuestion;

import java.util.Random;

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
		int base = RealNumber.DEFAULT_BASE;

		RealNumber upper, lower;

		if(!config.getBoolean(KEY_GEN_ADDITION_CARRY_ALLOWED)){
			boolean swap = upperDigitCount < lowerDigitCount;
			int[] upperDigits = new int[swap ? lowerDigitCount : upperDigitCount];
			int[] lowerDigits = new int[swap ? upperDigitCount : lowerDigitCount];

			for(int i = 0; i < upperDigits.length; i++){
				FooBarRange fooRange = new FooBarRange(i + 1 == upperDigits.length ? 1 : 0, base);
				if(i >= lowerDigits.length){
					upperDigits[i] = fooRange.generateRandom(random);
					continue;
				}
				FooBarRange barRange = new FooBarRange(i + 1 == lowerDigits.length ? 1 : 0, base);


				FooBar fooBar = FooBarFactory.sumLessThan(fooRange, barRange, base);
				upperDigits[i] = fooBar.getFoo();
				lowerDigits[i] = fooBar.getBar();
			}

			upper = RealNumber.smallEndianDigits(base, 1, 0, swap ? lowerDigits : upperDigits);
			lower = RealNumber.smallEndianDigits(base, 1, 0, swap ? upperDigits : lowerDigits);
		}else{
			upper = RealNumber.bigEndianDigits(base, 1, 0, NumberUtils.randomRangeArray(random, upperDigitCount, 0, base));
			lower = RealNumber.bigEndianDigits(base, 1, 0, NumberUtils.randomRangeArray(random, upperDigitCount, 0, base));
		}


		RealNumber answer = upper.plus(lower);
		if(BuildConfig.DEBUG){
			RealNumber answer2 = lower.plus(upper);
			if(!answer.equals(answer2)){
				throw new AssertionError(upper.toString() + " + " + lower.toString() + " = " + answer.toString() + " OR " + answer2.toString());
			}
		}

		return new Problem(new TripletQuestion(upper, OPERATOR, lower), new SingleAnswer(answer)); // TODO
	}
}
