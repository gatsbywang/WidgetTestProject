package com.demo.wiget.kugouslidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

/**
 * Created by 花歹 on 2017/11/7.
 * Email:   gatsbywang@126.com
 * Description:
 * 分析方案：
 * 1、继承HorizontalScrollView，写好两个布局（menu，content）
 * 2、menu、content宽度不对，需指定内容，菜单的宽度
 * 3、利用代码解决滚动到对应位置判断是打开还是关闭状态
 * 4、处理快速滑动
 * 5、处理内容部分缩放，菜单部分位移和透明度
 * 6、处理好点击事件分发
 * Thought:
 */

public class SlidingMenu extends HorizontalScrollView {
    private int mMenuWidth;

    private View mContentView;
    private View mMenuView;

    //快速滑动处理 GestureDetecor手势处理类
    private final GestureDetector mGesureDetector;

    //是否快速滑动打开
    private boolean mMenuIsOpen = false;

    //代表是否拦截
    private boolean mIsIntercept = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        float rightMargin = typedArray.getDimension(R.styleable.SlidingMenu_menuRightMargin, dp2px(50));
        mMenuWidth = (int) (getScreenWidth() - rightMargin);
        typedArray.recycle();

        mGesureDetector = new GestureDetector(context, onGestureListener);
    }


    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //只关注快速滑动，只要快速滑动就会回调

            //velocityX 快速往左边滑动的时候是一个负数，往右边滑动的时候是一个正数
            if (mMenuIsOpen) {
                //打开的时候往左边快速滑动关闭
                if (velocityX < 0) {
                    closeMenu();
                    return true;
                }
            } else {
                //关闭的时候往右边快速滑动打开
                if (velocityX > 0) {
                    openMenu();
                    return true;
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };


    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //1、宽度不对，指定宽度

    @Override
    protected void onFinishInflate() {
        //这个方法是布局解析完毕，也就是XML布局解析完毕
        super.onFinishInflate();
        //指定宽高 1、菜单页的宽度是屏幕的宽带 - 右边一小部分距离（自定义属性）
        ViewGroup container = (ViewGroup) getChildAt(0);
        int childCount = container.getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("只能放置两个子view");
        }
        mMenuView = container.getChildAt(0);
        ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParams);

        //2、内容页宽度是屏幕的宽度
        mContentView = container.getChildAt(1);
        ViewGroup.LayoutParams contentViewParams = mContentView.getLayoutParams();
        contentViewParams.width = getScreenWidth();
        mContentView.setLayoutParams(contentViewParams);

        //2、初始化进来是关闭的,这里调用没用
        //scrollTo(mMenuWidth, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //2、初始化进来是关闭的
        scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        mIsIntercept = false;
        //当菜单打开的时候，点击右边关闭
        if (mMenuIsOpen) {
            float currenX = ev.getX();
            if (currenX > mMenuWidth) {
                //1.关闭菜单
                closeMenu();
                //2.子view不需要响应任何事件，拦截子view的事件
                mIsIntercept = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    //3、手指抬起是二选一，要么关闭要么打开
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //如果拦截不要执行自己的onTouch
        if (mIsIntercept) {
            return true;
        }

        //快速滑动
        if (mGesureDetector.onTouchEvent(ev)) {
            //这里返回true代表不执行下面的代码
            return true;

        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int currentScrollX = getScrollX();
                if (currentScrollX > mMenuWidth / 2) {
                    //关闭
                    closeMenu();
                } else {
                    //打开
                    openMenu();
                }
                //确保super.onTouchEvent不会执行
                return true;
        }
        return super.onTouchEvent(ev);
    }

    //4、处理右边的缩放，左边的缩放和透明度


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //算一个梯度值
        float scale = 1f * l / mMenuWidth;  //scale变化1-0
        //右边的缩放 最小0.7f 最大是1f
        float rightScale = 0.7f + 0.3f * scale;
        //设置右边的缩放,默认以中心点缩放
        //设置中心点位置
        ViewCompat.setPivotX(mContentView, 0);
        ViewCompat.setPivotY(mContentView, mContentView.getMeasuredHeight() / 2);
        ViewCompat.setScaleX(mContentView, rightScale);
        ViewCompat.setScaleY(mContentView, rightScale);

        //  设置菜单的缩放和透明度
        //  透明是半透明<->完全透明 0.7f-1.0f
        float alpha = 0.7f + (1 - scale) * 0.3f; //0.7f -1.0f
        ViewCompat.setAlpha(mMenuView, alpha);

        //  缩放0.7f - 1.0f
        float leftScale = 0.7f + (1 - scale) * 0.3f; //0.7f -1.0f
        ViewCompat.setScaleX(mMenuView, leftScale);
        ViewCompat.setScaleY(mMenuView, leftScale);

        //推出按钮刚开始在右边，设置平移，正数向右平移，负数向左平移
        ViewCompat.setTranslationX(mMenuView, 0.25f * l);


    }

    /**
     * 打开菜单，滚动到0的位置
     */
    private void openMenu() {
        smoothScrollTo(0, 0);
        mMenuIsOpen = true;
    }

    /**
     * 关闭菜单，滚动到 mMenuWidth的位置
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mMenuIsOpen = false;

    }
}
