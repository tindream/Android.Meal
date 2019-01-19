package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class LoadInfo {

    public LoadType type;
    public String msg;

    public LoadInfo() {
    }

    public LoadInfo(LoadType type) {
        this.type = type;
    }

    public LoadInfo(LoadType type, String msg) {
        this(type);
        this.msg = msg;
    }

    public String getMsg() {
        String desc = type + "";
        if (msg != null) {
            if (type == LoadType.error) desc += "\n";
            else desc += "\t";
            desc += msg;
        }
        return desc;
    }

    @Override
    public String toString() {
        return type + "=>" + getMsg();
    }
}
