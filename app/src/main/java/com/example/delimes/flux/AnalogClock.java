package com.example.delimes.flux;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;

public class AnalogClock extends View {

    Context context;
    Paint p;
    Path wallpath;
    public ConstraintLayout constraintLayout;
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
    private float analogClockX = 0;
    private float analogClockY = 0;
    private ConstraintLayout.LayoutParams params;

    private boolean mAttached;
    String text = "";
    private GestureDetector gestureDetector;

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

    int scrollTimeX = 0;
    int scrollTimeY = 0;
    CountDownTimer countDownTimerX = new CountDownTimer(0, 0) {
        @Override
        public void onTick(long l) {

        }
        @Override
        public void onFinish() {

        }
    };
    CountDownTimer countDownTimerY = new CountDownTimer(0, 0) {
        @Override
        public void onTick(long l) {

        }
        @Override
        public void onFinish() {

        }
    };
    boolean hitX = false;
    boolean hitY = false;
    Rect textBounds = new Rect();
    public int clockColor;

    public AnalogClock(Context context) {
        super(context);

        this.context = context;
        MainActivity.tickHandler = new Handler();
        constraintLayout = ((MainActivity)context).constraintLayout;
        gestureDetector = new GestureDetector(context, new MyGestureListener());

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
        clockColor = Color.WHITE;
        setVisibility(View.INVISIBLE);


    }


    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalogClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void drawFirstQuartReserve(Canvas canvas, int indexOfQuart, int indexOfThird, int bias, int indexOfHour) {

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



    private void drawHourdHandFirstPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f * 2, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f * 2, p);;

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left - side * 0.20f * 2,
                bottom - side * 0.50f - side * 0.25f * 2 - side * 0.20f,
                left,
                bottom - side * 0.50f - side * 0.25f * 2 + side * 0.20f, p);

    }

    private void drawHourdHandSecondPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f * 2, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f * 2, p);;

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left - side * 0.20f * 2,
                bottom - side * 0.50f - side * 0.25f - side * 0.20f,
                left,
                bottom - side * 0.50f - side * 0.25f + side * 0.20f, p);

    }

    private void drawHourdHandThirdPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f * 2, p);;

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left - side * 0.20f * 2,
                bottom - side * 0.50f - side * 0.20f,
                left,
                bottom - side * 0.50f + side * 0.20f, p);

    }

    private void drawHourdHandFourthPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f * 2, p);;

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left - side * 0.20f * 2,
                bottom - side * 0.50f + side * 0.25f - side * 0.20f,
                left,
                bottom - side * 0.50f + side * 0.25f + side * 0.20f, p);


    }

    private void drawHourdHandFifthPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f * 2, p);;

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left - side * 0.20f * 2,
                bottom - side * 0.50f + side * 0.25f * 2 - side * 0.20f,
                left,
                bottom - side * 0.50f + side * 0.25f * 2 + side * 0.20f, p);


    }

    private void drawHourdHandAllPositionFirstQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f * 2, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.25f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.25f * 2, p);;


    }


    private void drawMinuteHandFirstPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left, bottom - side * 0.50f - side * 0.20f * 2);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f - side * 0.20f * 2 + side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f - side * 0.20f * 2 - side * 0.20f );
        wallpath.lineTo(left, bottom - side * 0.50f - side * 0.20f * 2);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandSecondPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left, bottom - side * 0.50f - side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f - side * 0.20f + side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f - side * 0.20f - side * 0.20f );
        wallpath.lineTo(left, bottom - side * 0.50f - side * 0.20f);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandThirdPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left, bottom - side * 0.50f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f + side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f - side * 0.20f );
        wallpath.lineTo(left, bottom - side * 0.50f);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandFourthPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left, bottom - side * 0.50f + side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f + side * 0.20f + side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f + side * 0.20f - side * 0.20f );
        wallpath.lineTo(left, bottom - side * 0.50f + side * 0.20f);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandFifthPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left, bottom - side * 0.50f + side * 0.20f * 2);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f + side * 0.20f * 2 + side * 0.20f);
        wallpath.lineTo( left - side * 0.40f, bottom - side * 0.50f + side * 0.20f * 2 - side * 0.20f );
        wallpath.lineTo(left, bottom - side * 0.50f + side * 0.20f * 2);
        canvas.drawPath(wallpath, p);

    }


    private void drawSecondHandFirstPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( left, bottom - side * 0.50f - side * 0.20f * 2, side * 0.20f, p);

    }

    private void drawSecondHandSecondPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(  left, bottom - side * 0.50f - side * 0.20f, side * 0.20f, p);

    }

    private void drawSecondHandThirdPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( left, bottom - side * 0.50f, side * 0.20f, p);

    }

    private void drawSecondHandFourthPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( left, bottom - side * 0.50f + side * 0.20f, side * 0.20f, p);

    }

    private void drawSecondHandFifthPositionFirstQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( left, bottom - side * 0.50f + side * 0.20f * 2, side * 0.20f, p);

    }

    private void drawSecondHandAllPositionFirstQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f * 2, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f - side * 0.20f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left, bottom - side * 0.50f + side * 0.20f * 2, p);;


    }


    private void drawSecondHandFirstQuart(Canvas canvas, int indexOfThird, int bias) {

        upperRightCornerY = bias + side;
        left = x - side;
        top = upperRightCornerY - side;
        right = x;
        bottom = upperRightCornerY;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 1) {

            //SecondHand
            if (MainActivity.currSeconds == 8) {
                drawSecondHandFirstPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 9) {
                drawSecondHandSecondPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 10) {
                drawSecondHandThirdPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 11) {
                drawSecondHandFourthPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 12) {
                drawSecondHandFifthPositionFirstQuart(canvas);
            } else {
                //drawSecondHandAllPositionFirstQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //SecondHand
            if (MainActivity.currSeconds == 13) {
                drawSecondHandFirstPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 14) {
                drawSecondHandSecondPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 15) {
                drawSecondHandThirdPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 16) {
                drawSecondHandFourthPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 17) {
                drawSecondHandFifthPositionFirstQuart(canvas);
            } else {
                //drawSecondHandAllPositionFirstQuart(canvas);
            }
        } else if (indexOfThird == 3) {

            //SecondHand
            if (MainActivity.currSeconds == 18) {
                drawSecondHandFirstPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 19) {
                drawSecondHandSecondPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 20) {
                drawSecondHandThirdPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 21) {
                drawSecondHandFourthPositionFirstQuart(canvas);
            } else if (MainActivity.currSeconds == 22) {
                drawSecondHandFifthPositionFirstQuart(canvas);
            } else {
                //drawSecondHandAllPositionFirstQuart(canvas);
            }
        }



    }

    private void drawMinuteHandFirstQuart(Canvas canvas, int indexOfThird, int bias) {

        upperRightCornerY = bias + side;
        left = x - side;
        top = upperRightCornerY - side;
        right = x;
        bottom = upperRightCornerY;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 1) {

            //MinuteHand
            if (MainActivity.currMinutes == 8) {
                drawMinuteHandFirstPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 9) {
                drawMinuteHandSecondPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 10) {
                drawMinuteHandThirdPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 11) {
                drawMinuteHandFourthPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 12) {
                drawMinuteHandFifthPositionFirstQuart(canvas);
            }


        } else if (indexOfThird == 2) {

            //MinuteHand
            if (MainActivity.currMinutes == 13) {
                drawMinuteHandFirstPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 14) {
                drawMinuteHandSecondPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 15) {
                drawMinuteHandThirdPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 16) {
                drawMinuteHandFourthPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 17) {
                drawMinuteHandFifthPositionFirstQuart(canvas);
            }

        } else if (indexOfThird == 3) {

            //MinuteHand
            if (MainActivity.currMinutes == 18) {
                drawMinuteHandFirstPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 19) {
                drawMinuteHandSecondPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 20) {
                drawMinuteHandThirdPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 21) {
                drawMinuteHandFourthPositionFirstQuart(canvas);
            } else if (MainActivity.currMinutes == 22) {
                drawMinuteHandFifthPositionFirstQuart(canvas);
            }


        }



    }

    private void drawHourdHandFirstQuart(Canvas canvas, int indexOfThird, int bias) {

        upperRightCornerY = bias + side;
        left = x - side;
        top = upperRightCornerY - side;
        right = x;
        bottom = upperRightCornerY;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 1) {

            //HourdHand
            if ( (MainActivity.currHours == 13 || MainActivity.currHours == 1) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                //drawHourdHandFirstPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 13 || MainActivity.currHours == 1) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionFirstQuart(canvas);
            } else {
                //drawHourdHandAllPositionFirstQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //HourdHand
            if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionFirstQuart(canvas);
            } else {
                //drawHourdHandAllPositionFirstQuart(canvas);
            }

        } else if (indexOfThird == 3) {

            //HourdHand
            if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionFirstQuart(canvas);
            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                //drawHourdHandFifthPositionFirstQuart(canvas);
            } else {
                //drawHourdHandAllPositionFirstQuart(canvas);
            }


        }



    }

    private void drawFirstQuart(Canvas canvas, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {2, 3, 4};
        text = "";
        if (indexOfThird != 0 && indexOfThird != 4) {
            int currentHours = hours[indexOfHour];
            text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
        }

        upperRightCornerY = bias + side;
        left = x - side;
        top = upperRightCornerY - side;
        right = x;
        bottom = upperRightCornerY;

        p.reset();
//        p.setColor(clockColor);
//        p.setStyle(Paint.Style.FILL);
//        canvas.drawRect(left, top, right, bottom, p);

        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontHeight);
        p.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        p.setColor(clockColor);
        canvas.drawRect(left + side * 0.50f - textWidth * 0.50f, top + side * 0.50f - textHeight * 0.50f,
                right - side * 0.50f + textWidth * 0.50f , bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        canvas.drawText(text, left + side * 0.50f - textWidth * 0.50f, bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        //p.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(left, top, right, bottom, p);

        canvas.drawLine(right, top, right - side * 0.50f, top, p);
        canvas.drawLine(right - 1, top, right - 1, bottom, p);
        canvas.drawLine(right, bottom, right - side * 0.50f, bottom, p);

        p.setColor(Color.BLACK);
        canvas.drawLine(left - side * 0.10f, bottom - side * 0.50f - side * 0.20f * 2, left + side * 0.10f, bottom - side * 0.50f - side * 0.20f * 2, p);
        canvas.drawLine(left - side * 0.10f, bottom - side * 0.50f - side * 0.20f, left + side * 0.10f, bottom - side * 0.50f - side * 0.20f, p);
        canvas.drawLine(left - side * 0.20f, bottom - side * 0.50f, left + side * 0.20f, bottom - side * 0.50f, p);
        canvas.drawLine(left - side * 0.10f, bottom - side * 0.50f + side * 0.20f, left + side * 0.10f, bottom - side * 0.50f + side * 0.20f, p);
        canvas.drawLine(left - side * 0.10f, bottom - side * 0.50f + side * 0.20f * 2, left + side * 0.10f, bottom - side * 0.50f + side * 0.20f * 2, p);;


//        p.reset();
//        p.setColor(Color.BLACK);
//        p.setStyle(Paint.Style.FILL);
//
//        p.reset();
//        p.setStyle(Paint.Style.FILL);
//        wallpath.reset();
//
//        if (indexOfThird == 1) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 13 || MainActivity.currHours == 1) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                //drawHourdHandFirstPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 13 || MainActivity.currHours == 1) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionFirstQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionFirstQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 8) {
//                drawMinuteHandFirstPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 9) {
//                drawMinuteHandSecondPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 10) {
//                drawMinuteHandThirdPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 11) {
//                drawMinuteHandFourthPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 12) {
//                drawMinuteHandFifthPositionFirstQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 8) {
//                drawSecondHandFirstPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 9) {
//                drawSecondHandSecondPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 10) {
//                drawSecondHandThirdPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 11) {
//                drawSecondHandFourthPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 12) {
//                drawSecondHandFifthPositionFirstQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionFirstQuart(canvas);
//            }
//
//        } else if (indexOfThird == 2) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 14 || MainActivity.currHours == 2) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionFirstQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionFirstQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 13) {
//                drawMinuteHandFirstPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 14) {
//                drawMinuteHandSecondPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 15) {
//                drawMinuteHandThirdPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 16) {
//                drawMinuteHandFourthPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 17) {
//                drawMinuteHandFifthPositionFirstQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 13) {
//                drawSecondHandFirstPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 14) {
//                drawSecondHandSecondPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 15) {
//                drawSecondHandThirdPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 16) {
//                drawSecondHandFourthPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 17) {
//                drawSecondHandFifthPositionFirstQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionFirstQuart(canvas);
//            }
//        } else if (indexOfThird == 3) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 15 || MainActivity.currHours == 3) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionFirstQuart(canvas);
//            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                //drawHourdHandFifthPositionFirstQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionFirstQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 18) {
//                drawMinuteHandFirstPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 19) {
//                drawMinuteHandSecondPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 20) {
//                drawMinuteHandThirdPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 21) {
//                drawMinuteHandFourthPositionFirstQuart(canvas);
//            } else if (MainActivity.currMinutes == 22) {
//                drawMinuteHandFifthPositionFirstQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 18) {
//                drawSecondHandFirstPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 19) {
//                drawSecondHandSecondPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 20) {
//                drawSecondHandThirdPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 21) {
//                drawSecondHandFourthPositionFirstQuart(canvas);
//            } else if (MainActivity.currSeconds == 22) {
//                drawSecondHandFifthPositionFirstQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionFirstQuart(canvas);
//            }
//        }



    }

    private void drawHourdHandRightPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.30f - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f + side * 0.30f + side * 0.20f,
                top, p);

    }

    private void drawHourdHandLeftPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.30f - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f - side * 0.30f + side * 0.20f,
                top, p);

    }

    private void drawHourdHandFirstPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.25f * 2 - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f + side * 0.25f * 2 + side * 0.20f,
                top, p);

    }

    private void drawHourdHandSecondPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.25f - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f + side * 0.25f + side * 0.20f,
                top, p);


    }

    private void drawHourdHandThirdPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f + side * 0.20f,
                top, p);



    }

    private void drawHourdHandFourthPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.25f - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f - side * 0.25f + side * 0.20f,
                top, p);


    }

    private void drawHourdHandFifthPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.25f * 2 - side * 0.20f,
                top - side * 0.20f * 2,
                left + side * 0.50f - side * 0.25f * 2 + side * 0.20f,
                top, p);

    }

    private void drawHourdHandAllPositionSecondQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, top, p);
    }


    private void drawMinuteHandFirstPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f + side * 0.20f * 2, top);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2, top);
        canvas.drawPath(wallpath, p);


    }

    private void drawMinuteHandSecondPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f + side * 0.20f, top);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f - side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f + side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f, top);
        canvas.drawPath(wallpath, p);


    }

    private void drawMinuteHandThirdPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f, top);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f, top);
        canvas.drawPath(wallpath, p);


    }

    private void drawMinuteHandFourthPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f - side * 0.20f, top);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f - side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f + side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f, top);
        canvas.drawPath(wallpath, p);


    }

    private void drawMinuteHandFifthPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);

