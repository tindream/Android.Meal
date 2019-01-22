package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class LoginAutoEventInfo extends EventInfo {

    public String mac;
    public long UserId;

    public LoginAutoEventInfo() {
        this.Types = LoadType.AutoLogin;
        this.mac = Method.getMac();
        this.UserId = Config.Admin.UserId;
    }
}
