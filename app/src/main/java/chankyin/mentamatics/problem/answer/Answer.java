package chankyin.mentamatics.problem.answer;

import android.view.ViewGroup;
import chankyin.mentamatics.problem.Problem;

public interface Answer{
	void populateAnswerField(ViewGroup answerField, Problem problem);

	boolean usesTab();

	void handleTab();
}
