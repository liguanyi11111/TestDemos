package com.testdemo.recorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by liguanyi on 2015/8/19.
 *
 */
public class RecorderManager {
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;

    public RecorderManager(){
    }

    public void onStart(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    }

    public void onStop(){
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public void startRecord(){
        if(mMediaRecorder != null){
            try {
                mMediaRecorder.setOutputFile(File.createTempFile("test_record_", ".awm").getAbsolutePath());
                mMediaRecorder.prepare();
                mMediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecord(){
        if(mMediaRecorder != null){
            mMediaRecorder.stop();
        }
    }

    public void play(){

    }

    public void destroy(){
    }

}
