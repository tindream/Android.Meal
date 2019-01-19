package tinn.meal.ping.fragments;

import android.support.v4.app.Fragment;

import tinn.meal.ping.enums.IListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Method;

public class Fragment_Base extends Fragment implements IListener {
    protected ILoadListener listener;

    public void setListener(ILoadListener listener) {
        this.listener = listener;
    }

    public void onListener(LoadType type) {
        onListener(new LoadInfo(type));
    }

    public void onListener(LoadInfo e) {
        try {
            if (listener != null)
                listener.onReady(e);
        } catch (Exception ex) {
            Method.log(ex);
        }
    }
}
