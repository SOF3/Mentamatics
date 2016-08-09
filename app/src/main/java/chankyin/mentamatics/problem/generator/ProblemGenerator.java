package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.config.ConfigConstants;
import chankyin.mentamatics.math.real.RealNumber;
import chankyin.mentamatics.problem.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class ProblemGenerator implements ConfigConstants{
	public final static boolean IS_GENERATOR_IMPLEMENTED = false; // FIXME

	private static List<ProblemGenerator> available = Arrays.asList(
			AdditionProblemGenerator.getInstance(),
			SubtractionProblemGenerator.getInstance(),
			MultiplicationProblemGenerator.getInstance(),
			DivisionProblemGenerator.getInstance()
	);

	@SuppressWarnings("PointlessBooleanExpression")
	public static Problem generate(Config config, Random random){
		if(!IS_GENERATOR_IMPLEMENTED){
			Question question = new LiteralQuestion("Question here");
			Answer answer = new SingleAnswer(RealNumber.bigEndianDigits(1, 0, new int[]{1, 2, 3, 4}));
			return new Problem(question, answer);
		}
		List<ProblemGenerator> generators = new ArrayList<>(available);
		if(!config.getBoolean(KEY_GEN_ADDITION_ENABLED)){
			generators.remove(AdditionProblemGenerator.getInstance());
		}
		if(!config.getBoolean(KEY_GEN_SUBTRACTION_ENABLED)){
			generators.remove(SubtractionProblemGenerator.getInstance());
		}
		if(!config.getBoolean(KEY_GEN_MULTIPLICATION_ENABLED)){
			generators.remove(MultiplicationProblemGenerator.getInstance());
		}
		if(!config.getBoolean(KEY_GEN_DIVISION_ENABLED)){
			generators.remove(DivisionProblemGenerator.getInstance());
		}
		int i = Math.min(generators.size() - 1, (int) Math.floor(random.nextDouble() * generators.size()));
		return generators.get(i).generateProblem(config, random);
	}

	@NonNull
	protected abstract Problem generateProblem(Config config, Random random);
}
