package chankyin.mentamatics.ui.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.ui.BaseActivity;
import chankyin.mentamatics.ui.main.KeyboardFragment;
import lombok.Getter;
import lombok.Setter;

public class NumberInputField extends EditText implements View.OnFocusChangeListener, View.OnTouchListener{
	public final static char CHAR_SPECIAL_BACK = '\b';
	public final static char CHAR_SPECIAL_RESET = '\r';

	private BaseActivity keyboardActivity;

	@Getter @Setter private OnRealNumberValidListener onRealNumberValidListener;

	public NumberInputField(Context context){
		this(context, null);
	}

	public NumberInputField(Context context, AttributeSet set){
		super(context, set);

		setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		setOnTouchListener(this);
		setOnFocusChangeListener(this);

		setMinEms(5);
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent){
		requestFocus();

		InputMethodManager inputMgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMgr.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		openKeyboard();

		return true;
	}

	@Override
	public void onFocusChange(View view, boolean focused){
		InputMethodManager inputMgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMgr.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		if(focused){
			openKeyboard();
		}else{
			closeKeyboard();
		}
	}

	public void openKeyboard(){
		keyboardActivity = Main.getInstance().getCurrentActivity();
		if(keyboardActivity == null){
			Log.w(Main.TAG, "No activity to open keyboard in", new Throwable("Backtrace"));
			return;
		}

		FragmentManager manager = keyboardActivity.getFragmentManager();

		manager.beginTransaction()
				.show(manager.findFragmentById(R.id.fragment_keyboard))
				.addToBackStack(KeyboardFragment.TAG)
				.commit();
	}

	public void closeKeyboard(){
		BaseActivity activity = Main.getInstance().getCurrentActivity();
		if(activity == null){
			Log.w(Main.TAG, "No keyboardActivity to close keyboard in");
			return;
		}

		FragmentManager manager = keyboardActivity.getFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragment_keyboard);
		if(!(fragment instanceof KeyboardFragment)){
			Log.w(Main.TAG, "keyboardActivity doesn't have a KeyboardFragment");
			return;
		}
		KeyboardFragment keyboard = (KeyboardFragment) fragment;

		manager.beginTransaction()
				.hide(keyboard)
				.commit();
		manager.popBackStack(KeyboardFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public void respondKeyboardButton(char c){
		Editable text = getText();
		if('0' <= c && c <= '9' || c == '.'){
			text.append(c);
		}else if(c == '-'){
			if(text.length() != 0 && text.charAt(0) == '-'){
				text.delete(0, 1);
			}else{
				text.insert(0, "-");
			}
		}else if(c == CHAR_SPECIAL_BACK){
			text.delete(text.length() - 1, text.length());
		}else if(c == CHAR_SPECIAL_RESET){
			text.clear();
		}else{
			throw new IllegalArgumentException();
		}

		if(text.length() != 0 && onRealNumberValidListener != null){
			RealFloat number;
			try{
				number = RealFloat.parseString(text.toString());
			}catch(NumberFormatException e){
				return;
			}

			onRealNumberValidListener.onValid(number);
		}
	}

	public static interface OnRealNumberValidListener{
		public void onValid(@NonNull RealFloat number);
	}
}
