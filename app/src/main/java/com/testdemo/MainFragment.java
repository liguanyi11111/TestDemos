package com.testdemo;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.testdemo.accessibilitytest.AccessibilityFragment;
import com.testdemo.bluetoothle.BlueToothLeFragment;
import com.testdemo.bluetoothprofile.BluetoothProfileFragment;
import com.testdemo.bootself.BootFragment;
import com.testdemo.calltest.CallTestFragment;
import com.testdemo.contact.ContactFragment;
import com.testdemo.floatcheck.CheckFloatFragment;
import com.testdemo.floatcontrol.FloatControlFragment;
import com.testdemo.floatshow.ShowFloatFragment;
import com.testdemo.helloworld.HelloWorldFragment;
import com.testdemo.inputtouch.InputTouchFragment;
import com.testdemo.interpolator.InterpolatorFragment;
import com.testdemo.jnitest.JniTestFragment;
import com.testdemo.localscan.LocalInfoScanFragment;
import com.testdemo.nativeactivity.TestJniFragment;
import com.testdemo.neural.NeuralNetFragment;
import com.testdemo.plan.CoveragePlanFragment;
import com.testdemo.remotecontrol.RemoteControlFragment;
import com.testdemo.showimage.ShowImageFragment;
import com.testdemo.sockettest.SimpleSocketFragment;
import com.testdemo.standardcamera.StandardCameraFragment;
import com.testdemo.startotherapp.StartOtherAppFragment;
import com.testdemo.temptest.TempTestFragment;
import com.testdemo.usbcamera.UsbCameraFragment;
import com.testdemo.viewtest.ViewTestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cm on 2015/8/17.
 */
public class MainFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private List<ItemData> mItemDataList;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init() {
        initData();
        initView();
    }

    protected void initView() {
        ListView mListView = (ListView) mRootView.findViewById(R.id.list);
        mListView.setAdapter(new MainListAdapter());
//        mListView.setDividerHeight((int) (1 * getResources().getDisplayMetrics().density));
        mListView.setDivider(new ColorDrawable(0x40000000) {
            @Override
            public int getIntrinsicHeight() {
                return (int) (1 * getResources().getDisplayMetrics().density);
            }
        });
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        mItemDataList = new ArrayList<>();
        mItemDataList.add(new ItemData("TempTest", "用于测试的Fragment", new TempTestFragment()));
        mItemDataList.add(new ItemData("HelloWorld", "最基本的Fragment", new HelloWorldFragment()));
        mItemDataList.add(new ItemData("UsbCameraTest", "测试USB Camera", new UsbCameraFragment()));
        mItemDataList.add(new ItemData("ShowImageFragment", "显示测试图像", new ShowImageFragment()));
        mItemDataList.add(new ItemData("NeuralNetFragment", "神经网络demo测试", new NeuralNetFragment()));
        mItemDataList.add(new ItemData("TestJni", "用NativeActivity启动Native代码", new TestJniFragment()));
        mItemDataList.add(new ItemData("StandardCameraTest", "测试标准Camera", new StandardCameraFragment()));
        mItemDataList.add(new ItemData("CoveragePlan", "全覆盖路径规划测试", new CoveragePlanFragment()));
        mItemDataList.add(new ItemData("InputTouch", "尝试通过shell模拟点击", new InputTouchFragment()));
        mItemDataList.add(new ItemData("ViewTest", "测试一些自定义View", new ViewTestFragment()));
        mItemDataList.add(new ItemData("GetContactTest", "获取联系人Fragment", new ContactFragment()));
        mItemDataList.add(new ItemData("FloatControl", "悬浮窗远程控制测试", new FloatControlFragment()));
        mItemDataList.add(new ItemData("AccessibilityTest", "测试辅助功能", new AccessibilityFragment()));
        mItemDataList.add(new ItemData("FloatWindowCheckTest", "检测是否允许显示悬浮窗", new CheckFloatFragment()));
        mItemDataList.add(new ItemData("ShowFloatTest", "检测Toast类型悬浮窗", new ShowFloatFragment()));
        mItemDataList.add(new ItemData("InterpolatorTest", "不同差速器的运动图像", new InterpolatorFragment()));
        mItemDataList.add(new ItemData("SimpleSocket", "简易的Socket连接", new SimpleSocketFragment()));
        mItemDataList.add(new ItemData("StartOtherApp", "测试启动其他app", new StartOtherAppFragment()));
        mItemDataList.add(new ItemData("RemoteControl", "监控蓝牙设备控制按键", new RemoteControlFragment()));
        if(Build.VERSION.SDK_INT >= 18) {
            mItemDataList.add(new ItemData("BlueToothLE", "蓝牙BLE功能简单实现", new BlueToothLeFragment()));
        }
        mItemDataList.add(new ItemData("LocalScan", "扫描本地信息数据", new LocalInfoScanFragment()));
        mItemDataList.add(new ItemData("CallTest", "拨打电话测试", new CallTestFragment()));
        mItemDataList.add(new ItemData("BTProfile", "蓝牙Profile连接测试", new BluetoothProfileFragment()));
        mItemDataList.add(new ItemData("BootTest", "自启动测试", new BootFragment()));
        mItemDataList.add(new ItemData("JniTest", "Jni简单测试", new JniTestFragment()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemData itemData = mItemDataList.get(position);
        Log.d("Debug", "---" + itemData.title);
        getMainActivity().showFragment(itemData.fragment);
    }

    private class MainListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItemDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mItemDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView != null){
                holder = (ViewHolder)convertView.getTag();
            }else{
                convertView = View.inflate(getActivity(), R.layout.list_item, null);
                holder = new ViewHolder();
                holder.title = (TextView)convertView.findViewById(R.id.item_title);
                holder.content = (TextView)convertView.findViewById(R.id.item_content);
                convertView.setTag(holder);
            }
            ItemData itemData = mItemDataList.get(position);
            holder.title.setText(itemData.title);
            holder.content.setText(itemData.content);
            return convertView;
        }

        private class ViewHolder{
            private TextView title;
            private TextView content;
        }

    }

    private class ItemData{
        public String title;
        public String content;
        public Fragment fragment;

        public ItemData(String title, String content, Fragment fragment){
            this.title = title;
            this.content = content;
            this.fragment = fragment;
        }
    }

}
