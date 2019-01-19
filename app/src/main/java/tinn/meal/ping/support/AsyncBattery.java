package tinn.meal.ping.support;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.info.eventInfo.BatteryEventInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.TimeInfo;

public class AsyncBattery extends AsyncBase {
    public AsyncBattery() {
        initData(new TimeInfo(60));
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        long time = ((TimeInfo) info).onlyTime;
        //定时触发
        while (true) {
            int percent = Method.getBattery();
            Method.log("Battery:" + percent + "%");
            if (percent <= Config.Admin.BatteryPercent) {
                emitter.onNext(new BatteryEventInfo(percent));
                break;
            }
            long total = System.currentTimeMillis();
            long wait = time - total / 1000 % 60;
            wait = wait * 1000 + 1000 - total % 1000;
            Method.log("BatteryWait:" + wait);
            Thread.sleep(wait);
        }
    }
}
