package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.problem.Problem;

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
		return null; // TODO
	}
}
