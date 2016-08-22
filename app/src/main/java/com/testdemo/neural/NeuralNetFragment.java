package com.testdemo.neural;

import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 16-5-28.
 */
public class NeuralNetFragment extends BaseFragment{
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
                new NeuralNetwork().start();
            }
        });
    }
}
