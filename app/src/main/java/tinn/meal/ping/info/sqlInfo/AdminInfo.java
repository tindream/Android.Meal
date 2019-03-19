package tinn.meal.ping.info.sqlInfo;

import java.util.Date;

import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.support.Config;

public class AdminInfo {
    public int Version = 0;
    public long UserId;
    public String Name;
    public String Pad;
    public String Display;

    //同步pc设置
    //更新比较
    public long UpdateVersion;
    ///报警电量值
    public int BatteryPercent;
}