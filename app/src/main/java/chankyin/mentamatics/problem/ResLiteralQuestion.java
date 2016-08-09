package chankyin.mentamatics.problem;

import android.support.annotation.StringRes;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.nantaphop.fluentview.Fluent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static chankyin.mentamatics.Main.*;

@RequiredArgsConstructor
public class ResLiteralQuestion implements Question{
	@Getter @StringRes private final int literal;

	@Override
	public void populateQuestionLayout(LinearLayout layout){
		layout.addView(defluent(Fluent.with(
				new TextView(layout.getContext()))
				.setText(literal)
				.setLayoutParams(MP_MP)
		));
	}
}
