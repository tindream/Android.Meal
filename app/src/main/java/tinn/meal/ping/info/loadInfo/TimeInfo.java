package tinn.meal.ping.info.loadInfo;

import android.view.View;

import java.sql.Time;
import java.text.SimpleDateFormat;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Method;

public class TimeInfo extends LoadInfo {
    private long t;
    private SimpleDateFormat format;

    public String times;
    public String msg;
    public View view;
    public long onlyTime;
    public long endTime;

    public TimeInfo(long onlyTime) {
        this.type = LoadType.time;
        this.onlyTime = onlyTime;
    }

    public TimeInfo(LoadType type) {
        this.type = type;
    }

    public TimeInfo(View view, LoadType type, long time, long onlyTime, String msg) {
        this.type = type;
        this.endTime = time + onlyTime;
        if (Method.isEmpty(msg)) msg = "";
        this.msg = msg;
        this.onlyTime = onlyTime;
        this.view = view;
        this.t = new Time(0, 0, 0).getTime();
        this.format = new SimpleDateFormat("HH:mm:ss");
    }

    public boolean iWait() {
        return endTime > System.currentTimeMillis();
    }

    public String getTimes() {
        return format.format(new Time(t + endTime - System.currentTimeMillis())) + msg;
    }
}
