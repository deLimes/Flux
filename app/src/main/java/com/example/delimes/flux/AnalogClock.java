package com.example.delimes.flux;

import android.content.Context;
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

    boolean firstOccurrence = true;

    private Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("LOG", "Runnable Tick");
            MainActivity.onTimeChanged();
            invalidate();
            MainActivity.tickHandler.postDelayed(mTickRunnable, 1000);
        }
    };

    public AnalogClock(Context context) {
        super(context);

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

        int l = 0;
        int g = 0;
        for (int j = 0; j < 4 ; j++) {
            int k = 0;

            for (int i = 1; i <= 5; i++) {
                String text = "";
                if (i != 1 && i != 5) {
                    l += 1;
                    text = ("" + l).length() == 1 ? "0" + l : "" + l;
                }

                if (j == 0) {
                    /*
                    if (firstOccurrence){
                        x = side * 5;
                        y = 0;
                    }
                    */
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

                k += side;
            }
        }

        if (firstOccurrence) {
            firstOccurrence = false;
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
