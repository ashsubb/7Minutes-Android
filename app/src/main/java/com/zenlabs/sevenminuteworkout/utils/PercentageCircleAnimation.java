package com.zenlabs.sevenminuteworkout.utils;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class PercentageCircleAnimation extends Animation {

    private PercentageCircle circle;

    private float oldAngle;
    private float fromAngle;
    private float newAngle;
    private boolean reverse;

    public PercentageCircleAnimation(PercentageCircle circle, int startAngle, int fromAngle, int newAngle, boolean reverse) {
        this.oldAngle = startAngle;
        this.fromAngle = fromAngle;
        this.newAngle = newAngle;
        this.circle = circle;
        this.reverse = reverse;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);

        if (reverse) {
            circle.setStartAngle(angle);
            circle.setStartAngleCenter(180 - angle);
            circle.setAngle(360 - (angle + 90));
        } else {
            circle.setStartAngle(fromAngle);
            circle.setAngle(angle);

//            LogService.Log("Percentage", "START_ANGLE_POINT: " + oldAngle + " angle: " + angle);
        }
        circle.requestLayout();
    }
}
