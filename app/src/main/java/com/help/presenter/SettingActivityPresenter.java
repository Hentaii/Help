package com.help.presenter;

import android.content.Context;
import android.content.Intent;

import com.help.config.ISettingActivityView;
import com.help.view.UseHelpActivity;

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
        context.startActivity(new Intent(context, UseHelpActivity.class));
    }

    //分享
    public void share() {

    }
}
