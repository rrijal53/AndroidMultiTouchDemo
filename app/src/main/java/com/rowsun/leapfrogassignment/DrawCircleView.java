package com.rowsun.leapfrogassignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.HashSet;

/**
 * Created by rowsun on 7/30/17.
 */

public class DrawCircleView extends SurfaceView {
    private Paint paint;
    Canvas canvas;
    private final SurfaceHolder surfaceHolder;
    private HashSet<Circle> mCircles = new HashSet<Circle>();
    private SparseArray<Circle> mCirclePointer = new SparseArray<Circle>();

    public DrawCircleView(Context context) {
        super(context);
        surfaceHolder = getHolder();

        initialize(context);
    }

    private void initialize(Context context) {
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();

                canvas.drawColor(Color.BLACK);
                canvas.drawCircle(event.getX(), event.getY(), 50, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            invalidate();
        }
        return false;

    }

}
