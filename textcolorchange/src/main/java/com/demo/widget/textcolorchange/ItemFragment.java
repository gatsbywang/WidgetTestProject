package com.demo.widget.textcolorchange;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 花歹 on 2017/10/11.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class ItemFragment extends Fragment {

    public static ItemFragment newInstance(String item) {
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", item);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        Bundle bundle = getArguments();
        textView.setText(bundle.getString("title"));
        return view;
    }
}
