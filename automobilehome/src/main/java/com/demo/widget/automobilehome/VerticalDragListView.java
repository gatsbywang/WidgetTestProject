package com.demo.widget.automobilehome;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * Created by 花歹 on 2017/11/14.
 * Email:   gatsbywang@126.com
 * Description:
 * 1、后面不能拖动
 * 2、列表只能垂直推动
 * 3、拖动的范围只能是后面菜单View的高度
 * 4、手指松开只能是打开或者关闭
 * Thought:
 */

public class VerticalDragListView extends FrameLayout {

    private ViewDragHelper mDragHelper;

    private View mDragListView;

    //后面菜单的高度
    private int mMenuHeight;

    //菜单是否打开
    private boolean mMenuIsOpen;

    public VerticalDragListView(Context context) {
        this(context, null);
    }

    public VerticalDragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, mDragHelpCallback);
    }

    //1、拖动子view
    private ViewDragHelper.Callback mDragHelpCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //指定该子view是否可以拖动
            //制定特定的view进行拖动
            return mDragListView == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            //拖动范围只能是后面菜单View的高度
            if (top <= 0) {
                top = 0;
            }

            if (top >= mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            if (releasedChild == mDragListView) {
                if (mDragListView.getTop() > mMenuHeight / 2) {
                    //滚动菜单高度
                    mDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    mMenuIsOpen = true;
                } else {
                    //滚动0位置
                    mDragHelper.settleCapturedViewAt(0, 0);
                    mMenuIsOpen = false;
                }

                invalidate();
            }
        }
    };


    //现象，1、listView可以滑动，抽屉式效果没了
    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //菜单打开要全部拦截
        if (mMenuIsOpen) {
            return true;
        }

        //向下滑动拦截，不要给listView处理
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if (moveY - mDownY > 0&& !canChildScrollUp()) {
                    //向下滑动&滚动到了顶部，拦截不让ListView处理
                    return true;
                }
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 是否滚动到了顶部
     * 这段代码是swipeRefreshLayout中取出来的
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragListView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragListView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mDragListView, -1) || mDragListView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mDragListView, -1);
        }
    }

    //响应滚动
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("VerticalDragListVeiw 只能包含2个子布局");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            View menuView = getChildAt(0);
            mMenuHeight = menuView.getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
