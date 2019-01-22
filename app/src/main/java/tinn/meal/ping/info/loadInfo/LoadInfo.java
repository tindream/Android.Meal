package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class LoadInfo {

    public LoadType Types;
    public String Message;

    public LoadInfo() {
    }

    public LoadInfo(LoadType type) {
        this.Types = type;
    }

    public LoadInfo(LoadType type, String msg) {
        this(type);
        this.Message = msg;
    }

    public String getMsg() {
        String desc = Types + "";
        if (Message != null) {
            if (Types == LoadType.Error) desc += "\n";
            else desc += "\t";
            desc += Message;
        }
        return desc;
    }

    @Override
    public String toString() {
        return Types + "=>" + getMsg();
    }
}
