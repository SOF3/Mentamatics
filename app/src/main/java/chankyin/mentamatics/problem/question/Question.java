package chankyin.mentamatics.problem.question;

import android.widget.LinearLayout;

public interface Question{
	/**
	 * Fills a LinearLayout with the question.<br>
	 * This will not clear any pre-existent views in the LinearLayout.
	 * Since this is only a method to <i>populate</i> the layout,
	 * it will not do anything that kills the population. :)
	 *
	 * @param layout
	 */
	public void populateQuestionLayout(LinearLayout layout);
}
