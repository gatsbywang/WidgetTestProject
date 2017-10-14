package com.demo.widget.loadingviewfor58;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ShapeView mShapeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShapeView = (ShapeView) findViewById(R.id.shape_view);
    }

    public void change(View view) {
//        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0, 4000);
//        valueAnimator.setDuration(4000*1000);
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.start();
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.i("tag", String.valueOf(animation.getAnimatedValue()));
//                mShapeView.exchange();
//            }
//        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mShapeView.exchange();
            }
        }, 1000,1000);
    }
}
