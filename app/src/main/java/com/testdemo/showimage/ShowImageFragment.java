package com.testdemo.showimage;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liguanyi on 16-6-2.
 */
public class ShowImageFragment extends BaseFragment{
    private ImageView mImage;
    private Button mButton;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_image_test;
    }

    @Override
    protected void init() {
        super.init();
        mImage = findViewById(R.id.image);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lgy","ShowImageFragment onClick");
                mImage.setImageBitmap(getGrayBitmap(readData("test/raw_grey"), 2560, 720));
                Log.d("lgy","ShowImageFragment onClick end ");
            }
        });
    }

    private byte[] readData(String path){
        try {
            InputStream in = getResources().getAssets().open(path);
            int length = in.available();
            Log.d("lgy","ShowImageFragment readData length: " + length);
            byte[] data = new byte[length];
            in.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getGrayBitmap(byte[] data, int width, int height){
        int[] colors = new int[data.length];
        for(int i = 0 ; i < data.length ; i++){
            int d = data[i] & 0xff;
            colors[i] = (0xff << 24) | (d << 16) | (d << 8) | d;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        Log.d("lgy","ShowImageFragment getGrayBitmap bitmap : " + bitmap);
        return bitmap;
    }

    private Bitmap getRGBBitmap(byte[] data, int width, int height){
        int[] colors = new int[data.length / 4];
        for(int i = 0 ; i < colors.length ; i++){
            colors[i] = (data[i * 4 + 3] & 0xff << 24) | (data[i * 4 + 2] & 0xff << 16) |
                    (data[i * 4 + 1] & 0xff << 8) | data[i * 4 ] & 0xff;
            if(i % 50000 == 0){
                Log.d("lgy","ShowImageFragment getRGBBitmap data : " + data[i * 4] + " " + data[i * 4 + 3]);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(colors, 0, width, 0, 0, width, height);
        Log.d("lgy","ShowImageFragment getRGBBitmap bitmap : " + bitmap);
        return bitmap;
    }

//    private Bitmap
}
