package com.demo.widget.beziercurve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by 花歹 on 2017/12/25.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 * 思路：1、两个圆，一个圆固定位置不动，半径变化，还有一个圆半径不变，位置跟随手指移动
 */

public class BezierView extends View {


    //两个圆的圆心
    private PointF mFixationPoint, mDragPoint;

    private int mDragRadius = 10;

    private Paint mPaint;

    private int mMaxFixactionRadius = 7;
    private int mMinFixactionRadius = 3;
    private int mFixactionRadius;
    private Bitmap mDragBitmap;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragRadius = dip2px(mDragRadius);
        mMaxFixactionRadius = dip2px(mMaxFixactionRadius);
        mMinFixactionRadius = dip2px(mMinFixactionRadius);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private int dip2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDragPoint == null || mFixationPoint == null) {
            return;
        }
        //画两个圆
        //拖拽圆
        canvas.drawCircle(mDragPoint.x, mDragPoint.y, mDragRadius, mPaint);

        //画固定圆 有个初始大小，而且他的半径随着距离的增大而减小
//        double distance = getDistance(mDragPoint, mFixationPoint);
//        mFixactionRadius = (int) (mMaxFixactionRadius - distance / 14);

        Path bezierPath = getBezierPath();
        if (bezierPath != null) {

            //小到一定层度都不画了
            canvas.drawCircle(mFixationPoint.x, mFixationPoint.y, mFixactionRadius, mPaint);

            //画贝塞尔曲线
            canvas.drawPath(bezierPath, mPaint);
        }
        //画图片,中心位置才是手指拖动位置
        if (mDragBitmap != null) {
            canvas.drawBitmap(mDragBitmap, mDragPoint.x - mDragBitmap.getWidth() / 2,
                    mDragPoint.y - mDragBitmap.getHeight() / 2, null);
        }

    }

    /**
     * 获取贝塞尔路径
     *
     * @return
     */
    private Path getBezierPath() {
        double distance = getDistance(mDragPoint, mFixationPoint);
        mFixactionRadius = (int) (mMaxFixactionRadius - distance / 14);
        if (mFixactionRadius < mMinFixactionRadius) {
            //超过一定距离 贝塞尔和固定圆也不用画了
            return null;
        }

        Path path = new Path();
        //求角a
        //求斜率
        float dx = mDragPoint.x - mFixationPoint.x;
        float dy = mDragPoint.y - mFixationPoint.y;
        float tanA = dy / dx;
        //求角a
        double arcA = Math.atan(tanA);

        //p0
        float p0x = (float) (mFixationPoint.x + mFixactionRadius * Math.sin(arcA));
        float p0Y = (float) (mFixationPoint.y - mFixactionRadius * Math.cos(arcA));

        //p1
        float p1x = (float) (mDragPoint.x + mDragRadius * Math.sin(arcA));
        float p1Y = (float) (mDragPoint.y - mDragRadius * Math.cos(arcA));

        //p2
        float p2x = (float) (mDragPoint.x - mDragRadius * Math.sin(arcA));
        float p2Y = (float) (mDragPoint.y + mDragRadius * Math.cos(arcA));

        //p3
        float p3x = (float) (mFixationPoint.x - mFixactionRadius * Math.sin(arcA));
        float p3Y = (float) (mFixationPoint.y + mFixactionRadius * Math.cos(arcA));

        //拼装 贝塞尔曲线
        //开始画第一条开始点
        path.moveTo(p0x, p0Y);
        //第一条曲线的控制点
        PointF controlPoint = getControlPoint();
        //画贝塞尔曲线 p1为结束点
        path.quadTo(controlPoint.x, controlPoint.y, p1x, p1Y);

        //画第二条曲线
        path.lineTo(p2x, p2Y);
        path.quadTo(controlPoint.x, controlPoint.y, p3x, p3Y);
        path.close();


        return path;
    }

    private PointF getControlPoint() {

        return new PointF((mDragPoint.x + mFixationPoint.x) / 2,
                (mDragPoint.y + mFixationPoint.y) / 2);
    }

    /**
     * 获取两个圆之间的距离
     *
     * @param point1
     * @param point2
     * @return
     */
    private double getDistance(PointF point1, PointF point2) {
        return Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                //手指按下指定当前的位置
//                float downX = event.getX();
//                float downY = event.getY();
//                initPoint(downX, downY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = event.getX();
//                float moveY = event.getY();
//                updateDragPoint(moveX, moveY);
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        invalidate();
//        return true;
//    }

    public void updateDragPoint(float moveX, float moveY) {
        mDragPoint.x = moveX;
        mDragPoint.y = moveY;
        invalidate();
    }

    /**
     * 初始化位置
     *
     * @param downX
     * @param downY
     */
    public void initPoint(float downX, float downY) {

        mFixationPoint = new PointF(downX, downY);
        mDragPoint = new PointF(downX, downY);
    }

    /**
     * 绑定可以拖拽的控件
     *
     * @param view
     * @param listener
     */
    public static void attach(View view, BubbleTouchListener.DisappearListener listener) {
        view.setOnTouchListener(new BubbleTouchListener(view, view.getContext(), listener));
    }

    public void setDragBitmap(Bitmap dragBitmap) {
        this.mDragBitmap = dragBitmap;
    }

    /**
     * 处理手指松开
     */
    public void handleActionUp() {
        if (mFixactionRadius > mMinFixactionRadius) {
            //回弹,从0变化到1
            ValueAnimator animator = ObjectAnimator.ofFloat(1);

            animator.setDuration(350);
            final PointF start = new PointF(mDragPoint.x, mDragPoint.y);
            final PointF end = new PointF(mFixationPoint.x, mFixationPoint.y);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    PointF pointF = BezierViewUtil.getPointByPercent(start, end, percent);
                    //更新拖拽点
                    updateDragPoint(pointF.x, pointF.y);

                }
            });
            //设置个差值器
            animator.setInterpolator(new OvershootInterpolator(3f));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mListener != null) {
                        mListener.restore();
                    }
                }
            });
            animator.start();
        } else {
            //爆炸
            if (mListener != null) {
                mListener.dismiss(mDragPoint);
            }
        }

    }


    public BubbleListener mListener;

    public void setBubbleListener(BubbleListener listener) {
        this.mListener = listener;
    }

    public interface BubbleListener {
        //还原
        public void restore();

        //消失
        public void dismiss(PointF pointF);

    }


}
