package com.demo.widget.flowerlivestreamingloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by 花歹 on 2017/12/21.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class LoadingView extends RelativeLayout {

    private CircleView mLeft, mMiddle, mRight;

    private int mTranslationXDistance;

    private final long ANIMATON_TIME = 350;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLeft = getCircleView(context);
        mLeft.exchangeColor(Color.BLUE);
        mMiddle = getCircleView(context);
        mMiddle.exchangeColor(Color.RED);
        mRight = getCircleView(context);
        mRight.exchangeColor(Color.GREEN);
        addView(mLeft);
        addView(mRight);
        addView(mMiddle);
        mTranslationXDistance = dip2Px(20);
        post(new Runnable() {
            @Override
            public void run() {
                //让布局实例化还之后再去开启动画
                expendAnimator();
            }
        });
    }

    /**
     * 开启动画
     */
    private void expendAnimator() {
        //左边跑
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeft, "translationX",
                0, -mTranslationXDistance);
        //右边跑
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRight, "translationX",
                0, mTranslationXDistance);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATON_TIME);
        set.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //往里面跑
                shrinkAnimator();
            }
        });
        set.start();
    }

    private void shrinkAnimator() {
        //左边跑
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(mLeft, "translationX",
                -mTranslationXDistance, 0);
        //右边跑
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(mRight, "translationX",
                mTranslationXDistance, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATON_TIME);
        set.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //往外面跑
                //切换颜色，左边的给中间，中间给右边，右边的给左边
                int leftColor = mLeft.getColor();
                int rightColor = mRight.getColor();
                int middelColor = mMiddle.getColor();

                mLeft.exchangeColor(rightColor);
                mMiddle.exchangeColor(leftColor);
                mRight.exchangeColor(middelColor);

                expendAnimator();
            }
        });
        set.start();
    }

    private CircleView getCircleView(Context context) {

        CircleView circleView = new CircleView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dip2Px(10), dip2Px(10));
        layoutParams.addRule(CENTER_IN_PARENT);
        circleView.setLayoutParams(layoutParams);
        return circleView;
    }

    private int dip2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
