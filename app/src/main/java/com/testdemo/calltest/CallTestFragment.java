package com.testdemo.calltest;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by cm on 2015/9/17.
 */
public class CallTestFragment extends BaseFragment {
    private EditText mEditText;
    private Button mButton;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_call_test;
    }

    @Override
    protected void init() {
        super.init();
        mEditText = findViewById(R.id.edit_call);
        mEditText.setText("15301193360");
        mButton = findViewById(R.id.button_call);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
    }

    private void call(){
        String num = mEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
        startActivity(intent);
    }
}
