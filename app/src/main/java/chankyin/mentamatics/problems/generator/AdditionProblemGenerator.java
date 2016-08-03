package chankyin.mentamatics.problems.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.old.Config;
import chankyin.mentamatics.problems.Problem;

import java.util.Random;

import static chankyin.mentamatics.Main.randomRange;
import static chankyin.mentamatics.config.old.Config.*;

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
		int upperDigits = randomRange(random,
				config.getInteger(KEY_GEN_ADDITION_DIGITS_UPPER_MIN), config.getInteger(KEY_GEN_ADDITION_DIGITS_UPPER_MAX));
		int lowerDigits = randomRange(random,
				config.getInteger(KEY_GEN_ADDITION_DIGITS_LOWER_MIN), config.getInteger(KEY_GEN_ADDITION_DIGITS_LOWER_MAX));

		return null; // TODO
	}
}
