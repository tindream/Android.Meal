package tinn.meal.ping.support;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.TimeInfo;

public class AsyncLoad extends AsyncBase {
    public AsyncLoad() {
        initData(new LoadInfo());
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        new SQLiteServer().Load(emitter);
    }
}
