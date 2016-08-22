package com.testdemo.usbcamera;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 16-4-21.
 */
public class UsbCameraFragment extends BaseFragment{

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_usb_camera;
    }

    @Override
    protected void init() {
        super.init();
        final EditText text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int num = Integer.parseInt(text.getText().toString());
                    final CameraPreview cameraPreview = findViewById(R.id.camera_preview);
                    cameraPreview.setCameraId(num);
                    cameraPreview.start();
                    Toast.makeText(getMainActivity(), "start!", Toast.LENGTH_SHORT).show();
                }catch (NumberFormatException e){
                    Toast.makeText(getMainActivity(), "error num", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
