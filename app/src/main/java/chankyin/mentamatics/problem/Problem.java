package chankyin.mentamatics.problem;

import android.widget.LinearLayout;
import chankyin.mentamatics.R;
import chankyin.mentamatics.ui.main.MainActivity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Problem{
	private final Question question;
	private final Answer answer;

	@Setter private OnAnswerCorrectListener onAnswerCorrectListener;

	public void express(MainActivity activity){
		LinearLayout questionLayout = (LinearLayout) activity.findViewById(R.id.main_question);
		questionLayout.removeAllViews();
		question.populateQuestionLayout(questionLayout);

		LinearLayout answerLayout = (LinearLayout) activity.findViewById(R.id.main_answer);
		answerLayout.removeAllViews();
		answer.populateAnswerField(answerLayout, this);
	}

	public void onCorrect(){
		if(onAnswerCorrectListener != null){
			onAnswerCorrectListener.onAnswerCorrect();
		}
	}

	public static interface OnAnswerCorrectListener{
		public void onAnswerCorrect();
	}
}
