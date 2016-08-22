package com.testdemo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by cm on 2015/8/19.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
//        SDKInitializer.initialize(this.getApplicationContext());
    }
}
