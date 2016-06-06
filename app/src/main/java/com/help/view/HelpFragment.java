package com.help.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.help.R;
import com.help.util.Util;
import com.help.widge.CircleButtonWithProgerss;

/**
 * Created by gan on 2016/6/3.
 */
public class HelpFragment extends Fragment {
    private CircleButtonWithProgerss mBtHelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
//        mBtHelp = (CircleButtonWithProgerss) view.findViewById(R.id.bt_help);
        mBtHelp.setSweepAngle(360);
        mBtHelp.setOnChoseListener(new CircleButtonWithProgerss.OnChoseListener() {
            @Override
            public void onChose() {
                Util.Toast(getActivity(), "1231223123123213213");
            }
        });

        return view;
    }
}
