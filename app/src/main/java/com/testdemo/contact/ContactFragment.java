package com.testdemo.contact;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;

import com.testdemo.BaseFragment;
import com.testdemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liguanyi on 15-12-28.
 */
public class ContactFragment extends BaseFragment{

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_common;
    }

    @Override
    protected void init() {
        super.init();
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContact(getActivity());
            }
        });
    }

    private void getContact(Context context){
        String selection = "0==0) GROUP BY (" + CallLog.Calls.NUMBER;
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, selection, null, null);
        int num = 0;
        if (cursor != null) {
            if(cursor.moveToFirst()){
                do{
                    num ++;
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    //呼叫类型
                    String type;
                    switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(

                            CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.INCOMING_TYPE:
                            type = "呼入";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            type = "呼出";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            type = "未接";
                            break;
                        default:
                            type = "挂断";//应该是挂断.根据我手机类型判断出的
                            break;
                    }
                    SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                    //呼叫时间
                    String time = sfd.format(date);
                    //联系人
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                    //通话时间,单位:s
                    String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    Log.d("lgy", num + " name: " + name + " time: " + time + " num: " + number + " type: " + type);
                }while(cursor.moveToNext());

            }
        }
    }
}
