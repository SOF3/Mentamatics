package chankyin.mentamatics.ui.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.generator.ProblemGenerator;
import chankyin.mentamatics.ui.BaseActivity;
import chankyin.mentamatics.ui.pref.ConfigActivity;
import chankyin.mentamatics.ui.view.NumberInputField;
import lombok.Getter;
import lombok.NonNull;

import java.util.Random;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.*;
import static android.widget.RelativeLayout.*;
import static chankyin.mentamatics.LogUtils.debug;

public class HomeActivity extends BaseActivity{
	public final static String INTENT_EXTRA_PROBLEM_COUNT_QUITS = "chankyin.mentamatics.HOME_PROBLEM_COUNT_QUITS";

	public final static String PROBLEM_LAST_RANDOM_STATE = "Problem:lastRandomState";
	public final static String PROBLEM_TOTAL_ANSWERS = "Problem:totalAnswers";
	public final static String PROBLEM_TOTAL_TIME = "Problem:totalTime";

	@Getter(lazy = true) private final Handler handler = new Handler();
	@Getter(lazy = true) private final Random random = Main.getInstance(this).loadRandom(PROBLEM_LAST_RANDOM_STATE);

	@Getter private Problem currentProblem = null;

	@Getter private int countQuits;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		FragmentManager manager = getFragmentManager();
		manager.beginTransaction()
				.hide(manager.findFragmentById(R.id.fragment_keyboard))
				.commit();
		if(currentProblem == null){
			nextProblem();
		}

		int countQuits = getIntent().getIntExtra(INTENT_EXTRA_PROBLEM_COUNT_QUITS, 0);
		if(countQuits <= 0 && savedInstanceState != null){
			countQuits = savedInstanceState.getInt(INTENT_EXTRA_PROBLEM_COUNT_QUITS, 0);
		}
		this.countQuits = countQuits > 0 ? countQuits : -1;
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState){
		super.onSaveInstanceState(outState, outPersistentState);
		outState.putInt(INTENT_EXTRA_PROBLEM_COUNT_QUITS, countQuits);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.menu_pref){
			startActivity(new Intent(this, ConfigActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	public Problem nextProblem(){
		Main.getInstance(this).saveRandom(getRandom(), PROBLEM_LAST_RANDOM_STATE);
		Problem newProblem = ProblemGenerator.generate(Main.getInstance(this).getConfig(), getRandom());
		setCurrentProblem(newProblem);
		return newProblem;
	}

	public void setCurrentProblem(@NonNull final Problem currentProblem){
		this.currentProblem = currentProblem;
		currentProblem.setOnAnswerCorrectListener(new Problem.OnAnswerCorrectListener(){
			@Override
			public void onAnswerCorrect(){
				long endTime = System.nanoTime();
				Main.getInstance(HomeActivity.this).getStatsDb().incrementCorrect(endTime - currentProblem.getStartTime(), currentProblem.getQuestion().getType(), currentProblem.getQuestion().getFlags());
				--countQuits;
				if(countQuits == 0){
					setResult(RESULT_OK);
					finish();
				}

				Problem newProblem = nextProblem();
				newProblem.express(getFragmentManager().findFragmentById(R.id.fragment_problem).getView());
			}
		});

		debug("Current problem's solution: %s", currentProblem.getAnswer());

		getHandler().postDelayed(new Runnable(){
			@Override
			public void run(){
				View currentFocus = getCurrentFocus();
				if(currentFocus instanceof NumberInputField){
					((NumberInputField) currentFocus).openKeyboard();
				}
			}
		}, 100);
	}

	public void moveKeyboardToLeft(View view){
		findViewById(R.id.keyboard_gravity_left).setVisibility(GONE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_LEFT);
		if(SDK_INT >= JELLY_BEAN_MR1){
			params.addRule(ALIGN_PARENT_START);
		}
		findViewById(R.id.fragment_keyboard).setLayoutParams(params);
		findViewById(R.id.keyboard_gravity_right).setVisibility(VISIBLE);
	}

	public void moveKeyboardToRight(View view){
		findViewById(R.id.keyboard_gravity_left).setVisibility(VISIBLE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_RIGHT);
		if(SDK_INT >= JELLY_BEAN_MR1){
			params.addRule(ALIGN_PARENT_END);
		}
		findViewById(R.id.fragment_keyboard).setLayoutParams(params);
		findViewById(R.id.keyboard_gravity_right).setVisibility(GONE);
	}

	public void landSwitchKeyboardPositionLeft(View view){
		View main = findViewById(R.id.main_main_group);
		View keyboard = findViewById(R.id.main_keyboard_group);
		View left = findViewById(R.id.keyboard_land_gravity_left);
		View right = findViewById(R.id.keyboard_land_gravity_right);

		RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
		mainParams.addRule(ALIGN_PARENT_RIGHT);
		mainParams.addRule(RIGHT_OF, R.id.main_keyboard_group);
		if(SDK_INT >= JELLY_BEAN_MR1){
			mainParams.addRule(ALIGN_PARENT_END);
			mainParams.addRule(END_OF, R.id.main_keyboard_group);
		}

		RelativeLayout.LayoutParams keyboardParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
		keyboardParams.addRule(ALIGN_PARENT_LEFT);
		if(SDK_INT >= JELLY_BEAN_MR1){
			keyboardParams.addRule(ALIGN_PARENT_START);
		}

		keyboard.setLayoutParams(keyboardParams);
		main.setLayoutParams(mainParams);

		left.setVisibility(GONE);
		right.setVisibility(VISIBLE);
	}

	public void landSwitchKeyboardPositionRight(View view){
		View main = findViewById(R.id.main_main_group);
		View keyboard = findViewById(R.id.main_keyboard_group);
		View left = findViewById(R.id.keyboard_land_gravity_left);
		View right = findViewById(R.id.keyboard_land_gravity_right);

		RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
		mainParams.addRule(ALIGN_PARENT_LEFT);
		mainParams.addRule(LEFT_OF, R.id.main_keyboard_group);
		if(SDK_INT >= JELLY_BEAN_MR1){
			mainParams.addRule(ALIGN_PARENT_START);
			mainParams.addRule(START_OF, R.id.main_keyboard_group);
		}

		RelativeLayout.LayoutParams keyboardParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
		keyboardParams.addRule(ALIGN_PARENT_RIGHT);
		if(SDK_INT >= JELLY_BEAN_MR1){
			keyboardParams.addRule(ALIGN_PARENT_END);
		}

		keyboard.setLayoutParams(keyboardParams);
		main.setLayoutParams(mainParams);

		left.setVisibility(VISIBLE);
		right.setVisibility(GONE);
	}
}
