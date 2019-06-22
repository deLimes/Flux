package com.example.delimes.flux;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static com.example.delimes.flux.MainActivity.chosenYearNumber;
import static com.example.delimes.flux.MainActivity.taskExtra;

/**
 * Created by User on 20.06.2019.
 */

class Quarter extends View {
    Context context;
    final int quarter;
    Paint p;
    // координаты для рисования квадрата
    float x = 0;
    float y = 0;
    int side = 0;
    int width = 0;
    int height = 0;
    float doubleTapX = 0;
    float doubleTapY = 0;
    float januaryLength = 0;
    float februaryLength = 0;
    float marchLength = 0;
    float aprilLength = 0;
    float mayLength = 0;
    float juneLength = 0;
    float julyLength = 0;
    float augustLength = 0;
    float septemberLength = 0;
    float octoberLength = 0;
    float novemberLength = 0;
    float decemberLength = 0;

    float left = 0;
    float top =  0;
    float right = 0;
    float bottom = 0;

    Day selectedDay = null;
    Day currentDate = null;
    ArrayList<Day> days = new ArrayList<Day>();
    String monthName, reverseMonthName;;
    Rect textBounds = new Rect();
    SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
    boolean restore;
    boolean addCyclicTasks = false;

    Calendar calendar = GregorianCalendar.getInstance();
    public MainActivity mainActivity;

    boolean firstOccurrence = true;
    int scrollTime = 0;
    CountDownTimer countDownTimer = new CountDownTimer(0, 0) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
        }
    };

    // переменные для перетаскивания
    boolean drag = false;
    float dragX = 0;
    float dragY = 0;

    float posY = 0;
    float previousDragDirection = 0;

    private GestureDetector gestureDetector;

    float upperLeftCornerX = 0;
    float bottomLeftCornerY = 0;
    float upperRightCornerY = 0;
    float bottomRightCornerX = 0;

    boolean incrementYearWinter = false;
    boolean incrementYearSpring = false;
    boolean incrementYearSummer = false;
    boolean incrementYearAutumn = false;
    boolean decrementYearWinter = false;
    boolean decrementYearSpring = false;
    boolean decrementYearSummer = false;
    boolean decrementYearAutumn = false;

    //boolean timerIsON = false;

    float length = 0;

    public Quarter(Context context, int quarter) {
        super(context);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        this.quarter = quarter;
        init(context);

    }

    public Quarter(Context context, AttributeSet attrs, int quarter) {
        super(context, attrs);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        this.quarter = quarter;
        init(context);
    }

    public Quarter(Context context, AttributeSet attrs, int defStyle, int quarter) {
        super(context, attrs, defStyle);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        this.quarter = quarter;
        init(context);
    }

    private void init(Context context) {

        MainActivity.alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //Display display = getWindowManager().getDefaultDisplay();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = (int) (size.x * 0.15f);
        int displayHeight =  size.y;
        this.side = width / 2;

//        if(quarter == 1) {
//            this.x = size.x;
//            this.y = width;
//        }else if (quarter == 2){
//            this.x = width - width / 2;
//            this.y = 0;
//        }else if (quarter == 3){
//            this.x = 0;
//            this.y = width - width / 2;
//        }else if (quarter == 4){
//            this.x = width - width / 2;
//            this.y = height - width * 2;
//        }

        p = new Paint();
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        calendar.clear();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(year, month, day);
        MainActivity.currDate = new Date(calendar.getTimeInMillis());
        fillInDays(mainActivity.chosenYearNumber);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        if (quarter == 1) {
            canvas.drawColor(Color.rgb(106, 90, 205));
        }else if (quarter == 2){
            canvas.drawColor(Color.rgb(0, 255, 127));
        }else if (quarter == 3){
            canvas.drawColor(Color.YELLOW);
        }else if (quarter == 4){
            canvas.drawColor(Color.rgb(255, 215, 0));
        }
        drawQuarter(canvas);
    }

    public void fillInDays(int year){

        int l = 0;

        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        if (quarter == 1){
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
        }else if(quarter == 2){
            calendar.set(Calendar.MONTH, Calendar.APRIL);
        }else if(quarter == 3){
            calendar.set(Calendar.MONTH, Calendar.JULY);
        }else if(quarter == 4){
            calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        }

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int k = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;
//            if (quarter == 3) {
//                k += side;
//            }

            calendar.clear();
            if (quarter == 1){
                upperLeftCornerX = x - k;
                calendar.set(year, 0, i);
            }else if (quarter == 2){
                bottomLeftCornerY = y + k;
                calendar.set(year, 3, i);
            }else if (quarter == 3){
                bottomRightCornerX = x + k;
                calendar.set(year, 6, i);
            }else if (quarter == 4){
                upperRightCornerY = y - k;
                calendar.set(year, 9, i);
            }

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, left, top, right, bottom));
//
            if (date.getTime() == MainActivity.currDate.getTime()) {
                mainActivity.winter.currentDate = null;
                mainActivity.spring.currentDate = null;
                mainActivity.summer.currentDate = null;
                mainActivity.autumn.currentDate = null;

                if (quarter == 1) {
                    mainActivity.winter.currentDate = days.get(days.size() - 1);
                }else if (quarter == 2){
                    mainActivity.spring.currentDate = days.get(days.size() - 1);
                }else if (quarter == 3){
                    mainActivity.summer.currentDate = days.get(days.size() - 1);
                }else if (quarter == 4){
                    mainActivity.autumn.currentDate = days.get(days.size() - 1);
                }
            }
            if (selectedDay != null) {
                if (selectedDay.date.getMonth() == date.getMonth() &&
                        selectedDay.date.getDate() == date.getDate()) {
                    selectedDay = days.get(days.size() - 1);

                }
            }
//            if (quarter !=3){
//                k += side;
//            }
            k += side;
        }
//        if (side > 0) {
//            januaryLength = -upperLeftCornerX + getWidth()* 1.5f;
//            aprilLength = -bottomLeftCornerY + getHeight()/2;
//            julyLength = -bottomRightCornerX + getWidth()/2 ;
//            octoberLength = -upperRightCornerY + getHeight() * 1.5f;
//        }

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        if (quarter == 1){
            calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        }else if(quarter == 2){
            calendar.set(Calendar.MONTH, Calendar.MAY);
        }else if(quarter == 3){
            calendar.set(Calendar.MONTH, Calendar.AUGUST);
        }else if(quarter == 4){
            calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        }

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
//            if (quarter == 3) {
//                k += side;
//            }

            calendar.clear();
            if (quarter == 1){
                upperLeftCornerX = x - k;
                calendar.set(year, 1, i);
            }else if (quarter == 2){
                bottomLeftCornerY = y + k;
                calendar.set(year, 4, i);
            }else if (quarter == 3){
                bottomRightCornerX = x + k;
                calendar.set(year, 7, i);
            }else if (quarter == 4){
                upperRightCornerY = y - k;
                calendar.set(year, 10, i);
            }
            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, left, top, right, bottom));

            if (date.getTime() == MainActivity.currDate.getTime()) {
                mainActivity.winter.currentDate = null;
                mainActivity.spring.currentDate = null;
                mainActivity.summer.currentDate = null;
                mainActivity.autumn.currentDate = null;

                if (quarter == 1) {
                    mainActivity.winter.currentDate = days.get(days.size() - 1);
                }else if (quarter == 2){
                    mainActivity.spring.currentDate = days.get(days.size() - 1);
                }else if (quarter == 3){
                    mainActivity.summer.currentDate = days.get(days.size() - 1);
                }else if (quarter == 4){
                    mainActivity.autumn.currentDate = days.get(days.size() - 1);
                }
            }
            if (selectedDay != null) {
                if (selectedDay.date.getMonth() == date.getMonth() &&
                        selectedDay.date.getDate() == date.getDate()) {
                    selectedDay = days.get(days.size() - 1);

                }
            }
