package com.testdemo.temptest;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created liguanyi cm on 2015/9/17.
 */
public class TempTestFragment extends BaseFragment{
    private static final String TAG = TempTestFragment.class.getSimpleName();

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_temp_test;
    }



    @Override
    protected void init() {
        super.init();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lgy", "TempTest onClick");
                test();
            }
        });
    }

    private void test(){

        byte[] data = new byte[]{
            -86,85,77,1,15,-128,111,0,0,0,
                84,-12,111,4,0,0,0,18,-94,0,
                3,3,0,0,0,4,7,-4,6,0,
                0,0,0,0,5,6,-60,6,-17,7,
                -37,6,6,2,0,0,13,14,-14,6,
                14,0,38,0,-124,-1,16,0,38,0,
                -123,-1,16,16,11,0,-1,15,-1,15,
                -1,15,-1,15,-18,15,0,0,0,0,
                -36
          };
        byte result = 0;
        result ^= 77;

        for (int i = 3; i < data.length; i++) {
            result ^= data[i];
        }
        Log.d("lgy", "result: " + result);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
