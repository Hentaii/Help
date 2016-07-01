package com.help.model.bean;

import android.content.Context;
import android.support.annotation.StringRes;

import com.help.R;

import io.realm.RealmObject;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class HelpContact extends RealmObject{
    public String tel;
    public String name;
    public String smsText;
    public boolean sms;
    public String head;
    public long contactNo;

    public HelpContact() {
    }

    public HelpContact(Context context) {
        this(getString(context, R.string.example_tel),
                getString(context, R.string.example_name),
                getString(context, R.string.example_sms),
                true,
                "",
                System.currentTimeMillis()
        );
    }

    private static String getString(Context context, @StringRes int id) {
        return context.getResources().getString(id);
    }

    public HelpContact(String tel, String name, String smsText, boolean sms, String head, long contactNo) {
        this.tel = tel;
        this.name = name;
        this.smsText = smsText;
        this.sms = sms;
        this.head = head;
        this.contactNo = contactNo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public long getContactNo() {
        return contactNo;
    }

    public void setContactNo(long contactNo) {
        this.contactNo = contactNo;
    }
}