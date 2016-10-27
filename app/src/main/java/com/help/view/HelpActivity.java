package com.help.view;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.help.R;
import com.help.api.API;
import com.help.app.BaseActivity;
import com.help.util.Util;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvTabHelp;
    private TextView mTvTabMap;
    private HelpFragment mFgHelp;
    private MapFragment mFgMap;
    private Fragment currentFragment;
    private ImageView mIvSet;
    private SharedPreferences sp;
    private Networkreceiver networkreceiver;
    private MyVolumeReceiver mVolumeReceiver;
    private AudioManager audioManager;
    private long clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
        registerReceiver();

        myRegisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private void initFragment() {
        mFgHelp = new HelpFragment();
        mFgMap = new MapFragment();
        showFragment(mFgHelp);
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
        switch (v.getId()) {
            case R.id.tv_tb_left:
                mTvTabHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_use));
                mTvTabMap.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_unuse));
                mTvTabHelp.setTextColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
                mTvTabMap.setTextColor(getResources().getColor(R.color.colorWhite_ffffff));
                showFragment(mFgHelp);
                break;
            case R.id.tv_tb_right:
                mTvTabHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_unuse));
                mTvTabMap.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_use));
                mTvTabHelp.setTextColor(getResources().getColor(R.color.colorWhite_ffffff));
                mTvTabMap.setTextColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
                showFragment(mFgMap);
                break;
            case R.id.iv_set:
                startActivity(new Intent(HelpActivity.this, SettingActivity.class));
                break;
        }

    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null)
            transaction.hide(currentFragment);
        if (fragment.isAdded())
            transaction.show(fragment);
        else
            transaction.add(R.id.fl_content, fragment);
        currentFragment = fragment;
        transaction.commit();
    }
    //动态注册网络的状态，绑定到networkreceiver

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkreceiver = new Networkreceiver();
        this.registerReceiver(networkreceiver, filter);
    }

    //注销接收
    private void unregisterReceiver() {
        this.unregisterReceiver(networkreceiver);
    }


    private void myRegisterReceiver() {
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, filter);
    }


    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                clickTime = System.currentTimeMillis();
                if ((System.currentTimeMillis() - clickTime) > 2000) {
                    Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(this,"11111",Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(this,"22222",Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }*/

}

