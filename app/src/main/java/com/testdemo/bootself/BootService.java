package com.testdemo.bootself;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by cm on 2015/10/19.
 *
 */
public class BootService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BootService", "bootComplete");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BootService.this, BootService.this.getPackageName()+ " bootComplete", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
