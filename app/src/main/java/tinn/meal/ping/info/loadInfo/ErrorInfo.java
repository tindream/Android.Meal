package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class ErrorInfo extends LoadInfo {

    public LoadType from;

    public ErrorInfo(LoadType from, String msg) {
        super(LoadType.error);
        this.from = from;
        this.msg = msg;
    }
}
