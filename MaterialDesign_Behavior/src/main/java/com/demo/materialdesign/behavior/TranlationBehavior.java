package com.demo.materialdesign.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 花歹 on 2017/12/5.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 * 关注垂直滚动，而且向上的时候是出来，向下是隐藏
 */

public class TranlationBehavior extends FloatingActionButton.Behavior {

    public TranlationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 表示是否给应用了Behavior的view指定一个依赖布局，通常，当依赖的view布局发生变化时候，不管被依赖view的顺序怎样，
     * 被依赖的view也会重新布局
     *
     * @param parent
     * @param child      绑定behavior的view
     * @param dependency 依赖的view
     * @return 如果是child 是依赖的指定的view返回true，否则返回false
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   FloatingActionButton child,
                                   View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    /**
     * 当被依赖的view状态（如：位置，大小）发生变化时，这个方法被调用
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          FloatingActionButton child,
                                          View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    /**
     * 当CoordinatorLayout的子view试图开始嵌套滑动的时候被调用。当返回值为true的时候表明coordinatorLayout充当
     * nested scroll parent 处理这次滑动，需要注意的是只有当返回值为true的时候，Behavior 才能收到后面的一些
     * nested scroll 事件回调（如：onNestedPreScroll、onNestedScroll等）这个方法有个重要的参数axes，
     * 表明处理的滑动的方向。
     *
     * @param coordinatorLayout 和Behavior 绑定的View的父CoordinatorLayout
     * @param child             和Behavior 绑定的View
     * @param directTargetChild
     * @param target
     * @param axes              嵌套滑动 应用的滑动方向，看 {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
     *                          {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * 嵌套滚动发生之前被调用
     * 在nested scroll child 消费掉自己的滚动距离之前，嵌套滚动每次被nested scroll child
     * 更新都会调用onNestedPreScroll。注意有个重要的参数consumed，可以修改这个数组表示你消费
     * 了多少距离。假设用户滑动了100px,child 做了90px 的位移，你需要把 consumed［1］的值改成90，
     * 这样coordinatorLayout就能知道只处理剩下的10px的滚动。
     *
     * @param child
     * @param target
     * @param dx       用户水平方向的滚动距离
     * @param dy       用户竖直方向的滚动距离
     * @param consumed
     * @param type
     */
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull FloatingActionButton child,
                                  @NonNull View target,
                                  int dx,
                                  int dy,
                                  @NonNull int[] consumed,
                                  int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

    }


    private boolean isOut = false;

    /**
     * 进行嵌套滚动时被调用
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed        target 已经消费的x方向的距离
     * @param dyConsumed        target 已经消费的y方向的距离
     * @param dxUnconsumed      x 方向剩下的滚动距离
     * @param dyUnconsumed      y 方向剩下的滚动距离
     * @param type
     */
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed,
                               int type) {
        //向上的时候是出来的，向下是隐藏
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0) {
            if (!isOut) {
                //大于0，手指往上滑动，toolbar隐藏
                int translationY = ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).bottomMargin + child.getMeasuredHeight();
                child.animate().translationY(translationY).setDuration(1000).start();
                isOut = true;
            }
        } else {
            if (isOut) {
                //大于0，手指往下滑动，toolbar显示
                child.animate().translationY(0).setDuration(1000).start();
                isOut = false;
            }
        }
    }


    /**
     * 嵌套滚动结束时被调用，这是一个清除滚动状态等的好时机。
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param type
     */
    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull FloatingActionButton child,
                                   @NonNull View target,
                                   int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    /**
     * onStartNestedScroll返回true才会触发这个方法，接受滚动处理后回调，可以在这个方法里做一些准备工作，如一些状态的重置等。
     *
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param axes
     * @param type
     */
    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes,
                                       int type) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    /**
     * 用户松开手指并且会发生惯性动作之前调用，参数提供了速度信息，可以根据这些速度信息
     * 决定最终状态，比如滚动Header，是让Header处于展开状态还是折叠状态。返回true 表
     * 示消费了fling.
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX         x 方向的速度
     * @param velocityY         y 方向的速度
     * @return
     */
    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout,
                                    @NonNull FloatingActionButton child,
                                    @NonNull View target,
                                    float velocityX,
                                    float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * 摆放子 View 的时候调用，可以重写这个方法对子View 进行重新布局
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent,
                                 FloatingActionButton child,
                                 int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
