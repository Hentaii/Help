package com.help.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.help.config.IGetScreenShot;

import java.text.SimpleDateFormat;

/**
 * Created by gan on 2016/6/15.
 */
public class MapShotModel implements AMap.OnMapScreenShotListener {
    private Context context;
    private IGetScreenShot callback;

    public MapShotModel(Context context, IGetScreenShot callback) {
        this.context = context;
        this.callback = callback;
    }

    public void getMapScreenShot(AMap aMap) {
        aMap.getMapScreenShot(this);
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {

    }

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        if (null == bitmap) {
            return;
        }
        if (i != 0)
        {
            callback.getScreenShotSuccess(bitmap);
            Log.d("TAG", "地图渲染完成，截屏无网格");
        }
        else {
            Log.d("TAG", "地图未渲染完成，截屏有网格");
        }
    }
}
