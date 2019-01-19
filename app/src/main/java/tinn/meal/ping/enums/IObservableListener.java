package tinn.meal.ping.enums;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.info.loadInfo.LoadInfo;

public interface IObservableListener {
    void onReady(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception;
}
