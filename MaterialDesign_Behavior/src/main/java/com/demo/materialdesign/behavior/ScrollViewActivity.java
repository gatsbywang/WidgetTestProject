package com.demo.materialdesign.behavior;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * 用scrollview 仿behavior效果
 */
public class ScrollViewActivity extends AppCompatActivity {

    private View mTitleBar;

    private MyScrollView mScrollView;
    private ImageView mImageView;
    private int mImageViewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        mTitleBar = findViewById(R.id.title_bar);
        //注意和mTitleBar.setAlpha的区别
        mTitleBar.getBackground().setAlpha(0);

        mImageView = findViewById(R.id.image_view);
        mImageView.post(new Runnable() {
            @Override
            public void run() {

                mImageViewHeight = mImageView.getMeasuredHeight();
            }
        });

        mScrollView = findViewById(R.id.scroll_View);
        mScrollView.setOnScrollChangeListener(new MyScrollView.ScrollChangeListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                //获取图片的高度，根据当前滚动的位置，计算alpha值
                if (mImageViewHeight == 0)
                    return;
                float alpha = t * 1.0f / mImageViewHeight;
                if (alpha <= 0) {
                    alpha = 0;
                }
                if (alpha > 1) {
                    alpha = 1;
                }
                mTitleBar.getBackground().setAlpha((int) (alpha * 255));

            }
        });
    }
}
