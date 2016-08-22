package com.testdemo.inputtouch;

import android.util.Log;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.io.IOException;

/**
 * Created by liguanyi on 16-1-24.
 */
public class InputTouchFragment extends BaseFragment {

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_common;
    }

    @Override
    protected void init() {
        super.init();
        final View view = findViewById(R.id.test2);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LGY", "点击test2");
            }
        });
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                int x = location[0] + view.getWidth()/2;
                int y = location[1] + view.getHeight()/2;
                try {
                    Process process = Runtime.getRuntime().exec("/system/bin/input tap " + x + " " + y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
