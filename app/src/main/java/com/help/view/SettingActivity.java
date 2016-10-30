package com.help.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.help.R;
import com.help.app.BaseActivity;
import com.help.config.ISettingActivityView;
import com.help.presenter.SettingActivityPresenter;

/**
 * Created by KiSoo on 2016/6/3.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, ISettingActivityView {

    private LinearLayout ll_back;
    private RelativeLayout rl_instruction;
    //    private RelativeLayout rl_share;
    private RelativeLayout rl_quick_help;
    private Switch sth_help;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private SettingActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settting);
        sharedPreferences = getSharedPreferences("isCheck", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initView();
        initListener();
        presenter = new SettingActivityPresenter(this, this);
    }

    private void initListener() {
        rl_quick_help.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        rl_instruction.setOnClickListener(this);
//        rl_share.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rl_quick_help = (RelativeLayout) findViewById(R.id.rl_quick_help);
        sth_help = (Switch) findViewById(R.id.sth_help);
        setCheck(getIsChecked());
        rl_instruction = (RelativeLayout) findViewById(R.id.rl_instruction);
//        rl_share = (RelativeLayout) findViewById(R.id.rl_share);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_quick_help:
                presenter.help();
                break;
            case R.id.rl_instruction:
                presenter.instruction();
                break;
//            case R.id.rl_share:
//                presenter.share();
//                break;
        }
    }

    @Override
    public boolean getIsChecked() {
        return sharedPreferences.getBoolean("isCheck",false);
    }

    @Override
    public void setCheck(boolean check) {
        editor.putBoolean("isCheck", check);
        editor.commit();
        sth_help.setChecked(check);
    }
}
