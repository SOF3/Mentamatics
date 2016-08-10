package chankyin.mentamatics.problem.question;

import android.content.Context;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.config.ConfigConstants;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Operator;
import com.github.nantaphop.fluentview.Fluent;
import lombok.RequiredArgsConstructor;

import static chankyin.mentamatics.Main.defluent;

@RequiredArgsConstructor
public class TripletQuestion implements Question{
	private final RealFloat a;
	private final Operator op;
	private final RealFloat b;

	@Override
	public void populateQuestionLayout(LinearLayout layout){
		Context ctx = layout.getContext();
		if(Main.getInstance().getConfig().getBoolean(ConfigConstants.KEY_GUI_VERTICAL_ALIGN)){
			TableLayout table = new TableLayout(ctx);
			TableRow row0 = new TableRow(ctx);
			TextView upper = new TextView(ctx);
			upper.setText(a.toUserStringHtml());
			row0.addView(upper);
			table.addView(row0);
			TableRow row1 = new TableRow(ctx);
			row1.addView(defluent(Fluent.with(new TextView(ctx)).setText(op.toString())));
			TextView lower = new TextView(ctx);
			lower.setText(b.toUserStringHtml());
			row1.addView(lower);
			table.addView(row1);
			layout.addView(table);
		}else{
			TextView textView = new TextView(ctx);
			textView.setText(Main.fromHtml(
					a.toUserString(true) + "&nbsp;" +
							Html.escapeHtml(op.toString()) + "&nbsp;" +
							b.toUserString(true)));
			layout.addView(textView);
		}
	}
}
