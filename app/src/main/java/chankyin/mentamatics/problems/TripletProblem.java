package chankyin.mentamatics.problems;

import android.widget.LinearLayout;
import chankyin.mentamatics.Main;

public class TripletProblem implements Question{
	private final double a;
	private final Operator op;
	private final double b;

	public TripletProblem(double a, Operator op, double b){
		this.a = a;
		this.op = op;
		this.b = b;
	}

	@Override
	public void populate(LinearLayout layout){
		if(Main.getInstance().getConfig().isVerticalAlign()){

		}
	}
}
