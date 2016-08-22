package com.testdemo.bootself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by cm on 2015/10/19.
 */
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootReceiver", "BootReceiver onReceiver " + intent.getAction());
        context.startService(new Intent(context, BootService.class));
    }
}
