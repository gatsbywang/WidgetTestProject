package com.demo.widget.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by 花歹 on 2017/10/12.
 * Email:   gatsbywang@126.com
 * Description: 圆形进度条
 * Thought:
 */

public class ProgressBar extends View {

    private int mDefaultColor = Color.BLUE;
    private int mProgressColor = Color.RED;
    private int mProgressTextColor = Color.RED;
    private int mProgressTextSize = 15;//15px
    private float mRoundWidth = 10;//10px

    private Paint mDefaultPaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;

    private int mMax = 100;
    private int mProgress = 90;


    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mDefaultColor = typedArray.getColor(R.styleable.ProgressBar_progreeBar_defaultColor, mDefaultColor);
        mProgressColor = typedArray.getColor(R.styleable.ProgressBar_progressColor, mProgressColor);
        mProgressTextColor = typedArray.getColor(R.styleable.ProgressBar_progressTextColor, mProgressTextColor);

        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize, sp2px(mProgressTextSize));
        mRoundWidth = typedArray.getDimension(R.styleable.ProgressBar_roundWidth, dip2px(mRoundWidth));


        typedArray.recycle();

        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(mDefaultColor);
        mDefaultPaint.setAntiAlias(true);
        mDefaultPaint.setDither(true);
        mDefaultPaint.setStrokeWidth(mRoundWidth);
        mDefaultPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);
        mProgressPaint.setStrokeWidth(mRoundWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mProgressTextSize);
        mTextPaint.setStyle(Paint.Style.STROKE);
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dip2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //原始进度条为圆，可以画圆
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.drawCircle(centerX, centerY, getWidth() / 2 - mRoundWidth / 2, mDefaultPaint);

        //进度条为圆弧，不能画圆
        RectF rectF = new RectF(0 + mRoundWidth / 2, 0 + mRoundWidth / 2, getWidth() - mRoundWidth / 2, getHeight() - mRoundWidth / 2);
        float percent = (float) mProgress / mMax;
        canvas.drawArc(rectF, 0, percent * 360, false, mProgressPaint);


        //画文字
        String text = (int)(percent * 100) + "%";
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.width() / 2;

        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();

        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, mTextPaint);

    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            return;
        }
        this.mMax = max;

    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            return;
        }
        this.mProgress = progress;
        //刷新
        invalidate();

    }

}
