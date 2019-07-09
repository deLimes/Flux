package com.example.delimes.flux;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.jetbrains.annotations.NotNull;

public class AnalogClock extends View {

    Context context;
    Paint p;
    Path wallpath;
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
    String text = "";

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
        wallpath = new Path();
        float length = 5 * side;
    }


    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void drawFirstQuart(Canvas canvas, int indexOfQuart, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {2, 3, 4};
        text = "";
        if (indexOfThird != 0 && indexOfThird != 4) {
            int currentHours = hours[indexOfHour];
            text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
        }

        if (indexOfQuart == 0) {
            upperLeftCornerX = x - bias;
            left = upperLeftCornerX - side;
            top = y;
            right = upperLeftCornerX;
            bottom = y + side;
        } else if (indexOfQuart == 1) {
            bottomLeftCornerY = y + bias;
            left = upperLeftCornerX - side;
            top = bottomLeftCornerY;
            right = upperLeftCornerX;
            bottom = bottomLeftCornerY + side;
        } else if (indexOfQuart == 2) {
            bottomRightCornerX = bias;
            left = bottomRightCornerX;
            top = bottomLeftCornerY;//y-side/2;
            right = bottomRightCornerX + side;
            bottom = bottomLeftCornerY + side;
        } else if (indexOfQuart == 3) {
            upperRightCornerY = bias + side;
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
        //canvas.drawCircle(side * 2.5f, side * 2.5f, side / 4, p);
        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfQuart == 0 && indexOfThird != 0 && indexOfThird != 1) {

//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);;

            if (indexOfThird == 4 && (MainActivity.currSeconds == 53)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 54)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);


            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 55)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);


            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 56)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 57)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 58)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 59)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 0)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 1)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 2)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 3)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 4)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 5)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 6)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 7)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            }else {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
            }


        } else if (indexOfQuart == 1 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side / 2, p);

            if (indexOfThird == 3 && (MainActivity.currSeconds == 38)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 39)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 40)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 41)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 42)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 43)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 44)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 45)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 46)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 47)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 48)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 49)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 50)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 51)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 52)) {

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



        } else if (indexOfQuart == 2 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side / 2, top, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    */

            if (indexOfThird == 3 && (MainActivity.currSeconds == 23)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 24)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 25)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 26)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 27)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 28)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 29)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 30)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 31)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 32)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 33)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 34)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 35)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 36)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 37)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            }else {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);
            }


        } else if (indexOfQuart == 3 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side / 2, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    */


            if (indexOfThird == 1 && (MainActivity.currSeconds == 8)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 9)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 10)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 11)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);

                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 12)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 13)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 14)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 15)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 16)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 17)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 18)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 19)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 20)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 21)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 22)) {

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

    }

    private void drawSecondQuart(Canvas canvas, int indexOfQuart, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {7, 6, 5};
        text = "";
        if (indexOfThird != 0 && indexOfThird != 4) {
            int currentHours = hours[indexOfHour];
            text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
        }

        if (indexOfQuart == 0) {
            upperLeftCornerX = x - bias;
            left = upperLeftCornerX - side;
            top = y;
            right = upperLeftCornerX;
            bottom = y + side;
        } else if (indexOfQuart == 1) {
            bottomLeftCornerY = y + bias;
            left = upperLeftCornerX - side;
            top = bottomLeftCornerY;
            right = upperLeftCornerX;
            bottom = bottomLeftCornerY + side;
        } else if (indexOfQuart == 2) {
            bottomRightCornerX = bias;
            left = bottomRightCornerX;
            top = bottomLeftCornerY;//y-side/2;
            right = bottomRightCornerX + side;
            bottom = bottomLeftCornerY + side;
        } else if (indexOfQuart == 3) {
            upperRightCornerY = bias + side;
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
        //canvas.drawCircle(side * 2.5f, side * 2.5f, side / 4, p);
        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfQuart == 0 && indexOfThird != 0 && indexOfThird != 1) {

//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);;

            if (indexOfThird == 4 && (MainActivity.currSeconds == 53)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 54)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);


            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 55)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);


            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 56)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 57)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 58)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 59)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 0)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 1)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 2)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 3)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 4)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 5)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 6)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 7)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            }else {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
            }


        } else if (indexOfQuart == 1 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side / 2, p);

            if (indexOfThird == 3 && (MainActivity.currSeconds == 38)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 39)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 40)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 41)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 42)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 43)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 44)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 45)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 46)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 47)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 48)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 49)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 50)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 51)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 52)) {

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



        } else if (indexOfQuart == 2 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side / 2, top, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    */

            if (indexOfThird == 3 && (MainActivity.currSeconds == 23)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 24)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 25)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 26)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 27)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 28)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 29)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 30)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 31)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 32)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 33)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 34)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 35)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 36)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 37)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            }else {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);
            }


        } else if (indexOfQuart == 3 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side / 2, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    */


            if (indexOfThird == 1 && (MainActivity.currSeconds == 8)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 9)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 10)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 11)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);

                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 12)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 13)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 14)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 15)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 16)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 17)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 18)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 19)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 20)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 21)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 22)) {

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

    }

    private void drawThirdQuart(Canvas canvas, int indexOfQuart, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {10, 9, 8};
        text = "";
        if (indexOfThird != 0 && indexOfThird != 4) {
            int currentHours = hours[indexOfHour];
            text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
        }

        if (indexOfQuart == 0) {
            upperLeftCornerX = x - bias;
            left = upperLeftCornerX - side;
            top = y;
            right = upperLeftCornerX;
            bottom = y + side;
        } else if (indexOfQuart == 1) {
            bottomLeftCornerY = y + bias;
            left = upperLeftCornerX - side;
            top = bottomLeftCornerY;
            right = upperLeftCornerX;
            bottom = bottomLeftCornerY + side;
        } else if (indexOfQuart == 2) {
            bottomRightCornerX = bias;
            left = bottomRightCornerX;
            top = bottomLeftCornerY;//y-side/2;
            right = bottomRightCornerX + side;
            bottom = bottomLeftCornerY + side;
        } else if (indexOfQuart == 3) {
            upperRightCornerY = bias + side;
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
        //canvas.drawCircle(side * 2.5f, side * 2.5f, side / 4, p);
        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfQuart == 0 && indexOfThird != 0 && indexOfThird != 1) {

//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
//                    canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);;

            if (indexOfThird == 4 && (MainActivity.currSeconds == 53)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 54)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);


            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 55)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);


            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 56)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 4 && (MainActivity.currSeconds == 57)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 58)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 59)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 0)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 1)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 2)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 3)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 4)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 5)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 6)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 7)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        bottom,
                        upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        bottom + side * 0.20f * 2, p);

                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
                wallpath.lineTo(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

            }else {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f * 2, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f - side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f, bottom, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, upperLeftCornerX + side * 0.50f + side * 0.20f * 2, bottom, p);
            }


        } else if (indexOfQuart == 1 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side / 2, p);

            if (indexOfThird == 3 && (MainActivity.currSeconds == 38)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 39)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 40)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 41)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 42)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 43)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 44)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 45)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 46)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 47)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 48)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 49)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 50)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 51)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, right, bottomLeftCornerY + side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 52)) {

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



        } else if (indexOfQuart == 2 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side / 2, top, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);;
                    */

            if (indexOfThird == 3 && (MainActivity.currSeconds == 23)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 24)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 25)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 26)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 27)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 28)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 29)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 30)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 31)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 32)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);


                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 33)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 34)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 35)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 36)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 37)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);

                //HourdHand
                p.setColor(getResources().getColor(R.color.colorHourdHand));
                canvas.drawRect(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                        top - side * 0.20f * 2,
                        bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                        top, p);


                //MinuteHand
                p.setColor(getResources().getColor(R.color.colorMinuteHand));
                wallpath.moveTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
                wallpath.lineTo(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top);
                canvas.drawPath(wallpath, p);

                //SecondHand
                p.setColor(getResources().getColor(R.color.colorSecondHand));
                canvas.drawCircle(bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

            }else {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f * 2, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f - side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f, top, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, bottomRightCornerX + side * 0.50f + side * 0.20f * 2, top, p);
            }


        } else if (indexOfQuart == 3 && indexOfThird != 0 && indexOfThird != 4) {
            //canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side / 2, p);

                    /*
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                    canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
                    */


            if (indexOfThird == 1 && (MainActivity.currSeconds == 8)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 9)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 10)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 11)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);

                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 1 && (MainActivity.currSeconds == 12)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 13)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 14)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 15)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 16)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 2 && (MainActivity.currSeconds == 17)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 18)) {

                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 19)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 20)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 21)) {

                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f * 2, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f - side * 0.20f, p);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f, p);
                p.setColor(Color.RED);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f, p);
                p.setColor(Color.BLACK);
                canvas.drawLine(side * 2.5f, side * 2.5f, left, upperRightCornerY - side * 0.50f + side * 0.20f * 2, p);;
            } else if (indexOfThird == 3 && (MainActivity.currSeconds == 22)) {

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

    }

    private void drawArrowsFirstPositionFourthQuart(@NotNull Canvas canvas){

        p.setColor(Color.RED);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.20f * 2 - side * 0.20f,
                bottom,
                left + side * 0.50f - side * 0.20f * 2 + side * 0.20f,
                bottom + side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f - side * 0.20f * 2, bottom);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2, bottom);
        canvas.drawPath(wallpath, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

    }

    private void drawArrowsSecondPositionFourthQuart(@NotNull Canvas canvas){

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        p.setColor(Color.RED);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.20f - side * 0.20f,
                bottom,
                left + side * 0.50f - side * 0.20f + side * 0.20f,
                bottom + side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f - side * 0.20f, bottom);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f, bottom);
        canvas.drawPath(wallpath, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

    }

    private void drawArrowsThirdPositionFourthQuart(@NotNull Canvas canvas){

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        p.setColor(Color.RED);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.20f,
                bottom + side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f, bottom);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f, bottom);
        canvas.drawPath(wallpath, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f, bottom, side * 0.20f, p);

    }

    private void drawArrowsFourthPositionFourthQuart(@NotNull Canvas canvas){

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        p.setColor(Color.RED);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.20f - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.20f + side * 0.20f,
                bottom + side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f + side * 0.20f, bottom);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f, bottom);
        canvas.drawPath(wallpath, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

    }

    private void drawArrowsFifthPositionFourthQuart(@NotNull Canvas canvas){

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        p.setColor(Color.RED);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.20f * 2 - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.20f * 2 + side * 0.20f,
                bottom + side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f + side * 0.20f * 2, bottom);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2, bottom);
        canvas.drawPath(wallpath, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

    }

    private void drawArrowsAllPositionFourthQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);
    }

    private void drawFourthQuart(@NotNull Canvas canvas, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {1, 12, 11};

        int currentHours = hours[indexOfHour];
        text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;

        upperLeftCornerX = x - bias;
        left = upperLeftCornerX - side;
        top = y;
        right = upperLeftCornerX;
        bottom = y + side;

        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);
        p.setStyle(Paint.Style.FILL);

        canvas.drawText(text, left + side / 4f, bottom - side / 4f, p);

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, p);

        p.reset();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3 && (MainActivity.currSeconds == 53)) {
            drawArrowsFirstPositionFourthQuart(canvas);
        } else if (indexOfThird == 3 && (MainActivity.currSeconds == 54)) {
            drawArrowsSecondPositionFourthQuart(canvas);
        } else if (indexOfThird == 3 && (MainActivity.currSeconds == 55)) {
            drawArrowsThirdPositionFourthQuart(canvas);
        } else if (indexOfThird == 3 && (MainActivity.currSeconds == 56)) {
            drawArrowsFourthPositionFourthQuart(canvas);
        } else if (indexOfThird == 3 && (MainActivity.currSeconds == 57)) {
            drawArrowsFifthPositionFourthQuart(canvas);
        } else if (indexOfThird == 2 && (MainActivity.currSeconds == 58)) {
            drawArrowsFirstPositionFourthQuart(canvas);
        } else if (indexOfThird == 2 && (MainActivity.currSeconds == 59)) {
            drawArrowsSecondPositionFourthQuart(canvas);
        } else if (indexOfThird == 2 && (MainActivity.currSeconds == 0)) {
            drawArrowsThirdPositionFourthQuart(canvas);
        } else if (indexOfThird == 2 && (MainActivity.currSeconds == 1)) {
            drawArrowsFourthPositionFourthQuart(canvas);
        } else if (indexOfThird == 2 && (MainActivity.currSeconds == 2)) {
            drawArrowsFifthPositionFourthQuart(canvas);
        } else if (indexOfThird == 1 && (MainActivity.currSeconds == 3)) {
            drawArrowsFirstPositionFourthQuart(canvas);
        } else if (indexOfThird == 1 && (MainActivity.currSeconds == 4)) {
            drawArrowsSecondPositionFourthQuart(canvas);
        } else if (indexOfThird == 1 && (MainActivity.currSeconds == 5)) {
            drawArrowsThirdPositionFourthQuart(canvas);
        } else if (indexOfThird == 1 && (MainActivity.currSeconds == 6)) {
            drawArrowsFourthPositionFourthQuart(canvas);
        } else if (indexOfThird == 1 && (MainActivity.currSeconds == 7)) {
            drawArrowsFifthPositionFourthQuart(canvas);
        } else {
            drawArrowsAllPositionFourthQuart(canvas);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int fontHeight = side / 2;

        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        text = "" + ( ("" + MainActivity.currHours).length() == 1 ? "0" + MainActivity.currHours : "" + MainActivity.currHours )
                + ":" + ( ("" + MainActivity.currMinutes).length() == 1 ? "0" + MainActivity.currMinutes : "" + MainActivity.currMinutes )
                + ":" + ( ("" + MainActivity.currSeconds).length() == 1 ? "0" + MainActivity.currSeconds : "" + MainActivity.currSeconds );
        canvas.drawText(text, side * 2.5f, side * 2.5f, p);

        for (int j = 0; j < 4 ; j++) {
            int k = 0;
            int l = 0;

            for (int i = 0; i < 5; i++) {

                if (j == 0 && i > 0 && i < 4) {

                    drawFourthQuart(canvas, i, k, l);

                } else if (j == 1 && i > 0 && i < 4) {

                    //drawThirdQuart(canvas, j, i, k, l);

                } else if (j == 2 && i > 0 && i < 4) {

                    //drawSecondQuart(canvas, j, i, k, l);

                } else if (j == 3 && i > 0 && i < 4) {

                    //drawFirstQuart(canvas, j, i, k, l);
                }

                if (i > 0 && i < 4) {
                    l += 1;
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
