package tinn.meal.ping;

import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.support.Cache;
import tinn.meal.ping.support.Method;

public class MainReceived extends MainBaseActivity {
    protected void received(String msg) {
        try {
            if (msg.equals("hello,world")) return;
            EventInfo eventInfo = new Gson().fromJson(msg, EventInfo.class);
            if (eventInfo == null) return;
            if (!eventInfo.Result) {
                Cache.addNotified(eventInfo.Types, eventInfo.Message);
                Method.show(this, eventInfo.Message);
                return;
            }
            Cache.addNotified(eventInfo.Types);
            switch (eventInfo.Types) {
                case Login:
                    break;
                case AutoLogin:
                    ViewPager vp = findViewById(R.id.mainViewPager);
                    vp.setCurrentItem(0, true);
                    break;
            }
        } catch (JsonSyntaxException e) {
            if (!msg.equals("hello,world")) Method.log(msg);
        } catch (Exception e) {
            Method.log(e);
        }
    }
}
