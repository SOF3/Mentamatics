package chankyin.mentamatics.problem;

import android.widget.LinearLayout;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.ConfigConstants;
import chankyin.mentamatics.math.real.RealNumber;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TripletQuestion implements ConfigConstants, Question{
	private final RealNumber a;
	private final Operator op;
	private final RealNumber b;

	@Override
	public void populateQuestionLayout(LinearLayout layout){
		if(Main.getInstance().getConfig().getBoolean(KEY_GUI_VERTICAL_ALIGN)){

		}
	}
}
