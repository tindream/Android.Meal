package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;

public class LoginEventInfo extends EventInfo {

    public String user;
    public String pwd;

    public LoginEventInfo(String user, String pwd) {
        this.type = LoadType.login;
        this.user = user;
        this.pwd = pwd;
    }
}
