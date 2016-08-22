package com.testdemo.floatcheck;

import android.content.Context;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 15-11-27.
 *
 */
public class CheckFloatFragment extends BaseFragment{
    private Context mContext;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_check_float;
    }

    @Override
    protected void init() {
        super.init();
        mContext = getActivity().getApplicationContext();
        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().startService(new Intent("test_service"));
            }
        });
    }




}
