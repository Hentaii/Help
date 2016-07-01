package com.help.view;

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
import com.github.clans.fab.CircleImageView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.help.R;
import com.help.api.API;
import com.help.app.APP;
import com.help.config.IGetMapLocation;
import com.help.model.bean.HelpContact;
import com.help.service.LocationService;
import com.help.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.RealmResults;

/**
 * Created by gan on 2016/6/3.
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener {

    private FloatingActionMenu mFbMenu;
    private FloatingActionButton mFbAdd;
    private View view;
    private ServiceConnection sc;
    private TextView mTvPosition;
    private ImageView mIvRefresh;
    private String currentPosition;
    static private boolean firstLocation = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        firstLocation = true;
        initView();
        initListener();
        initService();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initContacts();
    }

    private void initContacts() {
        RealmResults<HelpContact> contacts = APP.getAllContact();
        mFbMenu.removeAllMenuButtons(R.id.fb_add);
        for (HelpContact contact : contacts) {
            addContect(contact);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_help;
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
        mFbAdd.setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
    }

    private void initView() {
        mFbMenu = (FloatingActionMenu) view.findViewById(R.id.fb_menu);
        mFbAdd = (FloatingActionButton) view.findViewById(R.id.fb_add);
        mTvPosition = (TextView) view.findViewById(R.id.tv_positon);
        mIvRefresh = (ImageView) view.findViewById(R.id.iv_refresh);
    }

    private void addContect(HelpContact contact) {
        CircleImageView circleImageView = new CircleImageView(getActivity());
        circleImageView.setOnClickListener(this);
        if (!contact.head.isEmpty())
            Picasso.with(getActivity()).load(new File(contact.getHead())).into(circleImageView);
        else
            Picasso.with(getActivity()).load(R.mipmap.dl_example_head).into(circleImageView);
        mFbMenu.addMenuButton(circleImageView);
        circleImageView.setTag(contact.getContactNo());
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(sc);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
                mTvPosition.setText(currentPosition);
                break;
            case R.id.fb_add:
                if (APP.getAllContact().size() < 4) {
                    HelpContact contact = new HelpContact(getActivity());
                    addContect(contact);
                    APP.add(contact);
                } else {
                    Util.Toast(getActivity(), "人数已满");
                    Log.e(TAG, "人数已经满了");
                }
                break;
            default:
                getActivity().startActivity(new Intent(getActivity(), ContactActivity.class).putExtra(API.KEY_CONTACT_NO, (long) v.getTag()));
                break;
        }
    }
}
