package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;

public class ErrorEventInfo extends EventInfo {

    public LoadType FromTypes;

    public ErrorEventInfo(LoadType from, String msg) {
        super(LoadType.Error);
        this.FromTypes = from;
        this.Message = msg;
    }
}
