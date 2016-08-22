package com.testdemo.recorder;

import android.view.View;
import android.widget.Button;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by cm on 2015/8/19.
 */
public class RecorderFragment extends BaseFragment implements View.OnClickListener{
    private RecorderManager mRecorderManager;
    private Button mButtonControl;
    private boolean mIsStart;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_recorder;
    }

    @Override
    protected void init() {
        mRecorderManager = new RecorderManager();
        mButtonControl = findViewById(R.id.recorder_control);
        mButtonControl.setOnClickListener(this);
        mRootView.findViewById(R.id.play).setOnClickListener(this);
        mIsStart = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recorder_control:
                if(mIsStart) {
                    mButtonControl.setText("停止录音");
                    mRecorderManager.stopRecord();
                }else{
                    mButtonControl.setText("开始录音");
                    mRecorderManager.startRecord();
                }
                mIsStart = !mIsStart;
                break;
            case R.id.play:

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecorderManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecorderManager.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
