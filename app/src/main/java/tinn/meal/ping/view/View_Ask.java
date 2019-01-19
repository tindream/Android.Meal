package tinn.meal.ping.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import tinn.meal.ping.R;
import tinn.meal.ping.enums.LoadType;

public class View_Ask extends View_Base {
    public void show(String msg) {
        super.showMore();

        TextView textView = view_custom.findViewById(R.id.ask_msg);
        textView.setText(msg);
        textView = view_custom.findViewById(R.id.ask_conferma);
        textView.setText(R.string.btn_confirm);
        textView = view_custom.findViewById(R.id.ask_cancel);
        textView.setText(R.string.btn_cancel);
    }

    @Override
    protected boolean onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.ask_conferma:
                onListener(LoadType.conferma);
                break;
        }
        return super.onBtnClick(v);
    }

    public void init(Activity activity) {
        super.init(activity, R.layout.view_ask, new int[]{R.id.ask_cancel, R.id.ask_conferma});
    }
}