//            if (quarter !=3){
//                k += side;
//            }
            k += side;
        }
//        if (side > 0) {
//            februaryLength = -upperLeftCornerX + getWidth() * 1.5f;
//            mayLength = -bottomLeftCornerY + getHeight()/2;
//            augustLength = -bottomRightCornerX + getWidth()/2;
//            novemberLength = -upperRightCornerY + getHeight() * 1.5f;
//        }

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        if (quarter == 1){
            calendar.set(Calendar.MONTH, Calendar.MARCH);
        }else if(quarter == 2){
            calendar.set(Calendar.MONTH, Calendar.JUNE);
        }else if(quarter == 3){
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        }else if(quarter == 4){
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        }

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
//            if (quarter == 3) {
//                k += side;
//            }

            calendar.clear();
            if (quarter == 1){
                upperLeftCornerX = x - k;
                calendar.set(year, 2, i);
            }else if (quarter == 2){
                bottomLeftCornerY = y + k;
                calendar.set(year, 5, i);
            }else if (quarter == 3){
                bottomRightCornerX = x + k;
                calendar.set(year, 8, i);
            }else if (quarter == 4){
                upperRightCornerY = y - k;
                calendar.set(year, 11, i);
            }
            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, left, top, right, bottom));

            if (date.getTime() == MainActivity.currDate.getTime()) {
                mainActivity.winter.currentDate = null;
                mainActivity.spring.currentDate = null;
                mainActivity.summer.currentDate = null;
                mainActivity.autumn.currentDate = null;

                if (quarter == 1) {
                    mainActivity.winter.currentDate = days.get(days.size() - 1);
                }else if (quarter == 2){
                    mainActivity.spring.currentDate = days.get(days.size() - 1);
                }else if (quarter == 3){
                    mainActivity.summer.currentDate = days.get(days.size() - 1);
                }else if (quarter == 4){
                    mainActivity.autumn.currentDate = days.get(days.size() - 1);
                }
            }
            if (selectedDay != null) {
                if (selectedDay.date.getMonth() == date.getMonth() &&
                        selectedDay.date.getDate() == date.getDate()) {
                    selectedDay = days.get(days.size() - 1);

                }
            }
//            if (quarter !=3){
//                k += side;
//            }
            k += side;
        }
        if (side > 0) {
//            marchLength = -upperLeftCornerX + getWidth() * 1.5f - side/2;
//            juneLength = -bottomLeftCornerY + getHeight()/2;
//            septemberLength = -bottomRightCornerX + getWidth()/2;
//            decemberLength = -upperRightCornerY + getHeight() * 1.5f;

            if (quarter == 1) {
                //length = -upperLeftCornerX + getWidth() + side;
                calendar.clear();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
                maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                length = (31+maxDaysOfMonth+31)*side;
            }else if (quarter == 2){
                //length = -bottomLeftCornerY + getHeight() - side;
                //length = -bottomLeftCornerY + getHeight() - side*5;
                length = (30+31+30) * side ;
            }else if (quarter == 3){
                //length = -bottomRightCornerX + getWidth();
                //length = -bottomRightCornerX + getWidth() - side;
                length = (31+31+30) * side;
            }else if (quarter == 4) {
                //length = -upperRightCornerY + getHeight() + side;
                //length = -upperRightCornerY + getHeight() - side*3;
                length = (31+30+31) * side;
            }
        }
    }

    public void drawQuarter(Canvas canvas){

        int fontHeight = side / 2;
        int strokeWidth = side / 5;
        float monthNameHeight;
        float monthNameWidth;

        int l = 0;

        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        if (quarter == 1) {
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
        }else if(quarter == 2){
            calendar.set(Calendar.MONTH, Calendar.APRIL);
        }else if(quarter == 3){
            calendar.set(Calendar.MONTH, Calendar.JULY);
        }else if(quarter == 4){
            calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        }

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();

        p.getTextBounds(monthName, 0, monthName.length(), textBounds);
        monthNameHeight = textBounds.height();
        monthNameWidth = textBounds.width();

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int k = 0;
        int g = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;
            if (quarter == 3) {
                k += side;
            }

            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            if (quarter == 1) {
                upperLeftCornerX = x - k;
                left = upperLeftCornerX - side;
                top = y-side;
                right = upperLeftCornerX;
                bottom = y;
            }else if (quarter == 2){
                bottomLeftCornerY = y + k;
                left = x;
                top =  bottomLeftCornerY;
                right =  x+side;
                bottom = bottomLeftCornerY + side;
            }else if (quarter == 3){
                bottomRightCornerX = x + k;
                left = bottomRightCornerX - side;
                top = y - side;//y-side/2;
                right = bottomRightCornerX;
                bottom = y;
            }else if (quarter == 4){
                upperRightCornerY = y - k;
                left = x-side;
                top =  upperRightCornerY -side;
                right =  x;
                bottom = upperRightCornerY;
            }

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);
            p.setStyle(Paint.Style.FILL);
