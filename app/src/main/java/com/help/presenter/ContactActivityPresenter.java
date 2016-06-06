package com.help.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.help.app.APP;
import com.help.config.IContactActivityView;
import com.help.model.bean.HelpContact;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class ContactActivityPresenter {
    private Context context;
    private IContactActivityView view;
    private int contactNo;
    private String head = "";

    public ContactActivityPresenter(Context context, IContactActivityView view, int contactNo) {
        this.context = context;
        this.view = view;
        this.contactNo = contactNo;
        for (HelpContact mContact : APP.contacts) {
            if (mContact.getNo() == contactNo) {
                view.setHead(mContact.getHead());
                view.setSms(mContact.isSms());
                view.setSmsText(mContact.getSmsText());
                view.setTel(mContact.getTel());
                view.setName(mContact.getName());
                head = mContact.getHead();
            }
        }
    }

    public void rl_head() {
        GalleryFinal.openGallerySingle(1001, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                head = resultList.get(0).getPhotoPath();
                view.setHead(head);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                toast(errorMsg);
            }
        });
    }

    private void toast(String errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    public void tv_delete() {
        for (HelpContact contact : APP.contacts) {
            if (contact.getNo() == contactNo) {
                APP.contacts.remove(contact);
                break;
            }
        }
        view.finish();
    }

    //保存
    public void iv_save() {
        for (HelpContact mContact : APP.contacts) {
            if (mContact.getNo() == contactNo) {
                mContact.setHead(head);
                mContact.setSms(view.getSms());
                mContact.setSmsText(view.getSmsText());
                mContact.setTel(view.getTel());
                mContact.setName(view.getName());
                return;
            }
        }
        HelpContact contact = new HelpContact();
        contact.setName(view.getName());
        contact.setTel(view.getTel());
        contact.setSmsText(view.getSmsText());
        contact.setSms(view.getSms());
        contact.setHead(head);
        contact.setNo(contactNo);
        APP.contacts.add(contact);
    }

    public void analysis(Intent data) {
        //解析
        ContentResolver reContentResolverol = context.getContentResolver();
        Uri contactData = data.getData();
        Cursor cursor = reContentResolverol.query(contactData, null, null, null, null);
        cursor.moveToFirst();
        view.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                null,
                null);
        Log.e("--->","1");
        phone.moveToFirst();
        while (phone.moveToNext()) {
            Log.e("--->","2");
            view.setTel(phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            Log.e("--->",phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)+"");
            Log.e("--->",phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }

    }
}
