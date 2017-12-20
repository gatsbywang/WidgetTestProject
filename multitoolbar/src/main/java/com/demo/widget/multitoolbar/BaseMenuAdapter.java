package com.demo.widget.multitoolbar;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 花歹 on 2017/12/18.
 * Email:   gatsbywang@126.com
 * Description: 筛选菜单的 Adapter
 * Thought:
 */

public abstract class BaseMenuAdapter {


    private List<MenuObserver> observers = new ArrayList<>();

    private MenuObserver mObserver;

    public void registerDataSetObserver(MenuObserver observer) {
        mObserver = observer;
    }

    public void unregisterDataSetObserver(MenuObserver observer) {
//        observers.remove(observer);
        mObserver = null;
    }

    public void closeMenu() {
        if (mObserver != null) {
            mObserver.closeMenu();
        }
    }

    /**
     * 获取总共多少条
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 获取当前的TabView
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getTabView(int position, ViewGroup parent);


    public abstract View getMenuView(int position, ViewGroup parent);

    /**
     * 菜单打开
     *
     * @param tabView
     */
    public void menuOpen(View tabView) {

    }

    public void menuClose(View tabView) {
    }

}
