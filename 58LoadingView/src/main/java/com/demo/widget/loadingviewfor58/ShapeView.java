package com.demo.widget.loadingviewfor58;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import static com.demo.widget.loadingviewfor58.ShapeView.Shape.Circle;

/**
 * Created by 花歹 on 2017/10/12.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class ShapeView extends View {
    private Shape mCurrentShape = Circle;
    Paint mPaint;
    private Path mPath;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurrentShape) {
            case Square:
                //画正方形
                mPaint.setColor(ContextCompat.getColor(getContext(),R.color.rect));
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;

            case Circle:
                //画圆形
                int center = getWidth() / 2;
                mPaint.setColor(ContextCompat.getColor(getContext(),R.color.circle));
                canvas.drawCircle(center, center, center, mPaint);
                break;

            case Triangle:
                //画等边三角形 Path 画路线
                mPaint.setColor(ContextCompat.getColor(getContext(),R.color.triangle));
                if (mPath == null) {
                    //画路径
                    mPath = new Path();
                    mPath.moveTo(getWidth() / 2, 0);
                    mPath.lineTo(0, (float) (getWidth()*Math.sin(Math.PI*60/180)));
                    mPath.lineTo(getWidth(), (float) (getWidth()*Math.sin(Math.PI*60/180)));
//                    mPath.lineTo(getWidth() / 2, 0);
                    //路径闭环
                    mPath.close();
                }
                canvas.drawPath(mPath, mPaint);
                break;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;

            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }
        postInvalidate();
    }

    public Shape getCurrentShape() {
        return mCurrentShape;
    }

    public enum Shape {
        Circle, Square, Triangle

    }

}
