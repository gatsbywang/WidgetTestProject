package com.demo.widget.textcolorchange;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 花歹 on 2017/10/10.
 * Email:   gatsbywang@126.com
 * Description: 思路，1、一个文字两种颜色。2、能够从左往右，从右往左。3、整合到ViewPager
 * Thought:
 */

public class ColorTrackTextView extends TextView {
    //实现一个文字两种颜色，绘制默认色的画笔
    private Paint mDefaultPaint;

    //实现一个文字两种颜色，绘制变色的画笔
    private Paint mChangePaint;

    //当前进度
    private float mCurrentProgress = 0f;

    //2、实现不同朝向
    private Direction mDirection = Direction.LEFT_TO_RIGHT;


    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int defaultColor = array.getColor(R.styleable.ColorTrackTextView_defaultColor, getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());

        mDefaultPaint = getPaintByColor(defaultColor);
        mChangePaint = getPaintByColor(changeColor);

        array.recycle();
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        //设置字体的大小，就是TextView的字体大小
        paint.setTextSize(getTextSize());
        return paint;
    }


    //1、利用canvas.clipRect裁剪 实现左边用一种颜色的画笔，右边用一种颜色的画笔
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        int middle = (int) (mCurrentProgress * getWidth());

        if (mDirection == Direction.LEFT_TO_RIGHT) {

            //从左到右逐渐变色，那么变色位置应该从0开始到middle，左边是红色，右边是黑色
            //绘制变色
            //从左0位置开始到middle位置
            drawText(canvas, mChangePaint, 0, middle);

            //绘制原始色
            //从middle位置开始到getWidth位置
            drawText(canvas, mDefaultPaint, middle, getWidth());
        } else {
            //从右到左逐渐变色，那么变色位置应该从getwidth-middle开始到getwidth位置，左边是黑色，右边是红色
            //变色位置为从getwidth-middle开始到getwidth位置
            drawText(canvas, mChangePaint, getWidth() - middle, getWidth());

            //原始色位置从0到middle位置
            drawText(canvas, mDefaultPaint, 0, getWidth() - middle);

        }
    }


    /**
     * 绘制Text
     *
     * @param canvas
     * @param paint
     * @param start
     * @param end
     */
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        //保存画布
        canvas.save();
        //绘制不变色的
        Rect rect = new Rect(start, 0, end, getHeight());
        //利用裁剪实现两种颜色
        canvas.clipRect(rect);

        String text = getText().toString();
        //获取文字宽度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        //获取文字开始绘制的位置（这种为整体文字在中间）
        int x = getWidth() / 2 - bounds.width() / 2;

        //获取基线位置
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseline = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseline, paint);
        //释放画布
        canvas.restore();
    }

    /**
     * 设置朝向，从左到右，还是从右到左
     *
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    /**
     * 设置比例值
     *
     * @param currentProgress
     */
    public void setCurrentProgress(float currentProgress) {
        this.mCurrentProgress = currentProgress;

        invalidate();
    }

    /**
     * 设置变色
     *
     * @param changeColor
     */
    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }


    /**
     * 设置原始色
     *
     * @param defaultColor
     */
    public void setDefaultColor(int defaultColor) {
        this.mDefaultPaint.setColor(defaultColor);
    }


}
