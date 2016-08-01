package chankyin.mentamatics.problems;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.nantaphop.fluentview.Fluent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static chankyin.mentamatics.Main.*;

@RequiredArgsConstructor
public class LiteralQuestion implements Question{
	@Getter private String literal;

	@Override
	public void populate(LinearLayout layout){
		layout.addView(defluent(Fluent.with(
				new TextView(layout.getContext()))
				.setText(literal)
				.setLayoutParams(MP_MP)
		));
	}
}
