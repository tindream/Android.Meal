package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class LoginAutoEventInfo extends EventInfo {

    public String Mac;
    public long UserId;

    public LoginAutoEventInfo() {
        this.Types = LoadType.AutoLogin;
        this.Mac = Method.getMac();
        this.UserId = Config.Admin.UserId;
    }
}
