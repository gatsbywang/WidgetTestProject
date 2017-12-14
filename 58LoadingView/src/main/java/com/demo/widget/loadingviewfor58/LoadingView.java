package com.demo.widget.loadingviewfor58;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by 花歹 on 2017/12/13.
 * Email:   gatsbywang@126.com
 * Description: 58加载动画 动画1、位移动画，形状的View上抛和下落，2、缩放动画，中间的阴影缩小和放大
 * Thought:
 */

public class LoadingView extends LinearLayout {
    private ShapeView mShapeView;  //形状
    private View mShadowView;   //中间的阴影

    private int mTranslationDistance = 0;
    private final long ANIMATOR_DURATION = 350;

    private boolean mIsStopAnimator = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    /**
     * 初始化加载布局
     */
    private void initLayout() {
        //1、加载写好的 ui_loading_view
        //1.1实例化view
        //1.2 添加到该view
        inflate(getContext(), R.layout.ui_loading_view, this);

        mShapeView = findViewById(R.id.shape_view);
        mShadowView = findViewById(R.id.shadow_view);
        post(new Runnable() {
            @Override
            public void run() {
                startFallAnimator();
            }
        });

        //在onCreate方法中执行，布局文件解析之后执行
    }

    //下落动画
    private void startFallAnimator() {
        if (mIsStopAnimator) {
            return;
        }
        //属性动画
        //下落位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, dip2px(80));
//        translationAnimator.setDuration(ANIMATOR_DURATION);
        //中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1f, 0.3f);
//        scaleAnimator.setDuration(ANIMATOR_DURATION);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        //一起执行
        animatorSet.playTogether(translationAnimator, scaleAnimator);
        //按顺序执行
//        animatorSet.playSequentially(translationAnimator,scaleAnimator);


        //下落完之后开始上抛
        // 是一种思想
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //改变形状
                mShapeView.exchange();
                //下落完之后开始上抛
                startUpAnimator();
                //开始旋转


            }
        });
        animatorSet.start();
    }

    /**
     * 上抛的时候旋转
     */
    private void startRotationAnimator() {
        if (mIsStopAnimator) {
            return;
        }
        ObjectAnimator rotationAnimator = null;
        switch (mShapeView.getCurrentShape()) {

            case Circle:
                break;
            case Triangle:
                //120
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 120);
                break;
            case Square:
                //180
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
        }
        if (rotationAnimator == null) {
            return;
        }
        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.start();
    }

    /**
     * 开始执行上抛动画
     */
    private void startUpAnimator() {
        if (mIsStopAnimator) {
            return;
        }
        //属性动画
        //下落位移动画
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", dip2px(80), 0);
//        translationAnimator.setDuration(ANIMATOR_DURATION);
        //中间阴影缩小
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1);
//        scaleAnimator.setDuration(ANIMATOR_DURATION);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_DURATION);
        //一起执行
        animatorSet.playTogether(translationAnimator, scaleAnimator);
        //按顺序执行
//        animatorSet.playSequentially(translationAnimator,scaleAnimator);
        animatorSet.setInterpolator(new DecelerateInterpolator());

        //上抛完之后开始下落
        // 是一种思想
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //上抛完之后开始下落
                startFallAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                //开始旋转
                startRotationAnimator();
            }
        });
        animatorSet.start();

    }

    private float dip2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE);//不要再去摆放和计算

        //清理动画
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();

        //把loadingView从父布局移除
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);//从父布局移除
            removeAllViews(); //移除自己所有的View
        }

        mIsStopAnimator = true;

    }
}
