package chankyin.mentamatics.problem.question;

import android.widget.LinearLayout;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.ConfigConstants;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Operator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TripletQuestion implements ConfigConstants, Question{
	private final RealFloat a;
	private final Operator op;
	private final RealFloat b;

	@Override
	public void populateQuestionLayout(LinearLayout layout){
		if(Main.getInstance().getConfig().getBoolean(KEY_GUI_VERTICAL_ALIGN)){

		}
	}
}
