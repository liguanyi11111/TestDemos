package com.testdemo.bluetoothle;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liguanyi on 2015/8/31.
 *
 */
@TargetApi(18)
public class BlueToothLeFragment extends BaseFragment{
    private final static String TAG = BlueToothLeFragment.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    private TextView mTextShow;
    private Button mButton;
    private Handler mHandler;

    private boolean mIsScanning;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_bluetooth_le;
    }

    @Override
    protected void init() {
        super.init();
        mHandler = new Handler();
        mTextShow = findViewById(R.id.text_show);
        mTextShow.setText("扫描结果\n");
        mButton = findViewById(R.id.button_scan_audio);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsScanning){
                    Log.d(TAG, "正在扫描ing");
                    return;
                }
                Log.d(TAG, "开始扫描");
                if(mDeviceList == null) {
                    mDeviceList = new ArrayList<>();
                }
                mDeviceList.clear();
                mIsScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallBack);
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "结束扫描");
                        mBluetoothAdapter.stopLeScan(mLeScanCallBack);
                        mIsScanning = false;
                        connect();
                    }
                }, 12000);
            }
        });
        mBluetoothAdapter = ((BluetoothManager)getActivity().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if(mBluetoothAdapter == null){
            Toast.makeText(getActivity(), "不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
            return;
        }
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "不支持蓝牙BLE", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
            return;
        }
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    int i = 0;
    private void connect(){
        if(i >= mDeviceList.size()){
            Log.d(TAG, "END");
            return;
        }
        final BluetoothDevice device = mDeviceList.get(i);
        Log.d(TAG, "Connecting " + device.getName());
        mTextShow.setText(mTextShow.getText() + "\nConnecting " + device.getName());
        device.connectGatt(getActivity(), false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
                Log.d(TAG, "onConnectionStateChange" + " status " + status + " newStatus " + newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    if (gatt.getServices().size() == 0) {
                        gatt.disconnect();
                        i++;
                        connect();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTextShow.setText(mTextShow.getText() + "\n连接" + device.getName() + "成功！" + gatt.getServices().size());
                            for (BluetoothGattService service : gatt.getServices()) {
                                Log.d(TAG, "BluetoothGattService " + service.getUuid() + " status " + service.getType());
                                mTextShow.setText(mTextShow.getText() + "\n   BluetoothGattService： " + service.getUuid());
                                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                                    Log.d(TAG, "Characteristic " + characteristic.getUuid() + " permissions " +
                                            characteristic.getPermissions() + " Properties " + characteristic.getProperties());
                                    mTextShow.setText(mTextShow.getText() + "\n      Characteristic： " + characteristic.getUuid() + " permissions " + characteristic.getPermissions());
                                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                                        Log.d(TAG, "descriptor " + descriptor.getUuid() + " permissions " +
                                                descriptor.getPermissions() + " Properties " + printByteArray(descriptor.getValue()));
                                        mTextShow.setText(mTextShow.getText() + "\n         Characteristic： " + characteristic.getUuid() + " permissions " + characteristic.getPermissions());
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.d(TAG, "onServicesDiscovered" + " status " + status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(TAG, "onCharacteristicRead" + " status " + status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(TAG, "onCharacteristicWrite" + " status " + status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                Log.d(TAG, "onCharacteristicChanged");
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(TAG, "onDescriptorRead" + " status " + status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                Log.d(TAG, "onDescriptorWrite" + " status " + status);
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                Log.d(TAG, "onReliableWriteCompleted" + " status " + status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                Log.d(TAG, "onReadRemoteRssi " + rssi + " status " + status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                Log.d(TAG, "onMtuChanged " + mtu + " status " + status);
            }
        });
    }

    private List<BluetoothDevice> mDeviceList;
    private BluetoothAdapter.LeScanCallback mLeScanCallBack = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            Log.d(TAG, "name: " + device.getName() + " rssi " + rssi + " scanRecord size " + scanRecord.length);
            if(!mDeviceList.contains(device)) {
                mDeviceList.add(device);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTextShow.setText(mTextShow.getText() + "name: " + device.getName() + "\n  rssi " + rssi + "\n  scanRecord size " + scanRecord.length + "\r\n");
                }
            });
        }
    };

    private String printByteArray(byte[] values){
        String str = null;
        for(byte b : values){
            str += b + " ";
        }
        return str;
    }

}
