package tinn.meal.ping.info.eventInfo;

import tinn.meal.ping.enums.LoadType;

public class LoginEventInfo extends EventInfo {

    public long UserId;
    public String Name;
    public String Pad;

    public LoginEventInfo(String name, String pad) {
        this.Types = LoadType.Login;
        this.Name = name;
        this.Pad = pad;
    }
}
