package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Method;

public class LoginAutoEventInfo extends EventInfo {

    public String mac;

    public LoginAutoEventInfo() {
        this.type = LoadType.loginAuto;
        this.mac = Method.getMac();
    }
}
