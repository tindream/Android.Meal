package tinn.meal.ping.view;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import tinn.meal.ping.R;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class View_About extends View_Base {
    public void show() {
        super.show();

        TextView textView = view_custom.findViewById(R.id.about_title);
        textView.setText(R.string.nav_about);

        String desc = "Version：" + activity.getString(R.string.version);
        SpannableString span = new SpannableString(desc);
        span.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), 8, desc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView = view_custom.findViewById(R.id.id_msg);
        textView.setText(span); //更新UI
    }

    public void show(String msg) {
        TextView textView = view_custom.findViewById(R.id.id_msg);
        textView.setText(msg);

        super.show();
    }

    public void init(Activity activity) {
        super.init(activity, R.layout.view_about);
    }
}
