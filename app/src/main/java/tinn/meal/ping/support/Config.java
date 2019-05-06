package tinn.meal.ping.support;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Environment;
import android.util.DisplayMetrics;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.io.File;
import java.util.Currency;
import java.util.Locale;

import tinn.meal.ping.info.sqlInfo.AdminInfo;
import tinn.meal.ping.mqtt.MQTTService;
import tinn.meal.ping.mqtt.MyServiceConnection;
import tinn.meal.ping.view.View_Base;

public class Config {
    public static final String Text = "Meal";
    public static final String Loading = "Loading...";
    public static final String Suffix = "FatturaPaA*";
    public static final String None = "None";
    public final static String DbName = "dininglc";
    public final static int Port = 8087;
    public final static int MPort = 8088;
    public final static String Host = "47.254.135.116";
    public final static String DbUserName = "meiling";
    public final static String DbPassword = "meiling";

    public static AdminInfo Admin = new AdminInfo();
    public static View_Base view_base;
    public static boolean checking;

    public static Activity context;
    public static String currency;
    public static DisplayMetrics display;
    public static File file;

    //MQTT
    public static String Topic = "AndroidMeal";             //MQTT订阅的主题
    public static String ClientId;                          //MQTT客户端标识
    public static MqttAndroidClient mqttClient;             //MQTT客户端
    public static MQTTService mqttService;                  //MQTT客户端服务
    public static MyServiceConnection serviceConnection;    //MQTT连接

    public static void load(Activity activity) {
        Config.context = activity;
        file = new File(Environment.getExternalStorageDirectory(), "/Tinn/Meal");
        if (!file.exists()) file.mkdirs();
        display = Method.getDisplay(activity);
        currency = Currency.getInstance(Locale.GERMANY).getSymbol(Locale.GERMANY);
    }

    public static void load() {
    }
}