package com.rowsun.leapfrogassignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rowsun on 7/300/17.
 */

public class CircleView extends View {
    private Paint paint;
    Canvas canvas;
    private Rect mMeasuredRect;
    private List<Circle> mCircles = new ArrayList<Circle>();
    private SparseArray<Circle> mCirclePointer = new SparseArray<Circle>();

    public CircleView(Context context) {
        super(context);

        initialize();
    }

    private void initialize() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(final Canvas canv) {
        for (Circle circle : mCircles) {
            canv.drawCircle(circle.centerX, circle.centerY, 30, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (surfaceHolder.getSurface().isValid()) {
//                canvas.drawColor(Color.BLACK);
//                canvas.drawCircle(event.getX(), event.getY(), 50, paint);
//                surfaceHolder.unlockCanvasAndPost(canvas);
//            }
//
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            canvas.restore();
//        }
//        return false;

        Circle touchedCircle;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                clearCirclePointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                touchedCircle.centerX = xTouch;
                touchedCircle.centerY = yTouch;
                mCirclePointer.put(event.getPointerId(0), touchedCircle);

                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                touchedCircle = obtainTouchedCircle(xTouch, yTouch);

                mCirclePointer.put(pointerId, touchedCircle);
                touchedCircle.centerX = xTouch;
                touchedCircle.centerY = yTouch;
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();


                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedCircle = mCirclePointer.get(pointerId);

                    if (null != touchedCircle) {
                        touchedCircle.centerX = xTouch;
                        touchedCircle.centerY = yTouch;
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                clearCirclePointer();
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointerId = event.getPointerId(actionIndex);

                mCirclePointer.remove(pointerId);
                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                Circle removedCircle = obtainTouchedCircle(xTouch, yTouch);

                removeCircle(removedCircle);
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                return super.onTouchEvent(event);

        }

        return true;

    }


    private Circle obtainTouchedCircle(final int xTouch, final int yTouch) {
        Circle touchedCircle = getTouchedCircle(xTouch, yTouch);

        if (null == touchedCircle) {
            touchedCircle = new Circle(xTouch, yTouch);
            mCircles.add(touchedCircle);
        }

        return touchedCircle;
    }


    private Circle getTouchedCircle(final int xTouch, final int yTouch) {
        Circle touched = null;

        for (Circle circle : mCircles) {
            if ((circle.centerX - xTouch) * (circle.centerX - xTouch) + (circle.centerY - yTouch) * (circle.centerY - yTouch) <= 300 * 300) {
                touched = circle;
                break;
            }
        }

        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    private void clearCirclePointer() {
        mCirclePointer.clear();
        mCircles.clear();
    }

    private void removeCircle(Circle c) {
        mCircles.remove(c);
        invalidate();
    }
}
