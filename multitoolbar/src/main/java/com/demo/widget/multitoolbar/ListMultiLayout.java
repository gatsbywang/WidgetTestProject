package com.demo.widget.multitoolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by 花歹 on 2017/12/18.
 * Email:   gatsbywang@126.com
 * Description: adapter 设计模式 ->不同的tab样式，不同的页面->适配
 * Thought:
 */

public class ListMultiLayout extends LinearLayout implements View.OnClickListener {

    private LinearLayout mMenuTabView;

    private FrameLayout mMenuMiddleView;

    private View mShadowView;

    //阴影的颜色
    private int mShadowColor = 0x88888888;

    private FrameLayout mMenuContainerView;
    private int mCurrentPosition = -1;

    //筛选菜单的Adapter
    private BaseMenuAdapter mAdapter;
    private int mMenuContainerHeight;
    private final long DURATIONTIME = 350;
    private boolean mAnimatorExecute;

    public ListMultiLayout(Context context) {
        this(context, null);
    }

    public ListMultiLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListMultiLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    /**
     * 1、布局实例化 （组合控件）
     */
    private void initLayout() {
        //1.1、创建头部用来存放tab
        setOrientation(VERTICAL);
        mMenuTabView = new LinearLayout(getContext());
        mMenuTabView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mMenuTabView.setOrientation(HORIZONTAL);
        addView(mMenuTabView);
        //1.2 创建FrameLayout用来存放阴影+内容（frameLayout）
        mMenuMiddleView = new FrameLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                0);
        layoutParams.weight = 1;
        mMenuMiddleView.setLayoutParams(layoutParams);

        addView(mMenuMiddleView);

        //创建阴影可以不用设置LayoutParams，默认就是MATCH_PARENT，MATCH_PARENT
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0f);
        mShadowView.setVisibility(GONE);
        mShadowView.setOnClickListener(this);
        mMenuMiddleView.addView(mShadowView);
        //创建菜单 存放菜单内容
        mMenuContainerView = new FrameLayout(getContext());
        mMenuMiddleView.addView(mMenuContainerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mMenuContainerHeight == 0) {


            int height = MeasureSpec.getSize(heightMeasureSpec);
            mMenuContainerHeight = (int) (height * 0.75f);
            ViewGroup.LayoutParams layoutParams = mMenuContainerView.getLayoutParams();
            layoutParams.height = mMenuContainerHeight;
            mMenuContainerView.setLayoutParams(layoutParams);
            //内容进来的时候不显示
            mMenuContainerView.setTranslationY(-mMenuContainerHeight);
        }
    }

    /**
     * 具体的观察者
     */
    private class AdapterMenuObserver extends MenuObserver {

        @Override
        public void closeMenu() {
            //有注册就会收到通知
            ListMultiLayout.this.closeMenu();
        }
    }

    private AdapterMenuObserver mMenuObserver;

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseMenuAdapter adapter) {
        if (adapter == null) {
            throw new RuntimeException("adapter null");
        }

        if(mMenuObserver !=null) {
            //观察者
            mAdapter.unregisterDataSetObserver(mMenuObserver);
        }


        this.mAdapter = adapter;
        //注册观察者
        mMenuObserver = new AdapterMenuObserver();
        mAdapter.registerDataSetObserver(mMenuObserver);

        //获取有多少条
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            //获取菜单的tab
            View tabView = mAdapter.getTabView(i, mMenuTabView);
            mMenuTabView.addView(tabView);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tabView.getLayoutParams();
            layoutParams.weight = 1;
            mMenuTabView.setLayoutParams(layoutParams);

            //设置点击事件
            setTabClick(tabView, i);

            //获取菜单的内容
            View menuView = mAdapter.getMenuView(i, mMenuContainerView);
            menuView.setVisibility(GONE);
            mMenuContainerView.addView(menuView);

        }
    }

    /**
     * 设置 tab 的点击事件
     *
     * @param tabView
     * @param position
     */
    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == -1) {
                    //没打开
                    //没打开就打开
                    openMenu(position, tabView);
                } else {
                    if (mCurrentPosition == position) {
                        closeMenu();
                    } else {
                        //切换一下显示
                        View currentMenu = mMenuContainerView.getChildAt(mCurrentPosition);
                        currentMenu.setVisibility(GONE);
                        mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = position;
                        currentMenu = mMenuContainerView.getChildAt(position);
                        currentMenu.setVisibility(VISIBLE);
                        mAdapter.menuOpen(mMenuTabView.getChildAt(position));
                    }

                }
            }
        });

    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        if (mAnimatorExecute) {
            return;
        }

        //关闭的时候启动动画，位移动画，透明度动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView,
                "translationY", 0, -mMenuContainerHeight);
        translationAnimator.setDuration(DURATIONTIME);
        translationAnimator.start();
        mShadowView.setVisibility(GONE);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,
                "translationY", 1f, 0);
        alphaAnimator.setDuration(DURATIONTIME);
        //要等关闭动画执行完才去隐藏当前菜单
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShadowView.setVisibility(GONE);
                //获取当前位置显示当前菜单内容，菜单是加到了菜单容器
                View menuView = mMenuContainerView.getChildAt(mCurrentPosition);
                menuView.setVisibility(GONE);
                mAnimatorExecute = false;
                mCurrentPosition = -1;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                mAdapter.menuClose(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });
        alphaAnimator.start();
    }

    /**
     * 打开菜单
     *
     * @param position
     * @param tabView
     */
    private void openMenu(final int position, final View tabView) {

        if (mAnimatorExecute) {
            return;
        }

        mShadowView.setVisibility(VISIBLE);
        //获取当前位置显示当前菜单内容，菜单是加到了菜单容器
        View menuView = mMenuContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);


        //打开的时候启动动画，位移动画，透明度动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mMenuContainerView,
                "translationY", -mMenuContainerHeight, 0);
        translationAnimator.setDuration(DURATIONTIME);
        translationAnimator.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,
                "translationY", 0, 1f);
        alphaAnimator.setDuration(DURATIONTIME);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
                mCurrentPosition = position;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                //把当前的tab传到外面
                mAdapter.menuOpen(tabView);
            }
        });
        alphaAnimator.start();

    }

    @Override
    public void onClick(View v) {
        closeMenu();
    }
}
