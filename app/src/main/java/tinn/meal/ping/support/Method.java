package tinn.meal.ping.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.ufreedom.uikit.FloatingText;
import com.ufreedom.uikit.effect.ScaleFloatingAnimator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import tinn.meal.ping.R;
import tinn.meal.ping.enums.IListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.info.loadInfo.ValueInfo;
import tinn.meal.ping.view.View_About;
import tinn.meal.ping.view.View_Ask;
import tinn.meal.ping.view.View_Confirm;

public class Method {
    private static Toast toast;

    public static void requestPower(String permission) {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(Config.context, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(Config.context, permission)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(Config.context, new String[]{permission}, 1);
            }
        }
    }

    public static int getBattery() {
        BatteryManager manager = (BatteryManager) Config.context.getSystemService(Config.context.BATTERY_SERVICE);
        int percent = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);///当前电量百分比
        return percent;
    }

    // 調用系統方法分享文件
    public static void shareFile(Context context) {
        File file = new File(Environment.getExternalStorageDirectory(), "/MerinH/log.txt");
        if (file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, context.getString(R.string.btn_share)));
        } else {
            hit("File Not Find");
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (Exception e) {
                return mime;
            }
        }
        return mime;
    }

    public static Bitmap getRoundTopBitmap(Paint paint, Bitmap bitmap, int roundPx) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int zoom = 1;
        while (width * height > 1024 * 1024) {
            width /= 2;
            height /= 2;
            zoom *= 2;
        }
        if (zoom > 1) bitmap = Method.zoomImg(bitmap, zoom);
        Bitmap output = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        Rect block = new Rect(0, bitmap.getHeight() - 100, roundPx, bitmap.getHeight());
        canvas.drawRect(block, paint);
        block = new Rect(bitmap.getWidth() - roundPx, bitmap.getHeight() - roundPx, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRect(block, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    //获取圆角矩形图片方法
    public static Bitmap getRoundBitmap(Paint paint, Bitmap bitmap, int roundPx) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int zoom = 1;
        while (width * height > 1024 * 1024) {
            width /= 2;
            height /= 2;
            zoom *= 2;
        }
        if (zoom > 1) bitmap = Method.zoomImg(bitmap, zoom);
        Bitmap output = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static IListener ask(Activity activity, String msg) {
        View_Ask view_ask = new View_Ask();
        view_ask.init(activity);
        view_ask.show(msg);
        return view_ask;
    }

    public static IListener confirm(Activity activity, String msg) {
        View_Confirm view_confirm = new View_Confirm();
        view_confirm.init(activity);
        view_confirm.show(msg);
        return view_confirm;
    }

    public static void show(Activity activity) {
        View_About view_about = new View_About();
        view_about.init(activity);
        view_about.show();
    }
    public static void show(Activity activity, String msg) {
        View_About view_about = new View_About();
        view_about.init(activity);
        view_about.show(msg);
    }

    public static void loadLanguage() {
        Resources resources = Config.context.getResources();
        Configuration configuration = resources.getConfiguration();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static void setSize(View view, int width, double height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (width != 0) params.width = width;
        if (height != 0) params.height = (int) height;
        view.setLayoutParams(params);
    }

    public static void setMargins(View view, double left, double top, double right, double bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins((int) left, (int) top, (int) right, (int) bottom);
            view.requestLayout();
        }
    }

    //隐藏虚拟按键//并且全屏
    public static void hideBottomUIMenu(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE |
                    //全屏
                    //View.SYSTEM_UI_FLAG_FULLSCREEN |
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions |= 0x00001000;
            } else {
                uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            window.getDecorView().setSystemUiVisibility(uiOptions);
        });
    }

    //根据网络接口获取获取mac
    public static String getMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            log(e);
        }
        return Config.None;
    }

