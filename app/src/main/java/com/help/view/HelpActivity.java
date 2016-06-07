package com.help.view;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.R;
import com.help.api.API;
import com.help.app.APP;
import com.help.app.BaseActivity;
import com.help.model.bean.HelpContact;
import com.help.util.Util;

import org.litepal.crud.DataSupport;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvTabHelp;
    private TextView mTvTabMap;
    private HelpFragment mFgHelp;
    private MapFragment mFgMap;
    private ImageView mIvSet;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APP.contacts = DataSupport.findAll(HelpContact.class);
        initView();
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mFgHelp = new HelpFragment();
        mFgMap = new MapFragment();

        transaction.replace(R.id.fl_content, mFgHelp);
        transaction.commit();
    }

    private void initView() {
        setContentView(R.layout.help_activity);
        mTvTabHelp = (TextView) findViewById(R.id.tv_tb_left);
        mTvTabMap = (TextView) findViewById(R.id.tv_tb_right);
        mIvSet = (ImageView) findViewById(R.id.iv_set);
        mTvTabHelp.setOnClickListener(this);
        mTvTabMap.setOnClickListener(this);
        mIvSet.setOnClickListener(this);
        isFirst();
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

    @Override
    public void onClick(View v) {

        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.tv_tb_left:
                mTvTabHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.unused));
                mTvTabMap.setBackgroundDrawable(getResources().getDrawable(R.drawable.used));
                mTvTabHelp.setTextColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
                mTvTabMap.setTextColor(getResources().getColor(R.color.colorWhite_ffffff));
//                mTvTabHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.used_1));
//                mTvTabMap.setBackgroundDrawable(getResources().getDrawable(R.drawable.unuse_1));
//                mTvTabHelp.setTextColor(getResources().getColor(R.color.colorWhite_ffffff));
//                mTvTabMap.setTextColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
                if (mFgHelp == null) {
                    mFgHelp = new HelpFragment();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.fl_content, mFgHelp);
                break;
            case R.id.tv_tb_right:

//                mTvTabHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.unused));
//                mTvTabMap.setBackgroundDrawable(getResources().getDrawable(R.drawable.used));
                mTvTabHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.used_1));
                mTvTabMap.setBackgroundDrawable(getResources().getDrawable(R.drawable.unuse_1));
//                mTvTabHelp.setTextColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
//                mTvTabMap.setTextColor(getResources().getColor(R.color.colorWhite_ffffff));
                mTvTabHelp.setTextColor(getResources().getColor(R.color.colorWhite_ffffff));
                mTvTabMap.setTextColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
                if (mFgMap == null) {
                    mFgMap = new MapFragment();
                }
                transaction.replace(R.id.fl_content, mFgMap);
                break;
            case R.id.iv_set:
                startActivity(new Intent(HelpActivity.this, SettingActivity.class));
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataSupport.deleteAll(HelpContact.class);
        DataSupport.saveAll(APP.contacts);
    }
}
