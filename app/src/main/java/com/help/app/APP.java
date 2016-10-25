package com.help.app;

import android.app.Application;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.MapsInitializer;
import com.help.R;
import com.help.model.bean.HelpContact;
import com.help.util.galleryfinal.PicassoImageLoader;

import cn.bmob.v3.Bmob;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by gan on 2016/6/1.
 */
public class APP extends Application {
    public static Realm realm;
    private static RealmResults<HelpContact> allContact;

    public static RealmResults<HelpContact> getAllContact() {
        return allContact;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRealmJava();
        initGalleryFinal();
        initBmob();
    }

    private void initBmob() {
        Bmob.initialize(this, "eef42e04394984448a41a93d35c5ae78");
    }

    private void initRealmJava() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        allContact = realm.where(HelpContact.class).findAll();
    }

//    private void initBmob() {
//        Bmob.initialize(this, "eef42e04394984448a41a93d35c5ae78");
//    }

    private void initGalleryFinal() {
        /*****主题配置****/
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8))
                .setFabNornalColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8))
                .setFabPressedColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8))
                .build();

        /****功能配置****/
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)//开启相机功能
                .setEnableEdit(true)//开启编辑功能
                .setEnableRotate(true)//开启选择功能
                .setEnableCrop(true)//开启裁剪功能
                .setEnablePreview(true)//开启预览功能
                .build();
        /**设置核心配置信息**/
        CoreConfig config = new CoreConfig.Builder(this, new PicassoImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(config);
    }


    public static HelpContact getContact(final long position) {
        return realm.where(HelpContact.class).equalTo("contactNo", position).findFirst();
    }

    public static void delete(final HelpContact mContact) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(HelpContact.class)
                        .equalTo("contactNo", mContact.contactNo)
                        .findAll()
                        .deleteAllFromRealm();

            }
        });


    }

    public static void add(final HelpContact contact) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(contact);
            }
        });
    }
}
