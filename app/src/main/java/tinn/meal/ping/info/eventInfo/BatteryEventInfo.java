package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;

public class BatteryEventInfo extends EventInfo {

    public int percent;

    public BatteryEventInfo(int percent) {
        super(LoadType.battery);
        this.percent = percent;
    }
}
