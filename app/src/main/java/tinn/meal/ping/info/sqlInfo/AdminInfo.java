package tinn.meal.ping.info.sqlInfo;

import java.util.Date;

import tinn.meal.ping.data.SQLiteServer;
import tinn.meal.ping.support.Config;

public class AdminInfo {
    public final static String DbName = "dininglc";
    public final static int Port = 8084;

    public int Version = 0;
    public String Host = "127.0.0.1";
    public String User = "meiling";
    public String Pwd = "meiling";

    //同步pc设置
    //更新比较
    public long UpdateVersion;

    //允许自助人数
    public boolean AllowSelfPeople;
    //设备自助限制时间
    public long PhoneLimitTime;
    //餐桌自助限制时间
    public long TableLimitTime;
    //单人最大自助点菜数量
    public int MaxQuantity;
    ///报警电量值
    public int BatteryPercent;
}