package com.help.view;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.help.R;
import com.help.app.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by KiSoo on 2016/6/5.
 */
public class ContactActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private Switch sth_sms;
    private EditText et_tel;
    private EditText et_name;
    private EditText et_sms;
    private ImageView iv_save;
    private CircleImageView civ_head;
    private RelativeLayout rl_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void initData() {

    }

    private void initListener() {
        ll_back.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        sth_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;

        }
    }
}
