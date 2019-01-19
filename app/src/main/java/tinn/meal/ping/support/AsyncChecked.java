package tinn.meal.ping.support;

import android.os.Build;
import android.support.annotation.RequiresApi;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.data.SQLServer;
import tinn.meal.ping.info.eventInfo.VersionEventInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;

public class AsyncChecked extends AsyncBase {
    public AsyncChecked(VersionEventInfo ve) {
        initData(ve);
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        switch (info.type) {
            case checkVersion:
                if (Config.checking) {
                    Method.log("checking...");
                    return;
                }
                try {
                    Config.checking = true;
                    long time = System.currentTimeMillis();
                    new SQLServer().checked(emitter, ((VersionEventInfo) info).version);
                    Method.log("Check.Total=" + (System.currentTimeMillis() - time));
                } finally {
                    Config.checking = false;
                }
                break;
        }
    }
}
