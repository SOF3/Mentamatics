package chankyin.mentamatics.problem;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.ui.view.NumberInputField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingleAnswer implements Answer{
	@Getter @NonNull private final RealFloat solution;

	@Override
	public void populateAnswerField(ViewGroup answerField, final Problem problem){
		NumberInputField field = new NumberInputField(answerField.getContext());
		field.setLayoutParams(Main.MP_WC);
		field.setOnRealNumberValidListener(new MyOnRealNumberValidListener(problem));
		answerField.addView(field);
		field.requestFocus();
	}

	@RequiredArgsConstructor
	private class MyOnRealNumberValidListener implements NumberInputField.OnRealNumberValidListener{
		private final Problem problem;

		@Override
		public void onValid(@NonNull RealFloat number){
			if(number.equals(solution)){
				problem.onCorrect();
			}
		}
	}
}
