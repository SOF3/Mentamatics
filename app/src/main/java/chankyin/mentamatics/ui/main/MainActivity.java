package chankyin.mentamatics.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

import java.util.Random;

public class MainActivity extends BaseActivity{
	@Getter(lazy = true) private final Random random = new Random();

	@Getter private Problem currentProblem;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

	@Override
	@IdRes
	public int getContentViewId(){
		return R.id.main_content_view;
	}

	public void nextProblem(){
		Log.i(Main.TAG, "Generating next problem");
		setCurrentProblem(ProblemGenerator.generate(Main.getInstance(this).getConfig(), getRandom()));
	}

	public void setCurrentProblem(Problem currentProblem){
		this.currentProblem = currentProblem;
		currentProblem.setOnAnswerCorrectListener(new Problem.OnAnswerCorrectListener(){
			@Override
			public void onAnswerCorrect(){
				nextProblem();
				// TODO stat
			}
		});
		currentProblem.express(this);

		View currentFocus = getCurrentFocus();
		if(currentFocus instanceof NumberInputField){
			((NumberInputField) currentFocus).openKeyboard();
		}
	}
}
