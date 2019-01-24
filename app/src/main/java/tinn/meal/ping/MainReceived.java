package tinn.meal.ping;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import tinn.meal.ping.activity.LoginActivity;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.enums.requestType;
import tinn.meal.ping.info.eventInfo.ErrorEventInfo;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.support.Cache;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class MainReceived extends MainBaseActivity {
    protected void received(String msg) {
        try {
            if (msg.equals("hello,world")) return;
            EventInfo eventInfo = new Gson().fromJson(msg, EventInfo.class);
            if (eventInfo == null) return;
            Cache.addNotified(eventInfo.Types);
            switch (eventInfo.Types) {
                case AutoLogin:
                    Method.startNotification(1, "Received", "AutoLogin");
                    break;
                case Error:
                    ErrorEventInfo error = new Gson().fromJson(msg, ErrorEventInfo.class);
                    if (error.FromTypes == LoadType.AutoLogin) {
                        Method.confirm(this, error.Message).setListener(obj -> {
                            Config.Admin.UserId = 0;
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivityForResult(intent, requestType.loginUpdate);
                        });
                    }
                    break;
            }
        } catch (JsonSyntaxException e) {
            if (!msg.equals("hello,world")) Method.log(msg);
        } catch (Exception e) {
            Method.log(e);
        }
    }
}
