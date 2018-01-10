package com.demo.widget.guidepage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.demo.widget.guidepage.parallax.ParallaxViewPager;

public class MainActivity extends AppCompatActivity {

    private ParallaxViewPager mParallaxViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //直接findviewByid 设置adapter,不利于扩展，不利于易用性
        mParallaxViewPager = findViewById(R.id.vp_parallax);

        //给一个方法
        mParallaxViewPager.setLayout(getSupportFragmentManager(),new int[]{R.layout.fragment_page_first,
                R.layout.fragment_page_second,
                R.layout.fragment_page_third});

        //用最简便的方式让别人使用
    }
}
