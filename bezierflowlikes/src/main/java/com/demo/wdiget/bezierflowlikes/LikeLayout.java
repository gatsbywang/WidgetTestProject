package com.demo.wdiget.bezierflowlikes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by 花歹 on 2018/1/3.
 * Email:   gatsbywang@126.com
 * Description: 点赞效果
 * Thought:
 */

public class LikeLayout extends RelativeLayout {

    private Random mRandom;

    private int[] mImageRes;

    private int mWidth;
    private int mHeight;

    //图片的宽高
    private int mDrawableWidth, mDrawableHeight;

    private Interpolator[] mInterpolators;

    public LikeLayout(Context context) {
        this(context, null);
    }

    public LikeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();
        mImageRes = new int[]{R.mipmap.pl_blue, R.mipmap.pl_red, R.mipmap.pl_yellow};

        Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.pl_blue);
        mDrawableWidth = drawable.getIntrinsicWidth();
        mDrawableHeight = drawable.getIntrinsicHeight();

        mInterpolators = new Interpolator[]{new AccelerateDecelerateInterpolator()
                , new AccelerateInterpolator(), new DecelerateInterpolator(), new LinearInterpolator()};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 添加一个点赞的view
     */
    public void addLove() {
        //添加一个ImageView在底部
        final ImageView likeIv = new ImageView(getContext());
        //给一个图片资源
        likeIv.setImageResource(mImageRes[mRandom.nextInt(mImageRes.length - 1)]);
        //添加到底部的中心
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);
        likeIv.setLayoutParams(layoutParams);
        addView(likeIv);

        //添加的效果是：有放大和透明度变化
        AnimatorSet animator = getAnimator(likeIv);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画执行完毕移除view
                removeView(likeIv);
            }
        });
        animator.start();
    }

    /**
     * 获取爱心动画效果
     *
     * @param imageView
     * @return
     */
    private AnimatorSet getAnimator(ImageView imageView) {
        AnimatorSet allAnimatorSet = new AnimatorSet();


        //添加效果：有放大和透明度
        AnimatorSet innerAnimator = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0.3f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0.3f, 1.0f);

        innerAnimator.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        innerAnimator.setDuration(350);
        innerAnimator.start();

        //运行的路径动画


        //按顺序执行
        allAnimatorSet.playSequentially(innerAnimator, getBezierAnimator(imageView));


        return allAnimatorSet;
    }

    /**
     * 运动路径动画
     *
     * @param imageView
     * @return
     */

    private Animator getBezierAnimator(final ImageView imageView) {
        //
        PointF point0 = new PointF(mWidth / 2 - mDrawableWidth / 2, mHeight - mDrawableHeight);

        //确保p2点的y值一定要大于p1点的y值
        PointF point1 = getPoint(1);
        PointF point2 = getPoint(2);
        PointF point3 = new PointF(mRandom.nextInt(mWidth - mDrawableWidth), 0);

        LikeTypeEvaluator likeTypeEvaluator = new LikeTypeEvaluator(point1, point2);

        //ofFloat 第一个参数likeTypeEvaluator，第二个参数p0,第三个参数p3
        ValueAnimator besizerAnimator = ObjectAnimator.ofObject(likeTypeEvaluator, point0, point3);
        //加一些差值器
        besizerAnimator.setInterpolator(mInterpolators[mRandom.nextInt(mInterpolators.length-1)]);
        besizerAnimator.setDuration(1000);
        besizerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
                //透明度
                float t = animation.getAnimatedFraction();
                imageView.setAlpha(1 - t + 0.2f);
            }
        });
        return besizerAnimator;
    }

    /**
     * 获取 p1 p2点，确保p2点大于p1点
     *
     * @param index
     * @return
     */
    private PointF getPoint(int index) {
        return new PointF(mRandom.nextInt(mWidth - mDrawableWidth),
                mRandom.nextInt(mHeight / 2) + (index - 1) * (mHeight / 2));
    }
}
