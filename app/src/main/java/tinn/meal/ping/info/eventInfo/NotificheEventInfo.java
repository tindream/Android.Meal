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
        this.Message = "success";
    }

    public NotificheEventInfo(LoadType from, String msg) {
        super(LoadType.Error);
        this.from = from;
        this.Message = msg;
    }

    @Override
    public String getMsg() {
        String desc = "";
        if (Types == LoadType.Error) {
            desc += from + " " + Types;
        } else {
            desc += Types;
            if (!Method.isEmpty(Message)) desc += " " + Message;
        }
        return desc;
    }
}
