package com.demo.widget.flexlayout;

import android.database.DataSetObservable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 花歹 on 2017/10/31.
 * Email:   gatsbywang@126.com
 * Description: 流式不仅的Adapter
 * Thought:
 */

public abstract class BaseAdapter {

    //有多少个条目
    public abstract int getCount();

    //通过position获取view
    public abstract View getView(int position, ViewGroup parent);

    //观察者模式及时通知更新
    public void unregisterDataSetObserver(DataSetObservable observable) {

    }

    public void registerDataSetObserver(DataSetObservable observable) {

    }
}
