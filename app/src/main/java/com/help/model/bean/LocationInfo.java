package com.help.model.bean;


import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by gan on 2016/6/15.
 */
public class LocationInfo{
    private List<LatLng> latLngList;
    private String IMEI;

    public List<LatLng> getLatLngList() {
        return latLngList;
    }

    public void setLatLngList(List<LatLng> latLngList) {
        this.latLngList = latLngList;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }
}
