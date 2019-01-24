package tinn.meal.ping.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.w3c.dom.Text;

import tinn.meal.ping.R;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.ChildActivity;

public class NotificationActivity extends ChildActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_notification;
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        if (!Method.isEmpty(title)) toolbar.setTitle(title);
        String msg = getIntent().getStringExtra("msg");
        if (!Method.isEmpty(msg)) setValue(R.id.notification_text, msg);
    }
}
