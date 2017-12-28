package com.demo.widget.beziercurve;

import android.graphics.PointF;

/**
 * Created by 花歹 on 2017/12/28.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class BezierViewUtil {

    public static PointF getPointByPercent(PointF start, PointF end, float percent) {

        return new PointF(start.x + (end.x - start.x) * percent, start.y + (end.y - start.y) * percent);
    }
}
