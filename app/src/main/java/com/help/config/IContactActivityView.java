package com.help.config;

/**
 * Created by KiSoo on 2016/6/5.
 */
public interface IContactActivityView {
    void finish();

    String getName();

    String getTel();

    boolean getSms();

    String getSmsText();

    void setHead(String head);

    void setSms(boolean sms);

    void setSmsText(String smsText);

    void setTel(String tel);

    void setName(String name);
}
