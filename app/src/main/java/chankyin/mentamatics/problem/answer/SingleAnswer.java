package chankyin.mentamatics.problem.answer;

import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Problem;
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
		field.setTypeface(Typeface.MONOSPACE);
		field.setOnRealNumberValidListener(new MyOnRealNumberValidListener(problem));
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
			field.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
		}else{
			field.setGravity(Gravity.CENTER);
		}
		answerField.addView(field);
		field.requestFocus();
	}

	@Override
	public void handleTab(){

	}

	@Override
	public boolean usesTab(){
		return false;
	}

	@Override
	public String toString(){
		return "single solution: " + solution.toString();
	}

	@RequiredArgsConstructor
	private class MyOnRealNumberValidListener implements NumberInputField.OnRealNumberValidListener{
		private final Problem problem;

		@Override
		public void onValid(@NonNull RealFloat number){
			Log.d(Main.TAG, "onValid: " + number.toString() + " equals " + solution.toString());
			if(number.equals(solution)){
				problem.onCorrect();
			}
		}
	}
}
