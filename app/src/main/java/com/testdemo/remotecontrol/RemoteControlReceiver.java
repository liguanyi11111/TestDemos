package com.testdemo.remotecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by cm on 2015/9/11.
 */
public class RemoteControlReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        KeyEvent key = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        Log.d("RemoteControlReceiver", "action " + intent.getAction() + " Extra " + (key != null ? key.getKeyCode() : -1));
    }
}
