package com.testdemo.localscan;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 2015/9/17.
 *
 */
public class LocalInfoScanFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = LocalInfoScanFragment.class.getSimpleName();
    private Button mButtonScanContacts;
    private Button mButtonScanAudio;
    private TextView mTextShow;
    ContentResolver mResolver;
    @Override
    protected int getRootViewId() {
        return R.layout.fragment_local_scan;
    }

    @Override
    protected void init() {
        super.init();
        mResolver = getActivity().getContentResolver();
        mTextShow = findViewById(R.id.text_show);
        mButtonScanAudio = findViewById(R.id.button_scan_audio);
        mButtonScanAudio.setOnClickListener(this);
        mButtonScanContacts = findViewById(R.id.button_scan_contacts);
        mButtonScanContacts.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        new Thread(){
            @Override
            public void run() {
                switch (v.getId()){
                    case R.id.button_scan_audio:
                        scanAudio();
                        break;
                    case R.id.button_scan_contacts:
                        scanContacts();
                        break;
                }
            }
        }.start();
    }

    private void scanAudio(){
        final Cursor cursor = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null , MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        String temp = "count: " + cursor.getCount() + "\n";
        Log.d(TAG, "scanAudio count " + cursor.getCount());
        while(cursor.moveToNext()){
            temp += "id: " + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    + "\n  title: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    + "\n  album: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    + "\n  artist: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    + "\n  url: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    + "\n  displayName: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    + "\n  duration: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    + "\n  size: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    + "\r\n";
        }
        final String show = temp;
        mTextShow.post(new Runnable() {
            @Override
            public void run() {
                mTextShow.setText(show);
            }
        });
        cursor.close();
    }

    private void scanContacts(){
        long time = System.currentTimeMillis();
        final Cursor cursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]
                {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
        Log.d(TAG, "scanContacts count " + cursor.getCount());
        String temp = "count: " + cursor.getCount() + "\n";
        while(cursor.moveToNext()){
            temp += "name: " + cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    + "\n  number: " + cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    + "\r\n";
        }
        final String show = temp;
        mTextShow.post(new Runnable() {
            @Override
            public void run() {
                mTextShow.setText(show);
            }
        });
        cursor.close();
        Log.d(TAG, "scanContacts time " + (System.currentTimeMillis() - time));
    }
}
