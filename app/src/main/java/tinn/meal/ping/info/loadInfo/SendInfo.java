package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class SendInfo extends LoadInfo {

    public LoadType from;

    public SendInfo(LoadType from, String msg) {
        super(LoadType.send, msg);
        this.from = from;
    }
}
