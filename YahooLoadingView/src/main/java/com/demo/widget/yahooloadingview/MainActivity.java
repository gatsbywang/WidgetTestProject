package com.demo.widget.yahooloadingview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingView = findViewById(R.id.loadingview);
        //模拟获取后台数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingView.disapper();
            }
        },5000);
    }
}
