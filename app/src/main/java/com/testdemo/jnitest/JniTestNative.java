package com.testdemo.jnitest;

/**
 * Created by liguanyi on 15-10-29.
 */
public class JniTestNative {

    static {
        System.loadLibrary("JniTest");
    }


    public native String getString();


}
