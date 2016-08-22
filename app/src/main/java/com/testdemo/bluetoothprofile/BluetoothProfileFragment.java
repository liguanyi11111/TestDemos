package com.testdemo.bluetoothprofile;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by cm on 2015/9/18.
 */
public class BluetoothProfileFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = BluetoothProfileFragment.class.getSimpleName();
    private AudioManager mAudioManager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
            Log.i(TAG, "state " + state);
            if (AudioManager.SCO_AUDIO_STATE_CONNECTED == state) {
//                Log.i(TAG, "AudioManager.SCO_AUDIO_STATE_CONNECTED");
//                mAudioManager.setBluetoothScoOn(true);  //打开SCO
                Log.i(TAG, "Routing:" + mAudioManager.isBluetoothScoOn());
            }
//            else {//等待一秒后再尝试启动SCO
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mAudioManager.startBluetoothSco();
//                Log.i(TAG, "再次startBluetoothSco()");
//
//            }
        }
    };

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_bluetooth_profile;
    }

    @Override
    protected void init() {
        super.init();
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        findViewById(R.id.button_start).setOnClickListener(this);
        findViewById(R.id.button_stop).setOnClickListener(this);
        getActivity().registerReceiver(mReceiver, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                startRecording();
                break;
            case R.id.button_stop:
                stopRecording();
                break;
        }
    }

    private void startRecording(){
        if (!mAudioManager.isBluetoothScoAvailableOffCall()) {
            Log.i(TAG, "系统不支持蓝牙录音");
            return;
        }
        Log.i(TAG, "系统支持蓝牙录音");
        mAudioManager.stopBluetoothSco();
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.startBluetoothSco();//蓝牙录音的关键，启动SCO连接，耳机话筒才起作用
    }


    private void stopRecording(){
        if (mAudioManager.isBluetoothScoOn()) {
            mAudioManager.setBluetoothScoOn(false);
            mAudioManager.stopBluetoothSco();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        getActivity().unregisterReceiver(mReceiver);  //别遗漏
    }

}
