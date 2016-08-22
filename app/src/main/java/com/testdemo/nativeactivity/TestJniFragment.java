package com.testdemo.nativeactivity;

import android.content.Intent;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 16-4-25.
 *
 */

public class TestJniFragment extends BaseFragment{
    @Override
    protected int getRootViewId() {
        return R.layout.fragment_common;
    }

    @Override
    protected void init() {
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().startActivity(new Intent(getMainActivity(), TestJniActivity.class));
            }
        });
    }
}
