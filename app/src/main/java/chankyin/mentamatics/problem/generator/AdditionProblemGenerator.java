package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import chankyin.mentamatics.BuildConfig;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.StatsDb;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.range.QuartetRange;
import chankyin.mentamatics.math.RealFloatUtils;
import chankyin.mentamatics.math.foobar.FooBar;
import chankyin.mentamatics.math.foobar.FooBarFactory;
import chankyin.mentamatics.math.foobar.FooBarRange;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Operator;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.answer.SingleAnswer;
import chankyin.mentamatics.problem.question.Question;
import chankyin.mentamatics.problem.question.TripletQuestion;

import java.util.Random;

import static chankyin.mentamatics.TestUtils.debug;
import static chankyin.mentamatics.config.ConfigConstants.KEY_GEN_ADDITION_CARRY_ALLOWED;
import static chankyin.mentamatics.config.ConfigConstants.KEY_GEN_ADDITION_DIGITS;

public class AdditionProblemGenerator extends ProblemGenerator{
	public final static int FLAG_PREF_UPPER_DIGIT_MAX_MASK
			= 0x000_0000F;
	public final static int FLAG_PREF_UPPER_DIGIT_MIN_MASK
			= 0x000_000F0;
	public final static int FLAG_PREF_LOWER_DIGIT_MAX_MASK
			= 0x000_00F00;
	public final static int FLAG_PREF_LOWER_DIGIT_MIN_MASK
			= 0x000_0F000;
	public final static int FLAG_PREF_CARRY
			= 0x000_10000;
	public final static int FLAG_THIS_CARRIES
			= 0x100_00000;
	public final static int FLAG_THIS_DE_FACTO_MINUS
			= 0x200_00000;
	public final static int FLAG_THIS_LITERAL
			= 0x400_00000;
	public final static int FLAG_THIS_UPPER_DIGIT_MASK
			= 0x00F_00000;
	public final static int FLAG_THIS_LOWER_DIGIT_MASK
			= 0x0F0_00000;

	private static AdditionProblemGenerator ourInstance = new AdditionProblemGenerator();

	public static AdditionProblemGenerator getInstance(){
		return ourInstance;
	}

	private AdditionProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		QuartetRange digits = config.getIntDoubleRange(KEY_GEN_ADDITION_DIGITS);
		int upperDigitCount = Main.randomRange(random, digits.upperMin, digits.upperMax);
		int lowerDigitCount = Main.randomRange(random, digits.lowerMin, digits.lowerMax);
		debug("Addition.generateProblem: digits = %s, upperDigitCount = %d, lowerDigitCount = %d", digits, upperDigitCount, lowerDigitCount);
		int base = RealFloat.DEFAULT_BASE;

		boolean configCarries = config.getBoolean(KEY_GEN_ADDITION_CARRY_ALLOWED);
		@Size(2) RealFloat[] operands = configCarries ?
				generateCarry(random, base, upperDigitCount, lowerDigitCount) :
				generateNoCarry(random, base, upperDigitCount, lowerDigitCount);


		RealFloat answer = operands[0].plus(operands[1]);
		if(BuildConfig.DEBUG){
			RealFloat answer2 = operands[1].plus(operands[0]);
			if(!answer.equals(answer2)){
				throw new AssertionError(operands[0].toString() + " + " + operands[1].toString() + " = " + answer.toString() + " OR " + answer2.toString());
			}
		}

		int flags = 0;
		flags |= StatsDb.numberToFlag(digits.upperMax, FLAG_PREF_UPPER_DIGIT_MAX_MASK);
		flags |= StatsDb.numberToFlag(digits.upperMin, FLAG_PREF_UPPER_DIGIT_MIN_MASK);
		flags |= StatsDb.numberToFlag(digits.lowerMax, FLAG_PREF_LOWER_DIGIT_MAX_MASK);
		flags |= StatsDb.numberToFlag(digits.lowerMin, FLAG_PREF_LOWER_DIGIT_MIN_MASK);
		flags |= StatsDb.numberToFlag(upperDigitCount, FLAG_THIS_UPPER_DIGIT_MASK);
		flags |= StatsDb.numberToFlag(lowerDigitCount, FLAG_THIS_LOWER_DIGIT_MASK);
		if(configCarries){
			flags |= FLAG_PREF_CARRY;
			RealFloat.Operation prep = operands[0].prepareAddition(operands[1]);
			if(prep instanceof RealFloat.Operation.Literal){
				flags |= FLAG_THIS_LITERAL;
			}
			if(prep instanceof RealFloat.Operation.Arithmetic){
				RealFloat.Operation.Arithmetic arithmetic = (RealFloat.Operation.Arithmetic) prep;
				if(arithmetic.isAddition()){
					int[] foo = arithmetic.getOperand1(), bar = arithmetic.getOperand2();
					for(int i = 0; i < foo.length && i < bar.length; i++){
						if(foo[i] + bar[i] >= base){
							flags |= FLAG_THIS_CARRIES;
							break;
						}
					}
				}else{
					flags |= FLAG_THIS_DE_FACTO_MINUS;
				}
			}
		}
		TripletQuestion question = new TripletQuestion(operands[0], Operator.ADDITION, operands[1], Question.TYPE_ADD, flags);
		return new Problem(question, new SingleAnswer(answer));
	}

	@Size(2)
	private RealFloat[] generateCarry(Random random, int base, int upperDigitCount, int lowerDigitCount){
		return new RealFloat[]{
				RealFloat.bigEndianDigits(base, 1, 0, RealFloatUtils.randomRangeArray(random, upperDigitCount, 0, base, true)),
				RealFloat.bigEndianDigits(base, 1, 0, RealFloatUtils.randomRangeArray(random, lowerDigitCount, 0, base, true))
		};
	}

	@Size(2)
	private RealFloat[] generateNoCarry(Random random, int base, int upperDigitCount, int lowerDigitCount){
		boolean swap = upperDigitCount < lowerDigitCount;
		int[] upperDigits = new int[swap ? lowerDigitCount : upperDigitCount]; // always longer
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

			FooBar fooBar = FooBarFactory.getInstance().sumLessThan(random, fooRange, barRange, base);
			upperDigits[i] = fooBar.getFoo();
			lowerDigits[i] = fooBar.getBar();
		}

		return new RealFloat[]{
				RealFloat.littleEndianDigits(base, 1, 0, swap ? lowerDigits : upperDigits),
				RealFloat.littleEndianDigits(base, 1, 0, swap ? upperDigits : lowerDigits)
		};
	}
}
