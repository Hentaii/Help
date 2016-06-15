package com.help.presenter;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.help.config.IGetScreenShot;
import com.help.model.MapShotModel;

/**
 * Created by gan on 2016/6/15.
 */
public class GetMapShotPresenter {
    MapShotModel mapShotModel;

    public GetMapShotPresenter(Context context, IGetScreenShot callback) {
        mapShotModel = new MapShotModel(context, callback);
    }

    public void getMapScreenShot(AMap aMap) {
        mapShotModel.getMapScreenShot(aMap);
    }

}
