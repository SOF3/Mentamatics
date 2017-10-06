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
import chankyin.mentamatics.StatsDb;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.DecimalFormat;

import static chankyin.mentamatics.TestUtils.debug;

public class StatsFragment extends Fragment implements Runnable{
	private TextView overallAnswersView;
	private TextView overallTotalTimeView;
	private TextView overallAverageTimeView;
	private TextView recentAnswersView;
	private TextView recentTotalTimeView;
	private TextView recentAverageTimeView;
	private Handler handler;
	private boolean resumed;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
		handler = new Handler();

		View contentView = inflater.inflate(R.layout.frag_stats, container, false);
		overallAnswersView = contentView.findViewById(R.id.main_stats_answers);
		overallTotalTimeView = contentView.findViewById(R.id.main_stats_total_time);
		overallAverageTimeView = contentView.findViewById(R.id.main_stats_avg_time);
		recentAnswersView = contentView.findViewById(R.id.main_stats_answers_recent);
		recentTotalTimeView = contentView.findViewById(R.id.main_stats_total_time_recent);
		recentAverageTimeView = contentView.findViewById(R.id.main_stats_avg_time_recent);
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

		// TODO show more stats
		StatsDb statsDb = main.getStatsDb();
		StatsDb.StatSet overall = statsDb.getStats();
		StatsDb.StatSet last50 = statsDb.getLastStats(50);
		debug(ToStringBuilder.reflectionToString(overall));
		double overallSumDuration = overall.getSum() * 1e-9;
		int overallAnswers = overall.getCount();
		double recentSumDuration = overall.getSum() * 1e-9;
		int recentAnswers = overall.getCount();

		double usedTime = (System.nanoTime() - activity.getCurrentProblem().getStartTime()) * 1e-9;

		overallAnswersView.setText(getString(R.string.main_stats_format_correct_answers, overallAnswers));
		overallTotalTimeView.setText(getString(R.string.main_stats_format_total_time,
				DECIMAL_FORMAT.format(overallSumDuration),
				DECIMAL_FORMAT.format(usedTime)));
		if(overallAnswers == 0){
			overallAverageTimeView.setText(getString(R.string.main_stats_format_average_time,
					getResources().getString(R.string.stats_na),
					DECIMAL_FORMAT.format((overallSumDuration + usedTime) / (overallAnswers + 1))));
		}else{
			overallAverageTimeView.setText(getString(R.string.main_stats_format_average_time,
					DECIMAL_FORMAT.format(overallSumDuration / overallAnswers),
					DECIMAL_FORMAT.format((overallSumDuration + usedTime) / (overallAnswers + 1))));
		}

		recentAnswersView.setText(getString(R.string.main_stats_format_correct_answers, recentAnswers));
		recentTotalTimeView.setText(getString(R.string.main_stats_format_total_time,
				DECIMAL_FORMAT.format(recentSumDuration),
				DECIMAL_FORMAT.format(usedTime)));
		if(recentAnswers == 0){
			recentAverageTimeView.setText(getString(R.string.main_stats_format_average_time,
					getResources().getString(R.string.stats_na),
					DECIMAL_FORMAT.format((recentSumDuration + usedTime) / (recentAnswers + 1))));
		}else{
			recentAverageTimeView.setText(getString(R.string.main_stats_format_average_time,
					DECIMAL_FORMAT.format(recentSumDuration / recentAnswers),
					DECIMAL_FORMAT.format((recentSumDuration + usedTime) / (recentAnswers + 1))));
		}
		if(resumed){
			handler.postDelayed(this, 10);
		}
	}
}
