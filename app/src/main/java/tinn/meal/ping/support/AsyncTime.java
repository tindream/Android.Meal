package tinn.meal.ping.support;

import android.view.View;
import android.widget.TextView;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.TimeInfo;

public class AsyncTime extends AsyncBase {
    public AsyncTime(View view, long time, long onlyTime, String msg) {
        view.setTag(new TimeInfo(onlyTime));
        initData(new TimeInfo(view, LoadType.time, time, onlyTime, msg));
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        switch (info.type) {
            case time:
                TimeInfo timeInfo = (TimeInfo) info;
                emitter.onNext(new TimeInfo(LoadType.timeStart));
                if (timeInfo.iWait()) {
                    emitter.onNext(timeInfo);
                    Thread.sleep(1000 - System.currentTimeMillis() % 1000);
                }
                while (timeInfo.iWait()) {
                    TimeInfo temp = (TimeInfo) timeInfo.view.getTag();
                    if (timeInfo.onlyTime != temp.onlyTime) return;
                    emitter.onNext(timeInfo);
                    Thread.sleep(1000);
                }
                emitter.onNext(new LoadInfo(LoadType.timeComplete));
                break;
        }
    }

    @Override
    protected void onCallUI(LoadInfo info) {
        switch (info.type) {
            case time:
                TimeInfo timeInfo = (TimeInfo) info;
                ((TextView) timeInfo.view).setText(timeInfo.getTimes());
                break;
        }
        super.onCallUI(info);
    }
}