//
        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f - side * 0.20f * 2, top);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2 - side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2 + side * 0.20f, top - side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2, top);
        canvas.drawPath(wallpath, p);


    }


    private void drawSecondHandFirstPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f + side * 0.20f * 2, top, side * 0.20f, p);

    }

    private void drawSecondHandSecondPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f + side * 0.20f, top, side * 0.20f, p);

    }

    private void drawSecondHandThirdPositionSecondQuart(@NotNull Canvas canvas){
//
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f, top, side * 0.20f, p);

    }

    private void drawSecondHandFourthPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f - side * 0.20f, top, side * 0.20f, p);

    }

    private void drawSecondHandFifthPositionSecondQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f - side * 0.20f * 2, top, side * 0.20f, p);

    }

    private void drawSecondHandAllPositionSecondQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, top, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, top, p);

    }


    private void drawSecondHandSecondQuart(Canvas canvas, int indexOfThird, int bias) {

        bottomRightCornerX = bias;
        left = bottomRightCornerX;
        top = bottomLeftCornerY;//y-side/2;
        right = bottomRightCornerX + side;
        bottom = bottomLeftCornerY + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3) {

            //SecondHand
            if (MainActivity.currSeconds == 23) {
                drawSecondHandFirstPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 24) {
                drawSecondHandSecondPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 25) {
                drawSecondHandThirdPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 26) {
                drawSecondHandFourthPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 27) {
                drawSecondHandFifthPositionSecondQuart(canvas);
            } else {
                //drawSecondHandAllPositionSecondQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //SecondHand
            if (MainActivity.currSeconds == 28) {
                drawSecondHandFirstPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 29) {
                drawSecondHandSecondPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 30) {
                drawSecondHandThirdPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 31) {
                drawSecondHandFourthPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 32) {
                drawSecondHandFifthPositionSecondQuart(canvas);
            } else {
                //drawSecondHandAllPositionSecondQuart(canvas);
            }
        } else if (indexOfThird == 1) {

            //SecondHand
            if (MainActivity.currSeconds == 33) {
                drawSecondHandFirstPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 34) {
                drawSecondHandSecondPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 35) {
                drawSecondHandThirdPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 36) {
                drawSecondHandFourthPositionSecondQuart(canvas);
            } else if (MainActivity.currSeconds == 37) {
                drawSecondHandFifthPositionSecondQuart(canvas);
            } else {
                //drawSecondHandAllPositionSecondQuart(canvas);
            }
        }

    }

    private void drawMinuteHandSecondQuart(Canvas canvas, int indexOfThird, int bias) {

        bottomRightCornerX = bias;
        left = bottomRightCornerX;
        top = bottomLeftCornerY;//y-side/2;
        right = bottomRightCornerX + side;
        bottom = bottomLeftCornerY + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3) {

            //MinuteHand
            if (MainActivity.currMinutes == 23) {
                drawMinuteHandFirstPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 24) {
                drawMinuteHandSecondPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 25) {
                drawMinuteHandThirdPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 26) {
                drawMinuteHandFourthPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 27) {
                drawMinuteHandFifthPositionSecondQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //MinuteHand
            if (MainActivity.currMinutes == 28) {
                drawMinuteHandFirstPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 29) {
                drawMinuteHandSecondPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 30) {
                drawMinuteHandThirdPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 31) {
                drawMinuteHandFourthPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 32) {
                drawMinuteHandFifthPositionSecondQuart(canvas);
            }

        } else if (indexOfThird == 1) {

            //MinuteHand
            if (MainActivity.currMinutes == 33) {
                drawMinuteHandFirstPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 34) {
                drawMinuteHandSecondPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 35) {
                drawMinuteHandThirdPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 36) {
                drawMinuteHandFourthPositionSecondQuart(canvas);
            } else if (MainActivity.currMinutes == 37) {
                drawMinuteHandFifthPositionSecondQuart(canvas);
            }

        }

    }

    private void drawHourdHandSecondQuart(Canvas canvas, int indexOfThird, int bias) {

        bottomRightCornerX = bias;
        left = bottomRightCornerX;
        top = bottomLeftCornerY;//y-side/2;
        right = bottomRightCornerX + side;
        bottom = bottomLeftCornerY + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3) {

            //HourdHand
            if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandRightPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionSecondQuart(canvas);
            } else {
                //drawHourdHandAllPositionSecondQuart(canvas);
            }


        } else if (indexOfThird == 2) {

            //HourdHand
            if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionSecondQuart(canvas);
            } else {
                //drawHourdHandAllPositionSecondQuart(canvas);
            }


        } else if (indexOfThird == 1) {

            //HourdHand
            if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionSecondQuart(canvas);
            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandLeftPositionSecondQuart(canvas);
            } else {
                //drawHourdHandAllPositionSecondQuart(canvas);
            }

        }

    }

    private void drawSecondQuart(Canvas canvas, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {7, 6, 5};
        text = "";
        if (indexOfThird != 0 && indexOfThird != 4) {
            int currentHours = hours[indexOfHour];
            text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
        }

        bottomRightCornerX = bias;
        left = bottomRightCornerX;
        top = bottomLeftCornerY;//y-side/2;
        right = bottomRightCornerX + side;
        bottom = bottomLeftCornerY + side;

        p.reset();
//        p.setColor(clockColor);
//        p.setStyle(Paint.Style.FILL);
//        canvas.drawRect(left, top, right, bottom, p);

        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontHeight);
        p.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        p.setColor(clockColor);
        canvas.drawRect(left + side * 0.50f - textWidth * 0.50f, top + side * 0.50f - textHeight * 0.50f,
                right - side * 0.50f + textWidth * 0.50f , bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        canvas.drawText(text, left + side * 0.50f - textWidth * 0.50f, bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        //p.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(left, top, right, bottom, p);


        canvas.drawLine(left, bottom - side * 0.50f, left, bottom, p);
        canvas.drawLine(left, bottom - 1, right, bottom - 1, p);
        canvas.drawLine(right, bottom - side * 0.50f, right, bottom, p);

        p.setColor(Color.BLACK);
        canvas.drawLine(left + side * 0.50f - side * 0.20f * 2, top + side * 0.10f, left + side * 0.50f - side * 0.20f * 2, top - side * 0.10f, p);
        canvas.drawLine(left + side * 0.50f - side * 0.20f, top + side * 0.10f, left + side * 0.50f - side * 0.20f, top - side * 0.10f, p);
        canvas.drawLine(left + side * 0.50f, top + side * 0.20f, left + side * 0.50f, top - side * 0.20f, p);
        canvas.drawLine(left + side * 0.50f + side * 0.20f, top + side * 0.10f, left + side * 0.50f + side * 0.20f, top - side * 0.10f, p);
        canvas.drawLine(left + side * 0.50f + side * 0.20f * 2, top + side * 0.10f, left + side * 0.50f + side * 0.20f * 2, top - side * 0.10f, p);


//        p.reset();
//        p.setColor(Color.BLACK);
//        p.setStyle(Paint.Style.FILL);
//
//        p.reset();
//        p.setStyle(Paint.Style.FILL);
//        wallpath.reset();
//
//        if (indexOfThird == 3) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandRightPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 16 || MainActivity.currHours == 4) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionSecondQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionSecondQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 23) {
//                drawMinuteHandFirstPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 24) {
//                drawMinuteHandSecondPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 25) {
//                drawMinuteHandThirdPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 26) {
//                drawMinuteHandFourthPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 27) {
//                drawMinuteHandFifthPositionSecondQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 23) {
//                drawSecondHandFirstPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 24) {
//                drawSecondHandSecondPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 25) {
//                drawSecondHandThirdPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 26) {
//                drawSecondHandFourthPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 27) {
//                drawSecondHandFifthPositionSecondQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionSecondQuart(canvas);
//            }
//
//        } else if (indexOfThird == 2) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 17 || MainActivity.currHours == 5) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionSecondQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionSecondQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 28) {
//                drawMinuteHandFirstPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 29) {
//                drawMinuteHandSecondPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 30) {
//                drawMinuteHandThirdPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 31) {
//                drawMinuteHandFourthPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 32) {
//                drawMinuteHandFifthPositionSecondQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 28) {
//                drawSecondHandFirstPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 29) {
//                drawSecondHandSecondPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 30) {
//                drawSecondHandThirdPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 31) {
//                drawSecondHandFourthPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 32) {
//                drawSecondHandFifthPositionSecondQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionSecondQuart(canvas);
//            }
//        } else if (indexOfThird == 1) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 18 || MainActivity.currHours == 6) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionSecondQuart(canvas);
//            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandLeftPositionSecondQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionSecondQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 33) {
//                drawMinuteHandFirstPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 34) {
//                drawMinuteHandSecondPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 35) {
//                drawMinuteHandThirdPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 36) {
//                drawMinuteHandFourthPositionSecondQuart(canvas);
//            } else if (MainActivity.currMinutes == 37) {
//                drawMinuteHandFifthPositionSecondQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 33) {
//                drawSecondHandFirstPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 34) {
//                drawSecondHandSecondPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 35) {
//                drawSecondHandThirdPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 36) {
//                drawSecondHandFourthPositionSecondQuart(canvas);
//            } else if (MainActivity.currSeconds == 37) {
//                drawSecondHandFifthPositionSecondQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionSecondQuart(canvas);
//            }
//        }

    }


    private void drawHourdHandFirstPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f * 2, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f * 2, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(right,
                top + side * 0.50f + side * 0.25f * 2 - side * 0.20f,
                right + side * 0.20f * 2,
                top + side * 0.50f + side * 0.25f * 2 + side * 0.20f, p);

    }

    private void drawHourdHandSecondPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f * 2, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f * 2, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(right,
                top + side * 0.50f + side * 0.25f - side * 0.20f,
                right + side * 0.20f * 2,
                top + side * 0.50f + side * 0.25f+ side * 0.20f, p);

    }

    private void drawHourdHandThirdPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f * 2, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(right,
                top + side * 0.50f - side * 0.20f,
                right + side * 0.20f * 2,
                top + side * 0.50f + side * 0.20f, p);

    }

    private void drawHourdHandFourthPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f * 2, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(right,
                top + side * 0.50f - side * 0.25f - side * 0.20f,
                right + side * 0.20f * 2,
                top + side * 0.50f - side * 0.25f + side * 0.20f, p);

    }

    private void drawHourdHandFifthPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f * 2, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(right,
                top + side * 0.50f - side * 0.25f * 2 - side * 0.20f,
                right + side * 0.20f * 2,
                top + side * 0.50f - side * 0.25f * 2 + side * 0.20f, p);

    }

    private void drawHourdHandAllPositionThirdQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f * 2, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.25f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.25f * 2, p);

    }


    private void drawMinuteHandFirstPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(right, top + side * 0.50f + side * 0.20f * 2);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f + side * 0.20f * 2 + side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f + side * 0.20f * 2 - side * 0.20f );
        wallpath.lineTo( right, top + side * 0.50f + side * 0.20f * 2);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandSecondPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(right, top + side * 0.50f + side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f + side * 0.20f + side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f + side * 0.20f - side * 0.20f );
        wallpath.lineTo( right, top + side * 0.50f + side * 0.20f);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandThirdPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(right, top + side * 0.50f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f + side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f - side * 0.20f );
        wallpath.lineTo( right, top + side * 0.50f);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandFourthPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(right, top + side * 0.50f - side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f - side * 0.20f + side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f - side * 0.20f - side * 0.20f );
        wallpath.lineTo( right, top + side * 0.50f - side * 0.20f);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandFifthPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(right, top + side * 0.50f - side * 0.20f * 2);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f - side * 0.20f * 2 + side * 0.20f);
        wallpath.lineTo( right + side * 0.40f, top + side * 0.50f - side * 0.20f * 2 - side * 0.20f );
        wallpath.lineTo( right, top + side * 0.50f - side * 0.20f * 2);
        canvas.drawPath(wallpath, p);

    }


    private void drawSecondHandFirstPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(right, top + side * 0.50f + side * 0.20f * 2, side * 0.20f, p);

    }

    private void drawSecondHandSecondPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( right, top + side * 0.50f + side * 0.20f, side * 0.20f, p);

    }

    private void drawSecondHandThirdPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(  right, top + side * 0.50f, side * 0.20f, p);

    }

    private void drawSecondHandFourthPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( right, top + side * 0.50f - side * 0.20f, side * 0.20f, p);

    }

    private void drawSecondHandFifthPositionThirdQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle( right, top + side * 0.50f - side * 0.20f * 2, side * 0.20f, p);

    }

    private void drawSecondHandAllPositionThirdQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f * 2, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f + side * 0.20f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, right, top + side * 0.50f - side * 0.20f * 2, p);

    }

    private void drawSecondHandThirdQuart(Canvas canvas, int indexOfThird, int bias) {

        bottomLeftCornerY = y + bias;
        left = upperLeftCornerX - side;
        top = bottomLeftCornerY;
        right = upperLeftCornerX;
        bottom = bottomLeftCornerY + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();


        if (indexOfThird == 3) {

            //SecondHand
            if (MainActivity.currSeconds == 38) {
                drawSecondHandFirstPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 39) {
                drawSecondHandSecondPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 40) {
                drawSecondHandThirdPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 41) {
                drawSecondHandFourthPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 42) {
                drawSecondHandFifthPositionThirdQuart(canvas);
            } else {
                //drawSecondHandAllPositionThirdQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //SecondHand
            if (MainActivity.currSeconds == 43) {
                drawSecondHandFirstPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 44) {
                drawSecondHandSecondPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 45) {
                drawSecondHandThirdPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 46) {
                drawSecondHandFourthPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 47) {
                drawSecondHandFifthPositionThirdQuart(canvas);
            } else {
                //drawSecondHandAllPositionThirdQuart(canvas);
            }
        } else if (indexOfThird == 1) {

            //SecondHand
            if (MainActivity.currSeconds == 48) {
                drawSecondHandFirstPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 49) {
                drawSecondHandSecondPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 50) {
                drawSecondHandThirdPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 51) {
                drawSecondHandFourthPositionThirdQuart(canvas);
            } else if (MainActivity.currSeconds == 52) {
                drawSecondHandFifthPositionThirdQuart(canvas);
            } else {
                //drawSecondHandAllPositionThirdQuart(canvas);
            }
        }



        bottomLeftCornerY = bottomLeftCornerY + side;

    }

    private void drawMinuteHandThirdQuart(Canvas canvas, int indexOfThird, int bias) {

        bottomLeftCornerY = y + bias;
        left = upperLeftCornerX - side;
        top = bottomLeftCornerY;
        right = upperLeftCornerX;
        bottom = bottomLeftCornerY + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();


        if (indexOfThird == 3) {

            //MinuteHand
            if (MainActivity.currMinutes == 38) {
                drawMinuteHandFirstPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 39) {
                drawMinuteHandSecondPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 40) {
                drawMinuteHandThirdPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 41) {
                drawMinuteHandFourthPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 42) {
                drawMinuteHandFifthPositionThirdQuart(canvas);
            }


        } else if (indexOfThird == 2) {

            //MinuteHand
            if (MainActivity.currMinutes == 43) {
                drawMinuteHandFirstPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 44) {
                drawMinuteHandSecondPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 45) {
                drawMinuteHandThirdPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 46) {
                drawMinuteHandFourthPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 47) {
                drawMinuteHandFifthPositionThirdQuart(canvas);
            }

        } else if (indexOfThird == 1) {

            //MinuteHand
            if (MainActivity.currMinutes == 48) {
                drawMinuteHandFirstPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 49) {
                drawMinuteHandSecondPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 50) {
                drawMinuteHandThirdPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 51) {
                drawMinuteHandFourthPositionThirdQuart(canvas);
            } else if (MainActivity.currMinutes == 52) {
                drawMinuteHandFifthPositionThirdQuart(canvas);
            }

        }



        bottomLeftCornerY = bottomLeftCornerY + side;

    }

    private void drawHourdHandThirdQuart(Canvas canvas, int indexOfThird, int bias) {

        bottomLeftCornerY = y + bias;
        left = upperLeftCornerX - side;
        top = bottomLeftCornerY;
        right = upperLeftCornerX;
        bottom = bottomLeftCornerY + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();


        if (indexOfThird == 3) {

            //HourdHand
            if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                //drawHourdHandFirstPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionThirdQuart(canvas);
            } else {
                //drawHourdHandAllPositionThirdQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //HourdHand
            if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionThirdQuart(canvas);
            } else {
                //drawHourdHandAllPositionThirdQuart(canvas);
            }

        } else if (indexOfThird == 1) {

            //HourdHand
            if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionThirdQuart(canvas);
            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                //drawHourdHandFifthPositionThirdQuart(canvas);
            } else {
                //drawHourdHandAllPositionThirdQuart(canvas);
            }

        }



        bottomLeftCornerY = bottomLeftCornerY + side;

    }

    private void drawThirdQuart(Canvas canvas, int indexOfThird, int bias, int indexOfHour) {

        //int[] hours = {1, 12, 11, 10, 9, 8, 7, 6, 5, 2, 3, 4};
        int fontHeight = side / 2;
        int[] hours = {10, 9, 8};
        text = "";
        if (indexOfThird != 0 && indexOfThird != 4) {
            int currentHours = hours[indexOfHour];
            text = ("" + currentHours).length() == 1 ? "0" + currentHours : "" + currentHours;
        }

        bottomLeftCornerY = y + bias;
        left = upperLeftCornerX - side;
        top = bottomLeftCornerY;
        right = upperLeftCornerX;
        bottom = bottomLeftCornerY + side;

        p.reset();
//        p.setColor(clockColor);
//        p.setStyle(Paint.Style.FILL);
//        canvas.drawRect(left, top, right, bottom, p);

        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontHeight);
        p.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        p.setColor(clockColor);
        canvas.drawRect(left + side * 0.50f - textWidth * 0.50f, top + side * 0.50f - textHeight * 0.50f,
                right - side * 0.50f + textWidth * 0.50f , bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        canvas.drawText(text, left + side * 0.50f - textWidth * 0.50f, bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        //p.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(left, top, right, bottom, p);

        canvas.drawLine(left, top, right - side * 0.50f, top, p);
        canvas.drawLine(left, top, left, bottom, p);
        canvas.drawLine(left, bottom, right - side * 0.50f, bottom, p);

        p.setColor(Color.BLACK);
        canvas.drawLine(right - side * 0.10f, bottom - side * 0.50f - side * 0.20f * 2, right + side * 0.10f, bottom - side * 0.50f - side * 0.20f * 2, p);
        canvas.drawLine(right - side * 0.10f, bottom - side * 0.50f - side * 0.20f, right + side * 0.10f, bottom - side * 0.50f - side * 0.20f, p);
        canvas.drawLine(right - side * 0.20f, bottom - side * 0.50f, right + side * 0.20f, bottom - side * 0.50f, p);
        canvas.drawLine(right - side * 0.10f, bottom - side * 0.50f + side * 0.20f, right + side * 0.10f, bottom - side * 0.50f + side * 0.20f, p);
        canvas.drawLine(right - side * 0.10f, bottom - side * 0.50f + side * 0.20f * 2, right + side * 0.10f, bottom - side * 0.50f + side * 0.20f * 2, p);;


//        p.reset();
//        p.setColor(Color.BLACK);
//        p.setStyle(Paint.Style.FILL);
//        //canvas.drawCircle(side * 2.5f, side * 2.5f, side / 4, p);
//        p.reset();
//        p.setStyle(Paint.Style.FILL);
//        wallpath.reset();
//
//
//        if (indexOfThird == 3) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                //drawHourdHandFirstPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 19 || MainActivity.currHours == 7) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionThirdQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionThirdQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 38) {
//                drawMinuteHandFirstPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 39) {
//                drawMinuteHandSecondPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 40) {
//                drawMinuteHandThirdPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 41) {
//                drawMinuteHandFourthPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 42) {
//                drawMinuteHandFifthPositionThirdQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 38) {
//                drawSecondHandFirstPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 39) {
//                drawSecondHandSecondPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 40) {
//                drawSecondHandThirdPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 41) {
//                drawSecondHandFourthPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 42) {
//                drawSecondHandFifthPositionThirdQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionThirdQuart(canvas);
//            }
//
//        } else if (indexOfThird == 2) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 20 || MainActivity.currHours == 8) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionThirdQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionThirdQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 43) {
//                drawMinuteHandFirstPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 44) {
//                drawMinuteHandSecondPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 45) {
//                drawMinuteHandThirdPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 46) {
//                drawMinuteHandFourthPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 47) {
//                drawMinuteHandFifthPositionThirdQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 43) {
//                drawSecondHandFirstPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 44) {
//                drawSecondHandSecondPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 45) {
//                drawSecondHandThirdPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 46) {
//                drawSecondHandFourthPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 47) {
//                drawSecondHandFifthPositionThirdQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionThirdQuart(canvas);
//            }
//        } else if (indexOfThird == 1) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 21 || MainActivity.currHours == 9) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionThirdQuart(canvas);
//            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                //drawHourdHandFifthPositionThirdQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionThirdQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 48) {
//                drawMinuteHandFirstPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 49) {
//                drawMinuteHandSecondPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 50) {
//                drawMinuteHandThirdPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 51) {
//                drawMinuteHandFourthPositionThirdQuart(canvas);
//            } else if (MainActivity.currMinutes == 52) {
//                drawMinuteHandFifthPositionThirdQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 48) {
//                drawSecondHandFirstPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 49) {
//                drawSecondHandSecondPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 50) {
//                drawSecondHandThirdPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 51) {
//                drawSecondHandFourthPositionThirdQuart(canvas);
//            } else if (MainActivity.currSeconds == 52) {
//                drawSecondHandFifthPositionThirdQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionThirdQuart(canvas);
//            }
//        }



        bottomLeftCornerY = bottomLeftCornerY + side;

    }

    private void drawHourdHandLeftPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.30f - side * 0.20f,
                bottom,
                left + side * 0.50f - side * 0.30f + side * 0.20f,
                bottom + side * 0.20f * 2, p);


    }

    private void drawHourdHandRightPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.30f - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.30f + side * 0.20f,
                bottom + side * 0.20f * 2, p);

    }

    private void drawHourdHandFirstPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.25f * 2 - side * 0.20f,
                bottom,
                left + side * 0.50f - side * 0.25f * 2 + side * 0.20f,
                bottom + side * 0.20f * 2, p);


    }

    private void drawHourdHandSecondPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.25f - side * 0.20f,
                bottom,
                left + side * 0.50f - side * 0.25f + side * 0.20f,
                bottom + side * 0.20f * 2, p);


    }

    private void drawHourdHandThirdPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.20f,
                bottom + side * 0.20f * 2, p);


    }

    private void drawHourdHandFourthPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.25f - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.25f + side * 0.20f,
                bottom + side * 0.20f * 2, p);


    }

    private void drawHourdHandFifthPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);

        //HourdHand
        p.setColor(getResources().getColor(R.color.colorHourdHand));
        canvas.drawRect(left + side * 0.50f + side * 0.25f * 2 - side * 0.20f,
                bottom,
                left + side * 0.50f + side * 0.25f * 2 + side * 0.20f,
                bottom + side * 0.20f * 2, p);

    }

    private void drawHourdHandAllPositionFourthQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f * 2, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.25f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.25f * 2, bottom, p);
    }


    private void drawMinuteHandFirstPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

