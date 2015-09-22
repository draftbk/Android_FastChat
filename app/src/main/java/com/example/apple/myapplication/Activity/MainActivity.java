package com.example.apple.myapplication.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.apple.myapplication.R;

public class MainActivity extends Activity {
    MapView mMapView = null;
    private BaiduMap mBaiduMap;


    // 相关声明
    public LocationClient locationClient = null;
    //◊‘∂®“ÂÕº±Í
    BitmapDescriptor mCurrentMarker = null;
    boolean isFirstLoc = true;//  «∑Ò ◊¥Œ∂®Œª

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view œ˙ªŸ∫Û≤ª‘⁄¥¶¿Ì–¬Ω” ’µƒŒª÷√
            if (location == null || mMapView == null)
                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // ¥À¥¶…Ë÷√ø™∑¢’ﬂªÒ»°µΩµƒ∑ΩœÚ–≈œ¢£¨À≥ ±’Î0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);	//…Ë÷√∂®Œª ˝æ›


            if (isFirstLoc) {
                isFirstLoc = false;


                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);	//…Ë÷√µÿÕº÷––ƒµ„“‘º∞Àı∑≈º∂±
//				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView)this.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//ø™∆Ù∂®ŒªÕº≤„
        mBaiduMap.setMyLocationEnabled(true);

        locationClient = new LocationClient(getApplicationContext()); //  µ¿˝ªØLocationClient¿‡
        locationClient.registerLocationListener(myListener); // ◊¢≤·º‡Ã˝∫Ø ˝
        this.setLocationOption();	//…Ë÷√∂®Œª≤Œ ˝
        locationClient.start(); // ø™ º∂®Œª
        // baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // …Ë÷√Œ™“ª∞„µÿÕº

        // baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE); //…Ë÷√Œ™Œ¿–«µÿÕº
        // baiduMap.setTrafficEnabled(true); //ø™∆ÙΩªÕ®Õº


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    /**
     * …Ë÷√∂®Œª≤Œ ˝
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ¥Úø™GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// …Ë÷√∂®Œªƒ£ Ω
        option.setCoorType("bd09ll"); // ∑µªÿµƒ∂®ŒªΩ·π˚ «∞Ÿ∂»æ≠Œ≥∂»,ƒ¨»œ÷µgcj02
        option.setScanSpan(5000); // …Ë÷√∑¢∆∂®Œª«Î«Ûµƒº‰∏Ù ±º‰Œ™5000ms
        option.setIsNeedAddress(true); // ∑µªÿµƒ∂®ŒªΩ·π˚∞¸∫¨µÿ÷∑–≈œ¢
        option.setNeedDeviceDirect(true); // ∑µªÿµƒ∂®ŒªΩ·π˚∞¸∫¨ ÷ª˙ª˙Õ∑µƒ∑ΩœÚ
        locationClient.setLocOption(option);
    }
}