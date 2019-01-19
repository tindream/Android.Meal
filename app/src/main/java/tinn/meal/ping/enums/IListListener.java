package tinn.meal.ping.enums;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.ViewHolder;

public interface IListListener {
    <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object);
}
