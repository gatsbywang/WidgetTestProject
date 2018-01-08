package com.demo.wdiget.bezierflowlikes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private LikeLayout mLikeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLikeLayout = findViewById(R.id.like_layout);
    }

    public void addLike(View view) {
        mLikeLayout.addLove();
    }

}
