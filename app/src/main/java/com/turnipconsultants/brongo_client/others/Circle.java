package com.turnipconsultants.brongo_client.others;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.turnipconsultants.brongo_client.R;
import com.turnipconsultants.brongo_client.others.AllUtils.AllUtils;

/**
 * Created by mohit on 19-09-2017.
 */

public class Circle extends View {
    private static final String TAG = "Circle";
    private Paint bluePaint, whitePaint, bluePaintWithElev;
    int width;
    int height;
    RectF rect1, rect2, rect3, rect4;
    private float angle;
    int diff = 40;
    int innerCircleRadius = 50;
    float startAngle = 320F;

    private final int[] colours =
            new int[]{
                    ContextCompat.getColor(getContext(), R.color.primaryPurpleAlpha5),
                    ContextCompat.getColor(getContext(), R.color.primaryPurpleAlpha4),
                    ContextCompat.getColor(getContext(), R.color.primaryPurpleAlpha3),
                    ContextCompat.getColor(getContext(), R.color.primaryPurpleAlpha2),
                    ContextCompat.getColor(getContext(), R.color.primaryPurpleAlpha1),
                    ContextCompat.getColor(getContext(), R.color.primaryBuleAlpha5),
                    ContextCompat.getColor(getContext(), R.color.primaryBuleAlpha4),
                    ContextCompat.getColor(getContext(), R.color.primaryBuleAlpha3),
                    ContextCompat.getColor(getContext(), R.color.primaryBuleAlpha2),
                    ContextCompat.getColor(getContext(), R.color.primaryBuleAlpha1)};


    public Circle(Context context) {
        super(context);
        init();
    }

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Circle(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        init();
    }

    private void init() {
        angle = 0F;
        rect1 = new RectF();
        rect2 = new RectF();
        rect3 = new RectF();
        rect4 = new RectF();

        bluePaint = new Paint();
        bluePaint.setAntiAlias(true);
        bluePaint.setColor(Color.BLUE);
        //bluePaint.setShadowLayer(dpToPx(4), 0, 0, Color.GRAY);

        bluePaintWithElev = new Paint();
        bluePaintWithElev.setAntiAlias(true);
        bluePaintWithElev.setColor(Color.BLUE);
        bluePaintWithElev.setShadowLayer(dpToPx(5), 0, 0, Color.BLUE);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();


        Shader shader = new SweepGradient(width / 2, height / 2, colours, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(320F, width / 2, height / 2);
        shader.setLocalMatrix(matrix);
        bluePaint.setShader(shader);
        bluePaintWithElev.setShader(shader);


        float left, right, top, bottom;
        left = width / 2 - dpToPx(innerCircleRadius);
        right = height / 2 - dpToPx(innerCircleRadius);
        top = width / 2 + dpToPx(innerCircleRadius);
        bottom = height / 2 + dpToPx(innerCircleRadius);
        rect1.set(left, right, top, bottom);
        rect2.set(rect1.left - dpToPx(diff), rect1.top - dpToPx(diff), rect1.right + dpToPx(diff), rect1.bottom + dpToPx(diff));
        rect3.set(rect2.left - dpToPx(0.6F), rect2.top - dpToPx(0.6F), rect2.right + dpToPx(0.6F), rect2.bottom + dpToPx(0.6F));
        rect4.set(rect3.left - dpToPx(diff), rect3.top - dpToPx(diff), rect3.right + dpToPx(diff), rect3.bottom + dpToPx(diff));

        canvas.drawArc(rect4, startAngle, angle, true, bluePaintWithElev);
        canvas.drawArc(rect3, startAngle, angle, true, whitePaint);
        canvas.drawArc(rect2, startAngle, angle, true, bluePaint);

        //canvas.drawArc(rect1, startAngle, 360F, true, yellowPaint);
        canvas.drawOval(rect1, whitePaint);

        invalidate();
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }


    public int dpToPx(float dp) {
        return AllUtils.DensityUtils.dpToPx((int) dp);
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