//            if (quarter == 4){
//                p.setTextAlign(Paint.Align.CENTER);
//            }

            calendar.clear();
            if (quarter == 1) {
                calendar.set(mainActivity.numberYearPicker.getValue(), 0, i);
            }else if (quarter == 2){
                calendar.set(mainActivity.numberYearPicker.getValue(), 3, i);
            }else if (quarter == 3){
                calendar.set(mainActivity.numberYearPicker.getValue(), 6, i);
            }else if (quarter == 4){
                calendar.set(mainActivity.numberYearPicker.getValue(), 9, i);
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            canvas.drawText(text, left + side / 4, bottom - side / 4, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
//            if (firstOccurrence) {
//                Date date = new Date(calendar.getTimeInMillis());
//                days.add(new Day(date, left, top, right, bottom));
//
//                if (date.getTime() == MainActivity.currDate.getTime()) {
//                    currentDate = days.get(g);
//                    mainActivity.autumn.currentDate = null;
//                    mainActivity.spring.currentDate = null;
//                    mainActivity.summer.currentDate = null;
//                }
//
//                if (selectedDay != null) {
//                    if (selectedDay.date.getMonth() == date.getMonth() &&
//                            selectedDay.date.getDate() == date.getDate() ) {
//                        selectedDay = days.get(g);
//
//                    }
//                }
//            }else{
//                days.get(l).left = left;
//                days.get(l).top = top;
//                days.get(l).right = right;
//                days.get(l).bottom = bottom;
//
//                if (currentDate != null) {
//                    calendar.clear();
//                    calendar.set(mainActivity.numberYearPicker.getValue(), 0, i);
//                    Date date = new Date(calendar.getTimeInMillis());
//
//                    if (date.getTime() == MainActivity.currDate.getTime()) {
//                        currentDate = days.get(g);
//
//                        currentDate.left = left;
//                        currentDate.top = top;
//                        currentDate.right = right;
//                        currentDate.bottom = bottom;
//                    }
//                }
//
//                if(!days.get(l).dayClosed){
//                    p.setColor(Color.CYAN);
//                    p.setStrokeWidth(strokeWidth/2);
//                    p.setStyle(Paint.Style.STROKE);
//                    canvas.drawRect(left, top, right, bottom, p);
//                }
//
//                for (MainActivity.Task task : days.get(l).tasks) {
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                    if(sdf.format(new Date(task.startTime)).equals(sdf.format(days.get(l).date))){
//                        p.setColor(Color.rgb(221, 160, 221));
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                        p.setStyle(Paint.Style.FILL);
//                    }
//
//                    if(sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))){
//                        p.setColor(Color.rgb(139, 0, 139));
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                        p.setStyle(Paint.Style.FILL);
//                    }
//                }
//
//            }

            days.get(l).left = left;
            days.get(l).top = top;
            days.get(l).right = right;
            days.get(l).bottom = bottom;

            if (currentDate != null) {
                calendar.clear();
                if (quarter == 1) {
                    calendar.set(mainActivity.numberYearPicker.getValue(), 0, i);
                }else if (quarter == 2){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 3, i);
                }else if (quarter == 3){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 6, i);
                }else if (quarter == 4){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 9, i);
                }
                Date date = new Date(calendar.getTimeInMillis());

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(g);

                    currentDate.left = left;
                    currentDate.top = top;
                    currentDate.right = right;
                    currentDate.bottom = bottom;
                }
            }

            if (!days.get(l).dayClosed) {
                p.setColor(Color.CYAN);
                p.setStrokeWidth(strokeWidth / 2);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(left, top, right, bottom, p);
            }

            for (MainActivity.Task task : days.get(l).tasks) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                if (sdf.format(new Date(task.startTime)).equals(sdf.format(days.get(l).date))) {
                    p.setColor(Color.rgb(221, 160, 221));
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(left, top, right, bottom, p);
                    p.setStyle(Paint.Style.FILL);
                }

                if (sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))) {
                    p.setColor(Color.rgb(139, 0, 139));
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(left, top, right, bottom, p);
                    p.setStyle(Paint.Style.FILL);
                }
            }

            if (quarter !=3){
                k += side;
            }
            g += 1;
        }
        if (firstOccurrence) {
            januaryLength = -upperLeftCornerX + getWidth()* 1.5f;
            aprilLength = -bottomLeftCornerY + getHeight()/2;
            julyLength = -bottomRightCornerX + getWidth()/2;
            octoberLength = -upperRightCornerY + getHeight() * 1.5f;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        if (quarter == 1) {
            if (x <= januaryLength) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(monthName, getWidth() / 2, y - side * 1.5f, p);
            } else {
                p.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
            }
        }else if (quarter == 2){
            p.setTextAlign(Paint.Align.CENTER);
            if (y >= aprilLength ) {

                //canvas.drawText("April", x - side, getHeight() / 2, p);
                canvas.save();
                canvas.rotate(360f);
                int s = getHeight() / 2 + side;
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            } else {
                //p.setTextAlign(Paint.Align.CENTER);
                //canvas.drawText("January", upperLeftCornerX - side/2, y - side * 1.5f, p);
                canvas.save();
                canvas.rotate(360f);
                int s = (int) bottomLeftCornerY + side;
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            }
        }else if (quarter == 3){
            if (x >= julyLength  + monthNameWidth/2) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(monthName, getWidth() / 2, y + side / 2, p);
            } else {
                p.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(monthName, bottomRightCornerX, y + side / 2, p);
            }
        }else if (quarter == 4){
            p.setTextAlign(Paint.Align.CENTER);
            if (y <= octoberLength - reverseMonthName.length()*fontHeight/2 + side) {
                canvas.save();
                canvas.rotate(360f);
                int s = getHeight() / 2 +reverseMonthName.length()*fontHeight/2;
                for (char c : (reverseMonthName).toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            } else {
                canvas.save();
                canvas.rotate(360f);
                int s = (int) upperRightCornerY - side/2;
                for (char c : monthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s += fontHeight;
                }
                canvas.restore();
            }
        }

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        if (quarter == 1) {
            calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        }else if(quarter == 2){
            calendar.set(Calendar.MONTH, Calendar.MAY);
        }else if(quarter == 3){
            calendar.set(Calendar.MONTH, Calendar.AUGUST);
        }else if(quarter == 4){
            calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        }

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();

        p.getTextBounds(monthName, 0, monthName.length(), textBounds);
        monthNameHeight = textBounds.height();
        monthNameWidth = textBounds.width();

        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);
        //p.setStrokeWidth(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
            if (quarter == 3) {
                k += side;
            }

            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            if (quarter == 1) {
                upperLeftCornerX = x - k;
                left = upperLeftCornerX - side;
                top = y-side;
                right = upperLeftCornerX;
                bottom = y;
            }else if (quarter == 2){
                bottomLeftCornerY = y + k;
                left = x;
                top =  bottomLeftCornerY;
                right =  x + side;
                bottom = bottomLeftCornerY + side;
            }else if (quarter == 3){
                bottomRightCornerX = x + k;
                left = bottomRightCornerX - side;
                top = y - side;//y-side/2;
                right = bottomRightCornerX;
                bottom = y;
            }else if (quarter == 4){
                upperRightCornerY = y - k;
                left = x-side;
                top =  upperRightCornerY -side;
                right =  x;
                bottom = upperRightCornerY;
            }

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);
            p.setStyle(Paint.Style.FILL);

            calendar.clear();
            if (quarter == 1) {
                calendar.set(mainActivity.numberYearPicker.getValue(), 1, i);
            }else if (quarter == 2){
                calendar.set(mainActivity.numberYearPicker.getValue(), 4, i);
            }else if (quarter == 3){
                calendar.set(mainActivity.numberYearPicker.getValue(), 7, i);
            }else if (quarter == 4){
                calendar.set(mainActivity.numberYearPicker.getValue(), 10, i);
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            //canvas.drawText(text, left, y+side/4, p);
            if (quarter == 1) {
                canvas.drawText(text, left + side / 4, y - side / 4, p);
            }else {
                canvas.drawText(text, left + side / 4, bottom - side / 4, p);
            }

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
//            if (firstOccurrence) {
//                Date date = new Date(calendar.getTimeInMillis());
//                days.add(new Day(date, left, top, right, bottom));
//
//                if (date.getTime() == MainActivity.currDate.getTime()) {
//                    //currentDate = days.get(days.size()-1);
//                    currentDate = days.get(g);
//                    mainActivity.autumn.currentDate = null;
//                    mainActivity.spring.currentDate = null;
//                    mainActivity.summer.currentDate = null;
//                }
//
//                if (selectedDay != null) {
//                    if (selectedDay.date.getMonth() == date.getMonth() &&
//                            selectedDay.date.getDate() == date.getDate() ) {
//                        //selectedDay = days.get(days.size()-1);
//                        selectedDay = days.get(g);
//
//                    }
//                }
//            }else{
//                days.get(l).left = left;
//                days.get(l).top = top;
//                days.get(l).right = right;
//                days.get(l).bottom = bottom;
//
//                if (currentDate != null) {
//                    calendar.clear();
//                    calendar.set(mainActivity.numberYearPicker.getValue(), 1, i);
//                    Date date = new Date(calendar.getTimeInMillis());
//
//                    if (date.getTime() == MainActivity.currDate.getTime()) {
//                        currentDate = days.get(g);
//
//                        currentDate.left = left;
//                        currentDate.top = top;
//                        currentDate.right = right;
//                        currentDate.bottom = bottom;
//                    }
//                }
//
//                if(!days.get(l).dayClosed){
//                    p.setColor(Color.CYAN);
//                    p.setStrokeWidth(strokeWidth/2);
//                    p.setStyle(Paint.Style.STROKE);
//                    canvas.drawRect(left, top, right, bottom, p);
//                }
//
//                for (MainActivity.Task task : days.get(l).tasks) {
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                    if(sdf.format(new Date(task.startTime)).equals(sdf.format(days.get(l).date))){
//                        p.setColor(Color.rgb(221, 160, 221));
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                        p.setStyle(Paint.Style.FILL);
//                    }
//
//                    if(sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))){
//                        p.setColor(Color.rgb(139, 0, 139));
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                        p.setStyle(Paint.Style.FILL);
//                    }
//                }
//
//            }

            days.get(l).left = left;
            days.get(l).top = top;
            days.get(l).right = right;
            days.get(l).bottom = bottom;

            if (currentDate != null) {
                calendar.clear();
                if (quarter == 1) {
                    calendar.set(mainActivity.numberYearPicker.getValue(), 1, i);
                }else if (quarter == 2){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 4, i);
                }else if (quarter == 3){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 7, i);
                }else if (quarter == 4){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 10, i);
                }
                Date date = new Date(calendar.getTimeInMillis());

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(g);

                    currentDate.left = left;
                    currentDate.top = top;
                    currentDate.right = right;
                    currentDate.bottom = bottom;
                }
            }

            if (!days.get(l).dayClosed) {
                p.setColor(Color.CYAN);
                p.setStrokeWidth(strokeWidth / 2);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(left, top, right, bottom, p);
            }

            for (MainActivity.Task task : days.get(l).tasks) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                if (sdf.format(new Date(task.startTime)).equals(sdf.format(days.get(l).date))) {
                    p.setColor(Color.rgb(221, 160, 221));
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(left, top, right, bottom, p);
                    p.setStyle(Paint.Style.FILL);
                }

                if (sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))) {
                    p.setColor(Color.rgb(139, 0, 139));
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(left, top, right, bottom, p);
                    p.setStyle(Paint.Style.FILL);
                }
            }
            if (quarter !=3){
                k += side;
            }
            g += 1;
        }
        if (firstOccurrence) {
            februaryLength = -upperLeftCornerX + getWidth() * 1.5f;
            mayLength = -bottomLeftCornerY + getHeight()/2;
            augustLength = -bottomRightCornerX + getWidth()/2;
            novemberLength = -upperRightCornerY + getHeight() * 1.5f;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        if (quarter == 1) {
            if (x <= februaryLength && x >= januaryLength + monthNameWidth) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(monthName, getWidth() / 2, y - side * 1.5f, p);
            } else if (x <= februaryLength) {
                p.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(monthName, upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
            } else {
                p.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
            }
        }else if (quarter == 2){
            p.setTextAlign(Paint.Align.CENTER);
            if (y >= mayLength && y <= aprilLength - side * 1.5f) {
                canvas.save();
                canvas.rotate(360f);
                int s = getHeight() / 2 + side;
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            }else if(y >= mayLength + side*2){
                canvas.save();
                canvas.rotate(360f);
                int s = (int) (bottomLeftCornerY + (mayLength - aprilLength)+ side * 1.5f);
                for (char c : monthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s += fontHeight;
                }
                canvas.restore();
            } else if(y <= mayLength){
                canvas.save();
                canvas.rotate(360f);
                int s = (int) bottomLeftCornerY + side;
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            }
        }else if (quarter == 3){
            if(x <= julyLength - monthNameWidth/2  && x >= augustLength + monthNameWidth/2) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(monthName, getWidth()/2, y + side / 2, p);
            }else if(x >= julyLength - monthNameWidth/2){
                p.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(monthName, bottomRightCornerX + (augustLength - julyLength),  y + side / 2, p);
            }else {
                p.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(monthName, bottomRightCornerX,  y + side / 2, p);
            }
        }else if (quarter == 4){
            p.setTextAlign(Paint.Align.CENTER);
            if (y <= novemberLength - monthName.length()*fontHeight/2 + side/2 && y >= octoberLength + monthName.length()*fontHeight/2 + side/2) {
                canvas.save();
                canvas.rotate(360f);
                int s = getHeight() / 2 - monthName.length()*fontHeight/2 ;
                for (char c : monthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s += fontHeight;
                }
                canvas.restore();
            }else if(y <= novemberLength - monthName.length()*fontHeight/2 + side/2){
                canvas.save();
                canvas.rotate(360f);
                int s = (int) (upperRightCornerY + (novemberLength - octoberLength) - side);
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            } else if(y >= novemberLength - monthName.length()*fontHeight/2 + side/2){
                canvas.save();
                canvas.rotate(360f);
                int s = (int) upperRightCornerY - side/2;
                for (char c : monthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s += fontHeight;
                }
                canvas.restore();
            }
        }


        p.setColor(Color.CYAN);
        //p.setStrokeWidth(side);
        canvas.drawPoint(upperLeftCornerX, y, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        if (quarter == 1) {
            calendar.set(Calendar.MONTH, Calendar.MARCH);
        }else if(quarter == 2){
            calendar.set(Calendar.MONTH, Calendar.JUNE);
        }else if(quarter == 3){
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        }else if(quarter == 4){
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        }

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();

        p.getTextBounds(monthName, 0, monthName.length(), textBounds);
        monthNameHeight = textBounds.height();
        monthNameWidth = textBounds.width();


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
            if (quarter == 3) {
                k += side;
            }

            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            if (quarter == 1) {
                upperLeftCornerX = x - k;
                left = upperLeftCornerX - side;
                top = y-side;
                right = upperLeftCornerX;
                bottom = y;
            }else if (quarter == 2){
                bottomLeftCornerY = y + k;
                left = x;
                top =  bottomLeftCornerY;
                right =  x+side;
                bottom = bottomLeftCornerY + side;
            }else if (quarter == 3){
                bottomRightCornerX = x + k;
                left = bottomRightCornerX - side;
                top = y - side;//y-side/2;
                right = bottomRightCornerX;
                bottom = y;
            }else if (quarter == 4){
                upperRightCornerY = y - k;
                left = x-side;
                top =  upperRightCornerY -side;
                right =  x;
                bottom = upperRightCornerY;
            }

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);
            p.setStyle(Paint.Style.FILL);

            calendar.clear();
            if (quarter == 1) {
                calendar.set(mainActivity.numberYearPicker.getValue(), 2, i);
            }else if (quarter == 2){
                calendar.set(mainActivity.numberYearPicker.getValue(), 5, i);
            }else if (quarter == 3){
                calendar.set(mainActivity.numberYearPicker.getValue(), 8, i);
            }else if (quarter == 4){
                calendar.set(mainActivity.numberYearPicker.getValue(), 11, i);
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            //canvas.drawText(text, left, y+side/4, p);
            if (quarter == 1) {
                canvas.drawText(text, left + side / 4, y - side / 4, p);
            }else {
                canvas.drawText(text, left + side / 4, bottom - side / 4, p);
            }

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
//            if (firstOccurrence) {
//                Date date = new Date(calendar.getTimeInMillis());
//                days.add(new Day(date, left, top, right, bottom));
//
//                if (date.getTime() == MainActivity.currDate.getTime()) {
//                    currentDate = days.get(days.size()-1);
//                    mainActivity.autumn.currentDate = null;
//                    mainActivity.spring.currentDate = null;
//                    mainActivity.summer.currentDate = null;
//                }
//
//                if (selectedDay != null) {
//                    if (selectedDay.date.getMonth() == date.getMonth() &&
//                            selectedDay.date.getDate() == date.getDate() ) {
//                        selectedDay = days.get(days.size()-1);
//
//                    }
//                }
//            }else{
//                days.get(l).left = left;
//                days.get(l).top = top;
//                days.get(l).right = right;
//                days.get(l).bottom = bottom;
//
//                if (currentDate != null) {
//                    calendar.clear();
//                    calendar.set(mainActivity.numberYearPicker.getValue(), 2, i);
//                    Date date = new Date(calendar.getTimeInMillis());
//
//                    if (date.getTime() == MainActivity.currDate.getTime()) {
//                        currentDate = days.get(g);
//
//                        currentDate.left = left;
//                        currentDate.top = top;
//                        currentDate.right = right;
//                        currentDate.bottom = bottom;
//                    }
//                }
//
//                if(!days.get(l).dayClosed){
//                    p.setColor(Color.CYAN);
//                    p.setStrokeWidth(strokeWidth/2);
//                    p.setStyle(Paint.Style.STROKE);
//                    canvas.drawRect(left, top, right, bottom, p);
//                }
//
//                for (MainActivity.Task task : days.get(l).tasks) {
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//                    if(sdf.format(new Date(task.startTime)).equals(sdf.format(days.get(l).date))){
//                        p.setColor(Color.rgb(221, 160, 221));
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                        p.setStyle(Paint.Style.FILL);
//                    }
//
//                    if(sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))){
//                        p.setColor(Color.rgb(139, 0, 139));
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                        p.setStyle(Paint.Style.FILL);
//                    }
//                }
//
//            }

            days.get(l).left = left;
            days.get(l).top = top;
            days.get(l).right = right;
            days.get(l).bottom = bottom;

            if (currentDate != null) {
                calendar.clear();
                if (quarter == 1) {
                    calendar.set(mainActivity.numberYearPicker.getValue(), 2, i);
                }else if (quarter == 2){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 5, i);
                }else if (quarter == 3){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 8, i);
                }else if (quarter == 4){
                    calendar.set(mainActivity.numberYearPicker.getValue(), 11, i);
                }
                Date date = new Date(calendar.getTimeInMillis());

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(g);
                    Log.d("123", "drawQuarter: MainActivity.currDate "+MainActivity.currDate);

                    currentDate.left = left;
                    currentDate.top = top;
                    currentDate.right = right;
                    currentDate.bottom = bottom;
                }
            }

            if(!days.get(l).dayClosed){
                p.setColor(Color.CYAN);
                p.setStrokeWidth(strokeWidth/2);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(left, top, right, bottom, p);
            }

            for (MainActivity.Task task : days.get(l).tasks) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                if(sdf.format(new Date(task.startTime)).equals(sdf.format(days.get(l).date))){
                    p.setColor(Color.rgb(221, 160, 221));
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(left, top, right, bottom, p);
                    p.setStyle(Paint.Style.FILL);
                }

                if(sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))){
                    p.setColor(Color.rgb(139, 0, 139));
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(left, top, right, bottom, p);
                    p.setStyle(Paint.Style.FILL);
                }
            }

            if (quarter !=3){
                k += side;
            }
            g += 1;
        }
        //Log.d("cv", "length3: " + length+ "upperLeftCornerX"+upperLeftCornerX+ " getWidth()"+ getWidth()+" side "+ side+" x "+x);
        if (firstOccurrence) {
            marchLength = -upperLeftCornerX + getWidth() * 1.5f - side/2;
            juneLength = -bottomLeftCornerY + getHeight()/2;
            septemberLength = -bottomRightCornerX + getWidth()/2;
            decemberLength = -upperRightCornerY + getHeight() * 1.5f;

//            if (quarter == 1) {
//                length = -upperLeftCornerX + getWidth() + side;
//                Log.d("321", "Winter length: "+ length);
//            }else if (quarter == 2){
//                length = -bottomLeftCornerY + getHeight() - side;
//                Log.d("321", "Spring length: "+ length);
//            }else if (quarter == 3){
//                length = -bottomRightCornerX + getWidth();
//                Log.d("123", "Summer length: "+ length);
//            }else if (quarter == 4) {
//                length = -upperRightCornerY + getHeight() + side;
//                Log.d("321", "Autumn length: "+ length);
//            }
        }
