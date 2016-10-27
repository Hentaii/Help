package com.help.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.help.R;
import com.help.config.IGetMapLocation;
import com.help.model.bean.BmobLatLng;
import com.help.model.bean.TestBmob;
import com.help.service.LocationService;
import com.help.util.Util;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by gan on 2016/6/3.
 */
public class MapFragment extends android.support.v4.app.Fragment implements LocationSource {
    private View view;
    private EditText mEtSearch;
    private Button mBtSearch;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private ServiceConnection sc;
    private AMapLocation lastLocation;
    public ArrayList<LatLng> latLngList;
    private LocationService locationService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mBtSearch = (Button) view.findViewById(R.id.bt_search);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        init();
        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IMEI = mEtSearch.getText().toString();
                if (!TextUtils.isEmpty(IMEI)) {
                    BmobQuery<BmobLatLng> queryLatLng = new BmobQuery<BmobLatLng>();
                    queryLatLng.addWhereEqualTo("IMEI", Util.getIMEI(getActivity()));
                    queryLatLng.findObjects(new FindListener<BmobLatLng>() {
                        @Override
                        public void done(List<BmobLatLng> list, BmobException e) {
                            if (list.size() > 0) {
                                BmobLatLng bmobLatLng = list.get(0);
                                latLngList = (ArrayList<LatLng>) bmobLatLng.latLngList;
                                if (locationService != null) {
                                    locationService.stopSelf();
                                }
                                if (aMap != null) {
                                    aMap.clear();
                                    for (int i = 1; i < latLngList.size(); i++) {
                                        aMap.addPolyline(new PolylineOptions().add(latLngList.get(i - 1), latLngList.get(i)));
                                    }
                                }
                            }
                        }
                    });
                }

            }
        });
        return view;
    }


    private void init() {
        aMap = mapView.getMap();
        setUpMap();
        setUpDot();

    }

    private void setUpDot() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.dot));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(1);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        //设置定位资源。如果不设置此定位资源则定位按钮不可点击。
        aMap.setLocationSource(this);
        //设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
    }

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        initService(mListener);
    }

    @Override
    public void deactivate() {
        mListener = null;
    }


    private void initService(final OnLocationChangedListener mListener) {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                locationService = ((LocationService.LocationBinder) service).getService();
                locationService.initLocation(new IGetMapLocation() {
                    @Override
                    public void getLocationSuccess(AMapLocation amapLocation) {
                        mListener.onLocationChanged(amapLocation);
                        if (null == lastLocation) {
                            lastLocation = amapLocation;
                        }
                        aMap.addPolyline(new PolylineOptions().add(new LatLng(lastLocation.getLatitude(),
                                lastLocation.getLongitude()), new LatLng(amapLocation.getLatitude(), amapLocation
                                .getLongitude())));
                        lastLocation = amapLocation;
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("TAG", "onServiceDisconnected  " + name.toShortString());
            }
        };
        Intent service = new Intent(getActivity(), LocationService.class);
        getActivity().bindService(service, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d("TAG", "onDestroy");
        mapView.onDestroy();
    }

    //测试保存图片
//        mBtSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GetMapShotPresenter presenter = new GetMapShotPresenter(getActivity(), new IGetScreenShot() {
//                    @Override
//                    public void getScreenShotSuccess(Bitmap bitmap) {
//                        Util.Toast(getActivity(), "Click");
//                        mapView.setVisibility(View.GONE);
//                        mIvSearch.setVisibility(View.VISIBLE);
//                        mIvSearch.setImageBitmap(bitmap);
//                    }
//                });
//                presenter.getMapScreenShot(aMap);
//            }
//        });
}
