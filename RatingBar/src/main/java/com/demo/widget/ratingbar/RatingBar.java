package com.demo.widget.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 花歹 on 2017/10/16.
 * Email:   gatsbywang@126.com
 * Description:评分控件，建议平时使用的时候，直接用系统的RatingBar即可
 * Thought:
 * 1、先绘制5张没有选中的图片
 * 2、指定控件的宽高
 *
 * //最后记得优化
 */

public class RatingBar extends View {
    private Bitmap mDefaultBitmap;
    private Bitmap mSelectedBitmap;
    private int mGradeNumber = 5;

    private int mCurrentGrade = 0;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        //获取未选中bitmap
        int defaultResId = array.getResourceId(R.styleable.RatingBar_defaulRes, 0);
        if (defaultResId == 0) {
            throw new RuntimeException("请设置属性：RatingBar_defaulRes");
        }
        mDefaultBitmap = BitmapFactory.decodeResource(getResources(), defaultResId);

        //获取已选中bitmap
        int selectResId = array.getResourceId(R.styleable.RatingBar_selectedRes, 0);
        if (selectResId == 0) {
            throw new RuntimeException("请设置属性：RatingBar_selectedRes");
        }
        mSelectedBitmap = BitmapFactory.decodeResource(getResources(), selectResId);
        //获取总数量
        mGradeNumber = array.getInt(R.styleable.RatingBar_gradeNumber, mGradeNumber);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mDefaultBitmap.getHeight();
        int width = mDefaultBitmap.getWidth() * mGradeNumber;//还需要加上星星的间隔
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (int i = 0; i < mGradeNumber; i++) {
            int x = i * mDefaultBitmap.getWidth();

            //mCurrentGrade为1的时候，i=0的时候才画
            if (mCurrentGrade>i){
                canvas.drawBitmap(mSelectedBitmap, x, 0, null);
            }else{
                canvas.drawBitmap(mDefaultBitmap, x, 0, null);
            }

//            canvas.drawBitmap(mDefaultBitmap, x, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
            case MotionEvent.ACTION_MOVE://移动

                //event.getRawX()获取屏幕的x位置
                //event.getX()获取相对当前控件的位置
                float moveX = event.getX();
                int currentGrade = (int) (moveX / mDefaultBitmap.getWidth() + 1);
                //范围问题
                if (currentGrade < 0) {
                    currentGrade = 0;
                }
                if (currentGrade > mGradeNumber) {
                    currentGrade = mGradeNumber;
                }
                //优化1、分数相同的时候，不需要去刷新
                if(currentGrade!=mGradeNumber){
                    mCurrentGrade = currentGrade;
                    //再去刷新显示
                    invalidate();//ondraw()  尽量减少ondraw的调用
                }
            case MotionEvent.ACTION_UP://抬起
        }
        return true;//onTouch
    }
}
