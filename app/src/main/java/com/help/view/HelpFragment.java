package com.help.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.help.R;
import com.help.util.Util;
import com.help.widge.CircleButtonWithProgerss;

/**
 * Created by gan on 2016/6/3.
 */
public class HelpFragment extends Fragment {
    private FloatingActionMenu mFbMenu;
    private FloatingActionButton mFbAdd;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        initView();
        initListener();
        return view;
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
    }

    private void initView() {
        mFbMenu = (FloatingActionMenu) view.findViewById(R.id.fb_menu);
        mFbAdd = (FloatingActionButton) view.findViewById(R.id.fb_add);
    }

    private void addContect(FloatingActionMenu mFbMenu) {
        FloatingActionButton fb = new FloatingActionButton(getActivity());
        fb.setColorNormal(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
        fb.setColorPressed(getResources().getColor(R.color.colorPrimary));
        fb.setColorRipple(getResources().getColor(R.color.colorRippleGray));
        fb.setButtonSize(FloatingActionButton.SIZE_MINI);
        mFbMenu.addMenuButton(fb);

    }
}
