package com.help.app;

import com.help.R;
import com.help.model.bean.HelpContact;
import com.help.util.galleryfinal.PicassoImageLoader;

import org.litepal.LitePalApplication;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by gan on 2016/6/1.
 */
public class APP extends LitePalApplication {
    public static List<HelpContact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();
        initLitePal();
        initGalleryFinal();
    }

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

    private void initLitePal() {
        LitePalApplication.initialize(this);
    }
}
