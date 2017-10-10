package com.demp.widget.arc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 花歹 on 2017/9/25.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class QQStepView extends View {
    private int mDefaultColor = Color.BLUE;
    private int mStepColor = Color.RED;

    private float mBordeWidth = 20; //20px
    private int mStepTextSize;
    private int mStepTextColor;
    private Paint mDefaultArcPaint, mStepArcPaint, mTextPaint;

    private int mCurrentStep, mMaxStep;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //1、分析效果

        //2、确定自定义属性，编写attrs.xml

        //3、在布局中使用

        //4、在自定义view中获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mDefaultColor = array.getColor(R.styleable.QQStepView_defaultColor, mDefaultColor);
        mStepColor = array.getColor(R.styleable.QQStepView_stepColor, mStepColor);
        mBordeWidth = array.getDimension(R.styleable.QQStepView_borderWidth, mBordeWidth);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor);
        array.recycle();

        mDefaultArcPaint = new Paint();
        mDefaultArcPaint.setAntiAlias(true);
        mDefaultArcPaint.setColor(mDefaultColor);
        mDefaultArcPaint.setStrokeWidth(mBordeWidth);
        mDefaultArcPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆弧头尾圆滑
        mDefaultArcPaint.setStyle(Paint.Style.STROKE);  //设置空心


        mStepArcPaint = new Paint();
        mStepArcPaint.setAntiAlias(true);
        mStepArcPaint.setColor(mStepColor);
        mStepArcPaint.setStrokeWidth(mBordeWidth);
        mStepArcPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆弧头尾圆滑
        mStepArcPaint.setStyle(Paint.Style.STROKE);    //设置空心

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);

        mCurrentStep = 0;
        mMaxStep = 0;
        //5、onMeasure()

        //6、画外圆弧，内圆弧，还有文字
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //布局文件中出现宽度高度不一致，wrap_content
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //6.1 画非步数圆弧

        //边缘没有显示完整,注意设置RectF区域
        int center = getWidth() / 2;
        int radius = (int) (getWidth() / 2 - mBordeWidth / 2);
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectF, 135, 270, false, mDefaultArcPaint);


        //6.2 画步数圆弧
        //角度不能写死
        if (mMaxStep == 0) {
            return;
        }
        float sweepAngle = mCurrentStep*1.0f / mMaxStep;
        canvas.drawArc(rectF, 135, sweepAngle * 270, false, mStepArcPaint);

        //6.3 画文字
        String stepText = String.valueOf(mCurrentStep);
        //获取文字的宽度
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        //获取文字的基线
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(stepText, dx, baseLine, mTextPaint);

    }

    /**
     * @param maxStep
     */
    public synchronized void setMaxStep(int maxStep) {
        this.mMaxStep = maxStep;
    }

    /**
     * @param currentStep
     */
    public synchronized void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        //不断绘制
        invalidate();
    }

}
