package com.demo.widget.multitoolbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListMultiLayout mMultiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMultiLayout = findViewById(R.id.layout_list_multi);
        mMultiLayout.setAdapter(new MenuAdapter());

    }
}
