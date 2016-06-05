package com.help.app;

import com.help.model.bean.HelpContact;

import org.litepal.LitePalApplication;

import java.util.List;

/**
 * Created by gan on 2016/6/1.
 */
public class APP extends LitePalApplication {
    public static List<HelpContact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);

    }
}
