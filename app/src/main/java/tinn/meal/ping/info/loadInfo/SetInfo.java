package tinn.meal.ping.info.loadInfo;

import tinn.meal.ping.enums.LoadType;

public class SetInfo extends LoadInfo {

    public int imageId;
    public boolean iHeard;

    public SetInfo(int imageId, String msg) {
        super(LoadType.none);
        this.imageId = imageId;
        this.Message = msg;
    }

    public SetInfo(String msg) {
        super(LoadType.none);
        this.Message = msg;
        this.iHeard = true;
    }
}
