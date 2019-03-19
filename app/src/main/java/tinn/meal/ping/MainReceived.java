package tinn.meal.ping;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import tinn.meal.ping.activity.LoginActivity;
import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.enums.requestType;
import tinn.meal.ping.fragments.Fragment_My;
import tinn.meal.ping.info.eventInfo.ErrorEventInfo;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.info.eventInfo.LoginEventInfo;
import tinn.meal.ping.support.Cache;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class MainReceived extends MainBaseActivity {
    protected void received(String msg) {
        try {
            EventInfo eventInfo = new Gson().fromJson(msg, EventInfo.class);
            if (eventInfo == null) return;
            Cache.addNotified(eventInfo.Types);
            switch (eventInfo.Types) {
                case Login:
                    LoginEventInfo login = new Gson().fromJson(msg, LoginEventInfo.class);
                    Config.Admin.Display = login.Display;
                    Fragment_My Fragment_my = (Fragment_My) fragmentList.get(3);
                    Fragment_my.loadUser();
                    //Method.startNotification(1, "Received", "AutoLogin");
                    break;
                case Error:
                    ErrorEventInfo error = new Gson().fromJson(msg, ErrorEventInfo.class);
                    if (error.FromTypes == LoadType.Login) {
                        Method.confirm(this, error.Message).setListener(obj -> {
                            Config.Admin.UserId = 0;
                            new SQLiteServer().updateAdmin("UserId", Config.Admin.UserId);
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivityForResult(intent, requestType.loginUpdate);
                        });
                    }
                    break;
            }
        } catch (JsonSyntaxException e) {
            Method.log(msg);
            Method.startNotification(11, "测试", msg);
        } catch (Exception e) {
            Method.log(e);
        }
    }
}
