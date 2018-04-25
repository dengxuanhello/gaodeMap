package com.example.dx.gaodemap;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.example.dx.gaodemap.permisions.AfterPermissionGranted;
import com.example.dx.gaodemap.permisions.Permissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements INaviInfoCallback, TraceStatusListener, View.OnClickListener {

    private MapView mMapView;
    private LBSTraceClient lbsTraceClient;
    private MyLocationStyle myLocationStyle;
    private AMap mMap;
    private Button mStartBtn;
    private Button mEndBtn;
    private TextView mInfoView;
    private boolean isStart=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(null), MainActivity.this);
        setContentView(R.layout.main_activity);
        mMapView = (MapView) findViewById(R.id.map_view);
        mStartBtn = (Button) findViewById(R.id.start_track);
        mEndBtn = (Button) findViewById(R.id.end_track);
        mInfoView = (TextView) findViewById(R.id.info);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
        initmap();
    }

    @AfterPermissionGranted(111)
    private void initView(){
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//只定位一次。
        mMap.setMyLocationStyle(myLocationStyle);
        mMap.setMyLocationEnabled(true);
        lbsTraceClient = LBSTraceClient.getInstance(getApplicationContext());
        //drawLine();
        mStartBtn.setOnClickListener(this);
        mEndBtn.setOnClickListener(this);
    }

    private void initmap(){
        if (Permissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)) {
            initView();
        } else {
            Log.i("dxlog","no Permision");
            Permissions.requestPermissions(this, "", 111, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void printMsg(String msg){
        if(mInfoView != null){
            mInfoView.append(msg);
            mInfoView.append("\n");
        }
    }

    private void drawLine(){
        Location myLocation = mMap.getMyLocation();
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();

        List<LatLng> latLngs = new ArrayList<LatLng>();
        for(int i=0;i<1000;i++){
            printMsg("经度:"+longitude + ",纬度:"+latitude);
            latitude = latitude+0.000104;
            latLngs.add(new LatLng(latitude,longitude));
        }
        Log.i("dx", longitude + ":"+latitude+"----"+String.valueOf(latLngs.size()));
        mMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }

    private void drawLine2(){

        Location myLocation = mMap.getMyLocation();
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();

        List<LatLng> latLngs = new ArrayList<LatLng>();
        for(int i=0;i<1000;i++){
            longitude = longitude+0.000104;
            latLngs.add(new LatLng(latitude,longitude));
        }
        Log.i("dx", longitude + ":"+latitude+"----"+String.valueOf(latLngs.size()));
        mMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 255, 0, 0)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {
        printMsg(s);
        if(mMap != null && list1 != null && list1.size()>0) {
            printMsg("绘制"+list1.size()+"个坐标");
            mMap.addPolyline(new PolylineOptions().
                    addAll(list1).width(10).color(Color.argb(255, 1, 1, 1)));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mStartBtn)){
            if(!isStart) {
                lbsTraceClient.startTrace(this);
                isStart = true;
                printMsg("开始绘制行程轨迹");
            }
        }else if(v.equals(mEndBtn)){
            if(isStart) {
                lbsTraceClient.stopTrace();
                isStart = false;
                printMsg("结束绘制行程轨迹");
            }
        }
    }
}
