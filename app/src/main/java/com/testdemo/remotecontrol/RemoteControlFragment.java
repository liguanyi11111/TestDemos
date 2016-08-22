package com.testdemo.remotecontrol;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import android.widget.TextView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 2015/9/11.
 *
 */
public class RemoteControlFragment extends BaseFragment{
    private static final String TAG = RemoteControlFragment.class.getSimpleName();
    private TextView mShowText;
    private AudioManager mAudioManager;
    private ComponentName componentName;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_remote_control;
    }

    @Override
    protected void init() {
        super.init();
        mShowText = findViewById(R.id.text_show);
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        componentName = new ComponentName(getActivity().getPackageName(), RemoteControlReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(componentName);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAudioManager.unregisterMediaButtonEventReceiver(componentName);
    }
}
