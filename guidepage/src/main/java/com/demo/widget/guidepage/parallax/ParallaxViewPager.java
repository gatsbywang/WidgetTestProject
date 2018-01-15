package com.demo.widget.guidepage.parallax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.demo.widget.guidepage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 花歹 on 2018/1/9.
 * Email:   gatsbywang@126.com
 * Description: 引导页
 * Thought:
 */

public class ParallaxViewPager extends ViewPager {

    private List<ParallaxFragment> mFragments;

    public ParallaxViewPager(Context context) {
        this(context, null);
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFragments = new ArrayList<>();
    }

    public void setLayout(FragmentManager fm, int... layoutIds) {
        mFragments.clear();
        //实例化Fragment
        for (int layoutId : layoutIds) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ParallaxFragment.KEY_LAYOUT_ID, layoutId);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }

        //设置viewpager的adapter
        setAdapter(new ParallaxPagerAdapter(fm));


        //监听滑动滚动改变位移
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滚动 position当前位置  positionOffset 0-1  positionOffsetPixels 0-屏幕宽度px
                Log.e("TAG", "position->" + position
                        + " positionOffset->" + positionOffset
                        + " positionOffsetPixels->" + positionOffsetPixels);

                //获取左out 右in
                ParallaxFragment outFragment = mFragments.get(position);
                List<View> parallaxViews = outFragment.getParallaxViews();
                for (View parallaxView : parallaxViews) {
                    ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                    parallaxView.setTranslationX((-positionOffsetPixels)*tag.translationXOut);
                    parallaxView.setTranslationY((-positionOffsetPixels)*tag.translationYOut);
                }

                try {
                    ParallaxFragment inFragment = mFragments.get(position +1);
                    parallaxViews = inFragment.getParallaxViews();
                    for (View parallaxView : parallaxViews) {
                        ParallaxTag tag = (ParallaxTag) parallaxView.getTag(R.id.parallax_tag);
                        parallaxView.setTranslationX((getMeasuredWidth()-positionOffsetPixels)*tag.translationXOut);
                        parallaxView.setTranslationY((getMeasuredWidth()-positionOffsetPixels)*tag.translationYOut);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onPageSelected(int position) {
                //页面滚动完毕
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class ParallaxPagerAdapter extends FragmentPagerAdapter {

        public ParallaxPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


}
