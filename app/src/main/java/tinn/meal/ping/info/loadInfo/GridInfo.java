package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class GridInfo extends LoadInfo {

    public int imageId;

    public GridInfo(int imageId,  String msg) {
        super(LoadType.none);
        this.imageId = imageId;
        this.Message = msg;
    }
}
