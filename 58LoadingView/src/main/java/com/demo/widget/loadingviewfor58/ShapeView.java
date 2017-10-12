package com.demo.widget.loadingviewfor58;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 花歹 on 2017/10/12.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class ShapeView extends View {
//    private Shape  mCurrentShape = C

    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画正方形

        //画圆形

        //画三角形
    }

    public enum shape{
        Circle,Square,Triangle

    }

}
