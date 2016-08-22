package com.testdemo.bootself;

import android.content.Intent;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by cm on 2015/10/19.
 */
public class BootFragment extends BaseFragment{

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_boot;
    }

    @Override
    protected void init() {
        super.init();
        findViewById(R.id.button_boot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(), BootService.class));
            }
        });
    }
}
