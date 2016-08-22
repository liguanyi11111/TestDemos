package com.testdemo.standardcamera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 16-4-21.
 */
public class StandardCameraFragment extends BaseFragment{
    private CameraManager mCameraManager;
    private Handler mHandler;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_temp_test;
    }

    @Override
    protected void init() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCamera();
            }
        });
    }

    private void initCamera(){
        mCameraManager = (CameraManager)getMainActivity().getSystemService(Context.CAMERA_SERVICE);
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        try {
            String[] list = mCameraManager.getCameraIdList();
            Toast.makeText(getActivity(), "camera num : " + list.length, Toast.LENGTH_SHORT).show();

            try {
                Log.d("lgy", ">>>>>>>>>>>>>>>>>>>>> 1");
                mCameraManager.openCamera(CameraCharacteristics.LENS_FACING_BACK+"", new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(CameraDevice camera) {
                        Toast.makeText(getActivity(), "success " + camera.getId(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDisconnected(CameraDevice camera) {

                    }

                    @Override
                    public void onError(CameraDevice camera, int error) {
                        Toast.makeText(getActivity(), "error " + error, Toast.LENGTH_SHORT).show();
                    }
                }, mHandler);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "exception  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                try {
                    Log.d("lgy", ">>>>>>>>>>>>>>>>>>>>> dev/media1");
                    mCameraManager.openCamera("dev/media1", new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(CameraDevice camera) {
                            Toast.makeText(getActivity(), "success " + camera.getId(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDisconnected(CameraDevice camera) {

                        }

                        @Override
                        public void onError(CameraDevice camera, int error) {
                            Toast.makeText(getActivity(), "error " + error, Toast.LENGTH_SHORT).show();
                        }
                    }, mHandler);

                }catch (IllegalArgumentException e1){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "exception  " + e1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
