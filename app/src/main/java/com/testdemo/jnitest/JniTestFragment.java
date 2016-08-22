package com.testdemo.jnitest;

import android.widget.TextView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 15-10-29.
 * jni测试
 */
public class JniTestFragment extends BaseFragment{
    private TextView mTextView;


    @Override
    protected int getRootViewId() {
        return R.layout.fragment_jni_test;
    }

    @Override
    protected void init() {
        super.init();
        JniTestNative jniTest = new JniTestNative();
        mTextView = findViewById(R.id.jni_text);
        mTextView.setText(jniTest.getString());
    }
}
