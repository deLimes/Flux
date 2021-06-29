package com.delimes.flux;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

class Autumn extends View {
    Context context;
    Paint p;
    // координаты для рисования квадрата
    float x = 0;
    float y = 0;
    int side = 0;
    //int width;//del
    //int height;//del
    float doubleTapX = 0;
    float doubleTapY = 0;
    float octoberLength = 0;
    float novemberLength = 0;
    float decemberLength = 0;
    Day selectedDay = null;
    Day currentDate = null;
    ArrayList<Day> days = new ArrayList<Day>();
    String monthName, reverseMonthName;
    SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
    boolean restore;
    boolean addCyclicTasks;

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

    ////////////////////////////
    float posY = 0;
    float previousDragDirection = 0;
//    boolean incrementYear = false;
//    boolean decrementYear = false;
    ////////////////////////////

    Bitmap backingBitmap;
    Canvas drawCanvas;

    private GestureDetector gestureDetector;

    float upperLeftCornerX = 0;
    float upperRightCornerX = 0;
    float bottomLeftCornerY = 0;
    float upperRightCornerY = 0;
    float bottomRightCornerX = 0;

    float length = 0;

    public Autumn(Context context) {
        super(context);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        init(context);

    }

    public Autumn(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        init(context);
    }

    public Autumn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        init(context);
    }

    private void init(Context context) {

        MainActivity.alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
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

    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.rgb(255, 215, 0));
        drawAutumn(canvas);

    }

    public void fillInDays(int year){

        int l = 0;

        //IV-ый квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;

            calendar.clear();
            calendar.set(year, 9, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 10, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));

        }

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 11, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

    }

    public void drawAutumn(Canvas canvas){

        int Width = canvas.getWidth();//del
        int Height = canvas.getHeight();//del
        int fontHeight = side / 2;
        int strokeWidth = side / 5;
        int l = 0;

        //IV-ый квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();


        p.reset();
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int k = 0;
        int g = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;
            upperRightCornerY = y - k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = x-side;
            float top =  upperRightCornerY -side;
            float right =  x;
            float bottom = upperRightCornerY;

            p.reset();
            p.setTextAlign(Paint.Align.CENTER);
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(mainActivity.numberYearPicker.getValue(), 9, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            canvas.drawText(text, left+side/2, bottom-side/4, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
            if (firstOccurrence) {
                Date date = new Date(calendar.getTimeInMillis());
                days.add(new Day(date, left, top, right, bottom));

                //if (date.getTime() == currDate.getTime()) {
                if (date.compareTo(MainActivity.currDate) == 0) {
                    currentDate = days.get(days.size()-1);
                    mainActivity.winter.currentDate = null;
                    mainActivity.spring.currentDate = null;
                    mainActivity.summer.currentDate = null;
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
                    calendar.set(mainActivity.numberYearPicker.getValue(), 9, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    //if (date.getTime() == currDate.getTime()) {
                    if (date.compareTo(MainActivity.currDate) == 0) {
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

                    if( (sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date)))
                            && !days.get(l).dayClosed ){
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
            octoberLength = -upperRightCornerY + getHeight() * 1.5f;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
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

//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint((float) x, upperRightCornerY, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
            upperRightCornerY = y - k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = x-side;
            float top =  upperRightCornerY -side;
            float right =  x;
            float bottom = upperRightCornerY;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(mainActivity.numberYearPicker.getValue(), 10, i);
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
                    mainActivity.winter.currentDate = null;
                    mainActivity.spring.currentDate = null;
                    mainActivity.summer.currentDate = null;
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
                    calendar.set(mainActivity.numberYearPicker.getValue(), 10, i);
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

                    //if(sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))){
                    if( (sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date)))
                                && !days.get(l).dayClosed ){
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
            novemberLength = -upperRightCornerY + getHeight() * 1.5f;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
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

//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint((float) x, upperRightCornerY, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
//                k += side;
//                upperRightCornerY = y - k;
//                canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, x, upperRightCornerY, p);
            l += 1;
            upperRightCornerY = y - k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = x-side;
            float top =  upperRightCornerY -side;
            float right =  x;
            float bottom = upperRightCornerY;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(mainActivity.numberYearPicker.getValue(), 11, i);
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
                    //currentDate = new Day(date, left, top, right, bottom);
                    currentDate = days.get(days.size()-1);
                    mainActivity.winter.currentDate = null;
                    mainActivity.spring.currentDate = null;
                    mainActivity.summer.currentDate = null;
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
                    calendar.set(mainActivity.numberYearPicker.getValue(), 11, i);
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

                    //if(sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date))){
                    if( (sdf.format(new Date(task.finishTime)).equals(sdf.format(days.get(l).date)))
                            && !days.get(l).dayClosed ){
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

            decemberLength = -upperRightCornerY + getHeight() * 1.5f;
            length = -upperRightCornerY + getHeight() + side;
            //Log.d("XY", "upperRightCornerY:" + length);

            if (mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
                y = length;
            }
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

                if(calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
                    y = y - date.top + getHeight() / 2 + getHeight() / 4;
                }else if(calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
                    y = y - date.bottom + getHeight() / 2;
                }else if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                    y = y - date.bottom + getHeight() / 2 - getHeight() / 4;
                }

                if (y <= getHeight()) {
                    y = getHeight();
                }
                if (y >= length) {
                    y = length;
                }
            }
            invalidate();
        }

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





//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint((float) x, upperRightCornerY, p);
//
//
//            p.setColor(Color.BLUE);
//            p.setStrokeWidth(10);
//            canvas.drawPoint(x, y, p);

        if (firstOccurrence) {
            firstOccurrence = false;
        }
        if (restore) {
            restore = false;
            restore();
        }
        if (addCyclicTasks) {
            addCyclicTasks = false;
            addCyclicTasks();

            mainActivity.winter.invalidate();
            mainActivity.spring.invalidate();
            mainActivity.summer.invalidate();
            invalidate();
        }

    }

    public void restore (){

        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
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
        invalidate();
    }

    public void addCyclicTasks (){

        Iterator<MainActivity.Task> j = MainActivity.cyclicTasks.iterator();
        while (j.hasNext()) {
            MainActivity.Task t = j.next();
            mainActivity.refreshCyclicTasks(t);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        //////////////////////////////////////////////////////////////
        float lengthDraggingY = 0;
        float dragDirection = 0;
        boolean incrementYearWinter = false;
        boolean incrementYearSpring = false;
        boolean incrementYearSummer = false;
        boolean incrementYearAutumn = false;
        boolean decrementYearWinter = false;
        boolean decrementYearSpring = false;
        boolean decrementYearSummer = false;
        boolean decrementYearAutumn = false;
        //////////////////////////////////////////////////////////////


        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:

                countDownTimer.cancel();

                // включаем режим перетаскивания
                drag = true;

                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX - x;
                dragY = evY - y;

                //Log.d("WH", "W:" + Width + "H:" + Height);
                ////////////////////////////////////
                posY = evY;
                previousDragDirection = 0;
                ////////////////////////////////////



                break;
            // тащим
            case MotionEvent.ACTION_MOVE:
                // если режим перетаскивания включен
                if (drag) {
                    // определеяем новые координаты
                    //x = evX - dragX;////////////////////////////////////////////
                    y = evY - dragY;
                    if(y <= getHeight()) {
                        y = getHeight();
                        decrementYearAutumn = true;
                    }
                    if(y >= length){
                        y = length;
                        incrementYearAutumn = true;
                    }
                    invalidate();
                    //Log.d("XY", "X:" + x + "Y:" + y + "length "+length);
                    /////////////////////////////////////////////////////////////////////
                    dragDirection = evY - posY;
                    lengthDraggingY = previousDragDirection - dragDirection;
                    previousDragDirection = dragDirection;

                    //summer//
                    mainActivity.summer.x += lengthDraggingY;
                    if(mainActivity.summer.x >= 0) {
                        mainActivity.summer.x = 0;
                        decrementYearSummer = true;
                    }
                    if(mainActivity.summer.x <= mainActivity.summer.length){
                        mainActivity.summer.x = mainActivity.summer.length;
                        incrementYearSummer = true;
                    }
                    mainActivity.summer.invalidate();
                    //spring//
                    mainActivity.spring.y += lengthDraggingY;
                    if(mainActivity.spring.y >= 0) {
                        mainActivity.spring.y = 0;
                        decrementYearSpring = true;
                    }
                    if(mainActivity.spring.y <= mainActivity.spring.length){
                        mainActivity.spring.y = mainActivity.spring.length;
                        incrementYearSpring = true;
                    }
                    mainActivity.spring.invalidate();
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
                    Log.d("cvhn", "length: " + mainActivity.winter.length+ " getWidth()"+ mainActivity.winter.getWidth()+" side "+ mainActivity.winter.side);
                    mainActivity.winter.invalidate();

                    Log.d("vsm", "previousChosenYearNumber: "+ mainActivity.previousChosenYearNumber + " chosenYearNumber: "+ mainActivity.chosenYearNumber);
//                    if (mainActivity.previousChosenYearNumber == 0) {
//                        mainActivity.previousChosenYearNumber = mainActivity.chosenYearNumber;
//                    }


                    //Log.d("incrementYear", "Wn:" + incrementYearWinter + "Sp:" + incrementYearSpring + "Sm:" + incrementYearSummer + "At:" + incrementYearAutumn);
                    //Log.d("decrementYear", "Wn:" + decrementYearWinter + "Sp:" + decrementYearSpring + "Sm:" + decrementYearSummer + "At:" + decrementYearAutumn);
                    if (incrementYearWinter && incrementYearSpring && incrementYearSummer && incrementYearAutumn) {
                        mainActivity.numberYearPicker.increment();
                    }
                    if (decrementYearWinter && decrementYearSpring && decrementYearSummer && decrementYearAutumn) {
                        mainActivity.numberYearPicker.decrement();
//                        mainActivity.autumn.y = length;
//                        mainActivity.autumn.invalidate();
//                        mainActivity.summer.x = mainActivity.summer.length;
//                        mainActivity.summer.invalidate();
//                        mainActivity.spring.y = mainActivity.spring.length;
//                        mainActivity.spring.invalidate();
//                        mainActivity.winter.x = mainActivity.winter.length;
//                        mainActivity.winter.invalidate();
                    }

                    /////////////////////////////////////////////////////////////////////////////
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

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        width = width;
        height = height;
        //Log.d("WH", "W:" + width + "H:" + height);
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            doubleTapX = e.getX();
            doubleTapY = e.getY();

            Iterator<Day> j = days.iterator();
            while (j.hasNext()){
                Day b = j.next();
                if(b.top <= doubleTapY && b.bottom >= doubleTapY) {
                    selectedDay = b;
                    mainActivity.winter.selectedDay = null;
                    mainActivity.winter.invalidate();
                    mainActivity.spring.selectedDay = null;
                    mainActivity.spring.invalidate();
                    mainActivity.summer.selectedDay = null;
                    mainActivity.summer.invalidate();
                    invalidate();

                    calendar.clear();
                    calendar.setTimeInMillis(selectedDay.date.getTime());
                    MainActivity.dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
                            +calendar.get(Calendar.DAY_OF_MONTH) + " "
                            +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));

//                        taskTime.setText("__:__");
//                        taskDuration.setText("__:__");
//                        taskDescription.setText("");
//                        updateSchedule(selectedDay);

                }
            }

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {

            scrollTime = (int) velocityY;
            if (scrollTime < 0){
                scrollTime *= -1;
            }
            countDownTimer = new CountDownTimer(scrollTime, 50) {

                public void onTick(long millisUntilFinished) {
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
                    if(y <= getHeight()) {
                        y = getHeight();
                    }
                    if(y >= length){
                        y = length;
                    }

                    //обновить
                    invalidate();

                    //summer//
                    if(mainActivity.summer.x >= 0) {
                        mainActivity.summer.x = 0;
                    }
                    if(mainActivity.summer.x <= mainActivity.summer.length){
                        mainActivity.summer.x = mainActivity.summer.length;
                    }
                    mainActivity.summer.invalidate();
                    //spring//
                    if(mainActivity.spring.y >= 0) {
                        mainActivity.spring.y = 0;
                    }
                    if(mainActivity.spring.y <= mainActivity.spring.length){
                        mainActivity.spring.y = mainActivity.spring.length;
                    }
                    mainActivity.spring.invalidate();
                    //winter//
                    if(mainActivity.winter.x <= mainActivity.winter.getWidth()) {
                        mainActivity.winter.x = mainActivity.winter.getWidth();
                    }
                    if(mainActivity.winter.x >= mainActivity.winter.length){
                        mainActivity.winter.x = mainActivity.winter.length;
                    }
                    mainActivity.winter.invalidate();
                }

                public void onFinish() {
                    //Log.d("onFling", "done!");
                }
            }.start();


            return true;
        }

    }

//        public class Date
//        {
//            /**Картинка*/
//            private Bitmap bmp;
//
//            /**Позиция*/
//            public int x;
//            public int y;
//
//            /**Скорость по Х=15*/
//            private int mSpeed=25;
//
//            public double angle;
//
//            /**Ширина*/
//            public int width;
//
//            /**Ввыоста*/
//            public  int height;
//
//            public GameView gameView;
//
//            /**Конструктор*/
//            public Date(GameView gameView, Bitmap bmp) {
//                this.gameView=gameView;
//                this.bmp=bmp;
//
//                this.x = 0;            //позиция по Х
//                this.y = 120;          //позиция по У
//                this.width = 27;       //ширина снаряда
//                this.height = 40;      //высота снаряда
//
//                //угол полета пули в зависипости от координаты косания к экрану
//                angle = Math.atan((double)(y - gameView.shotY) / (x - gameView.shotX));
//            }
//
//            /**Перемещение объекта, его направление*/
//            private void update() {
//                x += mSpeed * Math.cos(angle);         //движение по Х со скоростью mSpeed и углу заданном координатой angle
//                y += mSpeed * Math.sin(angle);         // движение по У -//-
//            }
//
//            /**Рисуем наши спрайты*/
//            public void onDraw(Canvas canvas) {
//                update();                              //говорим что эту функцию нам нужно вызывать для работы класса
//                canvas.drawBitmap(bmp, x, y, null);
//            }
//        }







}
