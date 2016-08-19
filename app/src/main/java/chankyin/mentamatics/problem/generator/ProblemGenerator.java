package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import android.widget.Toast;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Answer;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.SingleAnswer;
import chankyin.mentamatics.problem.question.LiteralQuestion;
import chankyin.mentamatics.problem.question.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static chankyin.mentamatics.config.ConfigConstants.*;

public abstract class ProblemGenerator{
	public final static boolean IS_GENERATOR_IMPLEMENTED = true; // FIXME

	private static List<ProblemGenerator> available = Arrays.asList(
			AdditionProblemGenerator.getInstance(),
			SubtractionProblemGenerator.getInstance(),
			MultiplicationProblemGenerator.getInstance(),
			DivisionProblemGenerator.getInstance()
	);

	@SuppressWarnings("PointlessBooleanExpression")
	public static Problem generate(Config config, Random random){
		if(!IS_GENERATOR_IMPLEMENTED){
			double randomInt = random.nextInt(100) / 10d;
			Question question = new LiteralQuestion(String.format("%s", randomInt));
			Answer answer = new SingleAnswer(RealFloat.fromDouble(randomInt));
			return new Problem(question, answer);
		}
		List<ProblemGenerator> generators = new ArrayList<>(available);
		if(!config.getBoolean(KEY_GEN_ADDITION_ENABLED)){
			generators.remove(AdditionProblemGenerator.getInstance());
		}
		if(!config.getBoolean(KEY_GEN_SUBTRACTION_ENABLED) || !SubtractionProblemGenerator.IS_IMPLEMENTED){
			generators.remove(SubtractionProblemGenerator.getInstance());
		}
		if(!config.getBoolean(KEY_GEN_MULTIPLICATION_ENABLED) || !MultiplicationProblemGenerator.IS_IMPLEMENTED){
			generators.remove(MultiplicationProblemGenerator.getInstance());
		}
		if(!config.getBoolean(KEY_GEN_DIVISION_ENABLED) || !DivisionProblemGenerator.IS_IMPLEMENTED){
			generators.remove(DivisionProblemGenerator.getInstance());
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
}
