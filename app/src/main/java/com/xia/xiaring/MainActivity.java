package com.xia.xiaring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xia.xiaring.view.custom.XiaRing;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private XiaRing mXiaRing;
    private EditText mSizeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mXiaRing = (XiaRing) findViewById(R.id.xiaring);
        mSizeInput = (EditText) findViewById(R.id.size_input);

        Button mResetBtn = (Button) findViewById(R.id.reset);
        Button mMoveBtn = (Button) findViewById(R.id.move);
        Button mAddBtn = (Button) findViewById(R.id.add);
        Button mReduceBtn = (Button) findViewById(R.id.reduce);
        mResetBtn.setOnClickListener(this);
        mMoveBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mReduceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                clickReset();
                break;
            case R.id.move:
                clickMove();
                break;
            case R.id.add:
                clickAdd(true);
                break;
            case R.id.reduce:
                clickAdd(false);
                break;
        }
    }

    private void clickReset() {
        if (TextUtils.isEmpty(mSizeInput.getText().toString()))
            return ;
        int toProgress =  Integer.valueOf(mSizeInput.getText().toString());
        mXiaRing.resetProgress(toProgress);
    }

    private void clickMove() {
        if (TextUtils.isEmpty(mSizeInput.getText().toString()))
            return ;
        int toProgress =  Integer.valueOf(mSizeInput.getText().toString());
        mXiaRing.moveProgress(toProgress);
    }

    private void clickAdd(Boolean isAdd) {
        if (TextUtils.isEmpty(mSizeInput.getText().toString()))
            return ;
        int addProgress = Integer.valueOf(mSizeInput.getText().toString());
        if (isAdd) {
            mXiaRing.addProgress(addProgress);
        } else {
            mXiaRing.addProgress(-addProgress);
        }
    }
}
