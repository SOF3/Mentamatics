package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.range.QuadretRange;
import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Answer;
import chankyin.mentamatics.problem.Operator;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.SingleAnswer;
import chankyin.mentamatics.problem.question.Question;
import chankyin.mentamatics.problem.question.TripletQuestion;

import java.util.Random;

import static chankyin.mentamatics.config.ConfigConstants.*;

public class SubtractionProblemGenerator extends ProblemGenerator{
	public static final boolean IS_IMPLEMENTED = true;
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
		int[] upper, lower;
		boolean swap = false;

		FooBar digits = allowNegative ?
				new FooBar(upperDigitsRange.generateRandom(random), lowerDigitsRange.generateRandom(random)) :
				FooBarFactory.getInstance().fooGteBar(random, upperDigitsRange, lowerDigitsRange);

		if(allowBorrow){
			upper = generateNumber(random, digits.getFoo(), base);
			lower = generateNumber(random, digits.getBar(), base); // no need to worry about negative
		}else{
			upper = new int[digits.getFoo()];
			lower = new int[digits.getBar()];
			for(int i = 1; i <= upper.length; i++){
				int upperIndex = upper.length - i;
				int lowerIndex = lower.length - i; // may be negative
				if(lowerIndex < 0){
					upper[upperIndex] = upperIndex != 0 ? random.nextInt(base) : (random.nextInt(base - 1) + 1);
				}else if(lowerIndex == 0){
					FooBar fooBar = FooBarFactory.getInstance().fooGteBar(
							random, new FooBarRange(1, base - 1), new FooBarRange(1, base - 1));
					// no matter upperIndex is 0 or not, upper must not be 0 because upper >= lower and lower > 0
					upper[upperIndex] = fooBar.getFoo();
					lower[0] = fooBar.getBar();
				}else{
					FooBar fooBar = FooBarFactory.getInstance().fooGteBar(
							random, new FooBarRange(0, base - 1), new FooBarRange(0, base - 1));
					upper[upperIndex] = fooBar.getFoo();
					upper[lowerIndex] = fooBar.getBar();
				}
			}

			swap = digits.getFoo() == digits.getBar() && random.nextBoolean(); // 50% probability to be negative
		}

		RealFloat a = RealFloat.bigEndianDigits(base, 1, 0, upper);
		RealFloat b = RealFloat.bigEndianDigits(base, 1, 0, lower);
		Question question = swap ? new TripletQuestion(b, Operator.SUBTRACTION, a) : new TripletQuestion(a, Operator.SUBTRACTION, b);
		Answer answer = new SingleAnswer(swap ? b.minus(a) : a.minus(b));
		return new Problem(question, answer);
	}
}
