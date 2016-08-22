package com.testdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cm on 2015/8/17.
 */
public abstract  class BaseFragment extends Fragment{
    protected View mRootView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getRootViewId(), null);
        init();
        return mRootView;
    }

    protected abstract int getRootViewId();

    protected void init(){

    }

    protected MainActivity getMainActivity(){
        return (MainActivity)getActivity();
    }

    protected <E extends View> E findViewById(int id){
        return (E)mRootView.findViewById(id);
    }
}