//
        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f - side * 0.20f * 2, bottom);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f * 2, bottom);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandSecondPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f - side * 0.20f, bottom);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f, bottom);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandThirdPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f, bottom);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f, bottom);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandFourthPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f + side * 0.20f, bottom);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f, bottom);
        canvas.drawPath(wallpath, p);

    }

    private void drawMinuteHandFifthPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);

        //MinuteHand
        p.setColor(getResources().getColor(R.color.colorMinuteHand));
        wallpath.moveTo(left + side * 0.50f + side * 0.20f * 2, bottom);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2 + side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2 - side * 0.20f, bottom + side * 0.40f);
        wallpath.lineTo(left + side * 0.50f + side * 0.20f * 2, bottom);
        canvas.drawPath(wallpath, p);

    }


    private void drawSecondHandFirstPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f - side * 0.20f * 2, bottom, side * 0.20f, p);

    }

    private void drawSecondHandSecondPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f - side * 0.20f, bottom, side * 0.20f, p);

    }

    private void drawSecondHandThirdPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f, bottom, side * 0.20f, p);

    }

    private void drawSecondHandFourthPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f + side * 0.20f, bottom, side * 0.20f, p);

    }

    private void drawSecondHandFifthPositionFourthQuart(@NotNull Canvas canvas){

//        p.setColor(Color.BLACK);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
//        p.setColor(Color.RED);
//        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);


        //SecondHand
        p.setColor(getResources().getColor(R.color.colorSecondHand));
        canvas.drawCircle(left + side * 0.50f + side * 0.20f * 2, bottom, side * 0.20f, p);

    }

    private void drawSecondHandAllPositionFourthQuart(@NotNull Canvas canvas) {

        p.setColor(Color.BLACK);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f * 2, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f - side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f, bottom, p);
        canvas.drawLine(side * 2.5f, side * 2.5f, left + side * 0.50f + side * 0.20f * 2, bottom, p);
    }

    private void drawSecondHandFourthQuart(@NotNull Canvas canvas, int indexOfThird, int bias) {

        upperLeftCornerX = x - bias;
        left = upperLeftCornerX - side;
        top = y;
        right = upperLeftCornerX;
        bottom = y + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3) {

            //SecondHand
            if (MainActivity.currSeconds == 53) {
                drawSecondHandFirstPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 54) {
                drawSecondHandSecondPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 55) {
                drawSecondHandThirdPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 56) {
                drawSecondHandFourthPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 57) {
                drawSecondHandFifthPositionFourthQuart(canvas);
            } else {
                //drawSecondHandAllPositionFourthQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //SecondHand
            if (MainActivity.currSeconds == 58) {
                drawSecondHandFirstPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 59) {
                drawSecondHandSecondPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 0) {
                drawSecondHandThirdPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 1) {
                drawSecondHandFourthPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 2) {
                drawSecondHandFifthPositionFourthQuart(canvas);
            } else {
                //drawSecondHandAllPositionFourthQuart(canvas);
            }
        } else if (indexOfThird == 1) {

            //SecondHand
            if (MainActivity.currSeconds == 3) {
                drawSecondHandFirstPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 4) {
                drawSecondHandSecondPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 5) {
                drawSecondHandThirdPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 6) {
                drawSecondHandFourthPositionFourthQuart(canvas);
            } else if (MainActivity.currSeconds == 7) {
                drawSecondHandFifthPositionFourthQuart(canvas);
            } else {
                //drawSecondHandAllPositionFourthQuart(canvas);
            }
        }

        upperLeftCornerX = upperLeftCornerX - side;

    }

    private void drawMinuteHandFourthQuart(@NotNull Canvas canvas, int indexOfThird, int bias) {

        upperLeftCornerX = x - bias;
        left = upperLeftCornerX - side;
        top = y;
        right = upperLeftCornerX;
        bottom = y + side;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3) {

            //MinuteHand
            if (MainActivity.currMinutes == 53) {
                drawMinuteHandFirstPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 54) {
                drawMinuteHandSecondPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 55) {
                drawMinuteHandThirdPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 56) {
                drawMinuteHandFourthPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 57) {
                drawMinuteHandFifthPositionFourthQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //MinuteHand
            if (MainActivity.currMinutes == 58) {
                drawMinuteHandFirstPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 59) {
                drawMinuteHandSecondPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 0) {
                drawMinuteHandThirdPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 1) {
                drawMinuteHandFourthPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 2) {
                drawMinuteHandFifthPositionFourthQuart(canvas);
            }

        } else if (indexOfThird == 1) {

            //MinuteHand
            if (MainActivity.currMinutes == 3) {
                drawMinuteHandFirstPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 4) {
                drawMinuteHandSecondPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 5) {
                drawMinuteHandThirdPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 6) {
                drawMinuteHandFourthPositionFourthQuart(canvas);
            } else if (MainActivity.currMinutes == 7) {
                drawMinuteHandFifthPositionFourthQuart(canvas);
            }

        }

        upperLeftCornerX = upperLeftCornerX - side;

    }

    private void drawHourdHandFourthQuart(@NotNull Canvas canvas, int indexOfThird, int bias) {

        upperLeftCornerX = x - bias;
        left = upperLeftCornerX - side;
        top = y;
        right = upperLeftCornerX;
        bottom = y + side;

        p.reset();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);

        p.reset();
        p.setStyle(Paint.Style.FILL);
        wallpath.reset();

        if (indexOfThird == 3) {

            //HourdHand
            if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandLeftPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionFourthQuart(canvas);
            } else {
                //drawHourdHandAllPositionFourthQuart(canvas);
            }

        } else if (indexOfThird == 2) {

            //HourdHand
            if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFifthPositionFourthQuart(canvas);
            } else {
                //drawHourdHandAllPositionFourthQuart(canvas);
            }

        } else if (indexOfThird == 1) {

            //HourdHand
            if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandFirstPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
                drawHourdHandSecondPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 1 || MainActivity.currHours == 13) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
                drawHourdHandThirdPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 1 || MainActivity.currHours == 13) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
                drawHourdHandFourthPositionFourthQuart(canvas);
            } else if ( (MainActivity.currHours == 1 || MainActivity.currHours == 13) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
                drawHourdHandRightPositionFourthQuart(canvas);
            } else {
                //drawHourdHandAllPositionFourthQuart(canvas);
            }
        }

        upperLeftCornerX = upperLeftCornerX - side;

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
//        p.setColor(clockColor);
//        p.setStyle(Paint.Style.FILL);
//        canvas.drawRect(left, top, right, bottom, p);

        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontHeight);
        p.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        p.setColor(clockColor);
        canvas.drawRect(left + side * 0.50f - textWidth * 0.50f, top + side * 0.50f - textHeight * 0.50f,
                right - side * 0.50f + textWidth * 0.50f , bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        canvas.drawText(text, left + side * 0.50f - textWidth * 0.50f, bottom - side * 0.50f + textHeight * 0.50f, p);

        p.setColor(Color.BLACK);
        //p.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(left, top, right, bottom, p);

        canvas.drawLine(left, top, left, top + side * 0.50f, p);
        canvas.drawLine(left, top, right, top, p);
        canvas.drawLine(right, top, right, top + side * 0.50f, p);

        p.setColor(Color.BLACK);
        canvas.drawLine(left + side * 0.50f - side * 0.20f * 2, bottom + side * 0.10f, left + side * 0.50f - side * 0.20f * 2, bottom - side * 0.10f, p);
        canvas.drawLine(left + side * 0.50f - side * 0.20f, bottom + side * 0.10f, left + side * 0.50f - side * 0.20f, bottom - side * 0.10f, p);
        canvas.drawLine(left + side * 0.50f, bottom + side * 0.20f, left + side * 0.50f, bottom - side * 0.20f, p);
        canvas.drawLine(left + side * 0.50f + side * 0.20f, bottom + side * 0.10f, left + side * 0.50f + side * 0.20f, bottom - side * 0.10f, p);
        canvas.drawLine(left + side * 0.50f + side * 0.20f * 2, bottom + side * 0.10f, left + side * 0.50f + side * 0.20f * 2, bottom - side * 0.10f, p);


//        p.reset();
//        p.setColor(Color.BLACK);
//        p.setStyle(Paint.Style.FILL);
//
//        p.reset();
//        p.setStyle(Paint.Style.FILL);
//        wallpath.reset();
//
//        if (indexOfThird == 3) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandLeftPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 22 || MainActivity.currHours == 10) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionFourthQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionFourthQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 53) {
//                drawMinuteHandFirstPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 54) {
//                drawMinuteHandSecondPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 55) {
//                drawMinuteHandThirdPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 56) {
//                drawMinuteHandFourthPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 57) {
//                drawMinuteHandFifthPositionFourthQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 53) {
//                drawSecondHandFirstPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 54) {
//                drawSecondHandSecondPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 55) {
//                drawSecondHandThirdPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 56) {
//                drawSecondHandFourthPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 57) {
//                drawSecondHandFifthPositionFourthQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionFourthQuart(canvas);
//            }
//
//        } else if (indexOfThird == 2) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 23 || MainActivity.currHours == 11) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFifthPositionFourthQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionFourthQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 58) {
//                drawMinuteHandFirstPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 59) {
//                drawMinuteHandSecondPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 0) {
//                drawMinuteHandThirdPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 1) {
//                drawMinuteHandFourthPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 2) {
//                drawMinuteHandFifthPositionFourthQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 58) {
//                drawSecondHandFirstPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 59) {
//                drawSecondHandSecondPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 0) {
//                drawSecondHandThirdPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 1) {
//                drawSecondHandFourthPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 2) {
//                drawSecondHandFifthPositionFourthQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionFourthQuart(canvas);
//            }
//        } else if (indexOfThird == 1) {
//
//            //HourdHand
//            if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandFirstPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 0 || MainActivity.currHours == 12) && MainActivity.currMinutes >= 45 && MainActivity.currMinutes < 60) {
//                drawHourdHandSecondPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 1 || MainActivity.currHours == 13) && MainActivity.currMinutes >= 0 && MainActivity.currMinutes < 15) {
//                drawHourdHandThirdPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 1 || MainActivity.currHours == 13) && MainActivity.currMinutes >= 15 && MainActivity.currMinutes < 30) {
//                drawHourdHandFourthPositionFourthQuart(canvas);
//            } else if ( (MainActivity.currHours == 1 || MainActivity.currHours == 13) && MainActivity.currMinutes >= 30 && MainActivity.currMinutes < 45) {
//                drawHourdHandRightPositionFourthQuart(canvas);
//            } else {
//                //drawHourdHandAllPositionFourthQuart(canvas);
//            }
//
//            //MinuteHand
//            if (MainActivity.currMinutes == 3) {
//                drawMinuteHandFirstPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 4) {
//                drawMinuteHandSecondPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 5) {
//                drawMinuteHandThirdPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 6) {
//                drawMinuteHandFourthPositionFourthQuart(canvas);
//            } else if (MainActivity.currMinutes == 7) {
//                drawMinuteHandFifthPositionFourthQuart(canvas);
//            }
//
//            //SecondHand
//            if (MainActivity.currSeconds == 3) {
//                drawSecondHandFirstPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 4) {
//                drawSecondHandSecondPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 5) {
//                drawSecondHandThirdPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 6) {
//                drawSecondHandFourthPositionFourthQuart(canvas);
//            } else if (MainActivity.currSeconds == 7) {
//                drawSecondHandFifthPositionFourthQuart(canvas);
//            } else {
//                //drawSecondHandAllPositionFourthQuart(canvas);
//            }
//        }

        upperLeftCornerX = upperLeftCornerX - side;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        p.reset();
//        p.setColor(clockColor);
//        p.setStyle(Paint.Style.FILL);
//        canvas.drawRect(side, side, side * 4, side * 4, p);

        for (int j = 0; j < 4 ; j++) {
            int k = 0;
            int l = 0;

            for (int i = 0; i < 5; i++) {
                if (j == 0 && i > 0 && i < 4) {
                    drawFourthQuart(canvas, i, k, l);
                } else if (j == 1 && i > 0 && i < 4) {
                    drawThirdQuart(canvas, i, k, l);
                } else if (j == 2 && i > 0 && i < 4) {
                    drawSecondQuart(canvas, i, k, l);
                } else if (j == 3 && i > 0 && i < 4) {
                    drawFirstQuart(canvas, i, k, l);
                }

                if (i > 0 && i < 4) {
                    l += 1;
                }
                k += side;
            }
        }

        //drawHourdHand
        for (int j = 0; j < 4 ; j++) {
            int k = 0;
            int l = 0;

            for (int i = 0; i < 5; i++) {
                if (j == 0 && i > 0 && i < 4) {
                    drawHourdHandFourthQuart(canvas, i, k);
                } else if (j == 1 && i > 0 && i < 4) {
                    drawHourdHandThirdQuart(canvas, i, k);
                } else if (j == 2 && i > 0 && i < 4) {
                    drawHourdHandSecondQuart(canvas, i, k);
                } else if (j == 3 && i > 0 && i < 4) {
                    drawHourdHandFirstQuart(canvas, i, k);
                }

                if (i > 0 && i < 4) {
                    l += 1;
                }
                k += side;
            }
        }

        //drawMinuteHand
        for (int j = 0; j < 4 ; j++) {
            int k = 0;
            int l = 0;

            for (int i = 0; i < 5; i++) {
                if (j == 0 && i > 0 && i < 4) {
                    drawMinuteHandFourthQuart(canvas, i, k);
                } else if (j == 1 && i > 0 && i < 4) {
                    drawMinuteHandThirdQuart(canvas, i, k);
                } else if (j == 2 && i > 0 && i < 4) {
                    drawMinuteHandSecondQuart(canvas, i, k);
                } else if (j == 3 && i > 0 && i < 4) {
                    drawMinuteHandFirstQuart(canvas, i, k);
                }

                if (i > 0 && i < 4) {
                    l += 1;
                }
                k += side;
            }
        }

        //drawSecondHand
        for (int j = 0; j < 4 ; j++) {
            int k = 0;
            int l = 0;

            for (int i = 0; i < 5; i++) {
                if (j == 0 && i > 0 && i < 4) {
                    drawSecondHandFourthQuart(canvas, i, k);
                } else if (j == 1 && i > 0 && i < 4) {
                    drawSecondHandThirdQuart(canvas, i, k);
                } else if (j == 2 && i > 0 && i < 4) {
                    drawSecondHandSecondQuart(canvas, i, k);
                } else if (j == 3 && i > 0 && i < 4) {
                    drawSecondHandFirstQuart(canvas, i, k);
                }

                if (i > 0 && i < 4) {
                    l += 1;
                }
                k += side;
            }
        }


        text = "" + ( ("" + MainActivity.currHours).length() == 1 ? "0" + MainActivity.currHours : "" + MainActivity.currHours )
                + ":" + ( ("" + MainActivity.currMinutes).length() == 1 ? "0" + MainActivity.currMinutes : "" + MainActivity.currMinutes )
                + ":" + ( ("" + MainActivity.currSeconds).length() == 1 ? "0" + MainActivity.currSeconds : "" + MainActivity.currSeconds );

        int fontHeight = side / 2;

        p.reset();
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontHeight);
        p.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        p.setColor(clockColor);
        canvas.drawRect(side * 2.5f - textWidth * 0.5f, side * 2.5f - textHeight * 0.5f,
                side * 2.5f + textWidth * 0.5f, side * 2.5f + textHeight * 0.5f, p);

        p.setColor(Color.BLACK);
        canvas.drawText(text, side * 2.5f - textWidth * 0.5f, side * 2.5f + textHeight * 0.5f, p);




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
        final float evX = event.getRawX();
        final float evY = event.getRawY();


        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:

                countDownTimerX.cancel();
                countDownTimerY.cancel();
                // включаем режим перетаскивания
                drag = true;
                params = (ConstraintLayout.LayoutParams) getLayoutParams();

                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX - params.leftMargin;
                dragY = evY - params.topMargin;

                break;
            // тащим
            case MotionEvent.ACTION_MOVE:


                // если режим перетаскивания включен
                if (drag) {

                    params = (ConstraintLayout.LayoutParams) getLayoutParams();
                    if ( (getWidth() + (int)(evX - dragX)) <= constraintLayout.getWidth()
                            && params.leftMargin >= 0 ){

                        params.leftMargin = (int)(evX - dragX);
                    }else{
                        if((getWidth() + params.leftMargin) > constraintLayout.getWidth()){
                            params.leftMargin = constraintLayout.getWidth() - getWidth();
                        }
                        if(params.leftMargin < 0){
                            params.leftMargin = 0;
                        }
                    }
                    if ( (getHeight() + (int)(evY - dragY)) <= constraintLayout.getHeight()
                            && params.topMargin >= 0 ){

                        params.topMargin = (int)(evY - dragY);
                    }else{
                        if((getHeight() + params.topMargin) > constraintLayout.getHeight()){
                            params.topMargin = constraintLayout.getHeight() - getHeight();
                        }
                        if(params.topMargin < 0){
                            params.topMargin = 0;
                        }
                    }

                    setLayoutParams(params);
                }

                break;
            // касание завершено
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // выключаем режим перетаскивания
                drag = false;
                break;

        }

        gestureDetector.onTouchEvent(event);

        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            clockColor = Color.WHITE;
            MainActivity.winter.selectedDay = null;
            MainActivity.spring.selectedDay = null;
            MainActivity.summer.selectedDay = null;
            MainActivity.autumn.selectedDay = null;

            MainActivity.numberYearPicker.setValue(MainActivity.curentYearNumber);

            return super.onDoubleTap(e);
        }

