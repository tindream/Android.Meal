package tinn.meal.ping.info.eventInfo;

import java.text.DateFormat;
import java.util.Date;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class EventInfo extends LoadInfo {

    public long tableId;
    public Date date;
    public boolean result;

    public EventInfo() {
        this.date = new Date();
    }

    public EventInfo(LoadType type) {
        this();
        this.type = type;
    }

    public EventInfo(LoadType type, String msg) {
        this(type);
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        String desc = "";
        if (!Method.isEmpty(msg)) desc += msg;
        else desc += type;
        return desc;
    }

    @Override
    public String toString() {
        //本地时区
        // 可以精确到秒  2017-4-16 12:43:37
        DateFormat format = DateFormat.getDateTimeInstance();
        String desc = format.format(date) + "=>" + getMsg();
        return desc;
    }
}
