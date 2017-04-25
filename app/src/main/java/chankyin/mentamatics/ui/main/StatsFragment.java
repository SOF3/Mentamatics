package chankyin.mentamatics.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;

import java.text.DecimalFormat;

public class StatsFragment extends Fragment implements Runnable{
	private View contentView;
	private TextView answers;
	private TextView totalTime;
	private TextView avgTime;
	private Handler handler;

	private boolean resumed;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
		handler = new Handler();

		contentView = inflater.inflate(R.layout.frag_stats, container, false);
		answers = (TextView) contentView.findViewById(R.id.main_stats_answers);
		totalTime = (TextView) contentView.findViewById(R.id.main_stats_total_time);
		avgTime = (TextView) contentView.findViewById(R.id.main_stats_avg_time);
		return contentView;
	}

	@Override
	public void onResume(){
		super.onResume();
		handler.postDelayed(this, 10);
		resumed = true;
	}

	@Override
	public void onPause(){
		super.onPause();
		resumed = false;
	}

	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	@Override
	public void run(){
		HomeActivity activity = (HomeActivity) getActivity();
		if(activity == null){
			Log.d(Main.TAG, "StatsFragment.getActivity() == null");
			return;
		}
		Main main = Main.getInstance(activity);

		float oldTotal = main.getBriefStats().time;
		double usedTime = (System.nanoTime() - activity.getCurrentProblem().getStartTime()) * 1e-9;
		int oldAnswers = main.getBriefStats().answers;

		answers.setText(getString(R.string.main_stats_format_correct_answers, oldAnswers));
		totalTime.setText(getString(R.string.main_stats_format_total_time,
				DECIMAL_FORMAT.format(oldTotal),
				DECIMAL_FORMAT.format(usedTime)));
		avgTime.setText(getString(R.string.main_stats_format_average_time,
				oldAnswers == 0 ? getResources().getString(R.string.stats_na) : DECIMAL_FORMAT.format(oldTotal / oldAnswers),
				DECIMAL_FORMAT.format((oldTotal + usedTime) / (oldAnswers + 1))));
		if(resumed){
			handler.postDelayed(this, 10);
		}
	}
}
