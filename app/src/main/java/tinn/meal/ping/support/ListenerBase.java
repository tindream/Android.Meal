package tinn.meal.ping.support;

import tinn.meal.ping.enums.IListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;

public abstract class ListenerBase implements IListener {
    protected ILoadListener loadListener;

    public void setListener(ILoadListener listener) {
        this.loadListener = listener;
    }

    public void onListener(LoadType type) {
        onListener(new LoadInfo(type));
    }

    public void onListener(LoadInfo e) {
        try {
            if (loadListener != null)
                loadListener.onReady(e);
        } catch (Exception ex) {
            Method.log(ex);
        }
    }
}
