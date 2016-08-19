package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.problem.Problem;

import java.util.Random;

public class SubtractionProblemGenerator extends ProblemGenerator{
	public static final boolean IS_IMPLEMENTED = false;
	private static SubtractionProblemGenerator ourInstance = new SubtractionProblemGenerator();

	public static SubtractionProblemGenerator getInstance(){
		return ourInstance;
	}

	private SubtractionProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		return null; // TODO
	}
}
