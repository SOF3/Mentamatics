package chankyin.mentamatics.problems.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.ui.Quadret;
import chankyin.mentamatics.problems.Problem;

import java.util.Random;

public class AdditionProblemGenerator extends ProblemGenerator{
	private static AdditionProblemGenerator ourInstance = new AdditionProblemGenerator();

	public static AdditionProblemGenerator getInstance(){
		return ourInstance;
	}

	private AdditionProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		Quadret digits = config.getIntDoubleRange(KEY_GEN_ADDITION_DIGITS);
		int upperDigits = Main.randomRange(random, digits.upperMin, digits.upperMax);
		int lowerDigits = Main.randomRange(random, digits.lowerMin, digits.lowerMax);
//		int upperDigits = randomRange(random,
//				config.getInt(KEY_GEN_ADDITION_DIGITS_UPPER_MIN), config.getInt(KEY_GEN_ADDITION_DIGITS_UPPER_MAX));
//		int lowerDigits = randomRange(random,
//				config.getInt(KEY_GEN_ADDITION_DIGITS_LOWER_MIN), config.getInt(KEY_GEN_ADDITION_DIGITS_LOWER_MAX));

		return null; // TODO
	}
}
