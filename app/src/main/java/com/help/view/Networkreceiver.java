package com.help.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.NotificationCompat;
import com.help.R;


/**
 * Created by Jun on 16/10/20.
 */


public class Networkreceiver extends BroadcastReceiver {
    //网络状态信息的实例
    private NetworkInfo networkInfo;
    @Override
    public void onReceive(Context context, Intent intent) {
        //系统网络连接相关的操作管理类

        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //系统网络通知栏相关代码


        //首先创建Builder对象，用PendingIntent控制跳转到HelpActivity页面
        Notification.Builder builder=new Notification.Builder(context);
        //获取NotificationManager对象
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1=new Intent(context, HelpActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);
        //设置悬挂式通知
        builder.setFullScreenIntent(pendingIntent,true);
        //设置builder相关属性
        builder.setSmallIcon(R.drawable.notification_title);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setContentTitle("当前网络");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        //获取网络状态实例
        networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null&&networkInfo.isAvailable()) {

            //判断当前网络的形式，并采用通知的方式提醒用户。
            String name=networkInfo.getTypeName();

            if(name.equals("WIFI")) {
                builder.setContentText("您当前已连接无线WIFI!");
                notificationManager.notify(0,builder.build());
            }
            else {
                builder.setContentText("您当前连接的是移动数据网络!");
                notificationManager.notify(0,builder.build());
            }
        }else {
            builder.setContentText("您当前无网络可使用,请重新连接网络刷新页面!");

            notificationManager.notify(0,builder.build());
        }
    }
}
