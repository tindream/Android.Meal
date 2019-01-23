package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;

public class VersionEventInfo extends EventInfo {

    public long version;

    public VersionEventInfo(long version) {
        super(LoadType.check);
        this.version = version;
    }
}
