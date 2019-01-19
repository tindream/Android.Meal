package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class ProgressInfo extends LoadInfo {

    public int value;

    public ProgressInfo(int value) {
        this.type = LoadType.progress;
        this.value = value;
    }
}
