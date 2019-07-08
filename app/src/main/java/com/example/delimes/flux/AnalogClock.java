package com.example.delimes.flux;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class AnalogClock extends View {

    Context context;
    Paint p;
    // координаты для рисования квадрата
    float x = 0;
    float y = 0;
    int side = 0;
    int width = 0;
    int height = 0;
    float length = 0;

    float left = 0;
    float top =  0;
    float right = 0;
    float bottom = 0;

    float upperLeftCornerX = 0;
    float bottomLeftCornerY = 0;
    float upperRightCornerY = 0;
    float bottomRightCornerX = 0;

    // переменные для перетаскивания
    boolean drag = false;
    float dragX = 0;
    float dragY = 0;

    private boolean mAttached;

    private Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
            MainActivity.onTimeChanged();
            invalidate();
            if (mAttached) {
                MainActivity.tickHandler.postDelayed(mTickRunnable, 1000);
            }
        }
    };

    public AnalogClock(Context context) {
        super(context);

        this.context = context;
        MainActivity.tickHandler = new Handler();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = (int) (size.x * 0.15f);
        int displayHeight =  size.y;
        this.side = width / 2;

        p = new Paint();
        float length = 5 * side;
    }


    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int fontHeight = side / 2;
        int strokeWidth = side / 5;

        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int l = 0;
        int g = 0;
        for (int j = 0; j < 4 ; j++) {
            int k = 0;

            for (int i = 0; i < 5; i++) {
                String text = "";
                if (i != 0 && i != 4) {
                    int currentHours = hours[l];
                    text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
                    l += 1;
                }

                if (j == 0) {
                    upperLeftCornerX = x - k;
                    left = upperLeftCornerX - side;
                    top = y;
                    right = upperLeftCornerX;
                    bottom = y + side;
                } else if (j == 1) {
                    bottomLeftCornerY = y + k;
                    left = upperLeftCornerX - side;
                    top = bottomLeftCornerY;
                    right = upperLeftCornerX;
                    bottom = bottomLeftCornerY + side;
                } else if (j == 2) {
                    bottomRightCornerX = k;
                    left = bottomRightCornerX;
                    top = bottomLeftCornerY;//y-side/2;
                    right = bottomRightCornerX + side;
                    bottom = bottomLeftCornerY + side;
                } else if (j == 3) {
                    upperRightCornerY = k + side;
                    left = x - side;
                    top = upperRightCornerY - side;
                    right = x;
                    bottom = upperRightCornerY;
                }

                p.reset();
                p.setColor(Color.BLACK);
                p.setTextSize(fontHeight);
                p.setStyle(Paint.Style.FILL);

                canvas.drawText(text, left + side / 4, bottom - side / 4, p);

                p.setColor(Color.BLACK);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(left, top, right, bottom, p);

                p.reset();
                p.setColor(Color.BLACK);
                p.setStyle(Paint.Style.FILL);
                canvas.drawCircle(side * 2.5f, side * 2.5f, side / 4, p);

                if (j == 0 && i != 0 && i != 1) {

                    p.reset();
                    p.setStyle(Paint.Style.FILL);

//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);;

                    Log.d("123", "MainActivity.currSeconds: " + MainActivity.currSeconds);
                    //Log.d("123", "i: "+ i);
                    if (i == 4 && (MainActivity.currSeconds == 53)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 4 && (MainActivity.currSeconds == 54)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                    } else if (i == 4 && (MainActivity.currSeconds == 55)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 4 && (MainActivity.currSeconds == 56)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 4 && (MainActivity.currSeconds == 57)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 58)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 59)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 0)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                        ;
                    } else if (i == 3 && (MainActivity.currSeconds == 1)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                        ;
                    } else if (i == 3 && (MainActivity.currSeconds == 2)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 3)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 4)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 5)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 6)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 7)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    }else {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
                    }


                } else if (j == 1 && i != 0 && i != 4) {
                    //canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side / 2, p);

                    //Log.d("123", "onDraw: i"+i);
                    if (i == 3 && (MainActivity.currSeconds == 38)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 39)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 40)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 41)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 3 && (MainActivity.currSeconds == 42)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 43)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 44)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 45)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 46)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 2 && (MainActivity.currSeconds == 47)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 1 && (MainActivity.currSeconds == 48)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 1 && (MainActivity.currSeconds == 49)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 1 && (MainActivity.currSeconds == 50)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 1 && (MainActivity.currSeconds == 51)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    } else if (i == 1 && (MainActivity.currSeconds == 52)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    }else {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
                    }



                } else if (j == 2 && i != 0 && i != 4) {
                    //canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side / 2, top, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    */

                    if (i == 3 && (MainActivity.currSeconds == 23)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 24)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 25)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 26)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 27)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 28)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 29)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 30)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 31)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 32)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 33)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 34)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 35)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 36)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 37)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    }else {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    }


                } else if (j == 3 && i != 0 && i != 4) {
                    //canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side / 2, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    */


                    if (i == 1 && (MainActivity.currSeconds == 8)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 9)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 10)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 11)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);

                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 1 && (MainActivity.currSeconds == 12)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 13)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 14)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 15)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 16)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 2 && (MainActivity.currSeconds == 17)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 18)) {

                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 19)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 20)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 21)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    } else if (i == 3 && (MainActivity.currSeconds == 22)) {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        p.setColor(Color.RED);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    }else {

                        p.setColor(Color.BLACK);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                        canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    }



                }


                k += side;
            }
        }



    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            MainActivity.tickHandler.post(mTickRunnable);

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            MainActivity.timeChangedReceiver = new TimeChangedReceiver(context);
            //Register the broadcast receiver to receive TIME_TICK
            context.registerReceiver(MainActivity.timeChangedReceiver, filter);
        }

        // NOTE: It's safe to do these after registering the receiver since the always runs
        // in the main thread, therefore the receiver can't run before this method returns.

//        // The time zone may have changed while the receiver wasn't registered, so update the Time.
//        mCalendar = new Time();

//        // Make sure we update to the current time.
//        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            context.unregisterReceiver(MainActivity.timeChangedReceiver);
            mAttached = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:
                //positionOfTouchX = evX;
                //positionOfTouchY = evY;

                // включаем режим перетаскивания
                drag = true;

                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX - x;
                dragY = evY - y;

                    /*
                    dragX = x;
                    dragY = y;*/


                //invalidate();
                break;
            // тащим
            case MotionEvent.ACTION_MOVE:



                // если режим перетаскивания включен
                if (drag) {

                    //positionOfTouchX = evX;
                    //positionOfTouchY = evY;

                    // определеяем новые координаты
                    x = evX - dragX;
                    y = evY - dragY;
                    invalidate();
                    //Log.d("XY", "X:" + x + "Y:" + y);
                }

                break;
            // касание завершено
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // выключаем режим перетаскивания
                drag = false;
                break;

        }

        return true;
    }


}
