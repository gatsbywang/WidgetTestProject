package com.demo.widget.flexlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 花歹 on 2017/10/30.
 * Email:   gatsbywang@126.com
 * Description: 流式布局，可以认为应用于标签的摆放
 * Thought:
 */

public class FlexLayout extends ViewGroup {

    private BaseAdapter mAdapter;

    private List<List<View>> mChildViews = new ArrayList<>();

    public FlexLayout(Context context) {
        this(context, null);
    }

    public FlexLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mChildViews.clear();
        //1、for循环测量子view
        int childCount = getChildCount();
        //获取宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //高度需要根据子view计算
        int height = getPaddingTop() + getPaddingBottom();
        //当前行已经占的长度
        int lineWidth = getPaddingLeft();

        List<View> childViews = new ArrayList<>();
        int maxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == GONE) {
                continue;
            }

            //这段话执行之后就可以获取子view的宽高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            //2、判断换行的一句，还得考虑子view的换行
            //2.1 考虑childView的margin ViewGroup.LayoutParams没有margin值
            // 考虑系统的MarginLayoutParams
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            if (lineWidth + (childView.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin) > width) {
                //需要换行,累加高度
                height += maxHeight;
                //换行后初始化lineWidth,maxHeight
                lineWidth = getPaddingLeft();
                maxHeight = 0;
                mChildViews.add(childViews);
                childViews = new ArrayList<>();
            }
            //计算当前行最大高度
            int childHeight = childView.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin;
            maxHeight = Math.max(maxHeight, childHeight);
            //计算当前行已经占用的长度
            lineWidth += childView.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
            childViews.add(childView);

        }
        height += maxHeight;
        mChildViews.add(childViews);

        //2、指定自己的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 摆放子view
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left, top = getPaddingTop(), right, bottom;
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            childView.layout();
//        }

        for (List<View> childViews : mChildViews) {
            left = getPaddingLeft();
            //当前行最大的高度
            int maxHeight = 0;
            for (View childView : childViews) {

                if (childView.getVisibility() == GONE) {
                    continue;
                }


                //还得考虑margin值
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //加上leftmargin
                left += layoutParams.leftMargin;
                //top 加上当前的margin
                int childTop = top + layoutParams.topMargin;
                //right位置计算
                right = left + childView.getMeasuredWidth();
                //底部计算
                bottom = childTop + childView.getMeasuredHeight();
                //摆放
                childView.layout(left, childTop, right, bottom);
                //left位置计算，加上前一个childView的宽度（childView已经调用layout就认为childView为前一个）
                left += childView.getMeasuredWidth();

                //计算当前行最大高度的childView
                int childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                maxHeight = Math.max(maxHeight, childHeight);
            }
            // top的计算
            top += maxHeight;
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            //抛空指针异常
        }

        //清空所有子view
        removeAllViews();

        mAdapter = null;
        mAdapter = adapter;

        //获取数量
        int childCount = mAdapter.getCount();
        for (int i = 0; i < childCount; i++) {
            //通过位置获取View
            View childView = mAdapter.getView(i, this);
            addView(childView);
        }
    }

}
