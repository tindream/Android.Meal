package tinn.meal.ping.support;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

import io.reactivex.ObservableEmitter;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.IObservableListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.info.eventInfo.LoginAutoEventInfo;
import tinn.meal.ping.info.eventInfo.ErrorInfo;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.info.loadInfo.SendInfo;

public class TcpClient extends ListenerBase implements ILoadListener, IObservableListener {
    private Socket socket;

    public TcpClient() {
        connect();
    }

    private void connect() {
        new AsyncAdapter().setListener(this, this).init(LoadType.connect);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void send(EventInfo info) {
        new AsyncAdapter().setListener(this, this).init(new SendInfo(info.Types, new Gson().toJson(info)));
    }

    private void sendTo(ObservableEmitter<LoadInfo> emitter, SendInfo info) {
        try {
            if (!socket.isConnected()) throw new SQLException("未连接");
            byte[] buffer = info.Message.getBytes("utf-8");
            Method.log("发送数据：" + info.Message);
            //Socket输出流
            buffer = getSendData(buffer);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(buffer);
            outputStream.flush();
            emitter.onNext(new LoadInfo(LoadType.complete));
        } catch (Exception e) {
            Method.log(e);
            emitter.onNext(new ErrorInfo(info.from, e.getMessage()));
        }
    }

    private byte[] getSendData(byte[] data) {
        //先处理发送的数据，加上四字节长度
        byte[] buffer = new byte[data.length + 4];
        buffer[0] = (byte) (data.length >> 24);
        buffer[1] = (byte) (data.length >> 16);
        buffer[2] = (byte) (data.length >> 8);
        buffer[3] = (byte) data.length;
        for (int i = 0; i < data.length; i++) {
            buffer[i + 4] = data[i];
        }
        return buffer;
    }

    @Override
    public void onReady(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        switch (info.Types) {
            case connect:
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(Config.Admin.Host, Config.Admin.Port), 3000);
                    if (!socket.isConnected()) throw new SQLException("连接失败");
                    Method.log("socket连接成功");
                    emitter.onNext(new LoadInfo(LoadType.connected));
                    received(emitter);
                } catch (Exception e) {
                    Thread.sleep(125);
                    if (e.getClass().equals(ConnectException.class)
                            || e.getClass().equals(NoRouteToHostException.class)
                            || e.getClass().equals(SocketTimeoutException.class)) {
//                        Method.log(e.getMessage());
                        Thread.sleep(1000);
                    } else {
                        Method.log(e);
                    }
                    emitter.onNext(new LoadInfo(LoadType.connect));
                }
                break;
            case send:
                sendTo(emitter, (SendInfo) info);
                break;
        }
    }

    private void received(ObservableEmitter<LoadInfo> emitter) throws IOException {
        InputStream inputStream = socket.getInputStream();
        DataInputStream input = new DataInputStream(inputStream);
        while (true) {
            byte[] buffer = new byte[4];
            input.read(buffer, 0, 4);
            int length = (buffer[0] & 0xff) << 24 | (buffer[1] & 0xff) << 16 | (buffer[2] & 0xff) << 8 | (buffer[3] & 0xff);

            buffer = new byte[length];
            int index = 0;
            while (index < length) {
                index += input.read(buffer, index, length - index);
            }
            String msg = new String(buffer, "utf-8");
            emitter.onNext(new LoadInfo(LoadType.received, msg));
        }
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case connect:
                onListener(info);
                connect();
                break;
            case connected:
                if (Config.Admin.UserId > 0)
                    send(new LoginAutoEventInfo());
                break;
            case complete:
                Method.log("Sending...");
                Method.hit("Sending...");
                break;
            case Error:
                ErrorInfo er = (ErrorInfo) info;
                Cache.addNotified(er.FromTypes, info.Message);
                onListener(LoadType.notifiedUpdate);
                break;
            case received:
                onListener(new EventInfo(LoadType.received, info.Message));
                break;
        }
    }
}
