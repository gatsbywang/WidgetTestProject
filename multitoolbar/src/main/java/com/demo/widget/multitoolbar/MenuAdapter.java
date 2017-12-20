package com.demo.widget.multitoolbar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 花歹 on 2017/12/18.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class MenuAdapter extends BaseMenuAdapter {

    private String[] mItems = {"类型", "品牌", "价格", "更多"};

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false);
        tabView.setText(mItems[position]);
        return tabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        //真正开发过程中，不同位置显示的布局不一样
        TextView menuView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        menuView.setText(mItems[position]);
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        return menuView;
    }

    @Override
    public void menuOpen(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.RED);

    }

    @Override
    public void menuClose(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.BLACK);
    }
}
