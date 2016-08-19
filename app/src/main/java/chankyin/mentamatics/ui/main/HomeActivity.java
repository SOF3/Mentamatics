package chankyin.mentamatics.ui.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.problem.Problem;
import chankyin.mentamatics.problem.generator.ProblemGenerator;
import chankyin.mentamatics.ui.BaseActivity;
import chankyin.mentamatics.ui.pref.PrefActivity;
import chankyin.mentamatics.ui.view.NumberInputField;
import lombok.Getter;
import lombok.NonNull;

import java.util.Random;

public class HomeActivity extends BaseActivity{
	public final static String PROBLEM_LAST_RANDOM_STATE = "Problem:lastRandomState";

	@Getter(lazy = true) private final Handler handler = new Handler();
	@Getter(lazy = true) private final Random random = Main.getInstance(this).loadRandom(PROBLEM_LAST_RANDOM_STATE);

	@Getter private Problem currentProblem = null;

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

	public Problem nextProblem(){
		Main.getInstance(this).saveRandom(getRandom(), PROBLEM_LAST_RANDOM_STATE);
		Problem newProblem = ProblemGenerator.generate(Main.getInstance(this).getConfig(), getRandom());
		setCurrentProblem(newProblem);
		return newProblem;
	}

	public void setCurrentProblem(@NonNull Problem currentProblem){
		this.currentProblem = currentProblem;
		currentProblem.setOnAnswerCorrectListener(new Problem.OnAnswerCorrectListener(){
			@Override
			public void onAnswerCorrect(){
				Problem newProblem = nextProblem();
				newProblem.express(getFragmentManager().findFragmentById(R.id.fragment_problem).getView());
				// TODO stat
			}
		});

		Log.d(Main.TAG, "Current problem's solution: " + currentProblem.getAnswer());

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
}
