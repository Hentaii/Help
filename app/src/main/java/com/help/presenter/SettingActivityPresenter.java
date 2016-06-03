package com.help.presenter;

import android.content.Context;

import com.help.config.ISettingActivityView;

/**
 * Created by KiSoo on 2016/6/3.
 */
public class SettingActivityPresenter {
    private Context context;
    private ISettingActivityView view;

    public SettingActivityPresenter(Context context, ISettingActivityView view) {
        this.context = context;
        this.view = view;
    }

    //紧急救援
    public void help() {
        view.setCheck(!view.getIsChecked());
    }

    //帮助说明
    public void instruction() {

    }

    //分享
    public void share() {

    }
}
