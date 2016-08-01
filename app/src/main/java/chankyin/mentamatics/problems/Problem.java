package chankyin.mentamatics.problems;

import android.widget.LinearLayout;
import chankyin.mentamatics.R;
import chankyin.mentamatics.ui.MainActivity;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Problem{
	private final Question question;
	private final BigDecimal answer;

	public void express(MainActivity activity){
		LinearLayout layout = (LinearLayout) activity.findViewById(R.id.main_question);
		layout.removeAllViews();
		question.populate(layout);
	}
}
