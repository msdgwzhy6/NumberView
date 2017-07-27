package com.numberview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NumberView numberview;
    private Handler mHandler;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        numberview = (NumberView) findViewById(R.id.numberview);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (i <=100) {
                    numberview.setCurrentProgress(i);
                    i += 1;
                    mHandler.sendEmptyMessageDelayed(0, 100);
                }

            }
        };

        numberview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(0, 10);
            }
        });

    }
}
