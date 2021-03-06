package com.delimes.flux;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
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
import static com.delimes.flux.MainActivity.taskExtra;

class Summer extends View {
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
    float julyLength = 0;
    float augustLength = 0;
    float septemberLength = 0;
    Day selectedDay = null;
    Day currentDate = null;
    ArrayList<Day> days = new ArrayList<Day>();
    String monthName;
    Rect textBounds = new Rect();
    SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
    boolean restore;

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

    Bitmap backingBitmap;
    Canvas drawCanvas;

    private GestureDetector gestureDetector;

    float upperLeftCornerX = 0;
    float upperRightCornerX = 0;
    float bottomLeftCornerY = 0;
    float upperRightCornerY = 0;
    float bottomRightCornerX = 0;

    float length = 0;

    public Summer(Context context) {
        super(context);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        init(context);

    }

    public Summer(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        init(context);
    }

    public Summer(Context context, AttributeSet attrs, int defStyle) {
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

        canvas.drawColor(Color.YELLOW);
        drawSummer(canvas);

    }

    public void fillInDays(int year){

        int l = 0;

        //III-ий квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JULY);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;

            calendar.clear();
            calendar.set(year, 6, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 7, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));

        }

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 8, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

    }

    public void drawSummer(Canvas canvas){

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

        //III-ий квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.JULY);

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
            k += side;
            l = i - 1;

            bottomRightCornerX = x + k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = bottomRightCornerX - side;
            float top = y - side;//y-side/2;
            float right = bottomRightCornerX;
            float bottom = y;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(mainActivity.numberYearPicker.getValue(), 6, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            //canvas.drawText(text, left, y+side/4, p);
            canvas.drawText(text, left + side / 4, bottom - side / 4, p);

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
                    mainActivity.autumn.currentDate = null;
                }

                if (selectedDay != null) {
                    if (selectedDay.date.getMonth() == date.getMonth() &&
                            selectedDay.date.getDate() == date.getDate() ) {
                        selectedDay = days.get(days.size()-1);

                    }
                }
            } else {
                days.get(l).left = left;
                days.get(l).top = top;
                days.get(l).right = right;
                days.get(l).bottom = bottom;

                if (currentDate != null) {
                    calendar.clear();
                    calendar.set(mainActivity.numberYearPicker.getValue(), 6, i);
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
            g += 1;
        }
        if (firstOccurrence) {
            julyLength = -bottomRightCornerX + getWidth()/2 ;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
           /* if (x >= julyLength  + side*0.5f) {
                p.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("JULY", getWidth() / 2, y + side / 2, p);
            } else {
                p.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("JULY", bottomRightCornerX, y + side / 2, p);
            }*/

        if (x >= julyLength  + monthNameWidth/2) {
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(monthName, getWidth() / 2, y + side / 2, p);
        } else {
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(monthName, bottomRightCornerX, y + side / 2, p);
        }

//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint(bottomRightCornerX, y, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.AUGUST);

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
            k += side;
            l += 1;

            bottomRightCornerX = x + k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = bottomRightCornerX - side;
            float top = y - side;//y-side/2;
            float right = bottomRightCornerX;
            float bottom = y;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(mainActivity.numberYearPicker.getValue(), 7, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            canvas.drawText(text, left + side / 4, bottom - side / 4, p);

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
                    mainActivity.autumn.currentDate = null;
                }

                if (selectedDay != null) {
                    if (selectedDay.date.getMonth() == date.getMonth() &&
                            selectedDay.date.getDate() == date.getDate() ) {
                        selectedDay = days.get(days.size()-1);

                    }
                }
            } else {
                days.get(l).left = left;
                days.get(l).top = top;
                days.get(l).right = right;
                days.get(l).bottom = bottom;

                if (currentDate != null) {
                    calendar.clear();
                    calendar.set(mainActivity.numberYearPicker.getValue(), 7, i);
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
            g += 1;
        }
        if (firstOccurrence) {
            augustLength = -bottomRightCornerX + getWidth()/2;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);

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



//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint(bottomRightCornerX, y, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, mainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);

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
            k += side;
            l += 1;

            bottomRightCornerX = x + k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = bottomRightCornerX - side;
            float top = y - side;//y-side/2;
            float right = bottomRightCornerX;
            float bottom = y;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(mainActivity.numberYearPicker.getValue(), 8, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            //canvas.drawText(text, left, y+side/4, p);
            canvas.drawText(text, left + side / 4, bottom - side / 4, p);

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
                    mainActivity.autumn.currentDate = null;
                }

                if (selectedDay != null) {
                    if (selectedDay.date.getMonth() == date.getMonth() &&
                            selectedDay.date.getDate() == date.getDate() ) {
                        selectedDay = days.get(days.size()-1);

                    }
                }
            } else {
                days.get(l).left = left;
                days.get(l).top = top;
                days.get(l).right = right;
                days.get(l).bottom = bottom;

                if (currentDate != null) {
                    calendar.clear();
                    calendar.set(mainActivity.numberYearPicker.getValue(), 8, i);
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
            g += 1;
        }
        if (firstOccurrence) {

            septemberLength = -bottomRightCornerX + getWidth()/2;
            length = -bottomRightCornerX + getWidth();
            //Log.d("XY", "length:" + length);

            if (mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
                x = length;
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

                if(calendar.get(Calendar.MONTH) == Calendar.JULY) {
                    x = x - date.right + getWidth() / 2 - getWidth() / 4;
                }else if(calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
                    x = x - date.right + getWidth() / 2;
                }else if(calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
                    x = x - date.left + getWidth() / 2 + getWidth() / 4;
                }

                if(x >= 0) {
                    x = 0;
                }
                if(x <= length){
                    x = length;
                }
            }
            invalidate();
        }

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
                ////%%K///MainActivity.setDay(currentDate);

                ((MainActivity) context).updateSchedule(MainActivity.day);
            }
        }

        if (selectedDay != null) {
            p.setColor(Color.GREEN);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
            p.setStyle(Paint.Style.FILL);

            if (!firstOccurrence && selectedDay != MainActivity.day) {
                ////%%K///MainActivity.setDay(selectedDay);

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
//            canvas.drawPoint(bottomRightCornerX, y, p);
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

    }

    public void restore (){

        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
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
        invalidate();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:
                // если касание было начато в пределах квадрата

                // включаем режим перетаскивания
                drag = true;

                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX - x;
                dragY = evY - y;

                countDownTimer.cancel();

                //Log.d("XY1", "X:" + x + " Y:" + y + " evY:" + evY + " dragY:" + dragY + " length "+length+ " getHeight() "+getHeight());


                break;
            // тащим
            case MotionEvent.ACTION_MOVE:
                // если режим перетаскивания включен
                if (drag) {
                    // определеяем новые координаты
                    x = evX - dragX;
                    //y = evY - dragY;////////////////////////////////////////////////////////////
                    if(x >= 0) {
                        x = 0;
                    }
                    if(x <= length){
                        x = length;
                    }
                    invalidate();
                    //Log.d("XY2", "X:" + x + "Y:" + y + "length" + length);

                    //////////////////////////////////////////////////
                    float lengthDragging = x - length;
                    //Log.d("XY", "X:" + x + " Y:" + y + " evY:" + evY + " dragY:" + dragY + " length "+length+ " getHeight() "+getHeight()+ " lengthDragging:" + lengthDragging);
//
                    //////////////////////////////////////////////////
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

        public boolean onDoubleTap(MotionEvent e) {
            doubleTapX = e.getX();
            doubleTapY = e.getY();

            Iterator<Day> j = days.iterator();
            while (j.hasNext()){
                Day b = j.next();
                if(b.left <= doubleTapX && b.right >= doubleTapX) {
                    selectedDay = b;
                    mainActivity.winter.selectedDay = null;
                    mainActivity.winter.invalidate();
                    mainActivity.spring.selectedDay = null;
                    mainActivity.spring.invalidate();
                    mainActivity.autumn.selectedDay = null;
                    mainActivity.autumn.invalidate();
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
                    }else{
                        x -= millisUntilFinished / 30;
                    }
                    // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);

                    //проверить край
                    if(x >= 0) {
                        x = 0;
                    }
                    if(x <= length){
                        x = length;
                    }

                    //обновить
                    invalidate();
                }

                public void onFinish() {
                    //Log.d("onFling", "done!");
                }
            }.start();

            return true;
        }

    }


}
