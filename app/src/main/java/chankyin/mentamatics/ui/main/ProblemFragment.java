package chankyin.mentamatics.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import chankyin.mentamatics.R;

public class ProblemFragment extends Fragment{
	public final static String TAG = "ProblemFragment";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.frag_problem, container, false);
	}

	@Override
	public void onResume(){
		super.onResume();

		HomeActivity activity = (HomeActivity) getActivity();
		activity.getCurrentProblem().express(getView());
	}
}
