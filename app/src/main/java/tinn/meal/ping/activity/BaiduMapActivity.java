package tinn.meal.ping.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;

import tinn.meal.ping.R;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.ChildActivity;
import tinn.meal.ping.ui.MyLocationListener;

public class BaiduMapActivity extends ChildActivity implements View.OnClickListener {
    MapView mapView = null; //地图
    BaiduMap baiduMap;      //定义地图实例
    LocationClient locationClient = null;   //定位
    MyLocationListener myListener;          //继承BDAbstractLocationListener的定位监听

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_baidu;
        super.onCreate(savedInstanceState);
        try {
            String title = getIntent().getStringExtra("title");
            toolbar.setTitle(title);
            findViewById(R.id.baidu_location).setOnClickListener(this);
            myListener = new MyLocationListener();
            initMap();
        } catch (Exception e) {
            Method.log(e);
        }
    }

    private void initMap() {
        mapView = findViewById(R.id.baidu_View);
        baiduMap = mapView.getMap();//获取地图实例对象
        myListener.init(getApplicationContext(), baiduMap, true);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //设置定位图标是否有箭头
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        //声明LocationClient类
        locationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        locationClient.registerLocationListener(myListener);
        myListener.setListener(obj -> {
            locationClient.stop();
            if (obj.Types != LoadType.complete) locationClient.start();
        });
        LocationClientOption option = initLocation();
        locationClient.setLocOption(option);//将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        //调用LocationClient的start()方法，便可发起定位请求
        locationClient.start();
    }

    private LocationClientOption initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(0);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        //option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true);
        return option;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baidu_location:
                locationClient.start();
                break;
        }
    }
}
