package com.help.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.help.R;
import com.help.util.Util;

public class SplashActivity extends Activity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = Util.getSp(SplashActivity.this);

        if (sp.getBoolean("isFirst", true)) {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, HelpActivity.class));
                    finish();
                }
            }, 2000);
        }

    }


}
