package com.demo.widget.guidepage.parallax;

/**
 * Created by 花歹 on 2018/1/10.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class ParallaxTag {

    public float translationXIn;
    public float translationXOut;
    public float translationYIn;
    public float translationYOut;

    @Override
    public String toString() {
        return "translationXIn->" + translationXIn
                + "translationXOut->" + translationXOut
                + "translationYIn->" + translationYIn
                + "translationYOut->" + translationYOut;
    }
}