//        if (firstOccurrence) {
//            //marchLength = -upperLeftCornerX + getWidth() * 1.5f - side/2;
//            //length = -upperLeftCornerX + getWidth() + side;
//
//            if (mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
//                x = length;
//            }
//            if (currentDate != null || selectedDay != null) {
//                Day date = currentDate;
//                if (currentDate == null){
//                    date = selectedDay;
//                }
//                calendar.clear();
//                calendar.setTimeInMillis(date.date.getTime());
//
//                MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                        +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                        +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//
//
//                if(calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
//                    //x = x - date.left + getWidth() / 2 + getWidth() / 4;
//                    x = x - date.right + getWidth() / 2 + getWidth() / 4;
//                    /*
//                    if(x <= getWidth()) {
//                        x = getWidth();
//                    }
//                    if(x >= length){
//                        x = length;
//                    }
//                    */
//                }else if(calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
//                    //x = x - date.left + getWidth() / 2;
//                    x = x - date.right + getWidth() / 2;
//                    /*
//                    if(x <= getWidth()) {
//                        x = getWidth();
//                    }
//                    if(x >= length){
//                        x = length;
//                    }
//                    */
//                }else if(calendar.get(Calendar.MONTH) == Calendar.MARCH) {
//                    //x = x - date.right + getWidth() / 2 - getWidth() / 4;
//                    x = x - date.left + getWidth() / 2 - getWidth() / 4;
//                    /*
//                    if(x <= getWidth()) {
//                        x = getWidth();
//                    }
//                    if(x >= length){
//                        x = length;
//                    }
//                    */
//                }
//                if(x <= getWidth()) {
//                    x = getWidth();
//                }
//                if(x >= length){
//                    x = length;
//                }
//            }
//            invalidate();
//        }

        ///////////////////////////////////////////////////////////////////////
        if (quarter == 1) {
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);

            if (x >= februaryLength + monthNameWidth * 1.25f) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(monthName, getWidth() / 2, y - side * 1.5f, p);
            } else {
                p.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(monthName, upperLeftCornerX + (marchLength - februaryLength) - side / 2, y - side * 1.5f, p);
            }

            p.setColor(Color.RED);
            canvas.drawPoint(upperLeftCornerX, y, p);

            p.setColor(Color.WHITE);
            p.setStrokeWidth(strokeWidth);
            canvas.drawPoint(doubleTapX, doubleTapY, p);

            if (currentDate != null) {
                p.setColor(Color.WHITE);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if (firstOccurrence) {
                    calendar.clear();
                    calendar.setTimeInMillis(currentDate.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
                if (!firstOccurrence && MainActivity.day == null) {
                    MainActivity.day = currentDate;

                    ((MainActivity) context).updateSchedule(MainActivity.day);
                }
            }

            if (selectedDay != null) {
                p.setColor(Color.YELLOW);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if ( (!firstOccurrence || mainActivity.yearNumberChangedForDraw) && selectedDay != MainActivity.day) {
                    MainActivity.day = selectedDay;

                    ((MainActivity) context).updateSchedule(MainActivity.day);

                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }

            }


        }else if (quarter == 2){
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);
            p.setTextAlign(Paint.Align.CENTER);

            if (y >= juneLength && y <= mayLength - side * 2) {
                canvas.save();
                canvas.rotate(360f);
                int s = getHeight() / 2 + side;
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();

            } else if (y >= juneLength) {
                canvas.save();
                canvas.rotate(360f);
                int s =(int) (bottomLeftCornerY + (juneLength - mayLength) + side * 1.5f);
                for (char c : monthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
                    s += fontHeight;
                }
                canvas.restore();
            }



//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint(x, bottomLeftCornerY, p);
//
//
//            p.setColor(Color.BLUE);
//            p.setStrokeWidth(10);
//            canvas.drawPoint(x, y, p);



            p.setColor(Color.WHITE);
            p.setStrokeWidth(strokeWidth);
            canvas.drawPoint(doubleTapX, doubleTapY, p);


            //canvas.drawText("12", doubleTapX - side/2, y + side/4, p);
            if (currentDate != null) {
                p.setColor(Color.WHITE);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if (firstOccurrence) {
                    calendar.clear();
                    calendar.setTimeInMillis(currentDate.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
                if (!firstOccurrence && MainActivity.day == null) {
                    MainActivity.day = currentDate;

                    ((MainActivity) context).updateSchedule(MainActivity.day);
                }
            }

            if (selectedDay != null) {
                p.setColor(Color.YELLOW);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if ( (!firstOccurrence || mainActivity.yearNumberChangedForDraw) && selectedDay != MainActivity.day) {
                    MainActivity.day = selectedDay;

                    ((MainActivity) context).updateSchedule(MainActivity.day);

                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
            }
        }else if (quarter == 3){
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);
            if(x <= augustLength - monthNameWidth/2) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(monthName, getWidth()/2,  y + side / 2, p);
            }else {
                p.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(monthName, bottomRightCornerX + (septemberLength - augustLength), y + side / 2, p);
            }


            p.setColor(Color.WHITE);
            p.setStrokeWidth(strokeWidth);
            canvas.drawPoint(doubleTapX, doubleTapY, p);

            if (currentDate != null) {
                p.setColor(Color.WHITE);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if (firstOccurrence) {
                    calendar.clear();
                    calendar.setTimeInMillis(currentDate.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
                if (!firstOccurrence && MainActivity.day == null) {
                    MainActivity.day = currentDate;

                    ((MainActivity) context).updateSchedule(MainActivity.day);
                }
            }

            if (selectedDay != null) {
                p.setColor(Color.GREEN);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if ( (!firstOccurrence || mainActivity.yearNumberChangedForDraw) && selectedDay != MainActivity.day) {
                    MainActivity.day = selectedDay;

                    ((MainActivity) context).updateSchedule(MainActivity.day);

                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
            }

        }else if (quarter == 4){
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);
            p.setTextAlign(Paint.Align.CENTER);

            //if (y <= decemberLength  && y >= novemberLength + monthName.length()*fontHeight/2) {
            if (y >= novemberLength + side*2.5) {
                canvas.save();
                canvas.rotate(360f);
                int s = getHeight() / 2 - monthName.length()*fontHeight/2;
                for (char c : monthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s += fontHeight;
                }
                canvas.restore();

                //} else if (y <= decemberLength) {
            } else {
                canvas.save();
                canvas.rotate(360f);
                int s =(int) (upperRightCornerY + (decemberLength - novemberLength) - side);
                for (char c : reverseMonthName.toCharArray()) {
                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
                    s -= fontHeight;
                }
                canvas.restore();
            }

            p.setColor(Color.WHITE);
            p.setStrokeWidth(strokeWidth);
            canvas.drawPoint(doubleTapX, doubleTapY, p);

            if (currentDate != null) {
                p.setColor(Color.WHITE);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if (firstOccurrence) {
                    calendar.clear();
                    calendar.setTimeInMillis(currentDate.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
                if (!firstOccurrence && MainActivity.day == null) {
                    MainActivity.day = currentDate;

                    ((MainActivity) context).updateSchedule(MainActivity.day);
                }
            }

            if (selectedDay != null) {
                p.setColor(Color.GREEN);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
                p.setStyle(Paint.Style.FILL);

                if ( (!firstOccurrence || mainActivity.yearNumberChangedForDraw) && selectedDay != MainActivity.day) {
                    MainActivity.day = selectedDay;

                    ((MainActivity) context).updateSchedule(MainActivity.day);

                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
            }

        }


        if (firstOccurrence) {
            firstOccurrence = false;
        }
        if (mainActivity.yearNumberChangedForDraw && quarter == 4) {
            mainActivity.yearNumberChangedForDraw = false;
        }
        if (restore) {
            restore = false;
            restore();
        }
        if (addCyclicTasks && quarter == 4) {
            addCyclicTasks = false;
            addCyclicTasks();
        }

    }

    public void restore (){

        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        if (quarter == 1) {
            JsonArray array = parser.parse(MainActivity.yearStr.daysWinter).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

                for (MainActivity.Task task : mainActivity.winter.days.get(i).tasks) {
                    if (task.extra == taskExtra) {
                        task.shown = true;
                        MainActivity.changedeTasksOfYear = true;
                    }
                    //%%C del - MainActivity.setReminder(task, MainActivity.winter.days.get(i).date);
                    //%%C del - MainActivity.setReminder(task);
                    if (!task.isDone && task.isValid) {
                        days.get(i).dayClosed = false;
                    }
                }
            }
        }else if(quarter == 2){
            JsonArray array = parser.parse(MainActivity.yearStr.daysSpring).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

                for (MainActivity.Task task : mainActivity.spring.days.get(i).tasks) {
                    if (task.extra == taskExtra){
                        task.shown = true;
                        MainActivity.changedeTasksOfYear = true;
                    }
                    //%%C del - MainActivity.setReminder(task, MainActivity.spring.days.get(i).date);
                    //%%C del - MainActivity.setReminder(task);
                    if (!task.isDone && task.isValid){
                        days.get(i).dayClosed = false;
                    }
                }
                //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
            }
        }else if(quarter == 3){
            JsonArray array = parser.parse(MainActivity.yearStr.daysSummer).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

                for (MainActivity.Task task : mainActivity.summer.days.get(i).tasks) {
                    if (task.extra == taskExtra){
                        task.shown = true;
                        MainActivity.changedeTasksOfYear = true;
                    }
                    //%%C del - MainActivity.setReminder(task, MainActivity.summer.days.get(i).date);
                    //%%C del - MainActivity.setReminder(task);
                    if (!task.isDone && task.isValid){
                        days.get(i).dayClosed = false;
                    }
                }
                //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
            }
        }else if(quarter == 4){
            JsonArray array = parser.parse(MainActivity.yearStr.daysAutumn).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

                for (MainActivity.Task task : mainActivity.autumn.days.get(i).tasks) {
                    if (task.extra == MainActivity.taskExtra){
                        task.shown = true;
                        MainActivity.changedeTasksOfYear = true;
                    }
                    //%%C del - mainActivityObject.setReminder(task, MainActivity.autumn.days.get(i).date);
                    //%%C del - MainActivity.setReminder(task);
                    if (!task.isDone && task.isValid){
                        days.get(i).dayClosed = false;
                    }
                }
                //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
            }
        }
        invalidate();
    }

    public void addCyclicTasks (){

        if (quarter != 4){
            return;
        }
        Iterator<MainActivity.Task> j = MainActivity.cyclicTasks.iterator();
        while (j.hasNext()) {
            MainActivity.Task t = j.next();
            mainActivity.refreshCyclicTasks(t);
        }
        if (MainActivity.cyclicTasks.size() > 0){
            mainActivity.winter.invalidate();
            mainActivity.spring.invalidate();
            mainActivity.summer.invalidate();
            invalidate();//autumn
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        float lengthDraggingY;
        float dragDirection;

        incrementYearWinter = false;
        incrementYearSpring = false;
        incrementYearSummer = false;
        incrementYearAutumn = false;
        decrementYearWinter = false;
        decrementYearSpring = false;
        decrementYearSummer = false;
        decrementYearAutumn = false;

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:

                countDownTimer.cancel();
                // включаем режим перетаскивания
                drag = true;

                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX - x;
                dragY = evY - y;

                if (quarter == 4) {
                    posY = evY;
                    previousDragDirection = 0;
                }

                break;
            // тащим
            case MotionEvent.ACTION_MOVE:

                // если режим перетаскивания включен
                if (drag) {

                    if (quarter == 1) {
                        // определеяем новые координаты
                        x = evX - dragX;
                        if (x <= getWidth()) {
                            x = getWidth();
                        }
                        if (x >= length) {
                            x = length;
                        }
                    }else if (quarter == 2){
                        y = evY - dragY;
                        if(y >= 0) {
                            y = 0;
                        }
                        if( y <= -(length - getHeight()) ){
                            y = -(length - getHeight());
                        }
                    }else if (quarter == 3){
                        // определеяем новые координаты
                        x = evX - dragX;
                        if(x >= 0) {
                            x = 0;
                        }
                        if( x <= -(length - getWidth()) ){
                            x = -(length - getWidth());
                        }
                    }else if (quarter == 4){
                        // определеяем новые координаты
                        if( mainActivity.yearNumberChangedForMove ) {
                            //mainActivity.yearNumberChanged = false;
                            //dragY = evY - getHeight();
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
                                dragY = evY - length;
                                //decrementYearAutumn = true;
                            }else {
                                dragY = evY - getHeight();
                            }

                        }
                        y = evY - dragY;
                        if(y <= getHeight() - side * 4) {
                            decrementYearAutumn = true;
                        }
                        if(y <= getHeight()) {
                            y = getHeight();
                        }
                        if(y >= length + side * 4){
                            incrementYearAutumn = true;
                        }
                        if(y >= length){
                            y = length;
                        }

//                        if (mainActivity.yearNumberChanged){
//                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
//                                y = length;
//                            }
//                        }

                        dragDirection = evY - posY;
                        lengthDraggingY = previousDragDirection - dragDirection;
                        previousDragDirection = dragDirection;

                        //winter//
                        mainActivity.winter.x -= lengthDraggingY;
                        if(mainActivity.winter.x <= mainActivity.winter.getWidth()) {
                            mainActivity.winter.x = mainActivity.winter.getWidth();
                            decrementYearWinter = true;
                        }
                        if(mainActivity.winter.x >= mainActivity.winter.length){
                            mainActivity.winter.x = mainActivity.winter.length;
                            incrementYearWinter = true;
                        }
                        if (mainActivity.yearNumberChangedForMove){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                mainActivity.winter.x = mainActivity.winter.length;
                            }
                        }

                        //spring//
                        mainActivity.spring.y += lengthDraggingY;
                        if(mainActivity.spring.y >= 0) {
                            mainActivity.spring.y = 0;
                            decrementYearSpring = true;
                        }
                        if(mainActivity.spring.y <= -(mainActivity.spring.length - getHeight())){
                            mainActivity.spring.y = -(mainActivity.spring.length - getHeight());
                            incrementYearSpring = true;
                        }
                        if (mainActivity.yearNumberChangedForMove){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                mainActivity.spring.y = -(mainActivity.spring.length - getHeight());
                            }
                        }
                        //summer//
                        mainActivity.summer.x += lengthDraggingY;
                        if(mainActivity.summer.x >= 0) {
                            mainActivity.summer.x = 0;
                            decrementYearSummer = true;
                        }
                        if(mainActivity.summer.x <= -(mainActivity.summer.length - mainActivity.summer.getWidth())){
                            mainActivity.summer.x = -(mainActivity.summer.length - mainActivity.summer.getWidth());
                            incrementYearSummer = true;
                        }
                        if (mainActivity.yearNumberChangedForMove){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                mainActivity.summer.x = -(mainActivity.summer.length - mainActivity.summer.getWidth());
                            }
                        }

                        if (mainActivity.yearNumberChangedForMove){
                            mainActivity.yearNumberChangedForMove = false;
                        }

                        if (incrementYearWinter && incrementYearSpring && incrementYearSummer && incrementYearAutumn) {
                            mainActivity.numberYearPicker.increment();
                        }else if (decrementYearWinter && decrementYearSpring && decrementYearSummer && decrementYearAutumn) {
                            mainActivity.numberYearPicker.decrement();
                        }else {
                            mainActivity.winter.invalidate();
                            mainActivity.spring.invalidate();
                            mainActivity.summer.invalidate();
                        }
                    }
                    invalidate();
                }

                break;
            // касание завершено
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // выключаем режим перетаскивания
                drag = false;
                break;

        }

        if (gestureDetector.onTouchEvent(event)) return true;

        return true;
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            doubleTapX = e.getX();
            doubleTapY = e.getY();

            Iterator<Day> j = days.iterator();
            while (j.hasNext()){
                Day b = j.next();
                if (quarter == 1) {
                    if (b.left <= doubleTapX && b.right >= doubleTapX) {
                        selectedDay = b;
                        invalidate();
                        mainActivity.spring.selectedDay = null;
                        mainActivity.spring.invalidate();
                        mainActivity.summer.selectedDay = null;
                        mainActivity.summer.invalidate();
                        mainActivity.autumn.selectedDay = null;
                        mainActivity.autumn.invalidate();
                    }
                }else if (quarter == 2) {
                    if(b.top <= doubleTapY && b.bottom >= doubleTapY) {
                        selectedDay = b;
                        invalidate();
                        mainActivity.winter.selectedDay = null;
                        mainActivity.winter.invalidate();
                        mainActivity.summer.selectedDay = null;
                        mainActivity.summer.invalidate();
                        mainActivity.autumn.selectedDay = null;
                        mainActivity.autumn.invalidate();
                    }
                }else if (quarter == 3) {
                    if(b.left <= doubleTapX && b.right >= doubleTapX) {
                        selectedDay = b;
                        invalidate();
                        mainActivity.winter.selectedDay = null;
                        mainActivity.winter.invalidate();
                        mainActivity.spring.selectedDay = null;
                        mainActivity.spring.invalidate();
                        mainActivity.autumn.selectedDay = null;
                        mainActivity.autumn.invalidate();
                    }
                }else if (quarter == 4) {
                    if(b.top <= doubleTapY && b.bottom >= doubleTapY) {
                        selectedDay = b;
                        invalidate();
                        mainActivity.winter.selectedDay = null;
                        mainActivity.winter.invalidate();
                        mainActivity.spring.selectedDay = null;
                        mainActivity.spring.invalidate();
                        mainActivity.summer.selectedDay = null;
                        mainActivity.summer.invalidate();
                    }
                }

                if (selectedDay != null) {
                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
            }

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {

            if (quarter == 1 || quarter == 3) {
                scrollTime = (int) velocityX;
            }else if (quarter == 2 || quarter == 4){
                scrollTime = (int) velocityY;
            }

            if (scrollTime < 0){
                scrollTime *= -1;
            }

            countDownTimer = new CountDownTimer(scrollTime, 50) {

                public void onTick(long millisUntilFinished) {

                    if (quarter == 1) {
                        if (velocityX > 0) {
                            x += millisUntilFinished / 30;
                            //positionOfTouchX += millisUntilFinished / 30;;
                        } else {
                            x -= millisUntilFinished / 30;
                            //positionOfTouchX -= millisUntilFinished / 30;;
                        }
                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);

                        //проверить край
                        if (x <= getWidth()) {
                            x = getWidth();
                        }
                        if (x >= length) {
                            x = length;
                        }
                    }else if (quarter == 2){
                        if (velocityY > 0){
                            y += millisUntilFinished / 30;
                        }else{
                            y -= millisUntilFinished / 30;
                        }
                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);

                        //проверить край
                        if(y >= 0) {
                            y = 0;
                        }
                        if( y <= -(length - getHeight()) ){
                            y = -(length - getHeight());
                        }
                    }else if (quarter == 3){
                        if (velocityX > 0){
                            x += millisUntilFinished / 30;
                        }else{
                            x -= millisUntilFinished / 30;
                        }
                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);

                        //проверить край
                        if(x >= 0) {
                            x = 0;
                        }
                        if( x <= -(length - getWidth()) ){
                            x = -(length - getWidth());
                        }
                    }else if (quarter == 4){
                        if (velocityY > 0){
                            y += millisUntilFinished / 30;

                            /////////////////////////////////////////////////////////////////////
                            //summer//
                            mainActivity.summer.x -= millisUntilFinished / 30;
                            //spring//
                            mainActivity.spring.y -= millisUntilFinished / 30;
                            //winter//
                            mainActivity.winter.x += millisUntilFinished / 30;
                            /////////////////////////////////////////////////////////////////////////////
                        }else{
                            y -= millisUntilFinished / 30;

                            /////////////////////////////////////////////////////////////////////
                            //summer//
                            mainActivity.summer.x += millisUntilFinished / 30;
                            //spring//
                            mainActivity.spring.y += millisUntilFinished / 30;
                            //winter//
                            mainActivity.winter.x -= millisUntilFinished / 30;
                            /////////////////////////////////////////////////////////////////////////////
                        }
                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);


                        //проверить край

                        if(y <= getHeight() && !mainActivity.yearReducedForFling) {
                            y = getHeight();
                            decrementYearAutumn = true;
                        }
                        if(y >= length){
                            y = length;
                            incrementYearAutumn = true;
                        }

                        if (mainActivity.yearNumberChangedForFling){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                y = length;
                            }
                        }

                        //invalidate();

                        //winter//

                        if(mainActivity.winter.x <= mainActivity.winter.getWidth() && !mainActivity.yearReducedForFling) {
                            mainActivity.winter.x = mainActivity.winter.getWidth();
                            decrementYearWinter = true;
                        }
                        if(mainActivity.winter.x >= mainActivity.winter.length){
                            mainActivity.winter.x = mainActivity.winter.length;
                            incrementYearWinter = true;
                        }
                        if (mainActivity.yearNumberChangedForFling){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                mainActivity.winter.x = mainActivity.winter.length;
                            }
                        }

                        //mainActivity.winter.invalidate();
                        //spring//

                        if(mainActivity.spring.y >= 0 && !mainActivity.yearReducedForFling) {
                            mainActivity.spring.y = 0;
                            decrementYearSpring = true;
                        }
                        if(mainActivity.spring.y <= -(mainActivity.spring.length - getHeight())){
                            mainActivity.spring.y = -(mainActivity.spring.length - getHeight());
                            incrementYearSpring = true;
                        }
                        if (mainActivity.yearNumberChangedForFling){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                mainActivity.spring.y = -(mainActivity.spring.length - getHeight());
                            }
                        }

                        //mainActivity.spring.invalidate();
                        //summer//

                        if(mainActivity.summer.x >= 0 && !mainActivity.yearReducedForFling) {
                            mainActivity.summer.x = 0;
                            decrementYearSummer = true;
                        }
                        if(mainActivity.summer.x <= -(mainActivity.summer.length - mainActivity.summer.getWidth())){
                            mainActivity.summer.x = -(mainActivity.summer.length - mainActivity.summer.getWidth());
                            incrementYearSummer = true;
                        }
                        if (mainActivity.yearNumberChangedForFling){
                            if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
                                mainActivity.summer.x = -(mainActivity.summer.length - mainActivity.summer.getWidth());
                            }
                        }

                        //mainActivity.summer.invalidate();

                        if (mainActivity.yearReducedForFling){
                            mainActivity.yearReducedForFling = false;
                        }
                        if (mainActivity.yearNumberChangedForFling){
                            mainActivity.yearNumberChangedForFling = false;
                            mainActivity.yearNumberChangedForMove = false;
                        }

                        if (incrementYearWinter && incrementYearSpring && incrementYearSummer && incrementYearAutumn) {
                            //mainActivity.yearNumberChangedForFling = false;
                            incrementYearWinter = false;
                            incrementYearSpring = false;
                            incrementYearSummer = false;
                            incrementYearAutumn = false;
                            mainActivity.numberYearPicker.increment();
                        }else if (decrementYearWinter && decrementYearSpring && decrementYearSummer && decrementYearAutumn) {
                            //mainActivity.yearNumberChangedForFling = false;
                            decrementYearWinter = false;
                            decrementYearSpring = false;
                            decrementYearSummer = false;
                            decrementYearAutumn = false;
                            //mainActivity.decrementYear = true;
                            mainActivity.numberYearPicker.decrement();
                        }else {
                            mainActivity.winter.invalidate();
                            mainActivity.spring.invalidate();
                            mainActivity.summer.invalidate();
                        }
//                        mainActivity.winter.invalidate();
//                        mainActivity.spring.invalidate();
//                        mainActivity.summer.invalidate();
                    }

                    //обновить
                    invalidate();
                }

                public void onFinish() {
                    Log.d("onFling", "done!");
                }
            }.start();

            return true;
        }

    }




}
