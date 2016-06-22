package com.xia.xiaring;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.xia.xiaring.view.custom.XiaRing;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final XiaRing mXiaRing = (XiaRing) findViewById(R.id.xiaring);
        final EditText mEditText = (EditText) findViewById(R.id.value);

        Button resetBtn = (Button) findViewById(R.id.reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 60;
                if (!TextUtils.isEmpty(mEditText.getText())) {
                    value = Integer.valueOf(mEditText.getText().toString());
                }
                mXiaRing.resetProgress(0, value);
            }
        });
    }
}
