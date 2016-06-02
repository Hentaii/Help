package com.help.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.R;
import com.help.util.Util;

import java.util.ArrayList;


public class UseHelpActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private View view1, view2, view3, view4, view5;
    private ArrayList<View> views;
    private GuideViewPagerAdapter vpAdapter;
    private TextView mTvPrevious, mTvNext, mTvStart;
    private ImageView mIvAnim;
    public static int curPosition;
    private AnimationDrawable drawable;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        setContentView(R.layout.activity_use_help);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.use_help_view1, null);
        view2 = mLi.inflate(R.layout.use_help_view2, null);
        view3 = mLi.inflate(R.layout.use_help_view3, null);
        view4 = mLi.inflate(R.layout.use_help_view4, null);
        view5 = mLi.inflate(R.layout.use_help_view5, null);
        views = new ArrayList<View>();
        mTvPrevious = (TextView) findViewById(R.id.tv_previous);
        mTvNext = (TextView) findViewById(R.id.tv_next);
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mIvAnim = (ImageView) view2.findViewById(R.id.iv_anim);
        vpAdapter = new GuideViewPagerAdapter(views);
    }

    private void initData() {
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        mViewPager.setAdapter(vpAdapter);
        mIvAnim.setImageResource(R.drawable.frame_anim);
        drawable = (AnimationDrawable) mIvAnim.getDrawable();
        drawable.start();
        drawable.stop();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                if (position == views.size() - 1) {
                    mTvPrevious.setVisibility(View.GONE);
                    mTvNext.setVisibility(View.GONE);
                    mTvStart.setVisibility(View.VISIBLE);
                } else if (position == 0) {
                    mTvPrevious.setVisibility(View.GONE);
                    mTvStart.setVisibility(View.GONE);
                    mTvNext.setVisibility(View.VISIBLE);
                } else {
                    mTvStart.setVisibility(View.GONE);
                    mTvPrevious.setVisibility(View.VISIBLE);
                    mTvNext.setVisibility(View.VISIBLE);
                }
                if (position == 1) {
                    mIvAnim.post(new Runnable() {
                        @Override
                        public void run() {
                            mIvAnim.invalidate();
                            drawable.start();
                        }
                    });
                } else {
                    drawable.stop();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initListener() {
        mTvNext.setOnClickListener(this);
        mTvPrevious.setOnClickListener(this);
        mTvStart.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Util.Toast(UseHelpActivity.this, "123");
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                mViewPager.setCurrentItem(curPosition + 1);
                break;
            case R.id.tv_previous:
                mViewPager.setCurrentItem(curPosition - 1);
                break;
            case R.id.tv_start:
                finish();
                break;
        }
    }


}
