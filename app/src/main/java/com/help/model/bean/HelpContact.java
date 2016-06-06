package com.help.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class HelpContact extends DataSupport{
    private int tel;
    private String name;
    private String smsText;
    private boolean sms;
    private String head;

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
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
}
