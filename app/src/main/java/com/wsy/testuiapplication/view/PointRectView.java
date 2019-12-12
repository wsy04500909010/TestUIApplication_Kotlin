package com.wsy.testuiapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PointRectView extends View {

    private int mX, mY;
    private Paint mPaint, mPaint2;
    private Rect mrect, mrect2, mrect3;

    public PointRectView(Context context) {
        super(context);
        init();
    }

    public PointRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint2 = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(5);

        mrect = new Rect(100, 100, 200, 200);
        mrect2 = new Rect(80, 120, 180, 220);

        mrect3 = new Rect(300, 320, 500, 420);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 根据点击位置 边框颜色改变
//        if (mrect.contains(mX,mY)){
//            mPaint.setColor(Color.RED);
//        }else {
//            mPaint.setColor(Color.BLACK);
//        }
//
//        canvas.drawRect(mrect, mPaint);

        //测试矩形的交集和并集
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(mrect, mPaint);
//        canvas.drawRect(mrect2, mPaint);
//        mPaint.setColor(Color.BLUE);
//        mrect2.union(mrect);
//        canvas.drawRect(mrect2, mPaint);
//
//        canvas.drawRect(mrect3, mPaint);
//
//        mrect3.union(400, 200);
//        canvas.drawRect(mrect3, mPaint);
//
//        float[] points = {400, 200};
//        mPaint.setColor(Color.YELLOW);
//        canvas.drawPoints(points, mPaint);

        Path path = new Path();
        path.moveTo(10, 10);
        RectF rectf = new RectF(100, 10, 200, 100);
        mPaint2.setColor(Color.BLACK);
        canvas.drawRect(rectf, mPaint2);

        path.arcTo(rectf, 0, -90);

        mPaint.setColor(Color.RED);
        canvas.drawPath(path, mPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mX = (int) event.getX();
        mY = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            invalidate();

            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mX = -1;
            mY = -1;
        }
        postInvalidate();

        return super.onTouchEvent(event);
    }


}
