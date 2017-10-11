package com.demo.widget.textcolorchange;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by 花歹 on 2017/10/11.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class ViewPagerActivity extends AppCompatActivity {
    private String[] items = {"直播", "推荐", "视频", "图片", "精华"};

    private LinearLayout mIndicatorContainer;

    private List<ColorTrackTextView> mIndicators;

    private ViewPager mViewPager;
    private String TAG = ViewPagerActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mIndicators = new ArrayList<>();
        mIndicatorContainer = (LinearLayout) findViewById(R.id.indicator_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        initIndicator();
        initViewPager();

    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //position代表当前位置，positionOffset代表从0到1的百分比
                Log.e(TAG, "position -> " + position + " positionOffset -> " + positionOffset);

                //从左往右滑动，左边textview从全变色到全原始色，就说左边黑色，右边红色，方向为从右到左
                //由于是从变色到原色，那么变色区域逐渐减少，那么CurrentProgress应该有1到0
                ColorTrackTextView left = mIndicators.get(position);
                left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                left.setCurrentProgress(1 - positionOffset);

                try {
                    //从左往右滑动，右边textview从全原始色到全变色，就说左边红色，右边黑色，方向为从左到右
                    //由于是从原色到变色，那么变色区域逐渐变多，那么CurrentProgress应该有0到1
                    ColorTrackTextView right = mIndicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                }catch (Exception e){
                    //处理角标越界
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initIndicator() {

        for (int i = 0; i < items.length; i++) {
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            ColorTrackTextView colorTrackTextView = new ColorTrackTextView(this);
            colorTrackTextView.setTextSize(20);
            colorTrackTextView.setChangeColor(Color.RED);
            colorTrackTextView.setText(items[i]);
            colorTrackTextView.setLayoutParams(params);

            mIndicatorContainer.addView(colorTrackTextView);
            mIndicators.add(colorTrackTextView);
        }
    }
}
