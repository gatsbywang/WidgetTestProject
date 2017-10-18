package com.demo.widget.lettersidebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LetterSideBar.LetterTouchListener {

    private TextView mLetterTv;
    private LetterSideBar mLetterSideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLetterTv = (TextView) findViewById(R.id.letter_tv);
        mLetterSideBar = (LetterSideBar) findViewById(R.id.letterSideBar);
        mLetterSideBar.setLetterTouchListener(this);
    }


    @Override
    public void touch(CharSequence letter) {
        mLetterTv.setText(letter);
    }

    @Override
    public void leave() {
        mLetterTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLetterTv.setVisibility(View.GONE);
            }
        },1000);
    }
}
