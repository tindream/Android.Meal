package tinn.meal.ping.mqtt;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.support.Config;

public class MyServiceConnection implements ServiceConnection {
    private ILoadListener loadListener;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Config.mqttService = ((MQTTService.CustomBinder) iBinder).getService();
        Config.mqttService.setListener(loadListener);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }

    public void setListener(ILoadListener loadListener) {
        this.loadListener = loadListener;
        if (Config.mqttService != null) Config.mqttService.setListener(loadListener);
    }
}
