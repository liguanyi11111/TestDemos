package com.testdemo.floatcontrol;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by liguanyi on 16-1-14.
 */
public class FloatController {
    private WindowManager mWindowManager;
    private ViewGroup testView;
    private View content;
    private WindowManager.LayoutParams mParams;


    public FloatController(Context context){
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        testView = new LinearLayout(context);
        content = new View(context);
        content.setBackgroundColor(Color.RED);
        testView.addView(content, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        testView.setBackgroundColor(Color.WHITE);
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.format = PixelFormat.TRANSPARENT;
        mParams.width = 500;
        mParams.height = 500;
        mParams.x = 100;
        mParams.y = 100;
        mParams.gravity = Gravity.START | Gravity.TOP;
        mWindowManager.addView(testView, mParams);
    }

    boolean init;
    public void updatePosition(int x, int y, int width, int height){
        if(!init) {
            init = true;
            mParams.width = width;
            mParams.height = height;
            mParams.x = 0;
            mParams.y = y;
            mWindowManager.updateViewLayout(testView, mParams);
        }
//        if(x <= -500) {
//            mParams.flags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//            mWindowManager.updateViewLayout(testView, mParams);
//        }
        content.setTranslationX(x);
    }

    public void destroy(){
        mWindowManager.removeView(testView);
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }

}
