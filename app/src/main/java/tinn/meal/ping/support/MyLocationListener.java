package tinn.meal.ping.support;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import tinn.meal.ping.enums.IListener;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;

public class MyLocationListener extends BDAbstractLocationListener implements IListener {
    BaiduMap baiduMap;      //定义地图实例
    boolean auto = true;    //判断
    MyOrientationListener myOrientationListener;    //方向监听
    double latitude;        //获取纬度信息
    double longitude;       //获取经度信息
    int mXDirection;        //方向

    public void init(Context context, BaiduMap baiduMap, boolean auto) {
        this.baiduMap = baiduMap;
        this.auto = auto;
        myOrientationListener = new MyOrientationListener(context);
        initOritationListener();
        myOrientationListener.start();
    }

    public void stop() {
        myOrientationListener.stop();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
        latitude = location.getLatitude();       //获取纬度信息
        longitude = location.getLongitude();     //获取经度信息
        float radius = location.getRadius();            //获取定位精度，默认值为0.0f
        String coorType = location.getCoorType();       //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
        String cityCode = location.getCityCode();
        Method.log(cityCode + "---" + latitude + "--" + longitude + "----" + coorType + "--" + location.getCountry() + "--" + location.getCity() + "--" + location.getAddrStr());
        if (cityCode == null) {
            onListener(LoadType.cancel);
            return;
        }
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

          /*  // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);*/

        if (auto) {
            LatLng ll = new LatLng(latitude, longitude);
            //[1:20米（简称20米，后同），50米，100米，200米，500米，1公里，2公里，5公里，10公里，20公里，25公里，50公里，100公里，200公里，500公里，1000公里，2000公里，5000公里，10000公里]
            //分别对应：
            //[19级，18级，17级，16级，15级，14级，13级，12级，11级，10级，9级，8级，7级，6级，5级，4级，3级，2级，1级]
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 17);
            // 移动到某经纬度
            baiduMap.animateMapStatus(update);
        }

        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.direction(mXDirection);
        builder.latitude(latitude);
        builder.longitude(longitude);
        MyLocationData data = builder.build();
        baiduMap.setMyLocationData(data);
        onListener(LoadType.complete);
    }

    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mXDirection = (int) x;
                if (latitude == 0 && longitude == 0) return;
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mXDirection)
                        .latitude(latitude)
                        .longitude(longitude).build();
                // 设置定位数据
                baiduMap.setMyLocationData(locData);
                // 设置自定义图标
//                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
//                MyLocationConfigeration config = new MyLocationConfigeration(mCurrentMode, true, mCurrentMarker);
//                baiduMap.setMyLocationConfigeration(config);
            }
        });
    }

    protected ILoadListener loadListener;

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
}
