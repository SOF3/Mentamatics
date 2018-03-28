package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.ConfigConstants;
import chankyin.mentamatics.config.range.QuartetRange;
import chankyin.mentamatics.math.RealFloatUtils;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.math.real.annotation.DescendingDigits;
import chankyin.mentamatics.problem.Operator;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.answer.Answer;
import chankyin.mentamatics.problem.answer.SingleAnswer;
import chankyin.mentamatics.problem.question.Question;
import chankyin.mentamatics.problem.question.TripletQuestion;

import java.util.Random;

public class MultiplicationProblemGenerator extends ProblemGenerator{
	public static final boolean IS_IMPLEMENTED = false;
	private static MultiplicationProblemGenerator ourInstance = new MultiplicationProblemGenerator();

	public static MultiplicationProblemGenerator getInstance(){
		return ourInstance;
	}

	private MultiplicationProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		QuartetRange range = config.getIntDoubleRange(ConfigConstants.KEY_GEN_MULTIPLICATION_DIGITS);
		int flags = 0;

		int upperDigitCount = Main.randomRange(random, range.upperMin, range.upperMax);
		int lowerDigitCount = Main.randomRange(random, range.lowerMin, range.lowerMax);

		@DescendingDigits int[] upperDigits = RealFloatUtils.randomRangeArray(random, upperDigitCount, 0, RealFloat.DEFAULT_BASE, true);
		@DescendingDigits int[] lowerDigits = RealFloatUtils.randomRangeArray(random, lowerDigitCount, 0, RealFloat.DEFAULT_BASE, true);

		RealFloat upper = RealFloat.bigEndianDigits(1, 0, upperDigits);
		RealFloat lower = RealFloat.bigEndianDigits(1, 0, lowerDigits);

		Question question = new TripletQuestion(upper, Operator.MULTIPLICATION, lower, Question.TYPE_MULTIPLICATION, flags); // TODO add flags
		Answer answer = new SingleAnswer(upper.times(lower));
		return new Problem(question, answer);
	}
}
