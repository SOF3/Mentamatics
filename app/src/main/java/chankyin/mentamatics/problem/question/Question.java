package chankyin.mentamatics.problem.question;

import android.widget.LinearLayout;

public interface Question{
	int TYPE_PLACEHOLDER = 1;
	int TYPE_ADD = 0x101;
	int TYPE_SUBTRACT = 0x102;
	int TYPE_MULTIPLICATION = 0x103;
	int TYPE_DIVISION = 0x104;

	/**
	 * Fills a LinearLayout with the question.<br>
	 * This will not clear any pre-existent views in the LinearLayout.
	 * Since this is only a method to <i>populate</i> the layout,
	 * it will not do anything that kills the population. :)
	 *
	 * @param layout
	 */
	void populateQuestionLayout(LinearLayout layout);

	int getType();

	int getFlags();
}
