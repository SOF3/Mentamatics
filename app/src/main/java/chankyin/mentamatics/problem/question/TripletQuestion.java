package chankyin.mentamatics.problem.question;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.ConfigConstants;
import chankyin.mentamatics.math.real.RealFloat;
import chankyin.mentamatics.problem.Operator;
import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TripletQuestion implements Question{
	private final RealFloat a;
	private final Operator op;
	private final RealFloat b;
	@Getter private final int type;
	@Getter private final int flags;

	@Override
	public void populateQuestionLayout(LinearLayout layout){
		Context ctx = layout.getContext();
		if(Main.getInstance().getConfig().getBoolean(ConfigConstants.KEY_GUI_VERTICAL_ALIGN)){
//			TableLayout table = new TableLayout(ctx);
//			TableRow row0 = new TableRow(ctx);
//			TextView upper = new TextView(ctx);
//			upper.setText(a.toUserStringHtml());
//			row0.addView(upper);
//			table.addView(row0);
//			TableRow row1 = new TableRow(ctx);
//			row1.addView(defluent(Fluent.with(new TextView(ctx)).setText(op.toString())));
//			TextView lower = new TextView(ctx);
//			lower.setText(b.toUserStringHtml());
//			row1.addView(lower);
//			table.addView(row1);
//			layout.addView(table);
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			assert inflater != null;
			View view = inflater.inflate(R.layout.triplet_question_vert, layout);
			CharSequence aText = a.toUserStringHtml();
			CharSequence bText = b.toUserStringHtml();
			int delta = aText.length() - bText.length();
			if(delta < 0){
				aText = TextUtils.concat(StringUtils.repeat(' ', -delta), aText);
			}else if(delta > 0){
				bText = TextUtils.concat(StringUtils.repeat(' ', delta), bText);
			}
			((TextView) view.findViewById(R.id.triplet_operand_a)).setText(aText);
			((TextView) view.findViewById(R.id.triplet_operand_b)).setText(bText);
			((TextView) view.findViewById(R.id.triplet_operator)).setText(op.toString());
		}else{
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			assert inflater != null;
			View view = inflater.inflate(R.layout.triplet_question_plain, layout);
			TextView textView = view.findViewById(R.id.triplet_expr);
			textView.setText(Main.fromHtml(
					a.toUserString(true) + "&nbsp;" +
							Html.escapeHtml(op.toString()) + "&nbsp;" +
							b.toUserString(true)));
		}
	}

	@Override
	public String toString(){
		return a.toString() + " {" + op.name() + "} " + b.toString() + " (type = " + type + ")";
	}
}
