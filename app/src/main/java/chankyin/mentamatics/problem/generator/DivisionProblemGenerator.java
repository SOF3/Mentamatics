package chankyin.mentamatics.problem.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.Config;
import chankyin.mentamatics.problem.Problem;

import java.util.Random;

public class DivisionProblemGenerator extends ProblemGenerator{
	private static DivisionProblemGenerator ourInstance = new DivisionProblemGenerator();

	public static DivisionProblemGenerator getInstance(){
		return ourInstance;
	}

	private DivisionProblemGenerator(){
	}

	@NonNull
	@Override
	protected Problem generateProblem(Config config, Random random){
		return null; // TODO
	}
}
