package com.demo.widget.beziercurve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BezierView.attach(findViewById(R.id.tv_text), new BubbleTouchListener.DisappearListener() {
            @Override
            public void dismiss(View view) {

            }
        });
    }
}
