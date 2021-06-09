package com.zenlabs.sevenminuteworkout.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zenlabs.sevenminuteworkout.R;

public class PercentageCircle extends View {

    private static int START_ANGLE_POINT = -90;
    private static int START_ANGLE_POINT_CENTER = -90;

    private Paint paintBorder, darkPaintBorder;//, paint;
    private RectF rectBorder;//, rect;

    private float angle;

    private int borderCcolor;

    private int strokeWidth;

    private int theSize;

    public static final int STROKE_WIDTH = 50;
    public static final int STROKE_DARK_WIDTH = 15;

    public PercentageCircle(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public PercentageCircle(Context context) {
        super(context);
        init();
    }

    public void init() {

        strokeWidth = STROKE_WIDTH;
        borderCcolor = R.color.green_light_achievement;
        theSize = R.dimen.achievements_inner_circle_size;

        int size = (int) getResources().getDimension(theSize);

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeJoin(Paint.Join.ROUND);
//        paintBorder.setStrokeCap(Paint.Cap.ROUND);
        paintBorder.setStrokeWidth(strokeWidth);
        paintBorder.setColor(getResources().getColor(borderCcolor));


//        paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeJoin(Paint.Join.ROUND);
//        paint.setStrokeCap(Paint.Cap.ROUND);
//        paint.setStrokeWidth(strokeWidth);
//        paint.setColor(Color.parseColor("#99FFFFFF"));

        rectBorder = new RectF(strokeWidth / 2, strokeWidth / 2, size - strokeWidth / 2, size - strokeWidth / 2);
//        rect = new RectF(strokeWidth, strokeWidth, size - strokeWidth, size - strokeWidth);

        angle = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectBorder, START_ANGLE_POINT, angle, false, paintBorder);

//        canvas.drawArc(rect, START_ANGLE_POINT_CENTER, -angle, true, paint);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setStartAngle(float angle) {
        this.START_ANGLE_POINT = (int) angle;
    }

    public void setStartAngleCenter(float angle) {
        this.START_ANGLE_POINT_CENTER = (int) angle;
    }

    public void setStroke(int stroke) {
        strokeWidth = stroke;
        paintBorder.setStrokeWidth(strokeWidth);
    }

    public void setBorderColor(int borderColor) {
        this.borderCcolor = borderColor;
        this.paintBorder.setColor(getResources().getColor(borderCcolor));
    }

    public void setTheSize(int theSize) {
        this.theSize = theSize;
        int size = (int) getResources().getDimension(theSize);
        rectBorder = new RectF(strokeWidth / 2, strokeWidth / 2, size - strokeWidth / 2, size - strokeWidth / 2);
    }

}
