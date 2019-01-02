package com.example.delimes.flux;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

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
import static com.example.delimes.flux.MainActivity.taskExtra;

/**
 * Created by User on 04.05.2018.
 */

class Winter extends View {
    Context context;
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
    Day selectedDay = null;
    Day currentDate = null;
    ArrayList<Day> days = new ArrayList<Day>();
    String monthName;
    Rect textBounds = new Rect();
    SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
    boolean restore;

    Calendar calendar = GregorianCalendar.getInstance();


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

    Bitmap backingBitmap;
    Canvas drawCanvas;

    private GestureDetector gestureDetector;

    float upperLeftCornerX = 0;
    float upperRightCornerX = 0;
    float bottomLeftCornerY = 0;
    float upperRightCornerY = 0;
    float bottomRightCornerX = 0;

    float length = 0;

    public Winter(Context context) {
        super(context);

        this.context = context;
        init(context);

    }

    public Winter(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init(context);
    }

    public Winter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        init(context);
    }

    private void init(Context context) {

        MainActivity.alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        p = new Paint();
        gestureDetector = new GestureDetector(context, new Winter.MyGestureListener());

        calendar.clear();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(year, month, day);
        MainActivity.currDate = new Date(calendar.getTimeInMillis());

    }



    @Override
    protected void onDraw(Canvas canvas) {

        //del
//            if (firstOccurrence) {
//                drawCanvas = canvas;
//                firstOccurrence = false;
//
////                x = getWidth();//del
////                y = getHeight();//del
//            }
        //del

        canvas.drawColor(Color.rgb(106, 90, 205));
        drawWinter(canvas);

    }

    public void FillInDays(int year){

        int l = 0;

        //I-ый квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;

            calendar.clear();
            calendar.set(year, 0, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 1, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));

        }

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.MARCH);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 2, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

    }

    public void drawWinter(Canvas canvas){

        int Width = canvas.getWidth();//del
        int Height = canvas.getHeight();//del
        int fontHeight = side / 2;
        int strokeWidth = side / 5;
        float monthNameHeight;
        float monthNameWidth;

        int l = 0;


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        //I-ый квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, MainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();

        p.getTextBounds(monthName, 0, monthName.length(), textBounds);
        monthNameHeight = textBounds.height();
        monthNameWidth = textBounds.width();

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int k = 0;
        int g = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;

            upperLeftCornerX = x - k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = upperLeftCornerX - side;
            float top = y-side;
            float right = upperLeftCornerX;
            float bottom = y;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(MainActivity.numberYearPicker.getValue(), 0, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            canvas.drawText(text, left+side/4, bottom-side/4, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
            if (firstOccurrence) {
                Date date = new Date(calendar.getTimeInMillis());
                days.add(new Day(date, left, top, right, bottom));

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(days.size()-1);
                    MainActivity.autumn.currentDate = null;
                    MainActivity.spring.currentDate = null;
                    MainActivity.summer.currentDate = null;
                }

                if (selectedDay != null) {
                    if (selectedDay.date.getMonth() == date.getMonth() &&
                            selectedDay.date.getDate() == date.getDate() ) {
                        selectedDay = days.get(days.size()-1);

                    }
                }
            }else{
                days.get(l).left = left;
                days.get(l).top = top;
                days.get(l).right = right;
                days.get(l).bottom = bottom;

                if (currentDate != null) {
                    calendar.clear();
                    calendar.set(MainActivity.numberYearPicker.getValue(), 0, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    if (date.getTime() == MainActivity.currDate.getTime()) {
                        currentDate = days.get(g);

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

            }

            k += side;
            g += 1;
        }
        if (firstOccurrence) {
            januaryLength = -upperLeftCornerX + getWidth()* 1.5f;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        if(x <= januaryLength) {
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(monthName, getWidth() / 2, y - side * 1.5f, p);

               /* p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(getWidth()/2,y - side * 1.5f-monthNameHeight,getWidth()/2+monthNameWidth ,y - side * 1.5f, p);
                p.setStyle(Paint.Style.FILL);*/
        }else{
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
        }



//            p.setColor(Color.CYAN);
//            p.setStrokeWidth(side);
//            canvas.drawPoint(upperLeftCornerX, y, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, MainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();

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
            upperLeftCornerX = x - k;

            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = upperLeftCornerX - side;
            float top = y-side;
            float right = upperLeftCornerX;
            float bottom = y;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(MainActivity.numberYearPicker.getValue(), 1, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            //canvas.drawText(text, left, y+side/4, p);
            canvas.drawText(text, left+side/4, y-side/4, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
            if (firstOccurrence) {
                Date date = new Date(calendar.getTimeInMillis());
                days.add(new Day(date, left, top, right, bottom));

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(days.size()-1);
                    MainActivity.autumn.currentDate = null;
                    MainActivity.spring.currentDate = null;
                    MainActivity.summer.currentDate = null;
                }

                if (selectedDay != null) {
                    if (selectedDay.date.getMonth() == date.getMonth() &&
                            selectedDay.date.getDate() == date.getDate() ) {
                        selectedDay = days.get(days.size()-1);

                    }
                }
            }else{
                days.get(l).left = left;
                days.get(l).top = top;
                days.get(l).right = right;
                days.get(l).bottom = bottom;

                if (currentDate != null) {
                    calendar.clear();
                    calendar.set(MainActivity.numberYearPicker.getValue(), 1, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    if (date.getTime() == MainActivity.currDate.getTime()) {
                        currentDate = days.get(g);

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

            }
            k += side;
            g += 1;
        }
        if (firstOccurrence) {
            februaryLength = -upperLeftCornerX + getWidth() * 1.5f;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
//            if(x <= februaryLength && x >= januaryLength + side * 2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText("FEBRUARY", getWidth()/2, y - side * 1.5f, p);
//            }else if(x <= februaryLength){
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText("FEBRUARY", upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
//            }else if(x >= februaryLength){
//                p.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText("FEBRUARY", upperLeftCornerX - side, y - side * 1.5f, p);
//            }

//            if(x <= februaryLength - side*0.25f && x >= januaryLength + "january".length()*fontHeight/2 + side/2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
//            }else if(x <= februaryLength - side*0.25f){
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(monthName, upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
//            }else{
//                p.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
//            }


        if(x <= februaryLength && x >= januaryLength + monthNameWidth) {
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
        }else if(x <= februaryLength){
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(monthName, upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
        }else{
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
        }


        p.setColor(Color.CYAN);
        //p.setStrokeWidth(side);
        canvas.drawPoint(upperLeftCornerX, y, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, MainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.MARCH);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();

        p.getTextBounds(monthName, 0, monthName.length(), textBounds);
        monthNameHeight = textBounds.height();
        monthNameWidth = textBounds.width();


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
            upperLeftCornerX = x - k;

            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = upperLeftCornerX - side;
            float top = y-side;
            float right = upperLeftCornerX;
            float bottom = y;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(MainActivity.numberYearPicker.getValue(), 2, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            //canvas.drawText(text, left, y+side/4, p);
            canvas.drawText(text, left+side/4, y-side/4, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
            if (firstOccurrence) {
                Date date = new Date(calendar.getTimeInMillis());
                days.add(new Day(date, left, top, right, bottom));

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(days.size()-1);
                    MainActivity.autumn.currentDate = null;
                    MainActivity.spring.currentDate = null;
                    MainActivity.summer.currentDate = null;
                }

                if (selectedDay != null) {
                    if (selectedDay.date.getMonth() == date.getMonth() &&
                            selectedDay.date.getDate() == date.getDate() ) {
                        selectedDay = days.get(days.size()-1);

                    }
                }
            }else{
                days.get(l).left = left;
                days.get(l).top = top;
                days.get(l).right = right;
                days.get(l).bottom = bottom;

                if (currentDate != null) {
                    calendar.clear();
                    calendar.set(MainActivity.numberYearPicker.getValue(), 2, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    if (date.getTime() == MainActivity.currDate.getTime()) {
                        currentDate = days.get(g);

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

            }
            k += side;
            g += 1;
        }
        if (firstOccurrence) {

            marchLength = -upperLeftCornerX + getWidth() * 1.5f - side/2;
            //length = -upperLeftCornerX + getWidth();
            length = -upperLeftCornerX + getWidth() + side;

            if (currentDate != null || selectedDay != null) {
                Day date = currentDate;
                if (currentDate == null){
                    date = selectedDay;
                }
                calendar.clear();
                calendar.setTimeInMillis(date.date.getTime());

                MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
                        +calendar.get(Calendar.DAY_OF_MONTH) + " "
                        +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));


                if(calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                    //x = x - date.left + getWidth() / 2 + getWidth() / 4;
                    x = x - date.right + getWidth() / 2 + getWidth() / 4;
                    if(x <= getWidth()) {
                        x = getWidth();
                    }
                    if(x >= length){
                        x = length;
                    }
                }else if(calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
                    //x = x - date.left + getWidth() / 2;
                    x = x - date.right + getWidth() / 2;
                    if(x <= getWidth()) {
                        x = getWidth();
                    }
                    if(x >= length){
                        x = length;
                    }
                }else if(calendar.get(Calendar.MONTH) == Calendar.MARCH) {
                    //x = x - date.right + getWidth() / 2 - getWidth() / 4;
                    x = x - date.left + getWidth() / 2 - getWidth() / 4;
                    if(x <= getWidth()) {
                        x = getWidth();
                    }
                    if(x >= length){
                        x = length;
                    }
                }
                invalidate();
            }
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
//            if(x <= marchLength && x >= februaryLength + side * 2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText("MARCH", getWidth()/2, y - side * 1.5f, p);
//            }else if(x <= marchLength){
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText("MARCH", upperLeftCornerX + (marchLength - februaryLength) - side/2, y - side * 1.5f, p);
//            }

//            if( x >= februaryLength + side * 2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
//            }else {
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(monthName, upperLeftCornerX + (marchLength - februaryLength) - side/2, y - side * 1.5f, p);
//            }

        if( x >= februaryLength + monthNameWidth*1.25f) {
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
        }else {
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(monthName, upperLeftCornerX + (marchLength - februaryLength) - side/2, y - side * 1.5f, p);
        }


        p.setColor(Color.RED);
        //p.setStrokeWidth(side);
        canvas.drawPoint(upperLeftCornerX, y, p);


        //canvas.drawPoint(x-correctiveX-240, y-correctiveY-270, p);//NexusS

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

            if (!firstOccurrence && selectedDay != MainActivity.day) {
                MainActivity.day = selectedDay;

                ((MainActivity) context).updateSchedule(MainActivity.day);

                calendar.clear();
                calendar.setTimeInMillis(selectedDay.date.getTime());
                MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
                        + calendar.get(Calendar.DAY_OF_MONTH) + " "
                        + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
            }

        }


        if (firstOccurrence) {
            firstOccurrence = false;
        }
        if (restore) {
            restore = false;
            restore();
        }

    }

    public void restore (){

        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        JsonArray array = parser.parse(MainActivity.yearStr.daysWinter).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            MainActivity.winter.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

            for (MainActivity.Task task : MainActivity.winter.days.get(i).tasks) {
                if (task.extra == taskExtra){
                    task.shown = true;
                    MainActivity.changedeTasksOfYear = true;
                }
                //%%C del - MainActivity.setReminder(task, MainActivity.winter.days.get(i).date);
                //%%C del - MainActivity.setReminder(task);
                if (!task.isDone && task.isValid){
                    MainActivity.winter.days.get(i).dayClosed = false;
                }
            }
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

                countDownTimer.cancel();
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
                    //y = evY - dragY;/////////////////////////////////////////////
                    if(x <= getWidth()) {
                        x = getWidth();
                    }
                    if(x >= length){
                        x = length;
                    }
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
                if(b.left <= doubleTapX && b.right >= doubleTapX) {
                    selectedDay = b;
                    MainActivity.spring.selectedDay = null;
                    MainActivity.spring.invalidate();
                    MainActivity.summer.selectedDay = null;
                    MainActivity.summer.invalidate();
                    MainActivity.autumn.selectedDay = null;
                    MainActivity.autumn.invalidate();
                    invalidate();

                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
                            +calendar.get(Calendar.DAY_OF_MONTH) + " "
                            +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
                }
            }

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {

            scrollTime = (int) velocityX;
            if (scrollTime < 0){
                scrollTime *= -1;
            }
            countDownTimer = new CountDownTimer(scrollTime, 50) {

                public void onTick(long millisUntilFinished) {
                    if (velocityX > 0){
                        x += millisUntilFinished / 30;
                        //positionOfTouchX += millisUntilFinished / 30;;
                    }else{
                        x -= millisUntilFinished / 30;
                        //positionOfTouchX -= millisUntilFinished / 30;;
                    }
                    // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);

                    //проверить край
                    if(x <= getWidth()) {
                        x = getWidth();
                    }
                    if(x >= length){
                        x = length;
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
