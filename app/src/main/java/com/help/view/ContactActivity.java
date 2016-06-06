package com.help.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.help.R;
import com.help.api.API;
import com.help.app.APP;
import com.help.app.BaseActivity;
import com.help.config.IContactActivityView;
import com.help.model.bean.HelpContact;
import com.help.presenter.ContactActivityPresenter;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class ContactActivity extends BaseActivity implements View.OnClickListener, IContactActivityView, View.OnLongClickListener {
    private LinearLayout ll_back;
    private Switch sth_sms;
    private TextView tv_delete;
    private EditText et_tel;
    private EditText et_name;
    private EditText et_sms;
    private ImageView iv_save;
    private CircleImageView civ_head;
    private RelativeLayout rl_head;
    private ContactActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APP.contacts = DataSupport.findAll(HelpContact.class);
        initViews();
        initData();
        initListener();
    }

    private void initViews() {
        setContentView(R.layout.activity_contact);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        sth_sms = (Switch) findViewById(R.id.sth_sms);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_name = (EditText) findViewById(R.id.et_name);
        et_sms = (EditText) findViewById(R.id.et_sms);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
    }

    private void initData() {
        presenter = new ContactActivityPresenter(this, this,
                getIntent().getIntExtra(API.KEY_CONTACT_NO, APP.contacts.size()));
    }

    private void initListener() {
        ll_back.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        rl_head.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_head:
                presenter.rl_head();
                break;
            case R.id.tv_delete:
                presenter.tv_delete();
                break;
            case R.id.iv_save:
                presenter.iv_save();
                finish();
                break;
        }
    }

    @Override
    public String getName() {
        return et_name.getText().toString();
    }

    @Override
    public String getTel() {
        return et_tel.getText().toString();
    }

    @Override
    public boolean getSms() {
        return sth_sms.isChecked();
    }

    @Override
    public String getSmsText() {
        return et_sms.getText().toString();
    }

    @Override
    public void setHead(String head) {
        Picasso.with(this).load(new File(head)).into(civ_head);
    }

    @Override
    public void setSms(boolean sms) {
        sth_sms.setChecked(sms);
    }

    @Override
    public void setSmsText(String smsText) {
        et_sms.setText(smsText);
    }

    @Override
    public void setTel(String tel) {
        et_tel.setText(tel);
    }

    @Override
    public void setName(String name) {
        et_name.setText(name);
    }

    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 2001);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2001:
                Log.e("", "0");
                presenter.analysis(data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