//    //通过WiFiManager获取ip
//    public static String getIp(Activity activity) {
//        //获取wifi服务
//        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int i = wifiInfo.getIpAddress();
//        String ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
//        return ip;
//    }

    public static String EncryptMD5(String msg) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(msg.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (Exception e) {
            log(e);
            return Config.None;
        }
    }

    public static DisplayMetrics getDisplay(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        log("屏幕宽度x高度（dp）：" + (int) (dm.widthPixels / dm.density) + " x " + (int) (dm.heightPixels / dm.density) +
                "\t密度：" + dm.density);
        return dm;
    }

    public static void redDot(Context context, View view, String value) {
        Object obj = view.getTag();
        BadgeView badgeView;
        if (obj != null && obj.getClass() == BadgeView.class) {
            badgeView = (BadgeView) obj;
        } else {
            badgeView = new BadgeView(context);
            view.setTag(badgeView);
        }
        badgeView.setTargetView(view);
        badgeView.setBackground(6, context.getColor(R.color.colorRed));
        badgeView.setText(value);
        badgeView.setTextSize(7);
        badgeView.setTextColor(context.getColor(R.color.colorRed));
    }

    public static ValueInfo redDotNum(Context context, View view, int quantity, double total) {
        BadgeView badgeView = redDotNum(context, view, quantity);
        Object obj = badgeView.getTag();
        ValueInfo lastValue = new ValueInfo();
        if (obj != null) {
            lastValue = (ValueInfo) obj;
        }
        badgeView.setTag(new ValueInfo(quantity, total));
        return lastValue;
    }

    public static BadgeView redDotNum(Context context, View view, int quantity) {
        Object obj = view.getTag();
        BadgeView badgeView;
        if (obj != null && obj.getClass() == BadgeView.class) {
            badgeView = (BadgeView) obj;
        } else {
            badgeView = new BadgeView(context);
            view.setTag(badgeView);
        }
        badgeView.setTargetView(view);
        if (quantity > 99) {
            badgeView.setText("99+");
        } else {
            badgeView.setBadgeCount(quantity);
        }
        return badgeView;
    }

    public static void topDesc(Context context, View view, String desc) {
        Object obj = view.getTag();
        BadgeView badgeView;
        if (obj != null && obj.getClass() == BadgeView.class) {
            badgeView = (BadgeView) obj;
        } else {
            badgeView = new BadgeView(context);
            view.setTag(badgeView);
        }
        badgeView.setTargetView(view);
        badgeView.setText(desc);
        badgeView.setBackgroundColor(context.getColor(R.color.Transparent));
        badgeView.setTextColor(context.getColor(R.color.colorRed));
        badgeView.setTextSize(15);
        badgeView.setBadgeGravity(Gravity.LEFT);
        badgeView.setBadgeMargin(75, 5, 0, 0);
    }

    public static void tip(Activity activity, View view, String tip) {
        int size = 100;
        if (Config.display.density != 3) size = size * 55 / 100;
        FloatingText floatingText = new FloatingText.FloatingTextBuilder(activity)
                .textColor(activity.getColor(R.color.colorRed)) // 漂浮字体的颜色
                .textSize(size)  // 浮字体的大小
                .textContent(tip) // 浮字体的内容
                .offsetY(0) // FloatingText 相对其所贴附View的垂直位移偏移量
                .build();
        floatingText.attach2Window(); //将FloatingText贴附在Window上
        //启动漂浮效果
        floatingText.startFloating(view); // 传入一个View，FloatingText 就会相对于这个View执行漂浮效果
    }

    public static void tipFixed(Activity activity, View view, String tip, int size) {
        if (Config.display.density != 3) size = size * 55 / 100;
        FloatingText floatingText = new FloatingText.FloatingTextBuilder(activity)
                .textColor(activity.getColor(R.color.colorRed)) // 漂浮字体的颜色
                .textSize(size)  // 浮字体的大小
                .textContent(tip) // 浮字体的内容
                .floatingAnimatorEffect(new ScaleFloatingAnimator())//放大效果
                .offsetX(10) // FloatingText 相对其所贴附View的水平位移偏移量
                .offsetY(-view.getHeight() * 2 / 3) // FloatingText 相对其所贴附View的垂直位移偏移量
//                                                            .floatingAnimatorEffect(FloatingAnimator) // 漂浮动画
//                                                            .floatingPathEffect(FloatingPathEffect) // 漂浮的路径
                .build();
        floatingText.attach2Window(); //将FloatingText贴附在Window上
        //启动漂浮效果
        floatingText.startFloating(view); // 传入一个View，FloatingText 就会相对于这个View执行漂浮效果
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bitmap, int zoom) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) 1 / zoom;
        float scaleHeight = (float) 1 / zoom;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return temp;
    }

    //数字格式化
    public static String number(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    //货币格式化
    public static String money(double value) {
        return Config.currency + new DecimalFormat("0.00").format(value);
    }

    //byte[]保存到文件
    public static void byteToImage(byte[] data, File file) {
        try {
            FileOutputStream imageOutput = new FileOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception e) {
            log(e);
        }
    }

    //图像保存到文件
    public static void bitmapToImage(Bitmap bitmap, File file) {
        try {
            FileOutputStream imageOutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutput);
            imageOutput.close();
        } catch (Exception e) {
            log(e);
        }
    }

    //检查空字符串
    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public static void hit(String msg) {
        hit(Config.context, msg);
    }

    public static void hit(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void log(Object msg) {
        Log.e(Config.Text, msg.toString());
        FileWriter writer = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/Meal");
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file.getPath(), "/log.txt");
            writer = new FileWriter(file, true);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.write(format.format(new Date(System.currentTimeMillis())) + ": " + msg + "\r\n");
            writer.close();
        } catch (Exception e) {
            Log.e(Config.Text, msg(e));
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void log(Object msg, Throwable e) {
        log(msg + msg(e));
    }

    public static void log(Throwable e) {
        log(msg(e));
    }

    private static String msg(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
