package chankyin.mentamatics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.math.Decimal;
import chankyin.mentamatics.problems.Problem;
import chankyin.mentamatics.problems.generator.ProblemGenerator;
import chankyin.mentamatics.ui.pref.PrefActivity;
import lombok.Getter;

import java.util.Random;

public class MainActivity extends BaseActivity{
	@Getter(lazy = true) private final Random random = new Random();

	private EditText input;

	@Getter private Problem currentProblem;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		input = (EditText) findViewById(R.id.main_input);
		input.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent){
				input.requestFocus();
				InputMethodManager inputMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMgr.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				return true;
			}
		});

		nextProblem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.menu_pref){
			startActivity(new Intent(this, PrefActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	public void nextProblem(){
		setCurrentProblem(ProblemGenerator.generate(Main.getInstance(this).getConfig(), getRandom()));
	}

	public void setCurrentProblem(Problem currentProblem){
		this.currentProblem = currentProblem;
		currentProblem.express(this);
	}

	public void onInputButtonClick(View view){
		Editable text = onInputButtonClick0(view);
		if(text.length() == 0){
			return;
		}
		Decimal val;
		try{
			val = Decimal.parseString(text.toString());
		}catch(NumberFormatException e){
			return;
		}
		if(currentProblem.getAnswer().equalsIgnoreExponent(val)){
			nextProblem();
		}
	}

	public Editable onInputButtonClick0(View view){
		Editable text = input.getText();
		int length = text.length();
		char append;

		switch(view.getId()){
			case R.id.main_input_0:
				append = '0';
				break;
			case R.id.main_input_1:
				append = '1';
				break;
			case R.id.main_input_2:
				append = '2';
				break;
			case R.id.main_input_3:
				append = '3';
				break;
			case R.id.main_input_4:
				append = '4';
				break;
			case R.id.main_input_5:
				append = '5';
				break;
			case R.id.main_input_6:
				append = '6';
				break;
			case R.id.main_input_7:
				append = '7';
				break;
			case R.id.main_input_8:
				append = '8';
				break;
			case R.id.main_input_9:
				append = '9';
				break;
			case R.id.main_input_point:
				if(length == 0){
					text.append('0');
				}
				append = '.';
				break;
			case R.id.main_input_back:
				if(length >= 1){
					text.delete(length - 1, length);
				}
				return text;
			case R.id.main_input_reset:
				text.clear();
				return text;
			case R.id.main_input_sign:
				if(length >= 1 && text.charAt(0) == '-'){
					text.delete(0, 1);
				}else{
					text.insert(0, "-");
				}
				return text;
			default:
				throw new UnsupportedOperationException(getResources().getResourceName(view.getId()) + " not handled");
		}
		text.append(append);
		return text;
	}
}
