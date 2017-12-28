package com.demo.widget.beziercurve;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by 花歹 on 2017/12/27.
 * Email:   gatsbywang@126.com
 * Description: 监听当前View 的触摸事件
 * Thought:
 */

public class BubbleTouchListener implements View.OnTouchListener, BezierView.BubbleListener {

    private final DisappearListener mDisapperListener;
    //原来需要拖动爆炸的View
    private View mStaticView;

    private WindowManager mWindowManager;

    private BezierView mBezierView;

    private WindowManager.LayoutParams mParams;

    private FrameLayout mBombFrame;
    private ImageView mBombImage;

    public BubbleTouchListener(View view, Context context, DisappearListener listener) {
        this.mStaticView = view;

        this.mDisapperListener=listener;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mBezierView = new BezierView(context);
        mBezierView.setBubbleListener(this);

        mParams = new WindowManager.LayoutParams();

        mParams.format = PixelFormat.TRANSPARENT;

        mBombFrame = new FrameLayout(context);
        mBombImage = new ImageView(context);
        mBombImage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mBombFrame.addView(mBombImage);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //首先把自己隐藏
                mStaticView.setVisibility(View.INVISIBLE);
                //要在WindowManager上面搞个View ，
                mWindowManager.addView(mBezierView, mParams);
                //初始化贝塞尔View的点 RawX,RawY相对于屏幕宽高，但是windowmanager的高度不包含状态栏，所以
                //高度为RawY-状态栏高度
                //保证固定圆的中心在view的中心
                int[] locaiont = new int[2];
                mStaticView.getLocationOnScreen(locaiont);
                mBezierView.initPoint(locaiont[0] + mStaticView.getWidth() / 2,
                        locaiont[1] + mStaticView.getHeight() / 2);

                mBezierView.setDragBitmap(getBitmapByView(mStaticView));
                break;
            case MotionEvent.ACTION_MOVE:
                mBezierView.updateDragPoint(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                mBezierView.handleActionUp();
                break;
        }

        return true;
    }

    private Bitmap getBitmapByView(View view) {
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Override
    public void restore() {
        //把拖拽的view移除
        mWindowManager.removeView(mBezierView);
        //把原来的view显示
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss(PointF pointF) {
        mWindowManager.removeView(mBezierView);

        //要在 WindowManager 添加一个爆炸动画
        mWindowManager.addView(mBombFrame, mParams);
        //写个帧动画，设置到imageview中
//        mBombImage.setBackgroundResource(R.);

        AnimationDrawable drawable = (AnimationDrawable) mBombImage.getBackground();
        drawable.start();

        //设置的x和y 应该拖拽的view的x和y 一致
        mBombImage.setX(pointF.x-mStaticView.getWidth()/2);
        mBombImage.setY(mBezierView.getY()-mStaticView.getHeight()/2);
        //等它执行完之后
        mBombImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWindowManager.removeView(mBombFrame);
                //通知外面该view消失

            }
        }, getAnimationDrawableTime(drawable));

    }

    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrames = drawable.getNumberOfFrames();
        int time = 0;
        for (int i = 0; i < numberOfFrames; i++) {
            time += drawable.getDuration(i);
        }
        return time;
    }

    public interface DisappearListener {

        void dismiss(View view);
    }

}
