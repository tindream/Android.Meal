package tinn.meal.ping.enums;

import tinn.meal.ping.info.loadInfo.LoadInfo;

public interface ILoadListener {
    void onReady(LoadInfo info);
}
