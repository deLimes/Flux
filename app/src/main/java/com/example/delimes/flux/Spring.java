package com.example.delimes.flux;

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
import static com.example.delimes.flux.MainActivity.day;
import static com.example.delimes.flux.MainActivity.taskExtra;

class Spring extends View {
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
    float aprilLength = 0;
    float mayLength = 0;
    float juneLength = 0;
    Day selectedDay = null;
    Day currentDate = null;
    ArrayList<Day> days = new ArrayList<Day>();
    String monthName, reverseMonthName;
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

    public Spring(Context context) {
        super(context);

        this.context = context;
        init(context);

    }

    public Spring(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init(context);
    }

    public Spring(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        init(context);
    }

    private void init(Context context) {

        MainActivity.am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
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

        canvas.drawColor(Color.rgb(0, 255, 127));
        drawSpring(canvas);

    }

    public void fillInDays(int year){

        int l = 0;

        //II-ой квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.APRIL);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;

            calendar.clear();
            calendar.set(year, 3, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.MAY);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 4, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));

        }

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JUNE);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;

            calendar.clear();
            calendar.set(year, 5, i);

            Date date = new Date(calendar.getTimeInMillis());
            days.add(new Day(date, 0, 0, 0, 0));
        }

    }

    public void drawSpring(Canvas canvas){

        int Width = canvas.getWidth();//del
        int Height = canvas.getHeight();//del
        int fontHeight = side / 2;
        int strokeWidth = side / 5;
        int l = 0;

        //II-ой квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, MainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.APRIL);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();

        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int k = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l = i - 1;
            bottomLeftCornerY = y + k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = x;
            float top =  bottomLeftCornerY;
            float right =  x+side;
            float bottom = bottomLeftCornerY + side;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(MainActivity.numberYearPicker.getValue(), 3, i);
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
                    MainActivity.winter.currentDate = null;
                    MainActivity.autumn.currentDate = null;
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
                    calendar.set(MainActivity.numberYearPicker.getValue(), 3, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    if (date.getTime() == MainActivity.currDate.getTime()) {
                        currentDate.left = left;
                        currentDate.top = top;
                        currentDate.right = right;
                        currentDate.bottom = bottom;
                    }
                }

                //
                for (MainActivity.Task task : days.get(l).tasks) {
                    if(new Date(task.finishTime) == days.get(l).date){
                        p.setColor(Color.rgb(139, 0, 139));//75, 0, 130
                        p.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(left, top, right, bottom, p);
                        p.setStyle(Paint.Style.FILL);

                    }
                }
                //

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
        }
        if (firstOccurrence) {
            aprilLength = -bottomLeftCornerY + getHeight()/2;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
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

//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint(x, bottomLeftCornerY, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, MainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.MAY);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
            bottomLeftCornerY = y + k;
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = x;
            float top =  bottomLeftCornerY;
            float right =  x+side;
            float bottom = bottomLeftCornerY + side;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(MainActivity.numberYearPicker.getValue(), 4, i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
                p.setColor(Color.RED);
            }
            canvas.drawText(text, left+side/4, bottom-side/4, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, p);
            if (firstOccurrence) {
                calendar.clear();
                calendar.set(MainActivity.numberYearPicker.getValue(), 4, i);
                Date date = new Date(calendar.getTimeInMillis());
                days.add(new Day(date, left, top, right, bottom));

                if (date.getTime() == MainActivity.currDate.getTime()) {
                    currentDate = days.get(days.size()-1);
                    MainActivity.winter.currentDate = null;
                    MainActivity.autumn.currentDate = null;
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
                    calendar.set(MainActivity.numberYearPicker.getValue(), 4, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    if (date.getTime() == MainActivity.currDate.getTime()) {
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
        }
        if (firstOccurrence) {
            mayLength = -bottomLeftCornerY + getHeight()/2;
        }

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
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

//            p.setColor(Color.RED);
//            p.setStrokeWidth(side);
//            canvas.drawPoint(x, bottomLeftCornerY, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, MainActivity.numberYearPicker.getValue());
        calendar.set(Calendar.MONTH, Calendar.JUNE);

        monthName = dateFormat.format(calendar.getTimeInMillis());
        monthName = monthName.toUpperCase();
        reverseMonthName = new StringBuffer(monthName).reverse().toString();


        p.reset();
        p.setColor(Color.BLACK);
        p.setTextSize(fontHeight);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            l += 1;
            bottomLeftCornerY = y + k;

            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            float left = x;
            float top =  bottomLeftCornerY;
            float right =  x+side;
            float bottom = bottomLeftCornerY + side;

            p.reset();
            p.setColor(Color.BLACK);
            p.setTextSize(fontHeight);

            p.setStyle(Paint.Style.FILL);
            calendar.clear();
            calendar.set(MainActivity.numberYearPicker.getValue(), 5, i);
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
                    MainActivity.winter.currentDate = null;
                    MainActivity.autumn.currentDate = null;
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
                    calendar.set(MainActivity.numberYearPicker.getValue(), 5, i);
                    Date date = new Date(calendar.getTimeInMillis());

                    if (date.getTime() == MainActivity.currDate.getTime()) {
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
        }

        if (firstOccurrence) {

            juneLength = -bottomLeftCornerY + getHeight()/2;
            length = -bottomLeftCornerY + getHeight() - side;
            //Log.d("XY", "bottomLeftCornerY:" + length);

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

                if(calendar.get(Calendar.MONTH) == Calendar.APRIL) {
                    y = y - date.bottom + getHeight() / 2 - getHeight() / 4;
                    if (y >= 0) {
                        y = 0;
                    }
                    if (y <= length) {
                        y = length;
                    }
                }else if(calendar.get(Calendar.MONTH) == Calendar.MAY) {
                    y = y - date.top + getHeight() / 2;
                    if (y >= 0) {
                        y = 0;
                    }
                    if (y <= length) {
                        y = length;
                    }
                }else if(calendar.get(Calendar.MONTH) == Calendar.JUNE) {
                    y = y - date.top + getHeight() / 2 + getHeight() / 4;
                    if (y >= 0) {
                        y = 0;
                    }
                    if (y <= length) {
                        y = length;
                    }
                }
                invalidate();
            }
        }

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
        JsonArray array = parser.parse(MainActivity.yearStr.daysSpring).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            MainActivity.spring.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

            for (MainActivity.Task task : MainActivity.spring.days.get(i).tasks) {
                if (task.extra == taskExtra){
                    task.shown = true;
                    MainActivity.changedeTasksOfYear = true;
                }
                //%%C del - MainActivity.setReminder(task, MainActivity.spring.days.get(i).date);
                //%%C del - MainActivity.setReminder(task);
                if (!task.isDone){
                    MainActivity.spring.days.get(i).dayClosed = false;
                }
            }
            //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
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
                // если касание было начато в пределах квадрата

                // включаем режим перетаскивания
                drag = true;

                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX - x;
                dragY = evY - y;

                countDownTimer.cancel();

                //Log.d("WH", "W:" + Width + "H:" + Height);


                break;
            // тащим
            case MotionEvent.ACTION_MOVE:
                // если режим перетаскивания включен
                if (drag) {
                    // определеяем новые координаты
                    //x = evX - dragX;/////////////////////////////////////////////////////////
                    y = evY - dragY;
                    if(y >= 0) {
                        y = 0;
                    }
                    if(y <= length){
                        //y = length - getHeight();
                        y = length;
                    }
                    invalidate();
                    // Log.d("XY", "X:" + x + "Y:" + y +"length "+ length);

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
                    MainActivity.winter.selectedDay = null;
                    MainActivity.winter.invalidate();
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

            scrollTime = (int) velocityY;
            if (scrollTime < 0){
                scrollTime *= -1;
            }
            countDownTimer = new CountDownTimer(scrollTime, 50) {

                public void onTick(long millisUntilFinished) {
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
                    if(y <= length){
                        y = length;
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