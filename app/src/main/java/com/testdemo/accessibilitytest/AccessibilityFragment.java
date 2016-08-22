package com.testdemo.accessibilitytest;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 15-11-28.
 *
 */
public class AccessibilityFragment extends BaseFragment{

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_accessibility_test;
    }

    @Override
    protected void init() {
        findViewById(R.id.goto_open_accessibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOpenAccessibility();
            }
        });
        findViewById(R.id.goto_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetting();
            }
        });
    }

    private void gotoOpenAccessibility(){
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Log.d("lgy", "去开启辅助功能权限");
    }

    private void gotoSetting(){
        Log.d("lgy", "去设置页");
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().getApplication().startActivity(intent);
    }
}
