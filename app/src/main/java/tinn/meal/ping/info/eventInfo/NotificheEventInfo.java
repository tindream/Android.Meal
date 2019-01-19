package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Method;

public class NotificheEventInfo extends EventInfo {

    public LoadType from;
    public boolean iRead;

    public NotificheEventInfo(LoadType type, boolean iRead) {
        super(type);
        this.iRead = iRead;
    }

    public NotificheEventInfo(LoadType type) {
        super(type);
        this.msg = "success";
    }

    public NotificheEventInfo(LoadType from, String msg) {
        super(LoadType.error);
        this.from = from;
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        String desc = "";
        if (type == LoadType.error) {
            desc += from + " " + type;
        } else {
            desc += type;
            if (!Method.isEmpty(msg)) desc += " " + msg;
        }
        return desc;
    }
}
