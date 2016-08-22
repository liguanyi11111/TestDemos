package com.testdemo.startotherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by liguanyi on 2015/9/1.
 *
 */
public class StartOtherAppFragment extends BaseFragment implements View.OnClickListener{

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_other_app;
    }

    @Override
    protected void init() {
        super.init();
        findViewById(R.id.start_app).setOnClickListener(this);
        initData();
    }

    private void initData() {
        final PackageManager packageManager = getActivity().getPackageManager();
        new Thread(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> mAllApps = packageManager.queryIntentActivities(mainIntent, 0);
                for(int i = 0; i < mAllApps.size() ; i++){
                    ResolveInfo info = mAllApps.get(i);
                    Log.d("Debug", "packageName: " + info.activityInfo.packageName + "\nactivity: " + info.activityInfo.name);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_app:
                Intent intent = null;
                try {
                    intent = Intent.parseUri("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&src=测试程序#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end",0);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;
        }
    }
}
