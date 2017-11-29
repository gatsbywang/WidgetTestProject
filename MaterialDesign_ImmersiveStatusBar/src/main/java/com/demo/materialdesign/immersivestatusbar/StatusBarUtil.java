package com.demo.materialdesign.immersivestatusbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by 花歹 on 2017/11/28.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class StatusBarUtil {

    /**
     * 为activity的状态栏设置颜色
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color) {
        //5.0以上,4.4以下没办法了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //直接调用系统提供的方法
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //4.4-5.0 使用技巧，首先设置为全屏，然后在状态栏的部分加一个布局
            //这个把状态栏都给弄没了
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //这个是沉浸式
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //在状态栏家一个布局，根据setContentView源码，加一个布局
            View view = new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
            view.setBackgroundColor(color);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(view);


            ViewGroup contentView = activity.findViewById(android.R.id.content);
            //activity的布局
            View activityView = contentView.getChildAt(0);
            activityView.setFitsSystemWindows(true);

        }

    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    private static int getStatusBarHeight(Activity activity) {
        //
        Resources resources = activity.getResources();
        int statusBarHeightId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(statusBarHeightId);
    }

    /**
     * 设置activity沉浸式状态栏
     *
     * @param activity
     */
    public static void setStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
