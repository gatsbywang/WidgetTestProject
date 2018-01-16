package com.demo.widget.yahooloadingview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by 花歹 on 2018/1/15.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class LoadingView extends View {

    //旋转动画执行时间
    private long ROTATION_ANIMATION_TIME = 10000;

    private float mCurrentRotationAngle;

    private int[] mCircleColors;

    //大圆的半径
    private float mRotationRadius;

    //当前大圆半径
    private float mCurrentRotationRadius;

    //小圆半径
    private float mCircleRadius;

    private Paint mPaint;

    //当前空心圆的半径
    private float mHoleRadius;

    private float mDiagonalDist;

    //中心点
    private int mCenterX, mCenterY;

    private int mSplashColor = Color.WHITE;

    //代表当前状态所画动画
    private LoadingState mLoadingState;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCircleColors = getContext().getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mInitParams) {
            initParams();
        }

        if (mLoadingState == null) {
            mLoadingState = new RotationState();
        }
        mLoadingState.draw(canvas);
        //旋转动画
//        drawRotationAnimator(canvas);
    }

    private boolean mInitParams = false;

    /**
     * 初始化参数
     */
    private void initParams() {
        mRotationRadius = getMeasuredWidth() / 4;
        mCircleRadius = getMeasuredWidth() / 8;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);

        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
        mInitParams = true;

        mDiagonalDist = (float) Math.sqrt(mCenterX * mCenterX + mCenterY * mCenterY);
    }


//    private void drawRotationAnimator(Canvas canvas) {
//        //弄一个变量不断去改变 属性动画 旋转0-360
//        // 2π=360度
//        if (mAnimator == null) {
//            mAnimator = ObjectAnimator.ofFloat(0f, 2 * (float) Math.PI);
//            mAnimator.setDuration(ROTATION_ANIMATION_TIME);
//
//            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    //拿值
//                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
//                    //重新绘制
//                    invalidate();
//                }
//            });
//            //线性差值器
//            mAnimator.setInterpolator(new LinearInterpolator());
//            //不断反复
//            mAnimator.setRepeatCount(-1);
//            mAnimator.start();
//        }
//
//        //
//        canvas.drawColor(mSplashColor);
//
//        //画六个圆 每一份角度
//        double percentAngle = Math.PI * 2 / mCircleColors.length;
//        for (int i = 0; i < mCircleColors.length; i++) {
//
//            mPaint.setColor(mCircleColors[i]);
//            //当前角度 = 初始角度+旋转的角度
//            double currentAngle = percentAngle * i + mCurrentRotationAngle;
//            float cx = (float) (mCenterX + mRotationRadius * Math.cos(currentAngle));
//            float cy = (float) (mCenterY + mRotationRadius * Math.sin(currentAngle));
//            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
//        }
//
//    }

    public void disapper() {

        //关闭旋转动画
        if (mLoadingState instanceof RotationState) {
            RotationState rotationState = (RotationState) mLoadingState;
            rotationState.cancel();
        }

        //开始聚合动画
        mLoadingState = new MergeState();


    }

    public abstract class LoadingState {
        public abstract void draw(Canvas canvas);
    }

    /**
     * 旋转动画
     */
    public class RotationState extends LoadingState {

        private ValueAnimator mAnimator;

        public RotationState() {
            //弄一个变量不断去改变 属性动画 旋转0-360
            // 2π=360度
            if (mAnimator == null) {
                mAnimator = ObjectAnimator.ofFloat(0f, 2 * (float) Math.PI);
                mAnimator.setDuration(ROTATION_ANIMATION_TIME);

                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //拿值
                        mCurrentRotationAngle = (float) animation.getAnimatedValue();
                        //重新绘制
                        invalidate();
                    }
                });
                //线性差值器
                mAnimator.setInterpolator(new LinearInterpolator());
                //不断反复
                mAnimator.setRepeatCount(-1);
                mAnimator.start();
            }

        }

        @Override
        public void draw(Canvas canvas) {
            //背景
            canvas.drawColor(mSplashColor);

            //画六个圆 每一份角度
            double percentAngle = Math.PI * 2 / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {

                mPaint.setColor(mCircleColors[i]);
                //当前角度 = 初始角度+旋转的角度
                double currentAngle = percentAngle * i + mCurrentRotationAngle;
                float cx = (float) (mCenterX + mRotationRadius * Math.cos(currentAngle));
                float cy = (float) (mCenterY + mRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }
        }

        public void cancel() {
            mAnimator.cancel();
        }
    }

    /**
     * 聚合动画
     */
    public class MergeState extends LoadingState {

        private final ValueAnimator mAnimatior;

        public MergeState() {
            mAnimatior = ObjectAnimator.ofFloat(mRotationRadius, 0);
            mAnimatior.setDuration(ROTATION_ANIMATION_TIME);
            mAnimatior.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    mCurrentRotationAngle = (float) animation.getAnimatedValue();
                    //重新绘制
                    invalidate();

                }
            });

            mAnimatior.setInterpolator(new AnticipateInterpolator(3f));
            //等聚合完毕画展开
            mAnimatior.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new ExpendState();
                }
            });
            mAnimatior.start();
        }

        @Override
        public void draw(Canvas canvas) {
            //开始写聚合动画

            //背景
            canvas.drawColor(mSplashColor);
            //画六个圆 每一份角度
            double percentAngle = Math.PI * 2 / mCircleColors.length;
            for (int i = 0; i < mCircleColors.length; i++) {

                mPaint.setColor(mCircleColors[i]);
                //当前角度 = 初始角度+旋转的角度
                double currentAngle = percentAngle * i + mCurrentRotationAngle;
                float cx = (float) (mCenterX + mCurrentRotationRadius * Math.cos(currentAngle));
                float cy = (float) (mCenterY + mCurrentRotationRadius * Math.sin(currentAngle));
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
            }

        }
    }

    /**
     * 展开动画
     */
    public class ExpendState extends LoadingState {
        private final ValueAnimator mAnimator;

        public ExpendState() {
            mAnimator = ObjectAnimator.ofFloat(0, mDiagonalDist);
            mAnimator.setDuration(ROTATION_ANIMATION_TIME);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });

            mAnimator.start();

        }

        @Override
        public void draw(Canvas canvas) {
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(mSplashColor);
            mPaint.setStyle(Paint.Style.STROKE);

            //这个圆的半径应该是画笔的宽度一半+空心圆的半径才是 真正画的圆的半径
            float radius = strokeWidth/2 + mHoleRadius;
            //绘制一个圆
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaint);
        }
    }


}
