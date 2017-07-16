package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import android.widget.Toast;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.math.real.annotation.DescendingDigits;
import chankyin.mentamatics.problem.Answer;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.SingleAnswer;
import chankyin.mentamatics.problem.question.LiteralQuestion;
import chankyin.mentamatics.problem.question.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static chankyin.mentamatics.config.ConfigConstants.*;

public abstract class ProblemGenerator{
	public final static boolean IS_GENERATOR_IMPLEMENTED = true; // FIXME

	private final static ProblemGenerator[] AVAILABLE_GENERATORS = {
			AdditionProblemGenerator.getInstance(),
			SubtractionProblemGenerator.getInstance(),
			MultiplicationProblemGenerator.getInstance(),
			DivisionProblemGenerator.getInstance()
	};

	@SuppressWarnings("PointlessBooleanExpression")
	public static Problem generate(Config config, Random random){
		if(!IS_GENERATOR_IMPLEMENTED){
			double randomInt = random.nextInt(100) / 10d;
			Question question = new LiteralQuestion(String.format("%s", randomInt));
			Answer answer = new SingleAnswer(RealFloat.fromDouble(randomInt));
			return new Problem(question, answer);
		}
		List<ProblemGenerator> generators = new ArrayList<>();
		if(config.getBoolean(KEY_GEN_ADDITION_ENABLED)){
			generators.add(AdditionProblemGenerator.getInstance());
		}
		if(SubtractionProblemGenerator.IS_IMPLEMENTED && config.getBoolean(KEY_GEN_SUBTRACTION_ENABLED)){
			generators.add(SubtractionProblemGenerator.getInstance());
		}
		if(MultiplicationProblemGenerator.IS_IMPLEMENTED && config.getBoolean(KEY_GEN_MULTIPLICATION_ENABLED)){
			generators.add(MultiplicationProblemGenerator.getInstance());
		}
		if(DivisionProblemGenerator.IS_IMPLEMENTED && config.getBoolean(KEY_GEN_DIVISION_ENABLED)){
			generators.add(DivisionProblemGenerator.getInstance());
		}

		if(generators.size() == 0){
			Toast.makeText(Main.getInstance(), R.string.no_generators, Toast.LENGTH_LONG).show();
			generators.add(AdditionProblemGenerator.getInstance());
		}

		int i = Math.min(generators.size() - 1, (int) Math.floor(random.nextDouble() * generators.size()));
		return generators.get(i).generateProblem(config, random);
	}

	@NonNull
	protected abstract Problem generateProblem(Config config, Random random);

	@DescendingDigits
	protected int[] generateNumber(Random random, int length, int base){
		int[] ret = new int[length];
		ret[0] = random.nextInt(base - 1) + 1; // first number must be non-zero
		for(int i = 1; i < length; i++){
			ret[i] = random.nextInt(base);
		}
		return ret;
	}
}
