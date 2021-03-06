package com.help.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gan on 2016/6/1.
 */
public class Util {
    /**
     * 复制文本到剪贴板
     *
     * @param ctx
     * @param text
     */
    public static void copyToClipboard(Context ctx, String text) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Activity.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText("jude", text));
    }

    /**
     * 得到sp
     */
    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    /**
     * 取屏幕宽度
     *
     * @param ctx
     * @return
     */
    public static int getScreenWidth(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 取屏幕高度
     */
    public static int getScreenHeight(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 快捷的吐司显示
     *
     * @param ctx
     * @param content
     */
    public static void Toast(Context ctx, String content) {
        Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * dp转px
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 得到通讯录的电话号码
     *
     * @param context
     * @param uri
     * @return
     */
    public static Map<String, String> getPhoneContacts(Context context, Uri uri) {
        ContentResolver reContentResolverol = context.getContentResolver();
        Cursor cursor = reContentResolverol.query(uri, null, null, null, null);
        Map<String, String> map = new HashMap<String, String>();
        String username = null;
        String usernumber = null;
        cursor.moveToFirst();
        username = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        map.put("name", username);
        String contactId = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts._ID));
        Cursor phone = reContentResolverol.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                        + contactId, null, null);
        while (phone.moveToNext()) {
            usernumber = phone
                    .getString(phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        map.put("number", usernumber);
        phone.close();
        cursor.close();
        return map;
    }

    /**
     * 是否有网络
     */
    public static boolean isNetWorkAvilable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断是否为快速点击，是的话返回true，不是返回false
     *
     * @return 返回true 或false
     */
    public static class QuickClick {
        public static long lastClickTime;

        public static boolean isQuickClick() {

            long currentTime = System.currentTimeMillis();
            long time = currentTime - lastClickTime;

            if (0 < time && time < 600) {
                return true;
            } else {
                lastClickTime = currentTime;
                return false;
            }
        }
    }


    /**
     * 经纬度测距
     *
     * @param jingdu1
     * @param weidu1
     * @param jingdu2
     * @param weidu2
     * @return
     */
    public static double Distance(double jingdu1, double weidu1,
                                  double jingdu2, double weidu2) {
        double a, b, R;
        R = 6378137; // 地球半径
        weidu1 = weidu1 * Math.PI / 180.0;
        weidu2 = weidu2 * Math.PI / 180.0;
        a = weidu1 - weidu2;
        b = (jingdu1 - jingdu2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(weidu1)
                * Math.cos(weidu2) * sb2 * sb2));
        return d;
    }

    public static String getTime(long code) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(code * 1000L));
        return date;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String currentDate = sdf.format(curDate);
        return currentDate;
    }

    /**
     * 注意要添加权限:<uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        return IMEI;
    }
}
