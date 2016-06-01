package com.help.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.help.R;
import com.help.api.API;
import com.help.app.BaseActivity;
import com.help.util.Util;

public class HelpActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        isFirst();
    }

    private void initView() {
        setContentView(R.layout.help_activity);
    }

    private void isFirst() {
        sp = Util.getSp(HelpActivity.this);
        if (sp.getBoolean("isFirst", true)) {
            showDialog();
            sp.edit().putBoolean("isFirst", false).apply();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setMessage(API.FIRST_HELP); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(HelpActivity.this, UseHelpActivity.class));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
}
