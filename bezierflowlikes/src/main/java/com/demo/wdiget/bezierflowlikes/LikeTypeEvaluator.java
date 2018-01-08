package com.demo.wdiget.bezierflowlikes;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by 花歹 on 2018/1/3.
 * Email:   gatsbywang@126.com
 * Description: 自定义路径属性动画
 * Thought:
 */

public class LikeTypeEvaluator implements TypeEvaluator<PointF> {

    private PointF point1, point2;

    public LikeTypeEvaluator(PointF point1, PointF point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public PointF evaluate(float t, PointF point0, PointF point3) {
        //t [0,1]
        PointF pointF = new PointF();

        //三阶贝塞尔曲线公式
        pointF.x = point0.x * (1 - t) * (1 - t) * (1 - t)
                + 3 * point1.x * t * (1 - t) * (1 - t)
                + 3 * point2.x * t * t * (1 - t)
                + point3.x * t * t * t;
        pointF.y = point0.y * (1 - t) * (1 - t) * (1 - t)
                + 3 * point1.y * t * (1 - t) * (1 - t)
                + 3 * point2.y * t * t * (1 - t)
                + point3.y * t * t * t;


        return pointF;
    }
}
