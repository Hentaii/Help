package com.help.model.bean;

import com.amap.api.maps.model.LatLng;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by GanChenQ on 2016/10/27.
 */

public class BmobLatLng extends BmobObject {
    public List<LatLng> latLngList;
    public String IMEI;


    public BmobLatLng(List<LatLng> latLngList, String IMEI) {
        this.latLngList = latLngList;
        this.IMEI = IMEI;
    }
}
