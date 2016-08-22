package com.testdemo.sockettest;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * Created by cm on 2015/8/18.
 */
public class SimpleSocketFragment extends BaseFragment implements View.OnClickListener{
    private Socket mSocket;
    private TextView mText;
    private Handler mConnectHandler;
    private HandlerThread mThread;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_socket;
    }

    @Override
    protected void init() {
        findViewById(R.id.button_start).setOnClickListener(this);
        findViewById(R.id.button_stop).setOnClickListener(this);
        mText = findViewById(R.id.text);
        mThread = new HandlerThread("connectThread");
        mThread.start();
        mConnectHandler = new Handler(mThread.getLooper());
    }

    int successCount = 0;
    int failedCount = 0;
    long maxTime = 0;
    long minTime = Integer.MAX_VALUE;
    long averageTime = 0;
    private void startConnect(){
        mConnectHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopConnect();
                try {
                    Thread.sleep(500);
                    mSocket = new Socket();
                    long time = System.currentTimeMillis();
                    SocketAddress address = new InetSocketAddress("192.168.2.112", 8080);
                    Log.d("socket", "准备连接");
                    mSocket.connect(address, 15000);
                    long connectTime = System.currentTimeMillis() - time;
                    Log.d("socket", "连接成功 " + connectTime);
                    if(connectTime > maxTime){
                        maxTime = connectTime;
                    }
                    if(connectTime < minTime){
                        minTime = connectTime;
                    }
                    if(connectTime != averageTime){
                        averageTime = (averageTime*successCount + connectTime)/(successCount + 1);
                    }
                    successCount++;
                    startConnect();
//                    final InputStream in = mSocket.getInputStream();
//                    final byte[] buffer = new byte[512];
//                    while (mSocket.isConnected()){
//                        Arrays.fill(buffer, (byte) 0);
//                        try {
//                            int length = in.read(buffer, 0, buffer.length);
//                            if (length < 0) {
//                                Log.w("socket", "read data's length is less zero");
//                            } else {
//                                final String str = new String(buffer);
//                                mText.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mText.setText(mText.getText() + "\n" + str);
//                                    }
//                                });
//                            }
//                        } catch (Exception e) {
//                            Log.w("socket", "receive data exception", e);
//                        }
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("socket", "连接失败", e);
                    failedCount++;
                    startConnect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mText.post(new Runnable() {
                        @Override
                        public void run() {
                            mText.setText("成功: " + successCount + " 失败: " + failedCount + "\n" +
                                "maxTIme: " + maxTime + " minTime: " + minTime + " averageTime: " + averageTime);
                        }
                    });
                }
            }
        }, 1000);
    }

    private void stopConnect(){
        if(mSocket != null){
            try {
                mSocket.close();
                Log.d("socket", "已停止");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("socket", "停止失败" , e);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                startConnect();
                break;
            case R.id.button_stop:
                stopConnect();
                mThread.quit();
                break;
        }
    }
}
