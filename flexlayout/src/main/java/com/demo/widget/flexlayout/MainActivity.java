package com.demo.widget.flexlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlexLayout mFlexLayout;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlexLayout = (FlexLayout) findViewById(R.id.flex_layout);

        mItems = new ArrayList<>();
        mItems.add("hello world");
        mItems.add("hello worl");
        mItems.add("hello wor");
        mItems.add("hello wo");
        mItems.add("hello wo");
        mItems.add("hello world");
        mItems.add("hello worl");
        mItems.add("hello wor");
        mItems.add("hello wo");
        mItems.add("hello world");
        mItems.add("hello world  hello");

        mFlexLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                //
                TextView textView = (TextView) LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.item_flex_layout, parent, false);
                textView.setText(mItems.get(position));

                return textView;
            }
        });
    }
}
