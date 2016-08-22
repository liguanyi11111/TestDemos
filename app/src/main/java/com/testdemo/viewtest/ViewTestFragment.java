package com.testdemo.viewtest;

import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.util.ArrayList;
import java.util.List;

import com.testdemo.viewtest.ItemView.ItemData;

/**
 * Created by liguanyi on 16-1-4.
 */
public class ViewTestFragment extends BaseFragment{

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_view_test;
    }

    @Override
    protected void init() {
        super.init();
        final ExpandableView view = findViewById(R.id.icon_array_view);
        List<ItemData> dataList = new ArrayList<>();
        dataList.add(new ItemData("test1", null));
        dataList.add(new ItemData("test2", null));
        dataList.add(new ItemData("test3", null));
        dataList.add(new ItemData("test4", null));
        dataList.add(new ItemData("test5", null));
        dataList.add(new ItemData("test6", null));
        dataList.add(new ItemData("test7", null));
        view.setData(dataList);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.openOrClose();
            }
        });
    }
}
