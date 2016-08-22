package com.testdemo.floatshow;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 15-12-17.
 */
public class ShowFloatFragment extends BaseFragment{
    private WindowManager windowManager;
    private View view;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_show_float;
    }

    @Override
    protected void init() {
        super.init();
        windowManager = (WindowManager)getActivity().getApplication().getSystemService(Context.WINDOW_SERVICE);
        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloat();
            }
        });
    }

    private void showFloat(){
        Log.d("lgy","showFloat");
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = 500;
        params.height = 500;
        view = new View(getActivity().getApplicationContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lgy","onClick");
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("lgy","onTouch");
                return false;
            }
        });
        windowManager.addView(view, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(view.getParent() != null){
            windowManager.removeView(view);
        }
    }
}
