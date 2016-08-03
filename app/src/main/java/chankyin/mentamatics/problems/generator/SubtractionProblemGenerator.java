package chankyin.mentamatics.problems.generator;

import android.support.annotation.NonNull;
import chankyin.mentamatics.config.old.Config;
import chankyin.mentamatics.problems.Problem;

import java.util.Random;

public class SubtractionProblemGenerator extends ProblemGenerator{
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
