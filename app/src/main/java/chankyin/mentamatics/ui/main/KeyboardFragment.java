package chankyin.mentamatics.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import chankyin.mentamatics.R;
import chankyin.mentamatics.ui.BaseActivity;
import chankyin.mentamatics.ui.view.NumberInputField;

public class KeyboardFragment extends Fragment{
	public final static String TAG = "KeyboardFragment";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.frag_keyboard, container, false);
	}

	public void onInputButtonClick(View view){
		View focus = getActivity().getCurrentFocus();
		if(!(focus instanceof NumberInputField)){
			return;
		}

		char c;
		int i = view.getId();
		if(i == R.id.main_input_0){
			c = '0';
		}else if(i == R.id.main_input_1){
			c = '1';
		}else if(i == R.id.main_input_2){
			c = '2';
		}else if(i == R.id.main_input_3){
			c = '3';
		}else if(i == R.id.main_input_4){
			c = '4';
		}else if(i == R.id.main_input_5){
			c = '5';
		}else if(i == R.id.main_input_6){
			c = '6';
		}else if(i == R.id.main_input_7){
			c = '7';
		}else if(i == R.id.main_input_8){
			c = '8';
		}else if(i == R.id.main_input_9){
			c = '9';
		}else if(i == R.id.main_input_point){
			c = '.';
		}else if(i == R.id.main_input_back){
			c = NumberInputField.CHAR_SPECIAL_BACK;
		}else if(i == R.id.main_input_reset){
			c = NumberInputField.CHAR_SPECIAL_RESET;
		}else if(i == R.id.main_input_sign){
			c = '-';
		}else if(i == R.id.main_input_tab){
			((HomeActivity) getActivity()).getCurrentProblem().getAnswer().handleTab();
			return;
		}else{
			throw new UnsupportedOperationException(getResources().getResourceName(view.getId()) + " not handled");
		}

		((NumberInputField) focus).respondKeyboardButton(c);
	}

	@Override
	public void onHiddenChanged(boolean hidden){
		super.onHiddenChanged(hidden);

		((BaseActivity) getActivity()).setKeyboardOpened(!hidden);
	}

	public static KeyboardFragment get(BaseActivity baseActivity){
		return (KeyboardFragment) baseActivity.getFragmentManager().findFragmentByTag(TAG);
	}

	public void showTabButton(){

	}
}
