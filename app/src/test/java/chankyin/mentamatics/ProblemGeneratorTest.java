package chankyin.mentamatics;

import chankyin.mentamatics.problem.generator.AdditionProblemGenerator;
import org.junit.Test;

public class ProblemGeneratorTest{
	@Test
	public void additionGenerator(){
		AdditionProblemGenerator.getInstance();
	}
}
