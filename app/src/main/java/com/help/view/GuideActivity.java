package com.help.view;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.R;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    //定义ViewPager对象
    private ViewPager viewPager;

    //定义ViewPager适配器
    private GuideViewPagerAdapter vpAdapter;

    //定义一个ArrayList来存放View
    private ArrayList<View> views;

    // 定义各个界面View对象
    private View view1, view2, view3, view4;

    //底部小点的图片
    private ImageView[] points;

    //记录当前选中位置
    private int currentIndex;

    private TextView mTvIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();

    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        //实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.guide_page1, null);
        view2 = mLi.inflate(R.layout.guide_page2, null);
        view3 = mLi.inflate(R.layout.guide_page3, null);
        view4 = mLi.inflate(R.layout.guide_page4, null);
        mTvIn = (TextView) view4.findViewById(R.id.tv_in);


        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.vp_viewpager);

        // 实例化ArrayList对象
        views = new ArrayList<View>();

        // 实例化ViewPager适配器
        vpAdapter = new GuideViewPagerAdapter(views);
    }

    private void initData() {

        //将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);


        // 设置适配器数据
        viewPager.setAdapter(vpAdapter);

        mTvIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, HelpActivity.class));
                finish();
            }
        });
    }


    //设置当前页面的位置
    private void setCurView(int position) {
        if (position < 0 || position >= 4) {
            return;
        }
        viewPager.setCurrentItem(position);
    }




}