//        @Override
//        public void onLongPress(MotionEvent e) {
//
//            // Внимание! код дублируется в событии onDoubleTap()
//            clockColor = Color.WHITE;
//            MainActivity.winter.selectedDay = null;
//            MainActivity.spring.selectedDay = null;
//            MainActivity.summer.selectedDay = null;
//            MainActivity.autumn.selectedDay = null;
//
//            MainActivity.numberYearPicker.setValue(MainActivity.curentYearNumber);
//
//            super.onLongPress(e);
//        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {

            hitX = false;
            hitY = false;
            scrollTimeX = (int) velocityX;
            scrollTimeY = (int) velocityY;

            if (scrollTimeX < 0){
                scrollTimeX *= -1;
            }
            if (scrollTimeY < 0){
                scrollTimeY *= -1;
            }

            params = (ConstraintLayout.LayoutParams) getLayoutParams();

            countDownTimerX = new CountDownTimer(scrollTimeX, 5) {

                @Override
                public void onTick(long millisUntilFinished) {

                    if ( (getWidth() + params.leftMargin) <= constraintLayout.getWidth() &&
                            params.leftMargin >= 0) {

                        if (velocityX > 0 && !hitX) {
                            params.leftMargin += millisUntilFinished / 75;
                        } else if (velocityX < 0 && !hitX) {
                            params.leftMargin -= millisUntilFinished / 75;
                        }
                        if (velocityX > 0 && hitX) {
                            params.leftMargin -= millisUntilFinished / 75;
                        } else if (velocityX < 0 && hitX) {
                            params.leftMargin += millisUntilFinished / 75;
                        }
                    }else{
                        if ( getWidth() + params.leftMargin > constraintLayout.getWidth() ) {
                            params.leftMargin = constraintLayout.getWidth() - getWidth();
                        }
                        if (params.leftMargin < 0) {
                            params.leftMargin = 0;
                        }

                        hitX = !hitX;
                    }
                    setLayoutParams(params);
                }

                @Override
                public void onFinish() {
                }
            }.start();

            countDownTimerY = new CountDownTimer(scrollTimeY, 5) {

                @Override
                public void onTick(long millisUntilFinished) {

                    if ( (getHeight() + params.topMargin) <= constraintLayout.getHeight() &&
                            params.topMargin >= 0) {
                        if (velocityY > 0 && !hitY) {
                            params.topMargin += millisUntilFinished / 75;
                        } else if (velocityY < 0 && !hitY) {
                            params.topMargin -= millisUntilFinished / 75;
                        }
                        if (velocityY > 0 && hitY) {
                            params.topMargin -= millisUntilFinished / 75;
                        } else if (velocityY < 0 && hitY) {
                            params.topMargin += millisUntilFinished / 75;
                        }
                    }else{
                        if ( getHeight() + params.topMargin > constraintLayout.getHeight() ) {
                            params.topMargin = constraintLayout.getHeight() - getHeight();
                        }
                        if (params.topMargin < 0) {
                            params.topMargin = 0;
                        }

                        hitY = !hitY;
                    }
                    setLayoutParams(params);
                }

                @Override
                public void onFinish() {
                }
            }.start();



            return true;
        }
    }

}
