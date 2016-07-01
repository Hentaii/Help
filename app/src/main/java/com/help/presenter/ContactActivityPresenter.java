package com.help.presenter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.help.api.API;
import com.help.app.APP;
import com.help.config.IContactActivityView;
import com.help.model.bean.HelpContact;
import com.help.util.Util;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.realm.Realm;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class ContactActivityPresenter {
    private Context context;
    private IContactActivityView view;
    private HelpContact mContact;
    private String head;

    public ContactActivityPresenter(Context context, IContactActivityView view, long contactNo) {
        this.context = context;
        this.view = view;
        mContact = APP.getContact(contactNo);
        view.setHead(mContact.getHead());
        view.setSms(mContact.isSms());
        view.setSmsText(mContact.getSmsText());
        view.setTel(mContact.getTel());
        view.setName(mContact.getName());
        this.head = mContact.getHead();
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
                Util.Toast(context, errorMsg);
            }
        });
    }

    public void tv_delete() {
        new AlertDialog.Builder(context)
                .setTitle(API.TITLE)
                .setMessage(API.IF_DELETE)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        APP.delete(mContact);
                        view.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //保存
    public void iv_save() {
        APP.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mContact.setName(view.getName());
                mContact.setTel(view.getTel());
                mContact.setSmsText(view.getSmsText());
                mContact.setSms(view.getSms());
                mContact.setHead(head);
            }
        });
    }

    public void analysis(Intent data) {
        //解析
        if (data == null)
            return;
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
        Log.e("--->", "1");
        phone.moveToFirst();
        view.setTel(phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
    }


    public void ll_back() {
        new AlertDialog.Builder(context)
                .setTitle(API.TITLE)
                .setMessage(API.IF_BACK)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        view.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
