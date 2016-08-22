package com.testdemo.bluetoothprofile;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by cm on 2015/9/21.
 */
public class BTProfileReceiver extends BroadcastReceiver{
    private static final String TAG = BluetoothProfileFragment.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BTProfileReceiver onReceive");
        if(intent.getAction().equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE,
                    -1);
            Log.d(TAG, "==> Headset state: " + state);
        }else if(intent.getAction().equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE,
                    -1);
            Log.d(TAG, "==> A2dp state: " + state);
        }
    }
}
