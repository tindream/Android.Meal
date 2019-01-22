package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.HolderInfo;

public class LoaderInfo extends LoadInfo {

    public HolderInfo holderInfo;

    public LoaderInfo(LoadType type, HolderInfo holderInfo) {
        this.Types = type;
        this.holderInfo = holderInfo;
    }
}
