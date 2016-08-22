package com.testdemo.plan;

import android.graphics.Rect;

/**
 * Created by liguanyi on 16-3-17.
 *
 */
public class Constant {
    public static final byte TYPE_NONE = 0;
    public static final byte TYPE_NOT_KNOW = 1;
    public static final byte TYPE_OBSTACLE = 2;
    public static final byte TYPE_CLEARED = 10;


    public static final byte[] DOWN = new byte[]{0, 1};
    public static final byte[] UP = new byte[]{0, -1};
    public static final byte[] LEFT= new byte[]{-1, 0};
    public static final byte[] RIGHT= new byte[]{1, 0};


    //初始参数
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    public static final int[] POSITION = new int[]{1, 2};
    public static final Rect[] OBSTACLES = new Rect[]{
//            new Rect(2, 3, 6, 3),
//            new Rect(12, 3, 13, 8),
//            new Rect(5, 14, 15, 16),
//            new Rect(19, 8, 23, 16),
//            new Rect(7, 18, 15, 18),
//            new Rect(2, 28, 11, 28),
//            new Rect(17, 15, 18, 29),
    };
}
