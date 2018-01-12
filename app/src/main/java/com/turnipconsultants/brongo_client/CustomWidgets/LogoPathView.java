package com.turnipconsultants.brongo_client.CustomWidgets;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

/**
 * Created by mohit on 20-09-2017.
 */

public class LogoPathView extends View {

    private Paint paint;
    private Path path;
    private float length;
    private Context context;
    private int width, height;
    private boolean done = false;

    public LogoPathView(Context context) {
        super(context);
        init();
        if (isInEditMode()) return;
    }

    public LogoPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        if (isInEditMode()) return;
    }

    public LogoPathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        if (isInEditMode()) return;
    }

    private void init() {
        context = getContext();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(7.3F);
        paint.setStyle(Paint.Style.STROKE);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);


        path = new Path();

    }

    public void setPhase(float phase) {
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will call onDraw
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                Math.max(phase * pathLength, offset));
    }

    void Apply(int x, int y) {
        done = true;
        int cx = x / 2, cy = y - PX(10);
       /* path.moveTo(cx - PX(100), cy);//
        path.lineTo(cx - PX(50), cy);//
        path.lineTo(cx - PX(50), cy - PX(100));//
        path.lineTo(cx + PX(50), cy - PX(100));//
        path.lineTo(cx + PX(50), cy);//
        path.lineTo(cx, cy);//
        path.lineTo(cx, cy - PX(200));//
        path.lineTo(cx + PX(100), cy - PX(200));//
        path.lineTo(cx + PX(100), cy);//
        path.lineTo(cx + PX(150), cy);
        path.offset(-PX(20), 0);*/

        path.moveTo(cx - PX(90), cy);//
        path.lineTo(cx - PX(45), cy);//
        path.lineTo(cx - PX(45), cy - PX(90));//
        path.lineTo(cx + PX(45), cy - PX(90));//
        path.lineTo(cx + PX(45), cy);//
        path.lineTo(cx, cy);//
        path.lineTo(cx, cy - PX(180));//
        path.lineTo(cx + PX(90), cy - PX(180));//
        path.lineTo(cx + PX(90), cy);//
        path.lineTo(cx + PX(135), cy);
        path.offset(-PX(20), 0);


        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        ObjectAnimator animator = ObjectAnimator.ofFloat(LogoPathView.this, "phase", 1.0f, 0.0f);
        animator.setDuration(1500);
        animator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();

        if (!done)
            Apply(width, height);

        canvas.drawPath(path, paint);
    }

    public int PX(float dp) {
        return AllUtils.DensityUtils.dpToPx((int) dp);
    }


}