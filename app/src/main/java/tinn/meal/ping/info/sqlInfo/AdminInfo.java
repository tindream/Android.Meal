package tinn.meal.ping.info.sqlInfo;

import java.util.Date;

import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.support.Config;

public class AdminInfo {
    public final static String DbName = "dininglc";
    public final static int Port = 8087;

    public int Version = 0;
    public String Host = "47.254.135.116";
    public String UserName = "meiling";
    public String UserPwd = "meiling";

    public long UserId;
    public String Display;

    //同步pc设置
    //更新比较
    public long UpdateVersion;
    ///报警电量值
    public int BatteryPercent;
}