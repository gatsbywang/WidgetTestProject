package com.demo.widget.lettersidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 花歹 on 2017/10/17.
 * Email:   gatsbywang@126.com
 * Description: 字母表索引控件
 * Thought:
 */

public class LetterSideBar extends View {
    private Paint mPaint;

    private static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "S", "Y", "Z", "#"};
    //当前触摸的位置
    private String mCurrentTouchLetter;

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //自定义属性，颜色，字体大小
        mPaint.setTextSize(sp2px(12));//设置的是px
        //
        mPaint.setColor(Color.BLUE);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算指定宽度 =左右的pading+字母的宽度(取决于你的画笔)
        int textWidth = (int) mPaint.measureText("A");

        int width = getPaddingLeft() + getPaddingRight() + textWidth;

        //高度可以直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算出当前触摸字母
                float currentMoveY = event.getY();
                //当前触摸的高度/字母高度，通过位置获取字母
                //优化 当前选中字母相同时，不需要绘制
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                int currentPosition = (int) (currentMoveY / itemHeight);
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                if (currentPosition > mLetters.length - 1) {
                    currentPosition = mLetters.length - 1;
                }
                mCurrentTouchLetter = mLetters[currentPosition];
                if (mlistener != null) {
                    mlistener.touch(mCurrentTouchLetter);
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(mlistener!=null){
                    mlistener.leave();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画26个字母
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int dy = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);

        for (int i = 0; i < mLetters.length; i++) {
            //基线计算方式
            //1、字母高度的一半
            //2、前面所有字符的总高度

            int letterCenterY = i * itemHeight + itemHeight / 2 + getPaddingTop();
            int baseLine = letterCenterY + dy;
            float textWidth = mPaint.measureText(mLetters[i]);
            //x应该为：宽度/2-文字的宽度/2
            int x = (int) (getWidth() / 2 - textWidth / 2);

            //如果是当前字幕，要高亮
            //最好采用两个画笔
            if (mLetters[i].equals(mCurrentTouchLetter)) {
                mPaint.setColor(Color.RED);
                canvas.drawText(mLetters[i], x, baseLine, mPaint);
            } else {
                mPaint.setColor(Color.BLUE);
                canvas.drawText(mLetters[i], x, baseLine, mPaint);
            }
        }
    }


    private LetterTouchListener mlistener;

    public void setLetterTouchListener(LetterTouchListener letterTouchListener) {
        this.mlistener = letterTouchListener;
    }

    public interface LetterTouchListener {
        void touch(CharSequence letter);

        void leave();
    }

}
