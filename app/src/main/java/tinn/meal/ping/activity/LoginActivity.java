package tinn.meal.ping.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;

import tinn.meal.ping.R;
import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.enums.requestType;
import tinn.meal.ping.info.eventInfo.ErrorEventInfo;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.info.eventInfo.LoginEventInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.ChildActivity;

public class LoginActivity extends ChildActivity implements View.OnClickListener, ILoadListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_login;
        super.onCreate(savedInstanceState);
        try {
            Config.client.setListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorGrayLight));
            }
            setValue(R.id.login_user, "1");
            setValue(R.id.login_pad, "1");
        } catch (Exception e) {
            Method.log(e);
        }
        findViewById(R.id.login_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                String user = getValue(R.id.login_user);
                String pad = getValue(R.id.login_pad);
                Config.client.send(new LoginEventInfo(user, Method.EncryptMD5(pad + Config.Suffix)));
                TextView btn = findViewById(R.id.login_btn);
                btn.setText(Config.Loading);
                break;
        }
    }

    @Override
    public void onReady(LoadInfo info) {
        try {
            if (info.Types == LoadType.Error) {
                ErrorEventInfo error = (ErrorEventInfo) info;
                if (error.FromTypes == LoadType.Login) {
                    TextView btn = findViewById(R.id.login_btn);
                    btn.setText(R.string.btn_login);
                }
                return;
            }
            if (info.Message == null || info.Message.equals("hello,world")) return;
            EventInfo eventInfo = new Gson().fromJson(info.Message, EventInfo.class);
            if (eventInfo == null) return;
            switch (eventInfo.Types) {
                case Login:
                    LoginEventInfo login = new Gson().fromJson(info.Message, LoginEventInfo.class);
                    Config.Admin.UserId = login.UserId;
                    new SQLiteServer().updateAdmin("UserId", Config.Admin.UserId);

                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("UserId", login.UserId + "");  //向intent写入数据
                    data.putExtras(bundle);  //将数据模块放入切换类中
                    setResult(requestType.loginResult, data);
                    finish();
                    break;
                case Error:
                    ErrorEventInfo error = new Gson().fromJson(info.Message, ErrorEventInfo.class);
                    if (error.FromTypes == LoadType.Login) {
                        TextView btn = findViewById(R.id.login_btn);
                        btn.setText(R.string.btn_login);
                        Method.show(this, error.Message);
                    }
                    break;
            }
        } catch (Exception e) {
            Method.log(e);
        }
    }

    //按返回键直接返回桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
