package com.help.view;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.help.R;
import com.help.config.IGetMapLocation;
import com.help.model.LocationService;
import com.help.util.Util;
import com.help.widge.CircleButtonWithProgerss;

/**
 * Created by gan on 2016/6/3.
 */
public class HelpFragment extends Fragment {
    private FloatingActionMenu mFbMenu;
    private FloatingActionButton mFbAdd;
    private View view;
    private ServiceConnection sc;
    private TextView mTvPosition;
    private ImageView mIvRefresh;
    private String currentPosition;
    private boolean firstLocation = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        initView();
        initListener();
        initService();
        return view;
    }

    private void initService() {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocationService locationService = ((LocationService.LocationBinder) service).getService();
                locationService.initLocation(new IGetMapLocation() {
                    @Override
                    public void getLocationSuccess(AMapLocation amapLocation) {
                        currentPosition = amapLocation.getAddress();
                        if (firstLocation) {
                            mTvPosition.setText(currentPosition);
                            firstLocation = false;
                        }
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent service = new Intent(getActivity(), LocationService.class);
        getActivity().bindService(service, sc, Context.BIND_AUTO_CREATE);
    }

    private void initListener() {
        mFbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFbMenu.getChildCount() < 6) {
                    addContect(mFbMenu);
                } else {
                    Util.Toast(getActivity(), "人数已满");
                }
            }
        });
        mIvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvPosition.setText(currentPosition);
            }
        });
    }

    private void initView() {
        mFbMenu = (FloatingActionMenu) view.findViewById(R.id.fb_menu);
        mFbAdd = (FloatingActionButton) view.findViewById(R.id.fb_add);
        mTvPosition = (TextView) view.findViewById(R.id.tv_positon);
        mIvRefresh = (ImageView) view.findViewById(R.id.iv_refresh);
    }

    private void addContect(FloatingActionMenu mFbMenu) {
        FloatingActionButton fb = new FloatingActionButton(getActivity());
        fb.setColorNormal(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
        fb.setColorPressed(getResources().getColor(R.color.colorPrimary));
        fb.setColorRipple(getResources().getColor(R.color.colorRippleGray));
        fb.setButtonSize(FloatingActionButton.SIZE_MINI);
        mFbMenu.addMenuButton(fb);
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(sc);
        super.onDestroy();
    }
}
