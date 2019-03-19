package tinn.meal.ping.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.UUID;

import tinn.meal.ping.enums.IListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.eventInfo.ErrorEventInfo;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.info.eventInfo.LoginEventInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class MQTTService extends Service implements IListener {
    protected ILoadListener loadListener;

    @Override
    public void onCreate() {
        super.onCreate();
        // 服务器地址（协议+地址+端口号）
        Config.ClientId = UUID.randomUUID().toString();
        String host = "tcp://" + Config.Host + ":" + Config.MPort;
        Config.mqttClient = new MqttAndroidClient(this, host, Config.ClientId);
        // 设置MQTT监听并且接受消息
        Config.mqttClient.setCallback(mqttCallback);

        Connection();
    }

    /**
     * 连接MQTT服务器
     */
    public void Connection() {
        if (Config.Admin.UserId == 0 && (Config.Admin.Name == null || Config.Admin.Pad == null))
            return;
        MqttConnectOptions options = new MqttConnectOptions();
        // 清除缓存
        options.setCleanSession(true);
        // 设置超时时间，单位：秒
        options.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        options.setKeepAliveInterval(20);
        // 用户名
        options.setUserName(Config.Admin.UserId > 0 ? Config.Admin.UserId + "" : Config.Admin.Name);
        // 密码
        String pad = Config.Admin.UserId > 0 ? null : Config.Admin.Pad;
        options.setPassword(pad == null ? null : pad.toCharArray());     //将字符串转换为字符串数组
        try {
            Config.mqttClient.connect(options, null, iMqttActionListener);
        } catch (MqttException e) {
            Method.log(e);
        }
    }

    public void publish(EventInfo info) {
        if (Config.mqttClient == null) return;
        try {
            String msg = new Gson().toJson(info);
            Config.mqttClient.publish(Config.Topic + "/" + Config.ClientId, msg.getBytes(), 1, false);
            Method.log("Sending...");
        } catch (MqttException e) {
            Method.log(e);
            Method.hit(e.getMessage());
        } catch (Exception e) {
            Method.hit(e.getMessage());
        }
    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken arg0) {
            Method.log("MQTT连接成功 ");
            try {
                // 订阅topic话题
                Config.mqttClient.subscribe(new String[]{Config.Topic, Config.Topic + "/" + Config.ClientId}, new int[]{2, 2});
                publish(new LoginEventInfo(Config.Admin.Name, Config.Admin.Pad));
            } catch (MqttException e) {
                Method.log(e);
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            // 连接失败，重连
            Method.log("onFailure: " + arg1);
            if (arg1 instanceof MqttSecurityException) {
                int value = ((MqttSecurityException) arg1).getReasonCode();
                if (value == 4) {
                    ErrorEventInfo error = new ErrorEventInfo(LoadType.Login, arg1.toString());
                    onListener(new EventInfo(LoadType.received, new Gson().toJson(error)));
                    return;
                }
            } else {
                ErrorEventInfo error = new ErrorEventInfo(LoadType.connect, arg1.toString());
                onListener(new EventInfo(LoadType.received, new Gson().toJson(error)));
            }
            Connection();
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String msg = new String(message.getPayload());
            onListener(new EventInfo(LoadType.received, msg));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            //发送(传递)完成
        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            Connection();
        }
    };

    public void setListener(ILoadListener listener) {
        this.loadListener = listener;
    }

    public void onListener(LoadType type) {
        onListener(new LoadInfo(type));
    }

    public void onListener(LoadInfo e) {
        try {
            if (loadListener != null)
                loadListener.onReady(e);
        } catch (Exception ex) {
            Method.log(ex);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CustomBinder();
    }

    public class CustomBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }
}
