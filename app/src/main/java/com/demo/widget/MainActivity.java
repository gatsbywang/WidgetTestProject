package com.demo.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
        int height = mTextView.getMeasuredHeight();

        mTextView.post(new Runnable() {
            @Override
            public void run() {
                int height = mTextView.getMeasuredHeight();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int height = mTextView.getMeasuredHeight();
    }
}
