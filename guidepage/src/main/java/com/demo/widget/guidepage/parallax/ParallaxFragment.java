package com.demo.widget.guidepage.parallax;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.demo.widget.guidepage.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 花歹 on 2018/1/9.
 * Email:   gatsbywang@126.com
 * Description: 视差动画的Fragment
 * Thought:
 */

public class ParallaxFragment extends Fragment implements LayoutInflater.Factory2 {

    public static final String KEY_LAYOUT_ID = "layoutId";
    private SkinAppCompatViewInflater mAppCompatViewInflater;
    private boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

    //存放所有需要位移的view
    private List<View> mPallaxViews = new ArrayList<>();

    private int[] mParallaxAttrs = new int[]{R.attr.translationXIn, R.attr.translationXOut,
            R.attr.translationYIn, R.attr.translationYOut};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取布局的id
        int layoutId = getArguments().getInt(KEY_LAYOUT_ID);
        //View创建的时候，去解析属性
        //inflater在系统中是单例设计模式，代表所有的view都会在这个fragment中创建
        inflater = inflater.cloneInContext(getActivity());
        LayoutInflaterCompat.setFactory2(inflater, this);
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //view都会来这里创建
        View view = createView(parent, name, context, attrs);

        if (view != null) {
            //解析所有我们关注的属性
            analysisAttrs(view, context, attrs);
        }

        return view;
    }

    private void analysisAttrs(View view, Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, mParallaxAttrs);
        if (array != null && array.getIndexCount() != 0) {

            int n = array.getIndexCount();
            ParallaxTag tag = new ParallaxTag();
            for (int i = 0; i < n; i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case 0:
                        float xInt = array.getFloat(attr, 0f);
                        tag.translationXIn = xInt;
                        break;
                    case 1:
                        float xOut = array.getFloat(attr, 0f);
                        tag.translationXOut = xOut;
                        break;
                    case 2:
                        float yInt = array.getFloat(attr, 0f);
                        tag.translationYIn = yInt;
                        break;
                    case 3:
                        float yOut = array.getFloat(attr, 0f);
                        tag.translationYOut = yOut;
                        break;
                }
            }
            //自定义属性需要一一绑定
            view.setTag(R.id.parallax_tag, tag);
            Log.i("tag", tag.toString());
            mPallaxViews.add(view);

        }
        array.recycle();

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @SuppressLint("RestrictedApi")
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getActivity().getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public List<View> getParallaxViews() {
        return mPallaxViews;
    }
}
