package com.help.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by KiSoo on 2016/6/30.
 */
public abstract class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentId(),container,false);
    }

    protected abstract @LayoutRes int getContentId();

}
