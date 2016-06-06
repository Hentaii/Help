package com.help.model.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class HelpContact extends DataSupport{
    private String tel;
    private String name;
    private String smsText;
    private boolean sms;
    private String head;
    private int contactNo;

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contactNo) {
        this.contactNo = contactNo;
    }

    public int getNo() {
        return contactNo;
    }

    public void setNo(int no) {
        this.contactNo = no;
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
}
