package tinn.meal.ping.support;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.DisplayMetrics;

import java.util.Currency;
import java.util.Locale;

import tinn.meal.ping.info.sqlInfo.AdminInfo;
import tinn.meal.ping.view.View_Base;

public class Config {
    public static final String Text = "Meal";
    public static final String Loading = "Loading...";
    public static final String Suffix = "Tin";
    public static final String None = "None";

    public static AdminInfo Admin = new AdminInfo();
    public static View_Base view_base;
    public static boolean checking;

    public static Activity context;
    public static String currency;
    public static TcpClient client;
    public static DisplayMetrics display;

    public static void load(Activity activity) {
        display = Method.getDisplay(activity);
        currency = Currency.getInstance(Locale.GERMANY).getSymbol(Locale.GERMANY);
    }

    public static void load() {
        client = new TcpClient();
    }
}