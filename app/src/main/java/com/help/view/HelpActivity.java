package com.help.view;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.help.R;
import com.help.api.API;
import com.help.app.BaseActivity;
import com.help.config.IGetMapLocation;
import com.help.model.bean.BmobLatLng;
import com.help.service.LocationService;
import com.help.util.Util;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private ServiceConnection sc;
    private String mIMEI;
    private List<LatLng> mLatLingList = new ArrayList<>();
    private BmobLatLng mBmobLatLng;

    private long clickTime = 0;
    private boolean flag = false;
    private int count = 0;

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

                if ((System.currentTimeMillis() - clickTime) < 1000) {
                    count++;
                    if (count > 5) {
                        flag = true;
                    }
                } else {
                    count = 0;
                    flag = false;
                }
            }

            if (flag && count == 6) {
                Toast.makeText(context, "按了六下", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < mFgHelp.getmData().size(); i++) {
                    sendSms(mFgHelp.getmData().get(i).get("number").toString(),"您好，我现在可能遇到了危险,现在不方便打电话，如果10分钟内我没联系你，请你帮助我！");
                }
                mIMEI = Util.getIMEI(HelpActivity.this);
                mBmobLatLng = new BmobLatLng(mLatLingList, mIMEI);
                mBmobLatLng.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Log.d("TAG", "s-----" + s);
                        initUpdateLocationService();
                        if (null != e) {
                            Log.d("TAG", "e-----" + e.getMessage());
                        }
                    }
                });
            }
            clickTime = System.currentTimeMillis();
        }
    }


    private void sendSms(String phone, String message) {

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, HelpActivity.class), 0);

        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(phone, null, message, pi, null);

    }

    private void initUpdateLocationService() {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocationService locationService = ((LocationService.LocationBinder) service).getService();
                locationService.setInterval(10000);
                locationService.initLocation(new IGetMapLocation() {
                    @Override
                    public void getLocationSuccess(AMapLocation amapLocation) {

                        if (null != amapLocation) {
                            mBmobLatLng.latLngList.add(new LatLng(amapLocation.getLatitude(), amapLocation
                                    .getLongitude()));
                            Log.d("TAG", "location===>   " + amapLocation.getLatitude() + "------" + amapLocation
                                    .getLongitude());
                            mBmobLatLng.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Log.d("TAG", "update LatLngWrong ------ " + e.getMessage());
                                }
                            });
                        }
                    }

                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("TAG", "onServiceDisconnected  " + name.toShortString());
            }
        };
        Intent service = new Intent(HelpActivity.this, LocationService.class);
        bindService(service, sc, Context.BIND_AUTO_CREATE);
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

