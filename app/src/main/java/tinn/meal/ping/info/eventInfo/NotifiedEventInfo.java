package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Method;

public class NotifiedEventInfo extends EventInfo {

    public LoadType from;

    public NotifiedEventInfo(LoadType type) {
        super(type);
        this.Message = "success";
    }

    public NotifiedEventInfo(LoadType from, String msg) {
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
