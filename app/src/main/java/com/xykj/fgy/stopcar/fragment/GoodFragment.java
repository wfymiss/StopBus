package com.xykj.fgy.stopcar.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;

import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import com.amap.api.services.core.PoiItem;

import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xykj.fgy.stopcar.R;
import com.xykj.fgy.stopcar.adapter.PoiSearchAdapter;
import com.xykj.fgy.stopcar.widgets.SimpleSearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class GoodFragment extends Fragment implements LocationSource, AMapLocationListener {
    @BindView(R.id.me_map)
    MapView meMap;
    @BindView(R.id.me_search)
    SimpleSearchView meSearch;
    @BindView(R.id.lv)
    ListView listView;
    Unbinder unbinder;
    private AMap aMap;
    private PoiResult poiResult; // poi返回的结果
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private int currentPage = 0;// 当前页面，从0开始计数

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private double lon;
    private double lat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 初始化地图
        meMap.onCreate(savedInstanceState);
        return view;
    }

    private PoiSearch.Query query;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (aMap == null) {
            aMap = meMap.getMap();
            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            //设置定位监听
            aMap.setLocationSource(this);
            // 是否显示定位按钮
            settings.setZoomControlsEnabled(false);
            settings.setAllGesturesEnabled(true);


            // 是否可触发定位并显示定位层
            aMap.setMyLocationEnabled(true);


            //定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.radiusFillColor(android.R.color.transparent);
            myLocationStyle.strokeColor(android.R.color.transparent);
            aMap.setMyLocationStyle(myLocationStyle);

            init();
            //搜索功能
            search();
            setOnClick();

        }
    }

    private void setOnClick() {

    }


    private void search() {
        meSearch.setOnSearchListener(new SimpleSearchView.OnSearchListener() {
            private PoiSearch poiSearch;

            @Override
            public void search(String keycode) {
                listView.setVisibility(View.VISIBLE);
                PoiSearch.Query query = new PoiSearch.Query(keycode, "", "太原");
                Log.e("------", keycode + "");
                query.setPageSize(50);// 设置每页最多返回多少条poiitem
                query.setPageNum(currentPage);//
                poiSearch = new PoiSearch(getActivity(), query);
                Log.e("------", query + "");
                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {


                    private ArrayList<PoiItem> poiItems;

                    @Override
                    public void onPoiSearched(PoiResult poiResult, int errcode) {
                        Log.e("------", errcode + "");
                        if (errcode == 1000) {
                            if (null != poiResult && poiResult.getPois().size() > 0) {
                                poiItems = poiResult.getPois();
                                for (int i = 0; i < poiItems.size(); i++) {
                                    aMap.addMarker(new MarkerOptions().position(new LatLng(lon, lat))
                                            .title(poiItems.get(i).getSnippet())
                                    );
                                }

                            }
                        }
                        PoiSearchAdapter adapter = new PoiSearchAdapter(getActivity(), poiItems);
                        listView.setAdapter(adapter);

                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
                poiSearch.searchPOIAsyn();
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        });
    }

    // 地图的相关设置
    private void init() {
        //定位
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    public void onResume() {
        super.onResume();
        meMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        meMap.onPause();
    }

    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);

                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }
}
