package tinn.meal.ping.enums;

import tinn.meal.ping.info.loadInfo.LoadInfo;

public interface IListener {
    void setListener(ILoadListener listener);

    void onListener(LoadType type);

    void onListener(LoadInfo e);
}
