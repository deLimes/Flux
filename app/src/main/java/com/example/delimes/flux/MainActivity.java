package com.example.delimes.flux;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    static Context context;
    int tucherWidth = 100;
    int[] colors = new int[2];
    int[] colors2 = new int[2];
    boolean veryFirstLaunch = true;
    boolean firstOccurrence = true;
    public ConstraintLayout constraintLayout;
    static ConstraintLayout сonstraintLayoutForSchedule;
    static ConstraintLayout сonstraintLayoutTaskParameters;
    View linearLayout;
    FrameLayout frameLayoutOfScroll;
    Guideline guideline;
    static Quarter winter, spring, summer, autumn;
    static YearStr yearStr;
    static boolean yearRestored = false;

    static ImageView ivLargerImage;
    static NumberYearPicker numberYearPicker;
    static ExtensibleTextView dateMonth;
    TextView taskTime;
    TextView taskDuration;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_BOOT_COMPLETED: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    TextView labelStartOfTask, labelEndOfTask;
    TextView startOfTask, endOfTask;
    TextView taskCopyTo;
    EditText inDays;
    ExtensibleEditText taskDescription;
    Button buttonAddTask, buttonDeleteTask;
    CheckBox everyYear, everyMonth;
    ScrollView scheduleScroll;
    LayoutInflater ltInflater;
    LinearLayout linLayout;
    static Day day, previousDay;
    static Task task;
    ArrayList<Task> addedTasksOfYear = new ArrayList<Task>();
    ArrayList<Task> destroyedTasksOfYear = new ArrayList<Task>();
    static boolean changedeTasksOfYear, yearNumberChangedForMove, yearNumberChangedForFling, yearNumberChangedForDraw, processUpdateSchedule;
    public static ArrayList<Task> cyclicTasks = new ArrayList<Task>();
    View layoutDayOfWeek;
    TextView  monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    static int curentYearNumber;
    static int chosenYearNumber = 0;
    static int previousChosenYearNumber = 0;

    static Date currDate;
    public static long currHours;
    public static long currMinutes;
    public static long currSeconds;
    static Calendar calendar = GregorianCalendar.getInstance();

    ///////////////////////////////////////////////////////////////////////////
    static AlarmManager alarmManager;
    static ActivityManager activityManager;
    static PendingIntent pIntent;

    private static int notifyId = 101;
    static int taskExtra = 0;
    static int yearFromIntent = 0;
    ///////////////////////////////////////////////////////////////////////////

    boolean yearReducedForFling = false;

    AnalogClock analogClock;
    public static TimeChangedReceiver timeChangedReceiver;
    public static Handler tickHandler;
    private Menu menu_main;

    private final int MY_PERMISSIONS_REQUEST_RECEIVE_BOOT_COMPLETED = 101;
    private AlphaAnimation alphaAnimationClick = new AlphaAnimation(1f, 0.2f);
    private static long dateDoomsday = 95617497600000L;//(4999, 11, 31);

    public MainActivity() {
        this.context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        /////////////////////////////////////////////
        // Завершить работу данного окна
        //this.finish();
        //////////////////////////////////////////////


        //////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                            MY_PERMISSIONS_REQUEST_RECEIVE_BOOT_COMPLETED);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
        //////////////////////////////////////////////

        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        veryFirstLaunch = preference.getBoolean("veryFirstLaunch", true);

//        if (veryFirstLaunch) {
//            try {
//                final Intent intent = new Intent();
//                String manufacturer = android.os.Build.MANUFACTURER;
//                if ("xiaomi".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
//                } else if ("oppo".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
//                } else if ("vivo".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
//                } else if ("Letv".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
//                } else if ("Honor".equalsIgnoreCase(manufacturer)) {
//                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
//                }
//
//                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                if (list.size() > 0) {
//                    startActivity(intent);
//                }
//            } catch (Exception e) {
//                Log.e("exc", String.valueOf(e));
//            }
//        }


        restoreCyclicTasks();
        //numberYearPicker.setValue(curentYearNumber);







        //task = (Task) getIntent().getSerializableExtra("task");


        calendar.clear();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(year, month, day);
        currDate = new Date(MainActivity.calendar.getTimeInMillis());

        if (yearFromIntent != 0){
            numberYearPicker.setValue(yearFromIntent);
        }else {
            numberYearPicker.setValue(year);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("123", "onStop: ");

        saveCyclicTasks();
        if(MainActivity.yearRestored && (changedeTasksOfYear || addedTasksOfYear.size() > 0 || destroyedTasksOfYear.size() > 0) && autumn.days.size() > 0 ){
            Log.d("Year", "Year was saved");
            //saveYear();
            saveYearToFile();
        }

        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean("analogClockIsVisibile", analogClock.getVisibility() == View.VISIBLE);
        editor.putBoolean("veryFirstLaunch", false);

        editor.commit();

        //finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("123", "onDestroy: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new MyCalendar(this));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        /////////////////////////////////////////////
        // Завершить работу данного окна

//        boolean finish = true;
//        if (finish) {
//            Log.v("123", "onCreate: finished ");
//            this.finish();
//            return;
//        }

        context = this;

        calendar.setTimeInMillis(System.currentTimeMillis());
        curentYearNumber = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(4999, 11, 31);
        dateDoomsday = calendar.getTimeInMillis();

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        colors2[0] = Color.parseColor("#C1FFC1");
        colors2[1] = Color.parseColor("#B4EEB4");

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);



//        MyCalendar myCalendar = (MyCalendar) findViewById(R.id.myCalendar);
//
//        //////////////////////////////////////
//        TextView textView1 = (TextView) findViewById(R.id.textView1);
//        TextView textView2 = (TextView) findViewById(R.id.textView2);
//        TextView textView3 = (TextView) findViewById(R.id.textView3);
//        TextView textView4 = (TextView) findViewById(R.id.textView4);
//        TextView textView5 = (TextView) findViewById(R.id.textView5);
//        myCalendar.textView1 = textView1;
//        myCalendar.textView2 = textView2;
//        myCalendar.textView3 = textView3;
//        myCalendar.textView4 = textView4;
//        myCalendar.textView5 = textView5;
//        //////////////////////////////////////////////

        //myCalendar.setLayoutParams(new LinearLayout.LayoutParams(myCalendar.longSide, myCalendar.longSide));
        ////////////////////////////////////////////////////////////////
        guideline = new Guideline(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.guidePercent = -0.85f;
        params.orientation = LinearLayout.VERTICAL;
        guideline.setLayoutParams(params);
        guideline.setId(R.id.guideline);
        constraintLayout.addView(guideline);
        /////////////////////////////////////////////////////////////////



        //winter.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //Log.d("WH", "getWidth:" +winter.getMeasuredWidth()+"getHeight:"+winter.getMeasuredHeight());

        //TODO AddView


        constraintLayout.post(new Runnable() {
            @Override
            public void run() {

                ConstraintLayout.LayoutParams params;
                final int width = constraintLayout.getRight() + guideline.getLeft();
                int tucherWidth;
                int tucherHeight;

                //Winter
                tucherWidth = constraintLayout.getRight();
                tucherHeight = width;

                winter.side = width/2;
                winter.x = tucherWidth;
                winter.y = tucherHeight;//- tucherHeight / 2

                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topToTop = R.id.constraintLayout;

                params.width = tucherWidth;
                params.height = tucherHeight;
                winter.setLayoutParams(params);

                //Spring
                tucherWidth = width;
                tucherHeight = constraintLayout.getBottom()-width*2;
                //Log.d("WH", "tucherHeight:" +tucherHeight);

                spring.side = width/2;
                spring.x = tucherWidth - tucherWidth / 2;
                spring.y = 0;

                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topToBottom = R.id.winter;
                params.leftToLeft = R.id.constraintLayout;

                params.width = tucherWidth;
                params.height = tucherHeight;
                spring.setLayoutParams(params);


                //Summer
                tucherWidth = constraintLayout.getRight();
                tucherHeight = width;
                //Log.d("WH", "tucherHeight:" +tucherHeight);

                summer.side = width/2;
                summer.x = 0;
                summer.y = tucherHeight - tucherHeight / 2;

                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomToBottom = R.id.constraintLayout;
                params.leftToLeft = R.id.constraintLayout;

                params.width = tucherWidth;
                params.height = tucherHeight;
                summer.setLayoutParams(params);

                //Autumn
                tucherWidth = width;
                tucherHeight = constraintLayout.getBottom() - width * 2;
                //Log.d("WH", "tucherHeight:" +tucherHeight);

                autumn.side = width/2;
                autumn.x = tucherWidth - tucherWidth / 2f;
                autumn.y = tucherHeight;

                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topToBottom = R.id.winter;
                params.rightToRight = R.id.constraintLayout;

                params.width = tucherWidth;
                params.height = tucherHeight;
                autumn.setLayoutParams(params);

                //сonstraintLayoutForSchedule
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topToBottom = R.id.winter;
                params.leftToRight = R.id.spring;
                params.bottomToTop = R.id.summer;
                params.rightToLeft = R.id.autumn;

                params.width = constraintLayout.getRight() - width * 2;
                params.height = constraintLayout.getBottom() - width * 2;

                //сonstraintLayoutForSchedule.setBackgroundColor(Color.CYAN);
                сonstraintLayoutForSchedule.setLayoutParams(params);


                //NumberYearPicker
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topToTop = R.id.сonstraintLayoutForSchedule;
                params.rightToRight = R.id.сonstraintLayoutForSchedule;
                params.leftToLeft = R.id.сonstraintLayoutForSchedule;
                //valueText.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL );
                //numberYearPicker.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

//                calendar.clear();
//                calendar.setTimeInMillis(System.currentTimeMillis());

                numberYearPicker.setElementHeight((int)(width/1.5f));
                numberYearPicker.setElementWidth((int)(width/1.5f));
//                numberYearPicker.setElementHeight(width/2);
//                numberYearPicker.setElementWidth(width/2);
                numberYearPicker.setTextSize( (int)(width/1.5f/5f) );
                numberYearPicker.rebuild(getBaseContext());
                //numberYearPicker.setValue(calendar.get(Calendar.YEAR));
                numberYearPicker.setValue(curentYearNumber);

                numberYearPicker.setLayoutParams(params);

                /*//ivLargerImage
                params = new ConstraintLayout.LayoutParams(numberYearPicker.getWidth(), numberYearPicker.getHeight());
                params.topToTop = R.id.сonstraintLayoutForSchedule;
                params.rightToRight = R.id.сonstraintLayoutForSchedule;
                params.leftToLeft = R.id.сonstraintLayoutForSchedule;
//                params.topToTop = R.id.numberYearPicker;
//                params.rightToRight = R.id.numberYearPicker;
//                params.leftToLeft = R.id.numberYearPicker;
//                params.bottomToBottom = R.id.numberYearPicker;

                ivLargerImage.setVisibility(View.GONE);
                ivLargerImage.setLayoutParams(params);*/

                //dateMonth
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutForSchedule;
                params.topToBottom = R.id.numberYearPicker;
                params.rightToRight = R.id.сonstraintLayoutForSchedule;
                //params.bottomToBottom = R.id.numberYearPicker;
                //params.leftToLeft = R.id.сonstraintLayoutForSchedule;

                params.height = numberYearPicker.getHeight();
                dateMonth.setGravity( Gravity.CENTER_VERTICAL);

                //dateMonth.setBackgroundColor(Color.RED);

                dateMonth.setTextSize( (int)(width/1.5f/5f) );
                dateMonth.setTextColor(Color.BLACK);
                dateMonth.setLayoutParams(params);

                //сonstraintLayoutTaskParameters
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutForSchedule;
                params.topToBottom = R.id.dateMonth;
                params.rightToRight = R.id.сonstraintLayoutForSchedule;
                //params.bottomToTop = R.id.buttonAddTask;
                //params.width = constraintLayout.getRight() - width * 2;
                //сonstraintLayoutTaskParameters.setBackgroundColor(Color.RED);
                сonstraintLayoutTaskParameters.setVisibility(View.GONE);
                сonstraintLayoutTaskParameters.setLayoutParams(params);

                //taskTime
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToTop = R.id.сonstraintLayoutTaskParameters;
                //taskTime.setBackgroundColor(Color.GREEN);

                //params.width = (int)(width * 0.75f);
                params.height = width/2;
                taskTime.setLayoutParams(params);

                //taskDuration
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.taskTime;
                //taskDuration.setBackgroundColor(Color.GREEN);

                //params.width = (int)(width * 0.75f);;
                params.height = width/2;
                taskDuration.setLayoutParams(params);

                //taskDescription
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToRight = R.id.taskTime;
                params.rightToRight = R.id.сonstraintLayoutTaskParameters;
                params.topToTop = R.id.сonstraintLayoutTaskParameters;
                params.bottomToBottom = R.id.taskTime;
                params.leftMargin = 10;
                //params.bottomToBottom = R.id.сonstraintLayoutForSchedule;

                //params.width = constraintLayout.getRight() - width * 2 - (int)(width * 0.75f);
                params.width = constraintLayout.getRight() - width * 3;
                params.height = width;
                taskDescription.setLayoutParams(params);


                //layoutDayOfWeek
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.taskDuration;
                layoutDayOfWeek.setLayoutParams(params);


                //everyYear
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.layoutDayOfWeek;
                everyYear.setTextSize( (int)(width/1.5f/5f) );
                everyYear.setLayoutParams(params);

                //everyMonth
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.everyYear;
                everyMonth.setTextSize( (int)(width/1.5f/5f) );
                everyMonth.setLayoutParams(params);

                //inDays
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.everyMonth;
                inDays.setTextSize( (int)(width/1.5f/5f) );
                inDays.setLayoutParams(params);

                //labelStartOfTask
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.inDays;
                //params.leftMargin = 10;
                //params.rightToLeft = R.id.startOfTask;
                labelStartOfTask.setLayoutParams(params);

                //startOfTask
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToRight = R.id.labelStartOfTask;
                params.topToBottom = R.id.inDays;
                //params.rightToRight = R.id.сonstraintLayoutForSchedule;
                params.leftMargin = 10;
                startOfTask.setLayoutParams(params);
                //////startOfTask.setBackgroundColor(Color.BLUE);

                //labelEndOfTask
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.startOfTask;
                //params.rightToRight = R.id.сonstraintLayoutForSchedule;
                labelEndOfTask.setLayoutParams(params);

                //endOfTask
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToRight = R.id.labelEndOfTask;
                params.topToBottom = R.id.startOfTask;
                params.leftMargin = 10;
                //params.rightToLeft = R.id.labelEndOfTask;
                endOfTask.setLayoutParams(params);
                //////endOfTask.setBackgroundColor(Color.RED);


                //taskCopyTo
                params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
                params.topToBottom = R.id.labelEndOfTask;
                taskCopyTo.setLayoutParams(params);


                //buttonAddTask
                params = new ConstraintLayout.LayoutParams((int)(width/1.5f),(int)(width/1.5f));
                params.rightToRight = R.id.сonstraintLayoutForSchedule;
                params.topToBottom= R.id.сonstraintLayoutTaskParameters;

                buttonAddTask.setTextSize( (int)(width/1.5f/5f) );
                buttonAddTask.setLayoutParams(params);

                //buttonDeleteTask
                params = new ConstraintLayout.LayoutParams((int)(width/1.5f),(int)(width/1.5f));
                params.rightToLeft = R.id.buttonAddTask;
                params.topToBottom = R.id.сonstraintLayoutTaskParameters;

                buttonDeleteTask.setTextSize( (int)(width/1.5f/5f) );
                buttonDeleteTask.setLayoutParams(params);
                ////////////



                ////////////

                //linearLayout
                buttonAddTask.post(new Runnable() {
                    @Override
                    public void run() {

                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.leftToLeft = R.id.сonstraintLayoutForSchedule;
                        params.rightToRight = R.id.сonstraintLayoutForSchedule;
                        params.topToBottom = R.id.buttonAddTask;
                        params.bottomToBottom = R.id.сonstraintLayoutForSchedule;

                        //linearLayout.setBackgroundColor(Color.RED);
                        params.width = сonstraintLayoutForSchedule.getWidth();
                        params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                        linearLayout.setLayoutParams(params);

                        //analogClock
                        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
                        boolean analogClockIsVisibile = preference.getBoolean("analogClockIsVisibile", false);

                        if (analogClockIsVisibile) {
                            analogClock.setVisibility(View.VISIBLE);
                            MenuItem actionClockMenuItem = menu_main.findItem(R.id.action_clock);
                            actionClockMenuItem.setTitle(getResources().getString(R.string.action_clock_off));
                        }

                        analogClock.side = width/2;
                        analogClock.x = analogClock.side * 5;
                        analogClock.y = 0;

                        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.width = analogClock.side * 5;
                        params.height = params.width;
                        params.leftToLeft = R.id.constraintLayout;
                        //params.rightToRight = R.id.constraintLayout;
                        params.topToTop = R.id.constraintLayout;
                        //params.bottomToBottom = R.id.constraintLayout;

                        params.leftMargin = сonstraintLayoutForSchedule.getRight() - analogClock.side * 5;
                        params.topMargin = сonstraintLayoutForSchedule.getBottom() - analogClock.side * 5;

                        analogClock.setLayoutParams(params);

                        //ivLargerImage
                        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftToLeft = R.id.сonstraintLayoutForSchedule;
                        params.topToTop = R.id.сonstraintLayoutForSchedule;
                        params.rightToRight = R.id.сonstraintLayoutForSchedule;
                        params.bottomToTop = R.id.сonstraintLayoutTaskParameters;
                        //ivLargerImage.setBackgroundColor(R.color.ivLargerImageColor);	android.R.attr
                        //ivLargerImage.setBackgroundColor(((ColorDrawable)constraintLayout.getBackground()).getColor());
                        TypedValue typedValue = new TypedValue();
                        Resources.Theme theme = getTheme();
                        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true);
                        ivLargerImage.setBackgroundColor(typedValue.data);
                        ivLargerImage.setVisibility(View.GONE);
                        ivLargerImage.setLayoutParams(params);

                    }

                });


            }

        });


        ViewTreeObserver vto = constraintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // view сформирован, можно получать размеры


            }
        });

        winter = new Quarter(this,1);
        winter.setId(R.id.winter);
        //winter.setBackground(getDrawable(R.drawable.background_gradient_winter));
        winter.setBackgroundColor(getResources().getColor(R.color.colorWinter));
        constraintLayout.addView(winter);

        spring = new Quarter(this, 2);
        spring.setId(R.id.spring);
        //spring.setBackground(getDrawable(R.drawable.background_gradient_spring));
        spring.setBackgroundColor(getResources().getColor(R.color.colorSpring));
        constraintLayout.addView(spring);

        summer = new Quarter(this,3);
        summer.setId(R.id.summer);
        //summer.setBackground(getDrawable(R.drawable.background_gradient_summer));
        summer.setBackgroundColor(getResources().getColor(R.color.colorSummer));
        constraintLayout.addView(summer);

        autumn = new Quarter(this,4);
        autumn.setId(R.id.autumn);
        //autumn.setBackground(getDrawable(R.drawable.background_gradient_autumn));
        autumn.setBackgroundColor(getResources().getColor(R.color.colorAutumn));
        constraintLayout.addView(autumn);

        сonstraintLayoutForSchedule  = new ConstraintLayout(this);
        сonstraintLayoutForSchedule.setId(R.id.сonstraintLayoutForSchedule);
        constraintLayout.addView(сonstraintLayoutForSchedule);

        numberYearPicker = new NumberYearPicker(this, 0);
        numberYearPicker.setId(R.id.numberYearPicker);
        сonstraintLayoutForSchedule.addView(numberYearPicker);

        dateMonth = new ExtensibleTextView(this);
        dateMonth.setId(R.id.dateMonth);
        dateMonth.getPaint().setUnderlineText(true);

        сonstraintLayoutForSchedule.addView(dateMonth);

        сonstraintLayoutTaskParameters  = new ConstraintLayout(this);
        сonstraintLayoutTaskParameters.setId(R.id.сonstraintLayoutTaskParameters);
        //сonstraintLayoutTaskParameters.setBackgroundResource(R.drawable.layout_border);
        сonstraintLayoutForSchedule.addView(сonstraintLayoutTaskParameters);

        taskTime = new TextView(this);
        taskTime.setId(R.id.taskTime);
        taskTime.getPaint().setUnderlineText(true);
        сonstraintLayoutTaskParameters.addView(taskTime);

        taskDuration = new TextView(this);
        taskDuration.setId(R.id.taskDuration);
        taskDuration.getPaint().setUnderlineText(true);
        сonstraintLayoutTaskParameters.addView(taskDuration);

        taskDescription = new ExtensibleEditText(this);//Extensible
        taskDescription.setId(R.id.taskDescription);
        taskDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        taskDescription.setSingleLine();
//        taskDescription.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//        taskDescription.setMaxLines(5);
        taskDescription.setEnabled(false);
        сonstraintLayoutTaskParameters.addView(taskDescription);


        labelStartOfTask = new TextView(this);
        labelStartOfTask.setId(R.id.labelStartOfTask);
        labelStartOfTask.setText("Дата начала:");
        сonstraintLayoutTaskParameters.addView(labelStartOfTask);

        startOfTask = new TextView(this);
        startOfTask.setId(R.id.startOfTask);
        startOfTask.getPaint().setUnderlineText(true);
        сonstraintLayoutTaskParameters.addView(startOfTask);

        labelEndOfTask = new TextView(this);
        labelEndOfTask.setId(R.id.labelEndOfTask);
        labelEndOfTask.setText("Дата окончания:");
        сonstraintLayoutTaskParameters.addView(labelEndOfTask);

        endOfTask = new TextView(this);
        endOfTask.setId(R.id.endOfTask);
        endOfTask.getPaint().setUnderlineText(true);
        сonstraintLayoutTaskParameters.addView(endOfTask);


        taskCopyTo = new TextView(this);
        taskCopyTo.setId(R.id.taskCopyTo);
        taskCopyTo.setText("Копировать в дату...");
        taskCopyTo.getPaint().setUnderlineText(true);
        сonstraintLayoutTaskParameters.addView(taskCopyTo);

        buttonAddTask = new Button(this);
        buttonAddTask.setId(R.id.buttonAddTask);
        buttonAddTask.setText( "+" );
        buttonAddTask.setTextColor(Color.BLACK);
        сonstraintLayoutForSchedule.addView(buttonAddTask);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (day == null){
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Сначала нужно выбрать день",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }
                if (day.numberOfTasksPerDay() >= 99){
                    Toast toast = Toast.makeText(MainActivity.this,
                            "Достигнут предел - 99 задач в день",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }
                final Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                myCalender.setTimeInMillis(day.date.getTime());
                myCalender.set(Calendar.HOUR_OF_DAY, hour);
                myCalender.set(Calendar.MINUTE, minute);

                Task newTask = new Task(true, false,"", myCalender.getTimeInMillis(), 0, 0);
                day.tasks.add(newTask);
                addedTasksOfYear.add(newTask);

                MainActivity.task = newTask;

                сonstraintLayoutTaskParameters.setVisibility(View.VISIBLE);
                view.post(new Runnable() {
                    @Override
                    public void run() {

                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                        params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                        linearLayout.setLayoutParams(params);

                    }

                });

               /* calendar.clear();
                calendar.setTimeInMillis(task.startTime);
                taskTime.setText(((""+ calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY))+
                        ":"+ ((""+ calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE)));

                taskDuration.setText(
                        ((""+ task.durationHours).length() == 1 ? "0" + task.durationHours : "" + task.durationHours)+
                                ":"+ ((""+ task.durationMinutes).length() == 1 ? "0" + task.durationMinutes : "" + task.durationMinutes));

                taskDescription.setText(task.content);*/

                day.dayClosed = true;
                for (Task task : day.tasks) {
                    if(!task.isDone && task.isValid){
                        day.dayClosed = false;
                    }
                }

                updateSchedule(day);


                //setTestReminder();
                taskDescription.setEnabled(true);
                taskDescription.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(taskDescription, InputMethodManager.SHOW_IMPLICIT);

                //Замер памяти
//                SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preference.edit();
//
//                for (int i = 0; i < 365 * 5; i++) {
//                    Task taskCopy = new Task(false, false, "", 0, 0, 0);
//                    task.duplicate(taskCopy);
//
//                    Runtime.getRuntime().gc();
//                    long before = Runtime.getRuntime().freeMemory();
//
//                    cyclicTasks.add(taskCopy);
//                    long after = Runtime.getRuntime().freeMemory();
//
//                    long result = before - after;
//                    if(result > 0){
//                        Log.d("myLogs", "Memory used:" + result);
//                    }
//
//                    String jsonStr = new Gson().toJson(cyclicTasks);
//                    editor.putString("cyclicTasks", jsonStr);
//                    editor.commit();
//                }
                //Замер памяти

            }
        });

        buttonDeleteTask = new Button(this);
        buttonDeleteTask.setId(R.id.buttonDeleteTask);
        buttonDeleteTask.setText( "-" );
        buttonDeleteTask.setTextColor(Color.BLACK);
        сonstraintLayoutForSchedule.addView(buttonDeleteTask);
        buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {

                    day.tasks.remove(task);
                    if (previousDay != null){
                        previousDay.tasks.remove(task);
                    }
                    if (!addedTasksOfYear.remove(task)) {
                        destroyedTasksOfYear.add(task);
                    }


                    calendar.clear();
                    calendar.setTimeInMillis(task.startTime);
                    final Calendar myCalender = Calendar.getInstance();

                    myCalender.clear();
                    myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                    myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                    myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                    long dateTaskStartTime = myCalender.getTimeInMillis();

                    if (dateTaskStartTime == day.date.getTime()) {

                        if (task.monday ||
                                task.tuesday ||
                                task.wednesday ||
                                task.thursday ||
                                task.friday ||
                                task.saturday ||
                                task.sunday ||
                                task.everyYear ||
                                task.everyMonth ||
                                task.inDays > 0) {

                            //%%C del - [
                            //////////while (cyclicTasks.remove(task));
                            Iterator<Task> iter = cyclicTasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //%%C timel del - cyclicTasks.remove(task);
                                    t.remove = true;
                                }
                            }
//                            ]

                            task.remove = true;
                            refreshCyclicTasks(task);

                        }
                    }

                    task.removeFromAM = true;
                    setReminder(context, task, day.date);
                    //%%C del - setReminder(task);

                    day.dayClosed = true;
                    for (Task task : day.tasks) {
                        if(!task.isDone && task.isValid){
                            day.dayClosed = false;
                        }
                    }

                    MainActivity.task = null;
                    updateSchedule(day);
                    сonstraintLayoutTaskParameters.setVisibility(View.GONE);
                    view.post(new Runnable() {
                        @Override
                        public void run() {

                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                            params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                            linearLayout.setLayoutParams(params);

                        }

                    });

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();

                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(taskDescription.getWindowToken(), 0);

            }
        });



/*
        scheduleScroll = new ScrollView(this);
        scheduleScroll.setId(R.id.scheduleScroll);
        сonstraintLayoutForSchedule.addView(scheduleScroll);

        frameLayoutOfScroll = new FrameLayout(this);
        frameLayoutOfScroll.setId(R.id.frameLayoutOfScroll);
        scheduleScroll.addView(frameLayoutOfScroll);
*/
        ltInflater = getLayoutInflater();

        linearLayout = ltInflater.inflate(R.layout.layout, сonstraintLayoutForSchedule, false);
        сonstraintLayoutForSchedule.addView(linearLayout);
        linLayout = (LinearLayout) findViewById(R.id.linLayout);

        ivLargerImage = new ImageView(this);
        ivLargerImage.setId(R.id.ivLargerImage);
        сonstraintLayoutForSchedule.addView(ivLargerImage, сonstraintLayoutForSchedule.getChildCount());



        layoutDayOfWeek = ltInflater.inflate(R.layout.layout_day_of_week, сonstraintLayoutForSchedule, false);
        layoutDayOfWeek.setId(R.id.layoutDayOfWeek);
        сonstraintLayoutTaskParameters.addView(layoutDayOfWeek);

        dateMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(alphaAnimationClick);
                showDatePickerForDateMonth();

            }
        });

        monday = (TextView) layoutDayOfWeek.findViewById(R.id.monday);
        tuesday = (TextView) layoutDayOfWeek.findViewById(R.id.tuesday);
        wednesday = (TextView) layoutDayOfWeek.findViewById(R.id.wednesday);
        thursday = (TextView) layoutDayOfWeek.findViewById(R.id.thursday);
        friday = (TextView) layoutDayOfWeek.findViewById(R.id.friday);
        saturday = (TextView) layoutDayOfWeek.findViewById(R.id.saturday);
        sunday = (TextView) layoutDayOfWeek.findViewById(R.id.sunday);

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.monday = !task.monday;

                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.monday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.tuesday = !task.tuesday;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.tuesday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {


                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.wednesday = !task.wednesday;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.wednesday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {


                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.thursday = !task.thursday;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.thursday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.friday = !task.friday;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.friday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.saturday = !task.saturday;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.saturday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        saturday.setTextColor(getResources().getColor(android.R.color.background_light));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                        saturday.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (task != null) {
                    task.sunday = !task.sunday;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.sunday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        sunday.setTextColor(getResources().getColor(android.R.color.background_light));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                        sunday.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });




        taskDescription.setOnKeyListener(new View.OnKeyListener() {
                                             @Override
                                             public boolean onKey(View view, int keyCode, KeyEvent event) {
                                                 if (event.getAction() == KeyEvent.ACTION_DOWN){
                                                     if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                                         if(task != null) {
                                                             task.content = taskDescription.getText().toString();
                                                             return true;
                                                         }
                                                     }
                                                 }
                                                 return false;
                                             }
                                         });

        taskDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent event) {

                if(task != null) {
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    //установить контент
                    if (task.content != taskDescription.getText().toString()) {
                        changedeTasksOfYear = true;
                    }

                    task.content = taskDescription.getText().toString();

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    updateSchedule(day);


                    return false;
                }
                return true;

            }

        });


                taskTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(task != null) {
                            view.startAnimation(alphaAnimationClick);
                            showTimePicker("Choose start time:", false);
                        }

                    }
                });

        taskDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task != null) {
                    view.startAnimation(alphaAnimationClick);
                    showTimePicker("Choose task duration:", true);
                }
            }
        });

        startOfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task != null) {
                    view.startAnimation(alphaAnimationClick);
                    showDatePickerForStartTime();
                }
            }
        });

        endOfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task != null) {
                    view.startAnimation(alphaAnimationClick);
                    showDatePickerForFinishTime();
                }
            }
        });

        endOfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task != null) {
                    view.startAnimation(alphaAnimationClick);
                    showDatePickerForFinishTime();
                }
            }
        });

        taskCopyTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task != null) {
                    view.startAnimation(alphaAnimationClick);
                    showDatePickerForCopyTo();
                }
            }
        });

        //dictionary.clear();
                //dictionary.addAll(new ArrayList<String>(Arrays.asList(array)));
                //tasks

        /*
       GridView gridView = new GridView(this);
        gridView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, tasks));
        gridView.setNumColumns(2);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        gridView.setTextFilterEnabled(true);
*/


                //myCalendar.invalidate();


                //MySurfaceView mySurfaceView = new MySurfaceView(this);
                //MySurfaceView mySurfaceView = (MySurfaceView) findViewById(R.id.mySurfaceView);;

                //mySurfaceView.setLayoutParams(new LinearLayout.LayoutParams(2000, 2000));
                //linearLayout.addView(mySurfaceView);

        everyYear = new CheckBox(this);
        everyYear.setId(R.id.everyYear);
        everyYear.setText("Год");

        everyYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!processUpdateSchedule && task != null) {

                    task.everyYear = b;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });
        сonstraintLayoutTaskParameters.addView(everyYear);

        everyMonth = new CheckBox(this);
        everyMonth.setId(R.id.everyMonth);
        everyMonth.setText("Месяц");
        everyMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!processUpdateSchedule && task != null) {

                    task.everyMonth = b;
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                    //
                }
            }
        });

        сonstraintLayoutTaskParameters.addView(everyMonth);

        inDays = new EditText(this);
        inDays.setId(R.id.inDays);
        inDays.setHint("Кол-во дней");
        inDays.setInputType(InputType.TYPE_CLASS_NUMBER);
        inDays.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inDays.setSingleLine();

        inDays.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent event) {

                if(task != null) {
                    if (!textView.getText().toString().isEmpty()) {
                        int inDays = Integer.valueOf(textView.getText().toString());
                        task.inDays = inDays;
                    } else {
                        task.inDays = 0;
                    }
                    while (cyclicTasks.remove(task));
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            cyclicTasks.remove(task);
                        }
                    }
                    task.isCyclic = false;

                    if (task.monday ||
                            task.tuesday ||
                            task.wednesday ||
                            task.thursday ||
                            task.friday ||
                            task.saturday ||
                            task.sunday ||
                            task.everyYear ||
                            task.everyMonth ||
                            task.inDays > 0) {

                        if (task.finishTime == task.startTime){
                            task.finishTime = dateDoomsday;
                        }
                        task.isCyclic = true;
                        Task taskCopy = new Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);
                        cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    changedeTasksOfYear = true;
                    refreshCyclicTasks(task);
                    updateSchedule(day);

                    winter.invalidate();
                    spring.invalidate();
                    summer.invalidate();
                    autumn.invalidate();
                }



                //установить контент
                return false;
            }

        });
        сonstraintLayoutTaskParameters.addView(inDays);




        analogClock = new AnalogClock(this);
        analogClock.setId(R.id.analogClock);
        constraintLayout.addView(analogClock, constraintLayout.getChildCount());
        //сonstraintLayoutForSchedule.setAnalogClock((AnalogClock)сonstraintLayoutForSchedule.findViewById(R.id.analogClock));
        //сonstraintLayoutForSchedule.setAnalogClock(analogClock);
        //сonstraintLayoutForSchedule.draw(new Canvas());



        ///////////////////////////////////////////////////////////////////////////
        //task = (Task) getIntent().getSerializableExtra("task");
        String strTaskExtra = getIntent().getStringExtra("extra");
        if (strTaskExtra != null) {
            taskExtra = Integer.valueOf(strTaskExtra);
        }

        String strYear = getIntent().getStringExtra("year");
        if (strYear != null) {
            yearFromIntent = Integer.valueOf(strYear);
        }
        //Log.d("myLogs", "taskRxtra onCreate " + taskExtra);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);


        ///////////////////////////////////////////////////////////////////////////




//        NumberPicker numberYearPicker = new NumberPicker(this);
//        numberYearPicker.setMaxValue(4999);
//        numberYearPicker.setMinValue(0);
//        numberYearPicker.setValue(calendar.get(Calendar.YEAR));
//        numberYearPicker.getValue();
//        numberYearPicker.setOrientation(LinearLayout.HORIZONTAL);
//        constraintLayout.addView(numberYearPicker);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String strTaskExtra = intent.getStringExtra("extra");
        if (strTaskExtra != null) {
            taskExtra = Integer.valueOf(strTaskExtra);
        }

        String strYear = intent.getStringExtra("year");
        if (strYear != null) {
            yearFromIntent = Integer.valueOf(strYear);
        }

        //Сохраним текущий изменения
        //saveYear();

        // Для того, чтобы таску поставить признак shown=true
        winter.restore = true;
        spring.restore = true;
        summer.restore = true;
        autumn.restore = true;

        winter.invalidate();
        spring.invalidate();
        summer.invalidate();
        autumn.invalidate();
    }

    public static void setReminder(Context context,Task task, Date date) {

        Log.d("myLogs", "setReminder: 0");
//        try {
//            int jh = 1/0;
//        }catch (Exception e){
//            //Log.d("myLogs", "setReminder: 0.1"+ e.printStackTrace() );
//            e.printStackTrace();
//        }

        calendar.clear();
        calendar.setTimeInMillis(date.getTime());

        Log.d("myLogs", "setReminder1: .context " +context);
        Intent notificationIntent = new Intent(context, Receiver.class);
        notificationIntent.putExtra("extra", Integer.toString(task.extra));
        notificationIntent.putExtra("content", task.content);
        notificationIntent.putExtra("year", Integer.toString(calendar.get(Calendar.YEAR)));
        //notificationIntent.putExtra("task", task);

        Uri data = Uri.parse(notificationIntent.toUri(Intent.URI_INTENT_SCHEME));
        notificationIntent.setData(data);

        calendar.clear();
        calendar.setTimeInMillis(task.startTime);

        final Calendar myCalender = Calendar.getInstance();
        //myCalender.setTimeInMillis(task.startTime);
        myCalender.setTimeInMillis(date.getTime());
        myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        pIntent = PendingIntent.getBroadcast(context, task.extra, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (task.removeFromAM) {
            Log.d("myLogs", "setReminder: 1");
            alarmManager.cancel(pIntent);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifyId = Integer.valueOf(notificationIntent.getStringExtra("extra"));
            notificationManager.cancel(notifyId);
            task.removeFromAM = false;
        }else {
            Log.d("myLogs", "setReminder: 2 ");
            Log.d("myLogs", "setReminder: 2.1 "
                    + "isValid: " + task.isValid
                    + "!shown: " + !task.shown
                    + "!isDone: " + !task.isDone
            );
            Log.d("myLogs", "setReminder: 2.1 content: " + task.content);

            if(task.isValid && !task.shown && !task.isDone) {
                Log.d("myLogs", "setReminder: 3");
                //%%C del - am.cancel(pIntent);
                long alarmPeriodicTime = myCalender.getTimeInMillis();
                if (Build.VERSION.SDK_INT >= 23) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmPeriodicTime, pIntent);
                } else if (Build.VERSION.SDK_INT >= 19) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmPeriodicTime, pIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmPeriodicTime, pIntent);
                }
                //alarmManager.set(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pIntent);
                Log.d("myLogs", "setReminder: Date: "+new Date(myCalender.getTimeInMillis()));
                Log.d("myLogs", "setReminder: notificationIntent.extra: "+notificationIntent.getStringExtra("extra"));
            }
        }

    }

    public static void sendNotif(Context context, Intent intent) {

        //%%C - del
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("extra", intent.getStringExtra("extra"));
        notificationIntent.putExtra("content", intent.getStringExtra("content"));
        notificationIntent.putExtra("year", intent.getStringExtra("year"));

        Uri data = Uri.parse(notificationIntent.toUri(Intent.URI_INTENT_SCHEME));
        notificationIntent.setData(data);
        //
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Log.d("myLogs", "extra = " + notificationIntent.getStringExtra("extra"));
        //Task serializableTask = (Task) notificationIntent.getSerializableExtra("task");
        //if(serializableTask != null) {
        //    serializableTask.shown = true;
        //    notificationIntent.putExtra("task", serializableTask);
        //}


        //%%C - del
//        PendingIntent contentIntent = PendingIntent.getActivity(context,
//                Integer.valueOf(notificationIntent.getStringExtra("extra")), notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_CANCEL_CURRENT);

        Log.d("myLogs", "sendNotif: notificationIntent.extra: " + notificationIntent.getStringExtra("extra"));
        PendingIntent pIntent = PendingIntent.getActivity(context,
                Integer.valueOf(notificationIntent.getStringExtra("extra")), notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        //Notification.Builder builder = new Notification.Builder(context);

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.next_point);
        String channelId = "1234";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

        //%%C - del builder.setContentIntent(contentIntent)
        builder.setContentIntent(pIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker("Пора!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTimeoutAfter(dateDoomsday)
                .setOngoing(true)
                //.setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                //.setSound(Uri.parse("android.resource://com.example.delimes.flux/" + R.raw.next_point))
                .setSound(soundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Напоминание")
                //.setContentText(res.getString(R.string.notifytext))
                //%%C - del.setContentText(notificationIntent.getStringExtra("content")); // Текст уведомления
                .setContentText(intent.getStringExtra("content")); // Текст уведомления


        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //String channelId = "default_channel_id";

            //String channelDescription = "Default Channel";
            String channelDescription = "Channel";
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel  == null) {
                notificationChannel  = new NotificationChannel(channelId, channelDescription, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);//doesn't work
                notificationChannel.setLightColor(Color.BLUE);//doesn't work
                notificationChannel.enableVibration(true);//doesn't work
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});//doesn't work
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                notificationChannel.setSound(soundUri, audioAttributes);


                if (notificationManager != null) {
                    notificationManager.createNotificationChannel( notificationChannel );
                }
            }
            NotificationCompat.Builder builderCompat = new NotificationCompat.Builder(context, channelId);
            builderCompat.setContentTitle("Напоминание");                            // required
            //builderCompat.setDefaults(Notification.DEFAULT_ALL);
            builderCompat.setSmallIcon(android.R.drawable.ic_popup_reminder);   // required
            builderCompat.setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher));
            builderCompat.setContentText(intent.getStringExtra("content")); // required
            //builderCompat.setDefaults(Notification.DEFAULT_ALL);
            builderCompat.setAutoCancel(true);
            builderCompat.setTimeoutAfter(dateDoomsday);
            builderCompat.setContentIntent(pIntent);
            builderCompat.setTicker("Пора!");
            builderCompat.setWhen(System.currentTimeMillis());
            builderCompat.setOngoing(true);
            builderCompat.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            //builderCompat.setSound(Uri.parse("android.resource://com.example.delimes.flux/" + R.raw.next_point));//doesn't work
            builderCompat.setPriority(NotificationCompat.PRIORITY_HIGH);
            builderCompat.setLights(0xff0000ff, 300, 1000);// blue color//doesn't work
            builderCompat.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});//doesn't work
            builderCompat.setSound(soundUri);

            notification = builderCompat.build();
        }
        //////////////////////////////

        notifyId = Integer.valueOf(notificationIntent.getStringExtra("extra"));
        //notification.defaults |= Notification.DEFAULT_VIBRATE;//doesn't work
        notificationManager.notify(notifyId, notification);


    }


    ///////////////////////////////////////////////////////////////////////////////

    public void refreshCyclicTasks(Task task) {

        long millis;
        boolean needToReturn = false;
        Calendar myCalender = Calendar.getInstance();

        myCalender.clear();
        calendar.clear();

        long dateTaskStartTime = 1;
        long dateTaskFinishTime = -1;
        if (!task.remove) {
            calendar.setTimeInMillis(task.startTime);
            myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

            dateTaskStartTime = myCalender.getTimeInMillis();


            myCalender.clear();
            calendar.clear();
            calendar.setTimeInMillis(task.finishTime);
            myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

            dateTaskFinishTime = myCalender.getTimeInMillis();

        }

        //winter
//        long winterStart = winter.days.get(0).date.getTime();
//        long yearStart = winterStart;
//        long yearFinish =  autumn.days.get(autumn.days.size()-1).date.getTime();
//        long winterFinish = winter.days.get(winter.days.size()-1).date.getTime();

//        if((task.startTime >= yearStart && task.startTime <= yearFinish) ||
//                (task.finishTime >= yearStart && task.finishTime <= yearFinish)) {
//
//            if ((task.startTime >= winterStart && task.startTime <= winterFinish) ||
//                    (task.finishTime >= winterStart && task.finishTime <= winterFinish)) {

                Iterator<Day> j = winter.days.iterator();
                while (j.hasNext()) {
                    Day d = j.next();
                    millis = d.date.getTime();

                    if(millis != dateTaskStartTime){
                        //d.tasks.remove(task);
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            //%%C Del -
//                       [
//                            if (task.equals(t) && !t.done) {
//                                iter.remove();
//                            }
//                       ]

                            //%%C Проверка в ходе эксплуатации{
//                            if (task.equals(t) && !t.isDone) {
//                                iter.remove();
//                            }

                            if (task.equals(t)) {
                                if(!t.isDone) {
                                    iter.remove();
                                }
                                if(task.remove){
                                    t.removeFromAM = true;
                                    setReminder(context, t, d.date);
                                }
                            }
                            //%%C }Проверка в ходе эксплуатации
                        }
                    }else{
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            if (task.equals(t)) {
                                 boolean taskIsDone = t.isDone;
                                task.duplicate(t);
                                t.isDone = taskIsDone;
                                if (!task.alreadyReturned && task.finishTime != dateDoomsday) {
                                    needToReturn = true;
                                }
                            }
                        }

                    }

                    //if (millis >= date.getTime() && millis >= task.startTime && millis <= task.finishTime) {
                    if (millis >= dateTaskStartTime && millis <= dateTaskFinishTime) {

                        calendar.clear();
                        calendar.setTimeInMillis(millis);

                        myCalender.clear();
                        myCalender.setTimeInMillis(dateTaskStartTime);

                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if ((task.monday && dayOfWeek == Calendar.MONDAY) ||
                                (task.tuesday && dayOfWeek == Calendar.TUESDAY) ||
                                (task.wednesday && dayOfWeek == Calendar.WEDNESDAY) ||
                                (task.thursday && dayOfWeek == Calendar.THURSDAY) ||
                                (task.friday && dayOfWeek == Calendar.FRIDAY) ||
                                (task.saturday && dayOfWeek == Calendar.SATURDAY) ||
                                (task.sunday && dayOfWeek == Calendar.SUNDAY)) {

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }

                        Date datTaskStartTime = new Date(dateTaskStartTime);
                        //if (task.everyYear &&  chosenYearNumber > curentYearNumber &&
                        if (task.everyYear &&
                                d.date.getMonth() ==  datTaskStartTime.getMonth() &&
                                d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.everyMonth && d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.inDays > 0){

                            int accountYear = 0;
                            int accountDay = 0;


                            int numberOfYears = chosenYearNumber - myCalender.get(Calendar.YEAR);
                            for (int i = 0; i < numberOfYears; i++) {
                                accountYear = myCalender.get(Calendar.YEAR) + i;

                                if(accountYear % 4 == 0 && accountYear % 100 != 0 || accountYear % 400 == 0) {
                                    accountDay += 366;
                                }else {
                                    accountDay += 365;
                                }
                            }



                            accountDay += calendar.get(Calendar.DAY_OF_YEAR);
                            calendar.clear();
                            calendar.setTimeInMillis(dateTaskStartTime);
                            accountDay -= calendar.get(Calendar.DAY_OF_YEAR);

                            if(accountDay % task.inDays == 0 ) {
                                boolean addTask = true;
                                //d.tasks.remove(task);
                                Iterator<Task> iter = d.tasks.iterator();
                                while (iter.hasNext()) {
                                    Task t = iter.next();

                                    if (t.equals(task)) {
                                        //d.tasks.remove(task);
                                        addTask = false;
                                    }
                                }

                                if (addTask) {
                                    // d.tasks.add(task);
                                    Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                    task.duplicate(taskCopy);
                                    d.tasks.add(taskCopy);
                                }
                            }
                        }


                    }

                    d.dayClosed = true;
                    for (Task task2 : d.tasks) {
                        if(!task2.isDone && task.isValid){
                            d.dayClosed = false;
                        }
                    }

                }


           // }


            //spring
//            long springStart = spring.days.get(0).date.getTime();
//            long springFinish = spring.days.get(spring.days.size() - 1).date.getTime();
//            if ((task.startTime >= springStart && task.startTime <= springFinish) ||
//                    (task.finishTime >= springStart && task.finishTime <= springFinish)) {

                j = spring.days.iterator();
                while (j.hasNext()) {
                    Day d = j.next();
                    millis = d.date.getTime();

                    if(millis != dateTaskStartTime){
                        //d.tasks.remove(task);
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            //%%C Del -
//                       [
//                            if (task.equals(t) && !t.done) {
//                                iter.remove();
//                            }
//                       ]

                            //%%C Проверка в ходе эксплуатации{
//                            if (task.equals(t) && !t.isDone) {
//                                iter.remove();
//                            }

                            if (task.equals(t)) {
                                if(!t.isDone) {
                                    iter.remove();
                                }
                                if(task.remove){
                                    t.removeFromAM = true;
                                    setReminder(context, t, d.date);
                                }
                            }
                            //%%C }Проверка в ходе эксплуатации
                        }

                    }else{
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            if (task.equals(t)) {
                                boolean taskIsDone = t.isDone;
                                task.duplicate(t);
                                t.isDone = taskIsDone;
                                if (!task.alreadyReturned && task.finishTime != dateDoomsday) {
                                    needToReturn = true;
                                }
                            }

                        }

                    }
                    if (millis >= dateTaskStartTime && millis <= dateTaskFinishTime) {

                        calendar.clear();
                        calendar.setTimeInMillis(millis);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if ((task.monday && dayOfWeek == Calendar.MONDAY) ||
                                (task.tuesday && dayOfWeek == Calendar.TUESDAY) ||
                                (task.wednesday && dayOfWeek == Calendar.WEDNESDAY) ||
                                (task.thursday && dayOfWeek == Calendar.THURSDAY) ||
                                (task.friday && dayOfWeek == Calendar.FRIDAY) ||
                                (task.saturday && dayOfWeek == Calendar.SATURDAY) ||
                                (task.sunday && dayOfWeek == Calendar.SUNDAY)) {

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        Date datTaskStartTime = new Date(dateTaskStartTime);
                        //if (task.everyYear &&  chosenYearNumber > curentYearNumber &&
                        if (task.everyYear &&
                                d.date.getMonth() ==  datTaskStartTime.getMonth() &&
                                d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.everyMonth && d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.inDays > 0){

                            int accountYear = 0;
                            int accountDay = 0;


                            int numberOfYears = chosenYearNumber - myCalender.get(Calendar.YEAR);
                            for (int i = 0; i < numberOfYears; i++) {
                                accountYear = myCalender.get(Calendar.YEAR) + i;

                                if(accountYear % 4 == 0 && accountYear % 100 != 0 || accountYear % 400 == 0) {
                                    accountDay += 366;
                                }else {
                                    accountDay += 365;
                                }
                            }



                            accountDay += calendar.get(Calendar.DAY_OF_YEAR);
                            calendar.clear();
                            calendar.setTimeInMillis(dateTaskStartTime);
                            accountDay -= calendar.get(Calendar.DAY_OF_YEAR);

                            if(accountDay % task.inDays == 0  ) {
                                boolean addTask = true;
                                //d.tasks.remove(task);
                                Iterator<Task> iter = d.tasks.iterator();
                                while (iter.hasNext()) {
                                    Task t = iter.next();

                                    if (t.equals(task)) {
                                        //d.tasks.remove(task);
                                        addTask = false;
                                    }
                                }

                                if (addTask) {
                                    // d.tasks.add(task);
                                    Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                    task.duplicate(taskCopy);
                                    d.tasks.add(taskCopy);
                                }
                            }
                        }




                    }

                    d.dayClosed = true;
                    for (Task task2 : d.tasks) {
                        if(!task2.isDone && task.isValid){
                            d.dayClosed = false;
                        }
                    }
                }

            //}
            //summer
//            long summerStart = summer.days.get(0).date.getTime();
//            long summerFinish = summer.days.get(summer.days.size() - 1).date.getTime();
//            if ((task.startTime >= summerStart && task.startTime <= summerFinish) ||
//                    (task.finishTime >= summerStart && task.finishTime <= summerFinish)) {

                j = summer.days.iterator();
                while (j.hasNext()) {
                    Day d = j.next();
                    millis = d.date.getTime();

                    if(millis != dateTaskStartTime){
                        //d.tasks.remove(task);
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            //%%C Del -
//                       [
//                            if (task.equals(t) && !t.done) {
//                                iter.remove();
//                            }
//                       ]


                            //%%C Проверка в ходе эксплуатации{
//                            if (task.equals(t) && !t.isDone) {
//                                iter.remove();
//                            }

                            if (task.equals(t)) {
                                if(!t.isDone) {
                                    iter.remove();
                                }
                                if(task.remove){
                                    t.removeFromAM = true;
                                    setReminder(context, t, d.date);
                                }
                            }
                            //%%C }Проверка в ходе эксплуатации
                        }
                    }else{
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            if (task.equals(t)) {
                                boolean taskIsDone = t.isDone;
                                task.duplicate(t);
                                t.isDone = taskIsDone;
                                if (!task.alreadyReturned && task.finishTime != dateDoomsday) {
                                    needToReturn = true;
                                }
                            }
                        }

                    }
                    if (millis >= dateTaskStartTime && millis <= dateTaskFinishTime) {

                        calendar.clear();
                        calendar.setTimeInMillis(millis);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if ((task.monday && dayOfWeek == Calendar.MONDAY) ||
                                (task.tuesday && dayOfWeek == Calendar.TUESDAY) ||
                                (task.wednesday && dayOfWeek == Calendar.WEDNESDAY) ||
                                (task.thursday && dayOfWeek == Calendar.THURSDAY) ||
                                (task.friday && dayOfWeek == Calendar.FRIDAY) ||
                                (task.saturday && dayOfWeek == Calendar.SATURDAY) ||
                                (task.sunday && dayOfWeek == Calendar.SUNDAY)) {

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }



                        Date datTaskStartTime = new Date(dateTaskStartTime);
                        //if (task.everyYear &&  chosenYearNumber > curentYearNumber &&
                        if (task.everyYear &&
                                d.date.getMonth() ==  datTaskStartTime.getMonth() &&
                                d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.everyMonth && d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.inDays > 0){

                            int accountYear = 0;
                            int accountDay = 0;


                            int numberOfYears = chosenYearNumber - myCalender.get(Calendar.YEAR);
                            for (int i = 0; i < numberOfYears; i++) {
                                accountYear = myCalender.get(Calendar.YEAR) + i;

                                if(accountYear % 4 == 0 && accountYear % 100 != 0 || accountYear % 400 == 0) {
                                    accountDay += 366;
                                }else {
                                    accountDay += 365;
                                }
                            }



                            accountDay += calendar.get(Calendar.DAY_OF_YEAR);
                            calendar.clear();
                            calendar.setTimeInMillis(dateTaskStartTime);
                            accountDay -= calendar.get(Calendar.DAY_OF_YEAR);

                            if(accountDay % task.inDays == 0 ) {
                                boolean addTask = true;
                                //d.tasks.remove(task);
                                Iterator<Task> iter = d.tasks.iterator();
                                while (iter.hasNext()) {
                                    Task t = iter.next();

                                    if (t.equals(task)) {
                                        //d.tasks.remove(task);
                                        addTask = false;
                                    }
                                }

                                if (addTask) {
                                    // d.tasks.add(task);
                                    Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                    task.duplicate(taskCopy);
                                    d.tasks.add(taskCopy);
                                }
                            }
                        }



                    }

                    d.dayClosed = true;
                    for (Task task2 : d.tasks) {
                        if(!task2.isDone && task.isValid){
                            d.dayClosed = false;
                        }
                    }
                }

            //}
            //autumn
//            long autumnStart = autumn.days.get(0).date.getTime();
//            long autumnFinish = autumn.days.get(autumn.days.size() - 1).date.getTime();
//            if ((task.startTime >= autumnStart && task.startTime <= autumnFinish) ||
//                    (task.finishTime >= autumnStart && task.finishTime <= autumnFinish)) {

                j = autumn.days.iterator();
                while (j.hasNext()) {
                    Day d = j.next();
                    millis = d.date.getTime();

                    if(millis != dateTaskStartTime){
                        //d.tasks.remove(task);
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            //%%C Del -
//                       [
//                            if (task.equals(t) && !t.done) {
//                                iter.remove();
//                            }
//                       ]


                            //%%C Проверка в ходе эксплуатации{
//                            if (task.equals(t) && !t.isDone) {
//                                iter.remove();
//                            }

                            if (task.equals(t)) {
                                if(!t.isDone) {
                                    iter.remove();
                                }
                                if(task.remove){
                                    t.removeFromAM = true;
                                    setReminder(context, t, d.date);
                                }
                            }
                            //%%C }Проверка в ходе эксплуатации
                        }
                    }else{
                        Iterator<Task> iter = d.tasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            if (task.equals(t)) {
                                boolean taskIsDone = t.isDone;
                                task.duplicate(t);
                                t.isDone = taskIsDone;
                                if (!task.alreadyReturned && task.finishTime != dateDoomsday) {
                                    needToReturn = true;
                                }
                            }
                        }

                    }
                    if (millis >= dateTaskStartTime && millis <= dateTaskFinishTime) {

                        calendar.clear();
                        calendar.setTimeInMillis(millis);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if ((task.monday && dayOfWeek == Calendar.MONDAY) ||
                                (task.tuesday && dayOfWeek == Calendar.TUESDAY) ||
                                (task.wednesday && dayOfWeek == Calendar.WEDNESDAY) ||
                                (task.thursday && dayOfWeek == Calendar.THURSDAY) ||
                                (task.friday && dayOfWeek == Calendar.FRIDAY) ||
                                (task.saturday && dayOfWeek == Calendar.SATURDAY) ||
                                (task.sunday && dayOfWeek == Calendar.SUNDAY)) {

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }



                        Date datTaskStartTime = new Date(dateTaskStartTime);
                        //if (task.everyYear &&  chosenYearNumber > curentYearNumber &&
                        if (task.everyYear &&
                                d.date.getMonth() ==  datTaskStartTime.getMonth() &&
                                d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }


                        if (task.everyMonth && d.date.getDate() ==  datTaskStartTime.getDate() ){

                            boolean addTask = true;
                            //d.tasks.remove(task);
                            Iterator<Task> iter = d.tasks.iterator();
                            while (iter.hasNext()) {
                                Task t = iter.next();

                                if (t.equals(task)) {
                                    //d.tasks.remove(task);
                                    addTask = false;
                                }
                            }

                            if (addTask) {
                                // d.tasks.add(task);
                                Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                task.duplicate(taskCopy);
                                d.tasks.add(taskCopy);
                            }
                        }

                        if (task.inDays > 0){

                            int accountYear = 0;
                            int accountDay = 0;


                            int numberOfYears = chosenYearNumber - myCalender.get(Calendar.YEAR);
                            for (int i = 0; i < numberOfYears; i++) {
                                accountYear = myCalender.get(Calendar.YEAR) + i;

                                if(accountYear % 4 == 0 && accountYear % 100 != 0 || accountYear % 400 == 0) {
                                    accountDay += 366;
                                }else {
                                    accountDay += 365;
                                }
                            }



                            accountDay += calendar.get(Calendar.DAY_OF_YEAR);
                            calendar.clear();
                            calendar.setTimeInMillis(dateTaskStartTime);
                            accountDay -= calendar.get(Calendar.DAY_OF_YEAR);

                            if(accountDay % task.inDays == 0  ) {
                                boolean addTask = true;
                                //d.tasks.remove(task);
                                Iterator<Task> iter = d.tasks.iterator();
                                while (iter.hasNext()) {
                                    Task t = iter.next();

                                    if (t.equals(task)) {
                                        //d.tasks.remove(task);
                                        addTask = false;
                                    }
                                }

                                if (addTask) {
                                    // d.tasks.add(task);
                                    Task taskCopy = new Task(true, false,"", 0, 0, 0);
                                    task.duplicate(taskCopy);
                                    d.tasks.add(taskCopy);
                                }
                            }
                        }




                    }

                    d.dayClosed = true;
                    for (Task task2 : d.tasks) {
                        if(!task2.isDone && task.isValid){
                            d.dayClosed = false;
                        }
                    }

                }

        if (needToReturn){
            task.alreadyReturned = true;
            calendar.clear();
            calendar.setTimeInMillis(task.finishTime);
            changedeTasksOfYear = true;
            numberYearPicker.setValue(calendar.get(Calendar.YEAR));
        }

           // }

        //}
    }

    public void showTimePicker(String title, final boolean duration) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {

                    task.isCyclic = false;

                    if (duration){
                        if(task.durationHours != hourOfDay ||
                                task.durationMinutes != minute){
                            changedeTasksOfYear = true;
                        }
                        task.durationHours = hourOfDay;
                        task.durationMinutes = minute;

                        if (task.monday ||
                                task.tuesday ||
                                task.wednesday ||
                                task.thursday ||
                                task.friday ||
                                task.saturday ||
                                task.sunday ||
                                task.everyYear ||
                                task.everyMonth ||
                                task.inDays > 0) {

                            if (task.finishTime == task.startTime){
                                task.finishTime = dateDoomsday;
                            }
                            task.isCyclic = true;
                            Task taskCopy = new Task(true, false,"", 0, 0, 0);
                            task.duplicate(taskCopy);
                            cyclicTasks.add(taskCopy);
                        }else if (task.finishTime == dateDoomsday){
                            task.finishTime = task.startTime;
                        }

                       /* calendar.clear();
                        calendar.setTimeInMillis(task.startTime);
                        taskDuration.setText(
                                ((""+ task.durationHours).length() == 1 ? "0" + task.durationHours : "" + task.durationHours)+
                                        ":"+ ((""+ task.durationMinutes).length() == 1 ? "0" + task.durationMinutes : "" + task.durationMinutes));
*/
                    }else{

                        calendar.clear();
                        calendar.setTimeInMillis(task.startTime);
                        myCalender.clear();
                        myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                        long dateTaskStartTime = myCalender.getTimeInMillis();

                        calendar.clear();
                        calendar.setTimeInMillis(task.startTime);//day.date.getTime()
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        if(task.startTime != calendar.getTimeInMillis()){
                            changedeTasksOfYear = true;
                        }
                        if(task.startTime == task.finishTime){
                            task.finishTime = calendar.getTimeInMillis();
                        }
                        task.startTime = calendar.getTimeInMillis();


                        myCalender.clear();
                        myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                        myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                        task.clockStartTime = myCalender.getTimeInMillis();

                        //clear old remind
                        task.removeFromAM = true;
                        setReminder(context, task, day.date);
                        task.shown = false;

                        while (cyclicTasks.remove(task));;
                        Iterator<Task> iter = cyclicTasks.iterator();
                        while (iter.hasNext()) {
                            Task t = iter.next();

                            if (t.equals(task)) {
                                cyclicTasks.remove(task);
                            }
                        }

                        if (task.monday ||
                                task.tuesday ||
                                task.wednesday ||
                                task.thursday ||
                                task.friday ||
                                task.saturday ||
                                task.sunday ||
                                task.everyYear ||
                                task.everyMonth ||
                                task.inDays > 0) {

                            if (task.finishTime == task.startTime) {
                                task.finishTime = dateDoomsday;
                            }
                            task.isCyclic = true;
                            Task taskCopy = new Task(true, false, "", 0, 0, 0);
                            task.duplicate(taskCopy);
                            cyclicTasks.add(taskCopy);

                            //refreshCyclicTasks(task);
                            if (dateTaskStartTime == day.date.getTime()) {
                                refreshCyclicTasks(task);
                            }
                        } else if (task.finishTime == dateDoomsday) {
                            task.finishTime = task.startTime;
                        }

                        //%%C - del
//                        if(dateTaskStartTime == day.date.getTime()) {
//                            refreshCyclicTasks(task);
//                        }

                        //set new remind
                        setReminder(context, task, day.date);

                        /*taskTime.setText(((""+ calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY))+
                                ":"+ ((""+ calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE)));
*/
                    }

                    updateSchedule(day);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, duration? 0:hour, duration? 0:minute, true);

        timePickerDialog.setTitle(title);
        //timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void showDatePickerForFinishTime() {
        final Calendar myCalender = Calendar.getInstance();
        myCalender.setTimeInMillis(day.date.getTime());
        int year = myCalender.get(Calendar.YEAR);
        int month = myCalender.get(Calendar.MONTH);
        int dayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                calendar.clear();
                calendar.setTimeInMillis(task.finishTime);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                /*
                    task.finishTime = calendar.getTimeInMillis();
                    changedeTasksOfYear = true;
                }
                */
                if(task.finishTime != calendar.getTimeInMillis()){
                    changedeTasksOfYear = true;
                }
                if(calendar.getTimeInMillis() < task.startTime){
                    task.finishTime = task.startTime;
                }else if(task.isCyclic){
                    task.finishTime = calendar.getTimeInMillis();
                    myCalender.setTimeInMillis(task.startTime);
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            task.duplicate(t);
                            refreshCyclicTasks(t);
                            t.alreadyReturned = false;
                        }
                    }
                    numberYearPicker.setValue(myCalender.get(Calendar.YEAR));
                    return;
                }else {
                    task.finishTime = calendar.getTimeInMillis();
                }

                //if(cyclicTasks.contains(task)) {
                //    refreshCyclicTasks(task);
                //}
                Iterator<Task> iter = cyclicTasks.iterator();
                while (iter.hasNext()) {
                    Task t = iter.next();

                    if (t.equals(task)) {
                        task.duplicate(t);
                        refreshCyclicTasks(t);
                    }
                }
                updateSchedule(day);

                winter.invalidate();
                spring.invalidate();
                summer.invalidate();
                autumn.invalidate();
            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the end date for the task");
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }


    public void showDatePickerForDateMonth() {

        final Calendar myCalender = Calendar.getInstance();
        int year = myCalender.get(Calendar.YEAR);
        int month = myCalender.get(Calendar.MONTH);
        int dayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                myCalender.clear();
                myCalender.set(year, month, dayOfMonth);

                winter.selectedDay = null;
                spring.selectedDay = null;
                summer.selectedDay = null;
                autumn.selectedDay = null;

                Date date = new Date(myCalender.getTimeInMillis());
                if (month >= 0 && month <= 2) {
                    winter.selectedDay = new Day(date, 0, 0, 0, 0);
                    analogClock.clockColor = getResources().getColor(R.color.colorWinter);
                    if (month == 2){
                        analogClock.clockColor = getResources().getColor(R.color.colorSpring);
                    }
                }else if(month >= 3 && month <= 5){
                    spring.selectedDay = new Day(date, 0, 0, 0, 0);
                    analogClock.clockColor = getResources().getColor(R.color.colorSpring);
                    if (month == 5){
                        analogClock.clockColor = getResources().getColor(R.color.colorSummer);
                    }
                }else if(month >= 6 && month <= 8){
                    summer.selectedDay = new Day(date, 0, 0, 0, 0);
                    analogClock.clockColor = getResources().getColor(R.color.colorSummer);
                    if (month == 8){
                        analogClock.clockColor = getResources().getColor(R.color.colorAutumn);
                    }
                }else if(month >= 9 && month <= 11){
                    autumn.selectedDay = new Day(date, 0, 0, 0, 0);
                    analogClock.clockColor = getResources().getColor(R.color.colorAutumn);
                    if (month == 11){
                        analogClock.clockColor = getResources().getColor(R.color.colorWinter);
                    }
                }
                numberYearPicker.setValue(year);

            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the date");
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }
    public void showDatePickerForStartTime() {

        ////////////////////////////////////////////////////////////////////////////
        final Calendar myCalender = Calendar.getInstance();
        myCalender.setTimeInMillis(task.startTime);
        int year = myCalender.get(Calendar.YEAR);
        int month = myCalender.get(Calendar.MONTH);
        int dayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                calendar.clear();
                calendar.setTimeInMillis(task.startTime);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                myCalender.setTimeInMillis(calendar.getTimeInMillis());

                if(task.startTime != myCalender.getTimeInMillis()){
                    changedeTasksOfYear = true;
                    day.tasks.remove(task);
                    if (previousDay != null){
                        task.removeFromAM = true;
                        setReminder(context, task, previousDay.date);
                        previousDay.tasks.remove(task);
                        previousDay.dayClosed = true;
                        for (Task task : previousDay.tasks) {
                            if(!task.isDone && task.isValid){
                                previousDay.dayClosed = false;
                            }
                        }
                    }
                    if(task.startTime == task.finishTime){
                        task.finishTime = myCalender.getTimeInMillis();
                    }
                    task.startTime = myCalender.getTimeInMillis();
                }else{
                    return;
                }

                task.removeFromAM = true;
                setReminder(context, task, day.date);

                day.dayClosed = true;
                for (Task task : day.tasks) {
                    if(!task.isDone && task.isValid){
                        day.dayClosed = false;
                    }
                }

                if(task.finishTime < task.startTime){
                    task.finishTime = task.startTime;
                }

                updateSchedule(day);


                if (task.isCyclic) {
                    Iterator<Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        Task t = iter.next();

                        if (t.equals(task)) {
                            task.duplicate(t);
                        }
                    }
                }else {
                    calendar.clear();
                    calendar.set(year, month, dayOfMonth);
                    task.taskTransferDate = new Date(calendar.getTimeInMillis());
                }
                //чтоб задача переместилась после рестора
                numberYearPicker.setValue(year);


                //reschedule task
                Day dayOfYear = new Day(new Date(), 0, 0, 0, 0);
                int numberDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                if (winter.days.size() >= numberDayOfYear){

                    dayOfYear = winter.days.get(numberDayOfYear - 1);
                    dayOfYear.tasks.add(task);

                }else if(winter.days.size()
                        + spring.days.size() >= numberDayOfYear){

                    dayOfYear = spring.days.get(numberDayOfYear - 1 - winter.days.size());
                    dayOfYear.tasks.add(task);

                }else if(winter.days.size()
                        + spring.days.size()
                        + summer.days.size() >= numberDayOfYear){

                    dayOfYear = summer.days.get(numberDayOfYear - 1
                            - winter.days.size()
                            - spring.days.size());
                    dayOfYear.tasks.add(task);

                }else if(winter.days.size()
                        + spring.days.size()
                        + summer.days.size()
                        + autumn.days.size() >= numberDayOfYear){

                    dayOfYear = autumn.days.get(numberDayOfYear - 1
                            - winter.days.size()
                            - spring.days.size()
                            - summer.days.size());
                    dayOfYear.tasks.add(task);
                }

                task.shown = false;
                setReminder(context, task, dayOfYear.date);

                // Доработать не подсвечиваются дни перенесенных незавершенныз задач
                dayOfYear.dayClosed = true;
                for (Task task : dayOfYear.tasks) {
                    if(!task.isDone && task.isValid){
                        dayOfYear.dayClosed = false;
                    }
                }
                previousDay = dayOfYear;

                changedeTasksOfYear = true;
                //saveYearToFile();


                winter.invalidate();
                spring.invalidate();
                summer.invalidate();
                autumn.invalidate();

            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the start date for the task");
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    public void showDatePickerForCopyTo() {

        ////////////////////////////////////////////////////////////////////////////
        final Calendar myCalender = Calendar.getInstance();
        myCalender.setTimeInMillis(task.startTime);
        int year = myCalender.get(Calendar.YEAR);
        int month = myCalender.get(Calendar.MONTH);
        int dayOfMonth = myCalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                calendar.clear();
                calendar.setTimeInMillis(task.startTime);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Task taskCopy = new Task(true, false, task.content, calendar.getTimeInMillis(), task.durationHours, task.durationMinutes);
                task = taskCopy;

                calendar.clear();
                calendar.set(year, month, dayOfMonth);
                task.taskTransferDate = new Date(calendar.getTimeInMillis());
                //чтоб задача переместилась после рестора
                numberYearPicker.setValue(year);


                //reschedule task
                Day dayOfYear = new Day(new Date(), 0, 0, 0, 0);
                int numberDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                if (winter.days.size() >= numberDayOfYear){

                    dayOfYear = winter.days.get(numberDayOfYear - 1);
                    dayOfYear.tasks.add(task);

                }else if(winter.days.size()
                        + spring.days.size() >= numberDayOfYear){

                    dayOfYear = spring.days.get(numberDayOfYear - 1 - winter.days.size());
                    dayOfYear.tasks.add(task);

                }else if(winter.days.size()
                        + spring.days.size()
                        + summer.days.size() >= numberDayOfYear){

                    dayOfYear = summer.days.get(numberDayOfYear - 1
                            - winter.days.size()
                            - spring.days.size());
                    dayOfYear.tasks.add(task);

                }else if(winter.days.size()
                        + spring.days.size()
                        + summer.days.size()
                        + autumn.days.size() >= numberDayOfYear){

                    dayOfYear = autumn.days.get(numberDayOfYear - 1
                            - winter.days.size()
                            - spring.days.size()
                            - summer.days.size());
                    dayOfYear.tasks.add(task);
                }

                setReminder(context, task, dayOfYear.date);

                // Доработать не подсвечиваются дни перенесенных незавершенныз задач
                dayOfYear.dayClosed = true;
                for (Task task : dayOfYear.tasks) {
                    if(!task.isDone && task.isValid){
                        dayOfYear.dayClosed = false;
                    }
                }
                previousDay = dayOfYear;

                changedeTasksOfYear = true;
                //saveYearToFile();


                winter.invalidate();
                spring.invalidate();
                summer.invalidate();
                autumn.invalidate();

            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the date on which you want to copy the task");
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }


    public void updateSchedule(final Day selectedDay){

        if(selectedDay == null) {
            return;
        }

        processUpdateSchedule = true;

        taskTime.setText("__:__");
        taskDuration.setText("__:__");
        taskDescription.showIvLargerImage = false;
        taskDescription.setText("");
        startOfTask.setText("__.__.____");
        endOfTask.setText("__.__.____");
        everyYear.setChecked(false);
        everyMonth.setChecked(false);
        inDays.setText("");

        calendar.clear();
        calendar.setTimeInMillis(selectedDay.date.getTime());

//        if ( calendar.get(Calendar.YEAR) != numberYearPicker.getValue() ){
//            numberYearPicker.setValue(calendar.get(Calendar.YEAR));
//        }

        String strDateMonth = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
                +calendar.get(Calendar.DAY_OF_MONTH) + " "
                +calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        MainActivity.dateMonth.setText(strDateMonth);


        if(task != null) {
            calendar.clear();
            calendar.setTimeInMillis(task.startTime);
            taskTime.setText((("" + calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY)) +
                    ":" + (("" + calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE)));

            taskDuration.setText(
                    (("" + task.durationHours).length() == 1 ? "0" + task.durationHours : "" + task.durationHours) +
                            ":" + (("" + task.durationMinutes).length() == 1 ? "0" + task.durationMinutes : "" + task.durationMinutes));

            //taskDescription.setText(task.content);
            taskDescription.showIvLargerImage = false;
            taskDescription.setText("");
            taskDescription.showIvLargerImage = false;
            taskDescription.append(task.content);


            startOfTask.setText(new SimpleDateFormat("dd.MM.yyyy").format(task.startTime));
            endOfTask.setText(new SimpleDateFormat("dd.MM.yyyy").format(task.finishTime));

            if(task.monday){
                monday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                monday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            if(task.tuesday){
                tuesday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                tuesday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            if(task.wednesday){
                wednesday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                wednesday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            if(task.thursday){
                thursday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                thursday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            if(task.friday){
                friday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                friday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            if(task.saturday){
                saturday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                saturday.setTextColor(getResources().getColor(android.R.color.background_light));
            }else{
                saturday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                saturday.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }

            if(task.sunday){
                sunday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                sunday.setTextColor(getResources().getColor(android.R.color.background_light));
            }else{
                sunday.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                sunday.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }

            everyYear.setChecked(task.everyYear);
            everyMonth.setChecked(task.everyMonth);
            if(task.inDays > 0) {
                inDays.setText(String.valueOf(task.inDays));
            }


        }

        Collections.sort(selectedDay.tasks,  new TaskComparator());

        linLayout.removeAllViews();
        for (int i = 0; i < selectedDay.tasks.size(); i++) {
            //Log.d("myLogs", "i = " + i);
            final Task task = selectedDay.tasks.get(i);
            final View item = ltInflater.inflate(R.layout.item, linLayout, false);

            CheckBox checkBox = (CheckBox) item.findViewById(R.id.checkBox);
            checkBox.setChecked(task.isValid);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    changedeTasksOfYear = true;
                    task.isValid = b;
                    if(b){
                        setReminder(context, task, day.date);
                    }else{
                        task.removeFromAM = true;
                        setReminder(context, task, day.date);
                    }
                    //%%C del - setReminder(task);

                    day.dayClosed = true;
                    for (Task task : day.tasks) {
                        if(!task.isDone && task.isValid){
                            day.dayClosed = false;
                        }
                    }

                }
            });

            CheckBox checkBoxDone = (CheckBox) item.findViewById(R.id.checkBoxDone);
            checkBoxDone.setChecked(task.isDone);
            checkBoxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    changedeTasksOfYear = true;
                    task.isDone = b;

                    if(b){
                        task.removeFromAM = true;
                        setReminder(context, task, day.date);
                        //%%C del - setReminder(task);
                    }

                    day.dayClosed = true;
                    for (Task task : day.tasks) {
                        if(!task.isDone && task.isValid){
                            day.dayClosed = false;
                        }
                    }

                }
            });

            TextView tvTaskNumber = (TextView) item.findViewById(R.id.tvTaskNumber);
            tvTaskNumber.setText(""+(i+1) + ")");

            TextView tvTaskTime = (TextView) item.findViewById(R.id.tvTaskTime);
            calendar.clear();
            calendar.setTimeInMillis(task.startTime);
            tvTaskTime.setText(((""+ calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY))+
                    ":"+ ((""+ calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE)));
            TextView tvTaskDuration = (TextView) item.findViewById(R.id.tvTaskDuration);
            String text = ("" + i).length() == 1 ? "0" + i : "" + i;
            tvTaskDuration.setText(
                    ((""+ task.durationHours).length() == 1 ? "0" + task.durationHours : "" + task.durationHours)+
                            ":"+ ((""+ task.durationMinutes).length() == 1 ? "0" + task.durationMinutes : "" + task.durationMinutes));

            //item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            TextView tvTaskName = (TextView) item.findViewById(R.id.tvTaskName);
            tvTaskName.setText(task.content);

//            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.leftToRight = R.id.tvTaskTime;
//            params.rightToLeft = R.id.checkBoxDone;
//            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//            params.leftMargin = 10;
//            //params.width = сonstraintLayoutForSchedule.getWidth() - (int)(сonstraintLayoutForSchedule.getWidth() / 2.5f);
//            params.width = 0;
//            tvTaskName.setLayoutParams(params);



            calendar.clear();
            calendar.setTimeInMillis(task.startTime);
            final Calendar myCalender = Calendar.getInstance();

            myCalender.clear();
            myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

            long dateTaskStartTime = myCalender.getTimeInMillis();

            item.setBackgroundColor(colors[i % 2]);
            if (dateTaskStartTime == day.date.getTime()){

                if (task.monday ||
                        task.tuesday ||
                        task.wednesday ||
                        task.thursday ||
                        task.friday ||
                        task.saturday ||
                        task.sunday ||
                        task.everyYear ||
                        task.everyMonth ||
                        task.inDays > 0) {

                    item.setBackgroundColor(colors2[i % 2]);
                }
            }

            if( task == MainActivity.task ){
                item.setBackgroundResource(R.drawable.layout_border);//
            }

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    taskDescription.setEnabled(true);
                    taskDescription.requestFocus();

                    if (task == MainActivity.task && сonstraintLayoutTaskParameters.getVisibility() == View.GONE){

                        сonstraintLayoutTaskParameters.setVisibility(View.VISIBLE);
                        view.post(new Runnable() {
                            @Override
                            public void run() {

                                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                                params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                                linearLayout.setLayoutParams(params);

                            }

                        });

//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(taskDescription, InputMethodManager.SHOW_IMPLICIT);

                    }else if (task == MainActivity.task && сonstraintLayoutTaskParameters.getVisibility() == View.VISIBLE){
                        MainActivity.task = null;
                        //params.height = 0;
                        сonstraintLayoutTaskParameters.setVisibility(View.GONE);
                        view.post(new Runnable() {
                            @Override
                            public void run() {

                                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                                params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                                linearLayout.setLayoutParams(params);

                            }

                        });

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(taskDescription.getWindowToken(), 0);

                    }else {
                        MainActivity.task = task;
                        //params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        сonstraintLayoutTaskParameters.setVisibility(View.VISIBLE);
                        view.post(new Runnable() {
                            @Override
                            public void run() {

                                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                                params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                                linearLayout.setLayoutParams(params);

                            }

                        });

                        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //imm.showSoftInput(taskDescription, InputMethodManager.SHOW_IMPLICIT);

                    }


                    updateSchedule(day);


//                    taskIndex = selectedDay.tasks.indexOf(task);
//                    calendar.clear();
//                    calendar.setTimeInMillis(task.startTime);
//                    taskTime.setText(((""+ calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY))+
//                            ":"+ ((""+ calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE)));
//
//                    taskDuration.setText(
//                            ((""+ task.durationHours).length() == 1 ? "0" + task.durationHours : "" + task.durationHours)+
//                                    ":"+ ((""+ task.durationMinutes).length() == 1 ? "0" + task.durationMinutes : "" + task.durationMinutes));
//
//                    taskDescription.setText(task.content);
                }
            });
            linLayout.addView(item);
        }

        processUpdateSchedule = false;
    }

    public static void onTimeChanged() {

        calendar.clear();
        calendar.setTimeInMillis(System.currentTimeMillis());

        currHours = calendar.get(Calendar.HOUR_OF_DAY);
        currMinutes = calendar.get(Calendar.MINUTE);
        currSeconds = calendar.get(Calendar.SECOND);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu_main = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        // Операции для выбранного пункта меню
        switch (id) {
//            case R.id.action_save:
//
//                saveYearToFile();
//                return true;
//
//            case R.id.action_restore:
//
//                restoreYearFromFile();
//                return true;
//
//            case R.id.action_reset:
//
//                //Dialog
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//                alertDialog.setTitle("Сброс задач за все годы");  // заголовок
//                alertDialog.setMessage("Очистить задачи?"); // сообщение
//                alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int arg1) {
//                    }
//                });
//                alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int arg1) {
//                        Toast toast = Toast.makeText(MainActivity.this,
//                                "Задачи сбрасываются",
//                                Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.TOP, 0, 0);
//                        toast.show();
//
//                        cyclicTasks.clear();
//                        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
//                        preference.edit().clear().commit();
//
//                        toast = Toast.makeText(MainActivity.this,
//                                "Готово",
//                                Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.TOP, 0, 0);
//                        toast.show();
//
//                    }
//                });
//                alertDialog.setCancelable(true);
//                alertDialog.show();
//
//
//                return true;

            case R.id.action_clock:

                if (analogClock.getVisibility() == View.INVISIBLE) {
                    analogClock.setVisibility(View.VISIBLE);
                    item.setTitle(getResources().getString(R.string.action_clock_off));
                }else {
                    analogClock.setVisibility(View.INVISIBLE);
                    item.setTitle(getResources().getString(R.string.action_clock_on));
                }
                return true;


            default:
                //return true;
                return super.onOptionsItemSelected(item);

        }

    }

    public static Context getContext() {
        return context;
    }

    public void saveYear() {

        yearStr = new YearStr(Integer.valueOf(numberYearPicker.valueText.getText().toString()), winter.days, spring.days, summer.days, autumn.days, cyclicTasks);

        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        String jsonStr = new Gson().toJson(yearStr, YearStr.class);
        editor.putString(Integer.toString(yearStr.year), jsonStr);
        editor.commit();

    }

    public void restoreYear(String year) {

        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        //preference.edit().clear().commit();
        String json = preference.getString(year, "");
        if (!json.isEmpty()) {
            yearStr = new Gson().fromJson(json, YearStr.class);
            winter.restore = true;
            spring.restore = true;
            summer.restore = true;
            autumn.restore = true;
        }
    }

    public void saveYearToFile() {

        String strYear = numberYearPicker.valueText.getText().toString();
        if (strYear.isEmpty()){
            //Toast.makeText(this, "Year not saved!", Toast.LENGTH_LONG).show();
            return;
        }
        yearStr = new YearStr(Integer.valueOf(strYear), winter.days, spring.days, summer.days, autumn.days, cyclicTasks);
        String jsonStr = new Gson().toJson(yearStr, YearStr.class);

        try {

            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
                return;
            }
            // получаем путь к SD
            //File sdPath = getExternalStorageDirectory();
            File sdPath = getExternalCacheDir();
            // добавляем свой каталог к пути
            sdPath = new File(sdPath.getAbsolutePath());// + "/mytextfile.txt");
            // создаем каталог
            sdPath.mkdirs();
            // формируем объект File, который содержит путь к файлу
            File sdFile = new File(sdPath, "savedTasks"+numberYearPicker.valueText.getText().toString());
            if (!sdFile.exists()) {
                try {
                    sdFile.createNewFile();
                } catch (IOException e) {
                    //e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            try {
                // открываем поток для записи
                BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile, false));
                // пишем данные
                bw.write(jsonStr);//
                // закрываем поток
                bw.flush();
                bw.close();
//                Toast.makeText(this, "File saved: " + sdFile.getAbsolutePath(),
//                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void restoreYearFromFile(){

        Log.d("Year", "restoreYearFromFile:1");
        if(numberYearPicker == null){
            yearRestored = false;
            return;
        }

        yearRestored = true;
        Log.d("Year", "restoreYearFromFile:2");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
            return;
        }

        // получаем путь к SD
        //File sdPath = getExternalStorageDirectory();
        File sdPath = getExternalCacheDir();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath());// + "/mytextfile.txt");
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, "savedTasks"+numberYearPicker.valueText.getText().toString());
        if (!sdFile.exists()){
            return;
//            try {
//                sdFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        int year = Integer.valueOf(numberYearPicker.valueText.getText().toString());

        try {

            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            // читаем содержимое
            //while ((str = br.readLine()) != null) {
            String json =  br.readLine();
            if (!json.isEmpty()) {
                yearStr = new Gson().fromJson(json, YearStr.class);
                if (yearStr.year == year) {
                    winter.restore = true;
                    spring.restore = true;
                    summer.restore = true;
                    autumn.restore = true;
                }
            }
            //}
            //Toast.makeText(this, "File restore successfully!",Toast.LENGTH_SHORT).show();
            Log.d("Year", "restoreYearFromFile: File restore successfully!");
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Year", "restoreYearFromFile: "+e.getMessage());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Year", "restoreYearFromFile: "+e.getMessage());
        }

    }

    public void saveCyclicTasks() {

//        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preference.edit();
//
//        String jsonStr = new Gson().toJson(cyclicTasks);
//        editor.putString("cyclicTasks", jsonStr);
//        editor.commit();

        String jsonStr = new Gson().toJson(cyclicTasks);

        try {

            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
                return;
            }
            // получаем путь к SD
            //File sdPath = getExternalStorageDirectory();
            File sdPath = getExternalCacheDir();
            // добавляем свой каталог к пути
            sdPath = new File(sdPath.getAbsolutePath());// + "/mytextfile.txt");
            // создаем каталог
            sdPath.mkdirs();
            // формируем объект File, который содержит путь к файлу
            File sdFile = new File(sdPath, "cyclicTasks");
            if (!sdFile.exists()) {
                try {
                    sdFile.createNewFile();
                } catch (IOException e) {
                    //e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            try {
                // открываем поток для записи
                BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile, false));
                // пишем данные
                bw.write(jsonStr);//
                // закрываем поток
                bw.flush();
                bw.close();
//                Toast.makeText(this, "File saved: " + sdFile.getAbsolutePath(),
//                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void restoreCyclicTasks() {


//        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
//        //preference.edit().clear().commit();
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
//        String json = preference.getString("cyclicTasks", "");

        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
            return;
        }

        // получаем путь к SD
        //File sdPath = getExternalStorageDirectory();
        File sdPath = getExternalCacheDir();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath());// + "/mytextfile.txt");
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, "cyclicTasks");
        if (!sdFile.exists()){
            return;
        }

        String json = "";

        try {

            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            // читаем содержимое
            //while ((str = br.readLine()) != null) {
            json =  br.readLine();
            //}
            //Toast.makeText(this, "File restore successfully!",Toast.LENGTH_SHORT).show();
            Log.d("123", "restoreListDictionary: File restore successfully!");
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("123", "restoreListDictionary: "+e.getMessage());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("123", "restoreListDictionary: "+e.getMessage());
        }

        Calendar calendar = GregorianCalendar.getInstance();

        if (!json.isEmpty()) {
            JsonArray array = parser.parse(json).getAsJsonArray();
            cyclicTasks.clear();
            for (int i = 0; i < array.size(); i++) {
                Task cyclicTask = gson.fromJson(array.get(i), Task.class);
                calendar.setTimeInMillis(cyclicTask.finishTime);

                if (calendar.get(Calendar.YEAR) >= curentYearNumber) {
                    cyclicTasks.add(cyclicTask);
                }
            }
        }

    }

    /**
     * Created by User on 04.10.2017.
     */

//    //TODO Winter
//    class Winter extends View {
//        Context context;
//        Paint p;
//        // координаты для рисования квадрата
//        float x = 0;
//        float y = 0;
//        int side = 0;
//        int width = 0;
//        int height = 0;
//        float doubleTapX = 0;
//        float doubleTapY = 0;
//        float januaryLength = 0;
//        float februaryLength = 0;
//        float marchLength = 0;
//        Day selectedDay = null;
//        Day currentDate = null;
//        ArrayList<Day> days = new ArrayList<Day>();
//        String monthName;
//        Rect textBounds = new Rect();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
//        boolean restore;
//
//
//
//
//        boolean firstOccurrence = true;
//        int scrollTime = 0;
//        CountDownTimer countDownTimer = new CountDownTimer(0, 0) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
//            }
//        };
//
//        // переменные для перетаскивания
//        boolean drag = false;
//        float dragX = 0;
//        float dragY = 0;
//
//        Bitmap backingBitmap;
//        Canvas drawCanvas;
//
//        private GestureDetector gestureDetector;
//
//        float upperLeftCornerX = 0;
//        float upperRightCornerX = 0;
//        float bottomLeftCornerY = 0;
//        float upperRightCornerY = 0;
//        float bottomRightCornerX = 0;
//
//        float length = 0;
//
//        public Winter(Context context) {
//            super(context);
//            init(context);
//
//        }
//
//        public Winter(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            init(context);
//        }
//
//        public Winter(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//            init(context);
//        }
//
//        private void init(Context context) {
//
//            p = new Paint();
//
//            gestureDetector = new GestureDetector(context, new MyGestureListener());
//
//
//            calendar.clear();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            calendar.clear();
//            calendar.set(year, month, day);
//            currDate = new Date(calendar.getTimeInMillis());
//
//        }
//
//
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            //del
////            if (firstOccurrence) {
////                drawCanvas = canvas;
////                firstOccurrence = false;
////
//////                x = getWidth();//del
//////                y = getHeight();//del
////            }
//            //del
//
//            canvas.drawColor(Color.rgb(106, 90, 205));
//            drawWinter(canvas);
//
//
//
//
//
//        }
//
//        public void drawWinter(Canvas canvas){
//
//            int Width = canvas.getWidth();//del
//            int Height = canvas.getHeight();//del
//            int fontHeight = side / 2;
//            int strokeWidth = side / 5;
//            float monthNameHeight;
//            float monthNameWidth;
//
//            int l = 0;
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            //I-ый квартал
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.JANUARY);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//
//            p.getTextBounds(monthName, 0, monthName.length(), textBounds);
//            monthNameHeight = textBounds.height();
//            monthNameWidth = textBounds.width();
//
//            //1-ый месяц
//            int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            int k = 0;
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l = i - 1;
//
//                upperLeftCornerX = x - k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = upperLeftCornerX - side;
//                float top = y-side;
//                float right = upperLeftCornerX;
//                float bottom = y;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 0, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/4, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        autumn.currentDate = null;
//                        spring.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 0, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//
//                k += side;
//            }
//            if (firstOccurrence) {
//                januaryLength = -upperLeftCornerX + getWidth()* 1.5f;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            if(x <= januaryLength) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth() / 2, y - side * 1.5f, p);
//
//               /* p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(getWidth()/2,y - side * 1.5f-monthNameHeight,getWidth()/2+monthNameWidth ,y - side * 1.5f, p);
//                p.setStyle(Paint.Style.FILL);*/
//            }else{
//                p.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
//            }
//
//
//
////            p.setColor(Color.CYAN);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(upperLeftCornerX, y, p);
//
//            //2-ой месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//
//            p.getTextBounds(monthName, 0, monthName.length(), textBounds);
//            monthNameHeight = textBounds.height();
//            monthNameWidth = textBounds.width();
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//            //p.setStrokeWidth(10);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l += 1;
//                upperLeftCornerX = x - k;
//
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = upperLeftCornerX - side;
//                float top = y-side;
//                float right = upperLeftCornerX;
//                float bottom = y;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 1, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                //canvas.drawText(text, left, y+side/4, p);
//                canvas.drawText(text, left+side/4, y-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        autumn.currentDate = null;
//                        spring.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 1, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//                k += side;
//            }
//            if (firstOccurrence) {
//                februaryLength = -upperLeftCornerX + getWidth() * 1.5f;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
////            if(x <= februaryLength && x >= januaryLength + side * 2) {
////                p.setTextAlign(Paint.Align.CENTER);
////                canvas.drawText("FEBRUARY", getWidth()/2, y - side * 1.5f, p);
////            }else if(x <= februaryLength){
////                p.setTextAlign(Paint.Align.RIGHT);
////                canvas.drawText("FEBRUARY", upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
////            }else if(x >= februaryLength){
////                p.setTextAlign(Paint.Align.LEFT);
////                canvas.drawText("FEBRUARY", upperLeftCornerX - side, y - side * 1.5f, p);
////            }
//
////            if(x <= februaryLength - side*0.25f && x >= januaryLength + "january".length()*fontHeight/2 + side/2) {
////                p.setTextAlign(Paint.Align.CENTER);
////                canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
////            }else if(x <= februaryLength - side*0.25f){
////                p.setTextAlign(Paint.Align.RIGHT);
////                canvas.drawText(monthName, upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
////            }else{
////                p.setTextAlign(Paint.Align.LEFT);
////                canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
////            }
//
//
//            if(x <= februaryLength && x >= januaryLength + monthNameWidth) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
//            }else if(x <= februaryLength){
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(monthName, upperLeftCornerX + (februaryLength - januaryLength) - side, y - side * 1.5f, p);
//            }else{
//                p.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(monthName, upperLeftCornerX - side, y - side * 1.5f, p);
//            }
//
//
//            p.setColor(Color.CYAN);
//            //p.setStrokeWidth(side);
//            canvas.drawPoint(upperLeftCornerX, y, p);
//
//            //3-ий месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.MARCH);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//
//            p.getTextBounds(monthName, 0, monthName.length(), textBounds);
//            monthNameHeight = textBounds.height();
//            monthNameWidth = textBounds.width();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l += 1;
//                upperLeftCornerX = x - k;
//
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = upperLeftCornerX - side;
//                float top = y-side;
//                float right = upperLeftCornerX;
//                float bottom = y;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 2, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                //canvas.drawText(text, left, y+side/4, p);
//                canvas.drawText(text, left+side/4, y-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        autumn.currentDate = null;
//                        spring.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 2, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//                k += side;
//            }
//            if (firstOccurrence) {
//
//                marchLength = -upperLeftCornerX + getWidth() * 1.5f - side/2;
//                //length = -upperLeftCornerX + getWidth();
//                length = -upperLeftCornerX + getWidth() + side;
//
//                if (currentDate != null || selectedDay != null) {
//                    Day date = currentDate;
//                    if (currentDate == null){
//                        date = selectedDay;
//                    }
//                    calendar.clear();
//                    calendar.setTimeInMillis(date.date.getTime());
//
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                            +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//
//
//                    if(calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
//                        //x = x - date.left + getWidth() / 2 + getWidth() / 4;
//                        x = x - date.right + getWidth() / 2 + getWidth() / 4;
//                        if(x <= getWidth()) {
//                            x = getWidth();
//                        }
//                        if(x >= length){
//                            x = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
//                        //x = x - date.left + getWidth() / 2;
//                        x = x - date.right + getWidth() / 2;
//                        if(x <= getWidth()) {
//                            x = getWidth();
//                        }
//                        if(x >= length){
//                            x = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.MARCH) {
//                        //x = x - date.right + getWidth() / 2 - getWidth() / 4;
//                        x = x - date.left + getWidth() / 2 - getWidth() / 4;
//                        if(x <= getWidth()) {
//                            x = getWidth();
//                        }
//                        if(x >= length){
//                            x = length;
//                        }
//                    }
//                    invalidate();
//                }
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
////            if(x <= marchLength && x >= februaryLength + side * 2) {
////                p.setTextAlign(Paint.Align.CENTER);
////                canvas.drawText("MARCH", getWidth()/2, y - side * 1.5f, p);
////            }else if(x <= marchLength){
////                p.setTextAlign(Paint.Align.RIGHT);
////                canvas.drawText("MARCH", upperLeftCornerX + (marchLength - februaryLength) - side/2, y - side * 1.5f, p);
////            }
//
////            if( x >= februaryLength + side * 2) {
////                p.setTextAlign(Paint.Align.CENTER);
////                canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
////            }else {
////                p.setTextAlign(Paint.Align.RIGHT);
////                canvas.drawText(monthName, upperLeftCornerX + (marchLength - februaryLength) - side/2, y - side * 1.5f, p);
////            }
//
//            if( x >= februaryLength + monthNameWidth*1.25f) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth()/2, y - side * 1.5f, p);
//            }else {
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(monthName, upperLeftCornerX + (marchLength - februaryLength) - side/2, y - side * 1.5f, p);
//            }
//
//
//            p.setColor(Color.RED);
//            //p.setStrokeWidth(side);
//            canvas.drawPoint(upperLeftCornerX, y, p);
//
//
//            //canvas.drawPoint(x-correctiveX-240, y-correctiveY-270, p);//NexusS
//
//            p.setColor(Color.WHITE);
//            p.setStrokeWidth(strokeWidth);
//            canvas.drawPoint(doubleTapX, doubleTapY, p);
//
//
//            //canvas.drawText("12", doubleTapX - side/2, y + side/4, p);
//
//            if (currentDate != null) {
//                p.setColor(Color.WHITE);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (firstOccurrence) {
//                    calendar.clear();
//                    calendar.setTimeInMillis(currentDate.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//                if (!firstOccurrence && day == null) {
//                    day = currentDate;
//
//                    updateSchedule(day);
//                }
//            }
//
//            if (selectedDay != null) {
//                p.setColor(Color.YELLOW);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (!firstOccurrence && selectedDay != day) {
//                    day = selectedDay;
//
//                     updateSchedule(day);
//
//                    calendar.clear();
//                    calendar.setTimeInMillis(selectedDay.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//
//            }
//
//
//            if (firstOccurrence) {
//                firstOccurrence = false;
//            }
//            if (restore) {
//                restore = false;
//                restore();
//            }
//
//        }
//
//        public void restore (){
//
//            JsonParser parser = new JsonParser();
//            Gson gson = new Gson();
//            JsonArray array = parser.parse(yearStr.daysWinter).getAsJsonArray();
//            for (int i = 0; i < array.size(); i++) {
//                winter.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;
//
//                for (Task task : winter.days.get(i).tasks) {
//                    if (task.extra == taskExtra){
//                        task.shown = true;
//                        changedeTasksOfYear = true;
//                    }
//                    setReminder(task, winter.days.get(i).date);
//                    if (!task.done){
//                        winter.days.get(i).dayClosed = false;
//                    }
//                }
//            }
//
//
//        }
//
//
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            // координаты Touch-события
//            float evX = event.getX();
//            float evY = event.getY();
//
//            switch (event.getAction()) {
//                // касание началось
//                case MotionEvent.ACTION_DOWN:
//                    //positionOfTouchX = evX;
//                    //positionOfTouchY = evY;
//
//                    // включаем режим перетаскивания
//                    drag = true;
//
//                    // разница между левым верхним углом квадрата и точкой касания
//                    dragX = evX - x;
//                    dragY = evY - y;
//
//                    countDownTimer.cancel();
//                    /*
//                    dragX = x;
//                    dragY = y;*/
//
//
//                    //invalidate();
//                    break;
//                // тащим
//                case MotionEvent.ACTION_MOVE:
//
//
//
//                    // если режим перетаскивания включен
//                    if (drag) {
//
//                        //positionOfTouchX = evX;
//                        //positionOfTouchY = evY;
//
//                        // определеяем новые координаты
//                        x = evX - dragX;
//                        //y = evY - dragY;/////////////////////////////////////////////
//                        if(x <= getWidth()) {
//                            x = getWidth();
//                        }
//                        if(x >= length){
//                           x = length;
//                        }
//                        invalidate();
//                        //Log.d("XY", "X:" + x + "Y:" + y);
//                    }
//
//                    break;
//                // касание завершено
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    // выключаем режим перетаскивания
//                    drag = false;
//                    break;
//
//            }
//
//            if (gestureDetector.onTouchEvent(event)) return true;
//
//            return true;
//        }
//
//
//        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                doubleTapX = e.getX();
//                doubleTapY = e.getY();
//
//                Iterator<Day> j = days.iterator();
//                while (j.hasNext()){
//                    Day b = j.next();
//                    if(b.left <= doubleTapX && b.right >= doubleTapX) {
//                        selectedDay = b;
//                        spring.selectedDay = null;
//                        spring.invalidate();
//                        summer.selectedDay = null;
//                        summer.invalidate();
//                        autumn.selectedDay = null;
//                        autumn.invalidate();
//                        invalidate();
//
//                        calendar.clear();
//                        calendar.setTimeInMillis(selectedDay.date.getTime());
//                        dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                                +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                                +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                    }
//                }
//
//                return super.onDoubleTap(e);
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {
//
//                scrollTime = (int) velocityX;
//                if (scrollTime < 0){
//                    scrollTime *= -1;
//                }
//                countDownTimer = new CountDownTimer(scrollTime, 50) {
//
//                    public void onTick(long millisUntilFinished) {
//                        if (velocityX > 0){
//                            x += millisUntilFinished / 30;
//                            //positionOfTouchX += millisUntilFinished / 30;;
//                        }else{
//                            x -= millisUntilFinished / 30;
//                            //positionOfTouchX -= millisUntilFinished / 30;;
//                        }
//                       // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);
//
//                        //проверить край
//                        if(x <= getWidth()) {
//                            x = getWidth();
//                        }
//                        if(x >= length){
//                            x = length;
//                        }
//
//                        //обновить
//                        invalidate();
//                    }
//
//                    public void onFinish() {
//                        Log.d("onFling", "done!");
//                    }
//                }.start();
//
//                return true;
//            }
//
//        }
//
//
//
//
//    }

    //TODO Spring
//    class Spring extends View {
//        Context context;
//        Paint p;
//        // координаты для рисования квадрата
//        float x = 0;
//        float y = 0;
//        int side = 0;
//
//        //int width;//del
//        //int height;//del
//        float doubleTapX = 0;
//        float doubleTapY = 0;
//        float aprilLength = 0;
//        float mayLength = 0;
//        float juneLength = 0;
//        Day selectedDay = null;
//        Day currentDate = null;
//        ArrayList<Day> days = new ArrayList<Day>();
//        String monthName, reverseMonthName;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
//        boolean restore;
//
//
//        boolean firstOccurrence = true;
//        int scrollTime = 0;
//        CountDownTimer countDownTimer = new CountDownTimer(0, 0) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
//            }
//        };
//        // переменные для перетаскивания
//        boolean drag = false;
//        float dragX = 0;
//        float dragY = 0;
//
//        Bitmap backingBitmap;
//        Canvas drawCanvas;
//
//        private GestureDetector gestureDetector;
//
//        float upperLeftCornerX = 0;
//        float upperRightCornerX = 0;
//        float bottomLeftCornerY = 0;
//        float upperRightCornerY = 0;
//        float bottomRightCornerX = 0;
//
//        float length = 0;
//
//        public Spring(Context context) {
//            super(context);
//            init(context);
//
//        }
//
//        public Spring(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            init(context);
//        }
//
//        public Spring(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//            init(context);
//        }
//
//        private void init(Context context) {
//
//            p = new Paint();
//            gestureDetector = new GestureDetector(context, new MyGestureListener());
//
//            calendar.clear();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            calendar.clear();
//            calendar.set(year, month, day);
//            currDate = new Date(calendar.getTimeInMillis());
//        }
//
//
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            canvas.drawColor(Color.rgb(0, 255, 127));
//            drawSpring(canvas);
//
//        }
//
//        public void drawSpring(Canvas canvas){
//
//            int Width = canvas.getWidth();//del
//            int Height = canvas.getHeight();//del
//            int fontHeight = side / 2;
//            int strokeWidth = side / 5;
//            int l = 0;
//
//            //II-ой квартал
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.APRIL);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//            reverseMonthName = new StringBuffer(monthName).reverse().toString();
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            //1-ый месяц
//            int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            int k = 0;
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l = i - 1;
//                bottomLeftCornerY = y + k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = x;
//                float top =  bottomLeftCornerY;
//                float right =  x+side;
//                float bottom = bottomLeftCornerY + side;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 3, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/4, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        autumn.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 3, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//
//                k += side;
//            }
//            if (firstOccurrence) {
//                aprilLength = -bottomLeftCornerY + getHeight()/2;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            p.setTextAlign(Paint.Align.CENTER);
//            if (y >= aprilLength ) {
//
//                //canvas.drawText("April", x - side, getHeight() / 2, p);
//                canvas.save();
//                canvas.rotate(360f);
//                int s = getHeight() / 2 + side;
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            } else {
//                //p.setTextAlign(Paint.Align.CENTER);
//                //canvas.drawText("January", upperLeftCornerX - side/2, y - side * 1.5f, p);
//                canvas.save();
//                canvas.rotate(360f);
//                int s = (int) bottomLeftCornerY + side;
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            }
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(x, bottomLeftCornerY, p);
//
//            //2-ой месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.MAY);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//            reverseMonthName = new StringBuffer(monthName).reverse().toString();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l += 1;
//                bottomLeftCornerY = y + k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = x;
//                float top =  bottomLeftCornerY;
//                float right =  x+side;
//                float bottom = bottomLeftCornerY + side;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 4, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/4, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    calendar.clear();
//                    calendar.set(numberYearPicker.getValue(), 4, i);
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        autumn.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 4, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//
//                k += side;
//            }
//            if (firstOccurrence) {
//                mayLength = -bottomLeftCornerY + getHeight()/2;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            p.setTextAlign(Paint.Align.CENTER);
//
//            if (y >= mayLength && y <= aprilLength - side * 1.5f) {
//                canvas.save();
//                canvas.rotate(360f);
//                int s = getHeight() / 2 + side;
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            }else if(y >= mayLength + side*2){
//                canvas.save();
//                canvas.rotate(360f);
//                int s = (int) (bottomLeftCornerY + (mayLength - aprilLength)+ side * 1.5f);
//                for (char c : monthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s += fontHeight;
//                }
//                canvas.restore();
//            } else if(y <= mayLength){
//                canvas.save();
//                canvas.rotate(360f);
//                int s = (int) bottomLeftCornerY + side;
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            }
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(x, bottomLeftCornerY, p);
//
//            //3-ий месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.JUNE);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//            reverseMonthName = new StringBuffer(monthName).reverse().toString();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l += 1;
//                bottomLeftCornerY = y + k;
//
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = x;
//                float top =  bottomLeftCornerY;
//                float right =  x+side;
//                float bottom = bottomLeftCornerY + side;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 5, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/4, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        autumn.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 5, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//
//                k += side;
//            }
//
//            if (firstOccurrence) {
//
//                juneLength = -bottomLeftCornerY + getHeight()/2;
//                length = -bottomLeftCornerY + getHeight() - side;
//                //Log.d("XY", "bottomLeftCornerY:" + length);
//
//                if (currentDate != null || selectedDay != null) {
//                    Day date = currentDate;
//                    if (currentDate == null){
//                        date = selectedDay;
//                    }
//                    calendar.clear();
//                    calendar.setTimeInMillis(date.date.getTime());
//
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                            +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//
//                    if(calendar.get(Calendar.MONTH) == Calendar.APRIL) {
//                        y = y - date.bottom + getHeight() / 2 - getHeight() / 4;
//                        if (y >= 0) {
//                            y = 0;
//                        }
//                        if (y <= length) {
//                            y = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.MAY) {
//                        y = y - date.top + getHeight() / 2;
//                        if (y >= 0) {
//                            y = 0;
//                        }
//                        if (y <= length) {
//                            y = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.JUNE) {
//                        y = y - date.top + getHeight() / 2 + getHeight() / 4;
//                        if (y >= 0) {
//                            y = 0;
//                        }
//                        if (y <= length) {
//                            y = length;
//                        }
//                    }
//                    invalidate();
//                }
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            p.setTextAlign(Paint.Align.CENTER);
//
//            if (y >= juneLength && y <= mayLength - side * 2) {
//                canvas.save();
//                canvas.rotate(360f);
//                int s = getHeight() / 2 + side;
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//
//            } else if (y >= juneLength) {
//                canvas.save();
//                canvas.rotate(360f);
//                int s =(int) (bottomLeftCornerY + (juneLength - mayLength) + side * 1.5f);
//                for (char c : monthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x - side / 1.5f, s, p);
//                    s += fontHeight;
//                }
//                canvas.restore();
//            }
//
//
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(x, bottomLeftCornerY, p);
////
////
////            p.setColor(Color.BLUE);
////            p.setStrokeWidth(10);
////            canvas.drawPoint(x, y, p);
//
//
//
//            p.setColor(Color.WHITE);
//            p.setStrokeWidth(strokeWidth);
//            canvas.drawPoint(doubleTapX, doubleTapY, p);
//
//
//            //canvas.drawText("12", doubleTapX - side/2, y + side/4, p);
//            if (currentDate != null) {
//                p.setColor(Color.WHITE);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (firstOccurrence) {
//                    calendar.clear();
//                    calendar.setTimeInMillis(currentDate.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//                if (!firstOccurrence && day == null) {
//                    day = currentDate;
//
//                   updateSchedule(day);
//                }
//            }
//
//            if (selectedDay != null) {
//                p.setColor(Color.YELLOW);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (!firstOccurrence && selectedDay != day) {
//                    day = selectedDay;
//
//                   updateSchedule(day);
//
//                    calendar.clear();
//                    calendar.setTimeInMillis(selectedDay.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//            }
//
//            if (firstOccurrence) {
//                firstOccurrence = false;
//            }
//            if (restore) {
//                restore = false;
//                restore();
//            }
//
//        }
//
//        public void restore (){
//
//            JsonParser parser = new JsonParser();
//            Gson gson = new Gson();
//            JsonArray array = parser.parse(yearStr.daysSpring).getAsJsonArray();
//            for (int i = 0; i < array.size(); i++) {
//                spring.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;
//
//                for (Task task : spring.days.get(i).tasks) {
//                    if (task.extra == taskExtra){
//                        task.shown = true;
//                        changedeTasksOfYear = true;
//                    }
//                    setReminder(task, spring.days.get(i).date);
//                    if (!task.done){
//                        spring.days.get(i).dayClosed = false;
//                    }
//                }
//                //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
//            }
//
//        }
//
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            // координаты Touch-события
//            float evX = event.getX();
//            float evY = event.getY();
//
//            switch (event.getAction()) {
//                // касание началось
//                case MotionEvent.ACTION_DOWN:
//                    // если касание было начато в пределах квадрата
//
//                    // включаем режим перетаскивания
//                    drag = true;
//
//                    // разница между левым верхним углом квадрата и точкой касания
//                    dragX = evX - x;
//                    dragY = evY - y;
//
//                    countDownTimer.cancel();
//
//                    //Log.d("WH", "W:" + Width + "H:" + Height);
//
//
//                    break;
//                // тащим
//                case MotionEvent.ACTION_MOVE:
//                    // если режим перетаскивания включен
//                    if (drag) {
//                        // определеяем новые координаты
//                        //x = evX - dragX;/////////////////////////////////////////////////////////
//                        y = evY - dragY;
//                        if(y >= 0) {
//                            y = 0;
//                        }
//                        if(y <= length){
//                            //y = length - getHeight();
//                            y = length;
//                        }
//                        invalidate();
//                       // Log.d("XY", "X:" + x + "Y:" + y +"length "+ length);
//
//                    }
//
//                    break;
//                // касание завершено
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    // выключаем режим перетаскивания
//                    drag = false;
//                    break;
//
//            }
//
//            gestureDetector.onTouchEvent(event);
//
//            return true;
//        }
//
//        @Override
//        protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
//            width = width;
//            height = height;
//            //Log.d("WH", "W:" + width + "H:" + height);
//        }
//
//
//        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                doubleTapX = e.getX();
//                doubleTapY = e.getY();
//
//                Iterator<Day> j = days.iterator();
//                while (j.hasNext()){
//                    Day b = j.next();
//                    if(b.top <= doubleTapY && b.bottom >= doubleTapY) {
//                        selectedDay = b;
//                        winter.selectedDay = null;
//                        winter.invalidate();
//                        summer.selectedDay = null;
//                        summer.invalidate();
//                        autumn.selectedDay = null;
//                        autumn.invalidate();
//                        invalidate();
//
//                        calendar.clear();
//                        calendar.setTimeInMillis(selectedDay.date.getTime());
//                        dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                                +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                                +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                    }
//                }
//
//                return super.onDoubleTap(e);
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {
//
//                scrollTime = (int) velocityY;
//                if (scrollTime < 0){
//                    scrollTime *= -1;
//                }
//                countDownTimer = new CountDownTimer(scrollTime, 50) {
//
//                    public void onTick(long millisUntilFinished) {
//                        if (velocityY > 0){
//                            y += millisUntilFinished / 30;
//                        }else{
//                            y -= millisUntilFinished / 30;
//                        }
//                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);
//
//                        //проверить край
//                        if(y >= 0) {
//                            y = 0;
//                        }
//                        if(y <= length){
//                            y = length;
//                        }
//
//                        //обновить
//                        invalidate();
//                    }
//
//                    public void onFinish() {
//                        Log.d("onFling", "done!");
//                    }
//                }.start();
//
//
//                return true;
//            }
//
//        }
//
//
//    }

    //TODO Summer
//    class Summer extends View {
//        Context context;
//        Paint p;
//        // координаты для рисования квадрата
//        float x = 0;
//        float y = 0;
//        int side = 0;
//        int width = 0;
//        int height = 0;
//        float doubleTapX = 0;
//        float doubleTapY = 0;
//        float julyLength = 0;
//        float augustLength = 0;
//        float septemberLength = 0;
//        Day selectedDay = null;
//        Day currentDate = null;
//        ArrayList<Day> days = new ArrayList<Day>();
//        String monthName;
//        Rect textBounds = new Rect();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
//        boolean restore;
//
//
//
//        boolean firstOccurrence = true;
//        int scrollTime = 0;
//        CountDownTimer countDownTimer = new CountDownTimer(0, 0) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
//            }
//        };
//        // переменные для перетаскивания
//        boolean drag = false;
//        float dragX = 0;
//        float dragY = 0;
//
//        Bitmap backingBitmap;
//        Canvas drawCanvas;
//
//        private GestureDetector gestureDetector;
//
//        float upperLeftCornerX = 0;
//        float upperRightCornerX = 0;
//        float bottomLeftCornerY = 0;
//        float upperRightCornerY = 0;
//        float bottomRightCornerX = 0;
//
//        float length = 0;
//
//        public Summer(Context context) {
//            super(context);
//            init(context);
//
//        }
//
//        public Summer(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            init(context);
//        }
//
//        public Summer(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//            init(context);
//        }
//
//        private void init(Context context) {
//
//            p = new Paint();
//
//            gestureDetector = new GestureDetector(context, new MyGestureListener());
//
//            calendar.clear();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            calendar.clear();
//            calendar.set(year, month, day);
//            currDate = new Date(calendar.getTimeInMillis());
//
//        }
//
//
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            canvas.drawColor(Color.YELLOW);
//            drawSummer(canvas);
//
//
//
//
//
//        }
//
//        public void drawSummer(Canvas canvas){
//
//            int Width = canvas.getWidth();//del
//            int Height = canvas.getHeight();//del
//            int fontHeight = side / 2;
//            int strokeWidth = side / 5;
//            float monthNameHeight;
//            float monthNameWidth;
//            int l = 0;
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            //III-ий квартал
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.JULY);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//
//            p.getTextBounds(monthName, 0, monthName.length(), textBounds);
//            monthNameHeight = textBounds.height();
//            monthNameWidth = textBounds.width();
//
//            //1-ый месяц
//            int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            int k = 0;
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                k += side;
//                l = i - 1;
//
//                bottomRightCornerX = x + k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = bottomRightCornerX - side;
//                float top = y - side;//y-side/2;
//                float right = bottomRightCornerX;
//                float bottom = y;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 6, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                //canvas.drawText(text, left, y+side/4, p);
//                canvas.drawText(text, left + side / 4, bottom - side / 4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        spring.currentDate = null;
//                        autumn.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                } else {
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 6, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//            }
//            if (firstOccurrence) {
//                julyLength = -bottomRightCornerX + getWidth()/2 ;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//           /* if (x >= julyLength  + side*0.5f) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText("JULY", getWidth() / 2, y + side / 2, p);
//            } else {
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText("JULY", bottomRightCornerX, y + side / 2, p);
//            }*/
//
//            if (x >= julyLength  + monthNameWidth/2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth() / 2, y + side / 2, p);
//            } else {
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(monthName, bottomRightCornerX, y + side / 2, p);
//            }
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(bottomRightCornerX, y, p);
//
//            //2-ой месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.AUGUST);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//
//            p.getTextBounds(monthName, 0, monthName.length(), textBounds);
//            monthNameHeight = textBounds.height();
//            monthNameWidth = textBounds.width();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                k += side;
//                l += 1;
//
//                bottomRightCornerX = x + k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = bottomRightCornerX - side;
//                float top = y - side;//y-side/2;
//                float right = bottomRightCornerX;
//                float bottom = y;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 7, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left + side / 4, bottom - side / 4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        spring.currentDate = null;
//                        autumn.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                } else {
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 7, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//            }
//            if (firstOccurrence) {
//                augustLength = -bottomRightCornerX + getWidth()/2;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//
//            if(x <= julyLength - monthNameWidth/2  && x >= augustLength + monthNameWidth/2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth()/2, y + side / 2, p);
//            }else if(x >= julyLength - monthNameWidth/2){
//                p.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(monthName, bottomRightCornerX + (augustLength - julyLength),  y + side / 2, p);
//            }else {
//                p.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(monthName, bottomRightCornerX,  y + side / 2, p);
//            }
//
//
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(bottomRightCornerX, y, p);
//
//            //3-ий месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//
//            p.getTextBounds(monthName, 0, monthName.length(), textBounds);
//            monthNameHeight = textBounds.height();
//            monthNameWidth = textBounds.width();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                k += side;
//                l += 1;
//
//                bottomRightCornerX = x + k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = bottomRightCornerX - side;
//                float top = y - side;//y-side/2;
//                float right = bottomRightCornerX;
//                float bottom = y;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 8, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                //canvas.drawText(text, left, y+side/4, p);
//                canvas.drawText(text, left + side / 4, bottom - side / 4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        spring.currentDate = null;
//                        autumn.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                } else {
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 8, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//            }
//            if (firstOccurrence) {
//
//                septemberLength = -bottomRightCornerX + getWidth()/2;
//                length = -bottomRightCornerX + getWidth();
//                //Log.d("XY", "length:" + length);
//
//                if (currentDate != null || selectedDay != null) {
//                    Day date = currentDate;
//                    if (currentDate == null){
//                        date = selectedDay;
//                    }
//                    calendar.clear();
//                    calendar.setTimeInMillis(date.date.getTime());
//
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                            +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//
//                    if(calendar.get(Calendar.MONTH) == Calendar.JULY) {
//                        x = x - date.right + getWidth() / 2 - getWidth() / 4;
//                        if(x >= 0) {
//                            x = 0;
//                        }
//                        if(x <= length){
//                            x = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
//                        x = x - date.right + getWidth() / 2;
//                        if(x >= 0) {
//                            x = 0;
//                        }
//                        if(x <= length){
//                            x = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
//                        x = x - date.left + getWidth() / 2 + getWidth() / 4;
//                        if(x >= 0) {
//                            x = 0;
//                        }
//                        if(x <= length){
//                            x = length;
//                        }
//                    }
//                    invalidate();
//                }
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            if(x <= augustLength - monthNameWidth/2) {
//                p.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(monthName, getWidth()/2,  y + side / 2, p);
//            }else {
//                p.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(monthName, bottomRightCornerX + (septemberLength - augustLength), y + side / 2, p);
//            }
//
//
//            p.setColor(Color.WHITE);
//            p.setStrokeWidth(strokeWidth);
//            canvas.drawPoint(doubleTapX, doubleTapY, p);
//
//            if (currentDate != null) {
//                p.setColor(Color.WHITE);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (firstOccurrence) {
//                    calendar.clear();
//                    calendar.setTimeInMillis(currentDate.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//                if (!firstOccurrence && day == null) {
//                    day = currentDate;
//
//                    updateSchedule(day);
//                }
//            }
//
//            if (selectedDay != null) {
//                p.setColor(Color.GREEN);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (!firstOccurrence && selectedDay != day) {
//                    day = selectedDay;
//
//                    updateSchedule(day);
//
//                    calendar.clear();
//                    calendar.setTimeInMillis(selectedDay.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//            }
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint(bottomRightCornerX, y, p);
////
////            p.setColor(Color.BLUE);
////            p.setStrokeWidth(10);
////            canvas.drawPoint(x, y, p);
//
//
//            if (firstOccurrence) {
//                firstOccurrence = false;
//            }
//            if (restore) {
//                restore = false;
//                restore();
//            }
//
//        }
//
//        public void restore (){
//
//            JsonParser parser = new JsonParser();
//            Gson gson = new Gson();
//            JsonArray array = parser.parse(yearStr.daysSummer).getAsJsonArray();
//            for (int i = 0; i < array.size(); i++) {
//                summer.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;
//
//                for (Task task : summer.days.get(i).tasks) {
//                    if (task.extra == taskExtra){
//                        task.shown = true;
//                        changedeTasksOfYear = true;
//                    }
//                    setReminder(task, summer.days.get(i).date);
//                    if (!task.done){
//                        summer.days.get(i).dayClosed = false;
//                    }
//                }
//                //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
//            }
//
//        }
//
//
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            // координаты Touch-события
//            float evX = event.getX();
//            float evY = event.getY();
//
//            switch (event.getAction()) {
//                // касание началось
//                case MotionEvent.ACTION_DOWN:
//                    // если касание было начато в пределах квадрата
//
//                    // включаем режим перетаскивания
//                    drag = true;
//
//                    // разница между левым верхним углом квадрата и точкой касания
//                    dragX = evX - x;
//                    dragY = evY - y;
//
//                    countDownTimer.cancel();
//
//
//                    break;
//                // тащим
//                case MotionEvent.ACTION_MOVE:
//                    // если режим перетаскивания включен
//                    if (drag) {
//                        // определеяем новые координаты
//                        x = evX - dragX;
//                        //y = evY - dragY;////////////////////////////////////////////////////////////
//                        if(x >= 0) {
//                            x = 0;
//                        }
//                        if(x <= length){
//                            x = length;
//                        }
//                        invalidate();
//                        //Log.d("XY", "X:" + x + "Y:" + y + "length" + length);
//                    }
//
//                    break;
//                // касание завершено
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    // выключаем режим перетаскивания
//                    drag = false;
//                    break;
//
//            }
//
//            if (gestureDetector.onTouchEvent(event)) return true;
//
//            return true;
//        }
//
//
//        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//            public boolean onDoubleTap(MotionEvent e) {
//                doubleTapX = e.getX();
//                doubleTapY = e.getY();
//
//                Iterator<Day> j = days.iterator();
//                while (j.hasNext()){
//                    Day b = j.next();
//                    if(b.left <= doubleTapX && b.right >= doubleTapX) {
//                        selectedDay = b;
//                        winter.selectedDay = null;
//                        winter.invalidate();
//                        spring.selectedDay = null;
//                        spring.invalidate();
//                        autumn.selectedDay = null;
//                        autumn.invalidate();
//                        invalidate();
//
//                        calendar.clear();
//                        calendar.setTimeInMillis(selectedDay.date.getTime());
//                        dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                                +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                                +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                    }
//                }
//
//                return super.onDoubleTap(e);
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {
//
//                scrollTime = (int) velocityX;
//                if (scrollTime < 0){
//                    scrollTime *= -1;
//                }
//                countDownTimer = new CountDownTimer(scrollTime, 50) {
//
//                    public void onTick(long millisUntilFinished) {
//                        if (velocityX > 0){
//                            x += millisUntilFinished / 30;
//                        }else{
//                            x -= millisUntilFinished / 30;
//                        }
//                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);
//
//                        //проверить край
//                        if(x >= 0) {
//                            x = 0;
//                        }
//                        if(x <= length){
//                            x = length;
//                        }
//
//                        //обновить
//                        invalidate();
//                    }
//
//                    public void onFinish() {
//                        //Log.d("onFling", "done!");
//                    }
//                }.start();
//
//                return true;
//            }
//
//        }
//
//
//    }

    //TODO Autumn
//    class Autumn extends View {
//        Context context;
//        Paint p;
//        // координаты для рисования квадрата
//        float x = 0;
//        float y = 0;
//        int side = 0;
//        //int width;//del
//        //int height;//del
//        float doubleTapX = 0;
//        float doubleTapY = 0;
//        float octoberLength = 0;
//        float novemberLength = 0;
//        float decemberLength = 0;
//        Day selectedDay = null;
//        Day currentDate = null;
//        ArrayList<Day> days = new ArrayList<Day>();
//        String monthName, reverseMonthName;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
//        boolean restore;
//        boolean addCyclicTasks;
//
//
//
//        boolean firstOccurrence = true;
//        int scrollTime = 0;
//        CountDownTimer countDownTimer = new CountDownTimer(0, 0) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
//            }
//        };
//        // переменные для перетаскивания
//        boolean drag = false;
//        float dragX = 0;
//        float dragY = 0;
//
//        Bitmap backingBitmap;
//        Canvas drawCanvas;
//
//        private GestureDetector gestureDetector;
//
//        float upperLeftCornerX = 0;
//        float upperRightCornerX = 0;
//        float bottomLeftCornerY = 0;
//        float upperRightCornerY = 0;
//        float bottomRightCornerX = 0;
//
//        float length = 0;
//
//        public Autumn(Context context) {
//            super(context);
//            init(context);
//
//        }
//
//        public Autumn(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            init(context);
//        }
//
//        public Autumn(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//            init(context);
//        }
//
//        private void init(Context context) {
//
//            p = new Paint();
//            gestureDetector = new GestureDetector(context, new MyGestureListener());
//
//            calendar.clear();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            calendar.clear();
//            calendar.set(year, month, day);
//            currDate = new Date(calendar.getTimeInMillis());
//
//        }
//
//
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            canvas.drawColor(Color.rgb(255, 215, 0));
//            drawAutumn(canvas);
//
//        }
//
//        public void drawAutumn(Canvas canvas){
//
//            int Width = canvas.getWidth();//del
//            int Height = canvas.getHeight();//del
//            int fontHeight = side / 2;
//            int strokeWidth = side / 5;
//            int l = 0;
//
//            //IV-ый квартал
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.OCTOBER);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//            reverseMonthName = new StringBuffer(monthName).reverse().toString();
//
//
//            p.reset();
//            p.setTextAlign(Paint.Align.CENTER);
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            //1-ый месяц
//            int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            int k = 0;
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l = i - 1;
//                upperRightCornerY = y - k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = x-side;
//                float top =  upperRightCornerY -side;
//                float right =  x;
//                float bottom = upperRightCornerY;
//
//                p.reset();
//                p.setTextAlign(Paint.Align.CENTER);
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 9, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/2, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                   Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    //if (date.getTime() == currDate.getTime()) {
//                    if (date.compareTo(currDate) == 0) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        spring.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                            selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 9, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        //if (date.getTime() == currDate.getTime()) {
//                        if (date.compareTo(currDate) == 0) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//                }
//                k += side;
//            }
//            if (firstOccurrence) {
//                octoberLength = -upperRightCornerY + getHeight() * 1.5f;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            p.setTextAlign(Paint.Align.CENTER);
//            if (y <= octoberLength - reverseMonthName.length()*fontHeight/2 + side) {
//                canvas.save();
//                canvas.rotate(360f);
//                int s = getHeight() / 2 +reverseMonthName.length()*fontHeight/2;
//                for (char c : (reverseMonthName).toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            } else {
//                canvas.save();
//                canvas.rotate(360f);
//                int s = (int) upperRightCornerY - side/2;
//                for (char c : monthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s += fontHeight;
//                }
//                canvas.restore();
//            }
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint((float) x, upperRightCornerY, p);
//
//            //2-ой месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//            reverseMonthName = new StringBuffer(monthName).reverse().toString();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
//                l += 1;
//                upperRightCornerY = y - k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = x-side;
//                float top =  upperRightCornerY -side;
//                float right =  x;
//                float bottom = upperRightCornerY;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 10, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/4, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//
//                    if (date.getTime() == currDate.getTime()) {
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        spring.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 10, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//
//                }
//                k += side;
//            }
//            if (firstOccurrence) {
//                novemberLength = -upperRightCornerY + getHeight() * 1.5f;
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            p.setTextAlign(Paint.Align.CENTER);
//
//            if (y <= novemberLength - monthName.length()*fontHeight/2 + side/2 && y >= octoberLength + monthName.length()*fontHeight/2 + side/2) {
//                canvas.save();
//                canvas.rotate(360f);
//                int s = getHeight() / 2 - monthName.length()*fontHeight/2 ;
//                for (char c : monthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s += fontHeight;
//                }
//                canvas.restore();
//            }else if(y <= novemberLength - monthName.length()*fontHeight/2 + side/2){
//                canvas.save();
//                canvas.rotate(360f);
//                int s = (int) (upperRightCornerY + (novemberLength - octoberLength) - side);
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            } else if(y >= novemberLength - monthName.length()*fontHeight/2 + side/2){
//                canvas.save();
//                canvas.rotate(360f);
//                int s = (int) upperRightCornerY - side/2;
//                for (char c : monthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s += fontHeight;
//                }
//                canvas.restore();
//            }
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint((float) x, upperRightCornerY, p);
//
//            //3-ий месяц
//            calendar.clear();
//            calendar.set(Calendar.YEAR, numberYearPicker.getValue());
//            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
//
//            monthName = dateFormat.format(calendar.getTimeInMillis());
//            monthName = monthName.toUpperCase();
//            reverseMonthName = new StringBuffer(monthName).reverse().toString();
//
//
//            p.reset();
//            p.setColor(Color.BLACK);
//            p.setTextSize(fontHeight);
//
//            maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            for (int i = 1; i <= maxDaysOfMonth; i++) {
////                k += side;
////                upperRightCornerY = y - k;
////                canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, x, upperRightCornerY, p);
//                l += 1;
//                upperRightCornerY = y - k;
//                String text = ("" + i).length() == 1 ? "0" + i : "" + i;
//                float left = x-side;
//                float top =  upperRightCornerY -side;
//                float right =  x;
//                float bottom = upperRightCornerY;
//
//                p.reset();
//                p.setColor(Color.BLACK);
//                p.setTextSize(fontHeight);
//
//                p.setStyle(Paint.Style.FILL);
//                calendar.clear();
//                calendar.set(numberYearPicker.getValue(), 11, i);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
//                    p.setColor(Color.RED);
//                }
//                canvas.drawText(text, left+side/4, bottom-side/4, p);
//
//                p.setColor(Color.BLACK);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(left, top, right, bottom, p);
//                if (firstOccurrence) {
//                    Date date = new Date(calendar.getTimeInMillis());
//                    days.add(new Day(date, left, top, right, bottom));
//                    if (date.getTime() == currDate.getTime()) {
//                        //currentDate = new Day(date, left, top, right, bottom);
//                        currentDate = days.get(days.size()-1);
//                        winter.currentDate = null;
//                        spring.currentDate = null;
//                        summer.currentDate = null;
//                    }
//
//                    if (selectedDay != null) {
//                        if (selectedDay.date.getMonth() == date.getMonth() &&
//                                selectedDay.date.getDate() == date.getDate() ) {
//                            selectedDay = days.get(days.size()-1);
//                        }
//                    }
//                }else{
//                    days.get(l).left = left;
//                    days.get(l).top = top;
//                    days.get(l).right = right;
//                    days.get(l).bottom = bottom;
//
//                    if (currentDate != null) {
//                        calendar.clear();
//                        calendar.set(numberYearPicker.getValue(), 11, i);
//                        Date date = new Date(calendar.getTimeInMillis());
//
//                        if (date.getTime() == currDate.getTime()) {
//                            currentDate.left = left;
//                            currentDate.top = top;
//                            currentDate.right = right;
//                            currentDate.bottom = bottom;
//                        }
//                    }
//
//                    if(!days.get(l).dayClosed){
//                        p.setColor(Color.CYAN);
//                        p.setStrokeWidth(strokeWidth/2);
//                        p.setStyle(Paint.Style.STROKE);
//                        canvas.drawRect(left, top, right, bottom, p);
//                    }
//
//
//                }
//                k += side;
//            }
//
//            if (firstOccurrence) {
//
//                decemberLength = -upperRightCornerY + getHeight() * 1.5f;
//                length = -upperRightCornerY + getHeight() + side;
//                //Log.d("XY", "upperRightCornerY:" + length);
//
//                if (currentDate != null || selectedDay != null) {
//                    Day date = currentDate;
//                    if (currentDate == null){
//                        date = selectedDay;
//                    }
//                    calendar.clear();
//                    calendar.setTimeInMillis(date.date.getTime());
//
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                            +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//
//                    if(calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
//                        y = y - date.top + getHeight() / 2 + getHeight() / 4;
//                        if (y <= getHeight()) {
//                            y = getHeight();
//                        }
//                        if (y >= length) {
//                            y = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
//                        y = y - date.bottom + getHeight() / 2;
//                        if (y <= getHeight()) {
//                            y = getHeight();
//                        }
//                        if (y >= length) {
//                            y = length;
//                        }
//                    }else if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
//                        y = y - date.bottom + getHeight() / 2 - getHeight() / 4;
//                        if (y <= getHeight()) {
//                            y = getHeight();
//                        }
//                        if (y >= length) {
//                            y = length;
//                        }
//                    }
//                    invalidate();
//                }
//            }
//
//            p.setColor(Color.BLACK);
//            p.setStyle(Paint.Style.FILL);
//            p.setTextAlign(Paint.Align.CENTER);
//
//            //if (y <= decemberLength  && y >= novemberLength + monthName.length()*fontHeight/2) {
//            if (y >= novemberLength + side*2.5) {
//                canvas.save();
//                canvas.rotate(360f);
//                int s = getHeight() / 2 - monthName.length()*fontHeight/2;
//                for (char c : monthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s += fontHeight;
//                }
//                canvas.restore();
//
//            //} else if (y <= decemberLength) {
//            } else {
//                canvas.save();
//                canvas.rotate(360f);
//                int s =(int) (upperRightCornerY + (decemberLength - novemberLength) - side);
//                for (char c : reverseMonthName.toCharArray()) {
//                    canvas.drawText(String.valueOf(c), x + side / 1.5f, s, p);
//                    s -= fontHeight;
//                }
//                canvas.restore();
//            }
//
//            p.setColor(Color.WHITE);
//            p.setStrokeWidth(strokeWidth);
//            canvas.drawPoint(doubleTapX, doubleTapY, p);
//
//            if (currentDate != null) {
//                p.setColor(Color.WHITE);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(currentDate.left, currentDate.top, currentDate.right, currentDate.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (firstOccurrence) {
//                    calendar.clear();
//                    calendar.setTimeInMillis(currentDate.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//                if (!firstOccurrence && day == null) {
//                    day = currentDate;
//
//                    updateSchedule(day);
//                }
//            }
//
//            if (selectedDay != null) {
//                p.setColor(Color.GREEN);
//                p.setStyle(Paint.Style.STROKE);
//                canvas.drawRect(selectedDay.left, selectedDay.top, selectedDay.right, selectedDay.bottom, p);
//                p.setStyle(Paint.Style.FILL);
//
//                if (!firstOccurrence && selectedDay != day) {
//                    day = selectedDay;
//
//                    updateSchedule(day);
//
//                    calendar.clear();
//                    calendar.setTimeInMillis(selectedDay.date.getTime());
//                    dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " "
//                            + calendar.get(Calendar.DAY_OF_MONTH) + " "
//                            + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//                }
//            }
//
//
//
//
//
////            p.setColor(Color.RED);
////            p.setStrokeWidth(side);
////            canvas.drawPoint((float) x, upperRightCornerY, p);
////
////
////            p.setColor(Color.BLUE);
////            p.setStrokeWidth(10);
////            canvas.drawPoint(x, y, p);
//
//            if (firstOccurrence) {
//                firstOccurrence = false;
//            }
//            if (restore) {
//                restore = false;
//                restore();
//            }
//            if (addCyclicTasks) {
//                addCyclicTasks = false;
//                addCyclicTasks();
//
//                winter.invalidate();
//                spring.invalidate();
//                summer.invalidate();
//                invalidate();
//            }
//
//        }
//
//        public void restore (){
//
//            JsonParser parser = new JsonParser();
//            Gson gson = new Gson();
//            JsonArray array = parser.parse(yearStr.daysAutumn).getAsJsonArray();
//            for (int i = 0; i < array.size(); i++) {
//                autumn.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;
//
//                for (Task task : autumn.days.get(i).tasks) {
//                    if (task.extra == taskExtra){
//                        task.shown = true;
//                        changedeTasksOfYear = true;
//                    }
//                    setReminder(task, autumn.days.get(i).date);
//                    if (!task.done){
//                        autumn.days.get(i).dayClosed = false;
//                    }
//                }
//                //autumn.days.set(i, gson.fromJson(array.get(i), Day.class));
//            }
//
//        }
//
//        public void addCyclicTasks (){
//
//            Iterator<Task> j = cyclicTasks.iterator();
//            while (j.hasNext()) {
//                Task t = j.next();
//                refreshCyclicTasks(t);
//            }
//
//        }
//
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            // координаты Touch-события
//            float evX = event.getX();
//            float evY = event.getY();
//
//            switch (event.getAction()) {
//                // касание началось
//                case MotionEvent.ACTION_DOWN:
//
//                    countDownTimer.cancel();
//
//                    // включаем режим перетаскивания
//                    drag = true;
//
//                    // разница между левым верхним углом квадрата и точкой касания
//                    dragX = evX - x;
//                    dragY = evY - y;
//
//                    //Log.d("WH", "W:" + Width + "H:" + Height);
//
//
//                    break;
//                // тащим
//                case MotionEvent.ACTION_MOVE:
//                    // если режим перетаскивания включен
//                    if (drag) {
//                        // определеяем новые координаты
//                        //x = evX - dragX;////////////////////////////////////////////
//                        y = evY - dragY;
//                        if(y <= getHeight()) {
//                            y = getHeight();
//                        }
//                        if(y >= length){
//                            y = length;
//                        }
//                        invalidate();
//                        //Log.d("XY", "X:" + x + "Y:" + y + "length "+length);
//                    }
//
//                    break;
//                // касание завершено
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    // выключаем режим перетаскивания
//                    drag = false;
//                    break;
//
//            }
//
//            gestureDetector.onTouchEvent(event);
//
//            return true;
//        }
//
//        @Override
//        protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
//            width = width;
//            height = height;
//            //Log.d("WH", "W:" + width + "H:" + height);
//        }
//
//
//        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                doubleTapX = e.getX();
//                doubleTapY = e.getY();
//
//                Iterator<Day> j = days.iterator();
//                while (j.hasNext()){
//                    Day b = j.next();
//                    if(b.top <= doubleTapY && b.bottom >= doubleTapY) {
//                        selectedDay = b;
//                        winter.selectedDay = null;
//                        winter.invalidate();
//                        spring.selectedDay = null;
//                        spring.invalidate();
//                        summer.selectedDay = null;
//                        summer.invalidate();
//                        invalidate();
//
//                        calendar.clear();
//                        calendar.setTimeInMillis(selectedDay.date.getTime());
//                        dateMonth.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "
//                                +calendar.get(Calendar.DAY_OF_MONTH) + " "
//                                +calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
//
////                        taskTime.setText("__:__");
////                        taskDuration.setText("__:__");
////                        taskDescription.setText("");
////                        updateSchedule(selectedDay);
//
//                    }
//                }
//
//                return super.onDoubleTap(e);
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {
//
//                scrollTime = (int) velocityY;
//                if (scrollTime < 0){
//                    scrollTime *= -1;
//                }
//                countDownTimer = new CountDownTimer(scrollTime, 50) {
//
//                    public void onTick(long millisUntilFinished) {
//                        if (velocityY > 0){
//                            y += millisUntilFinished / 30;
//                        }else{
//                            y -= millisUntilFinished / 30;
//                        }
//                        // Log.d("onFling", "millisUntilFinished "+millisUntilFinished / 30);
//
//                        //проверить край
//                        if(y <= getHeight()) {
//                            y = getHeight();
//                        }
//                        if(y >= length){
//                            y = length;
//                        }
//
//                        //обновить
//                        invalidate();
//                    }
//
//                    public void onFinish() {
//                        //Log.d("onFling", "done!");
//                    }
//                }.start();
//
//
//                return true;
//            }
//
//        }
//
////        public class Date
////        {
////            /**Картинка*/
////            private Bitmap bmp;
////
////            /**Позиция*/
////            public int x;
////            public int y;
////
////            /**Скорость по Х=15*/
////            private int mSpeed=25;
////
////            public double angle;
////
////            /**Ширина*/
////            public int width;
////
////            /**Ввыоста*/
////            public  int height;
////
////            public GameView gameView;
////
////            /**Конструктор*/
////            public Date(GameView gameView, Bitmap bmp) {
////                this.gameView=gameView;
////                this.bmp=bmp;
////
////                this.x = 0;            //позиция по Х
////                this.y = 120;          //позиция по У
////                this.width = 27;       //ширина снаряда
////                this.height = 40;      //высота снаряда
////
////                //угол полета пули в зависипости от координаты косания к экрану
////                angle = Math.atan((double)(y - gameView.shotY) / (x - gameView.shotX));
////            }
////
////            /**Перемещение объекта, его направление*/
////            private void update() {
////                x += mSpeed * Math.cos(angle);         //движение по Х со скоростью mSpeed и углу заданном координатой angle
////                y += mSpeed * Math.sin(angle);         // движение по У -//-
////            }
////
////            /**Рисуем наши спрайты*/
////            public void onDraw(Canvas canvas) {
////                update();                              //говорим что эту функцию нам нужно вызывать для работы класса
////                canvas.drawBitmap(bmp, x, y, null);
////            }
////        }
//
//
//
//
//
//
//
//    }

    public class YearStr
    {
        public int year;
        public String daysWinter;
        public String daysSpring;
        public String daysSummer;
        public String daysAutumn;

        public String cyclicTasks;

        public YearStr(int year, ArrayList<Day> daysWinter, ArrayList<Day> daysSpring, ArrayList<Day> daysSummer, ArrayList<Day> daysAutumn, ArrayList<Task> cyclicTasks){
            this.year = year;
            this.daysWinter = new Gson().toJson(daysWinter);
            this.daysSpring = new Gson().toJson(daysSpring);
            this.daysSummer = new Gson().toJson(daysSummer);
            this.daysAutumn = new Gson().toJson(daysAutumn);

            this.cyclicTasks = new Gson().toJson(cyclicTasks);
        }
    }

//    public class Day
//    {
//        /**Позиция*/
//        public float left;
//        public float top;
//        public float right;
//        public float bottom;
//        public ArrayList<Task> tasks = new ArrayList<Task>();
//        public boolean dayClosed;
//
//        /**Дата*/
//        Date date;
//
//        /**Ширина*/
//        public int width;
//
//        /**Ввыоста*/
//        public  int height;
//
//        public Day(Date date, float left, float top, float right, float bottom) {
//            this.date = date;
//            this.left = left;
//            this.top = top;
//            this.right = right;
//            this.bottom = bottom;
//            this.dayClosed = true;
//
////            tasks.add(new Task(true,"Задача 1", date.getTime(), 1, 6));
////            tasks.add(new Task(false,"Задача 2", date.getTime(), 2, 15));
////            tasks.add(new Task(true,"Задача 3", date.getTime(), 17, 1));
//
//           /* Calendar rightNow = Calendar.getInstance();
//            Date d = new Date();
//            System.out.println(d);
//            rightNow.setTime(d);
//            rightNow.add(Calendar.HOUR, yourHours);
//            d = rightNow.getTime();
//            System.out.println(d);
//*/
//
//        }
//    }

    public static class Task
    {
        public long id;
        public int extra;
        public boolean isValid;
        public boolean isCyclic;//not used
        public boolean isDone;
        public String content;
        public Long startTime;
        public Long clockStartTime;
        public long finishTime;
        public int durationHours;
        public int durationMinutes;

        public boolean monday;
        public boolean tuesday;
        public boolean wednesday;
        public boolean thursday;
        public boolean friday;
        public boolean saturday;
        public boolean sunday;

        public boolean everyYear;
        public boolean everyMonth;
        public int inDays;

        public boolean remove;
        public boolean removeFromAM;
        public boolean shown;

        public Date taskTransferDate;
        public boolean alreadyReturned;

        public Task(boolean isValid, boolean isCyclic, String content, long startTime, int durationHours, int durationMinutes){

            this.id = new Random(System.nanoTime()).nextLong();
            this.extra = new Random(System.nanoTime()).nextInt();
            this.isValid = isValid;
            this.isCyclic = isCyclic;
            this.content = content;
            this.startTime = startTime;
            this.finishTime = startTime;
            this.durationHours = durationHours;
            this.durationMinutes = durationMinutes;
            this.inDays = 0;
            this.remove = false;
            this.removeFromAM = false;
            this.shown = false;

            calendar.clear();
            calendar.setTimeInMillis(startTime);
            Calendar myCalender = Calendar.getInstance();
            myCalender.clear();
            myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            this.clockStartTime = myCalender.getTimeInMillis();
            this.taskTransferDate = null;
            this.alreadyReturned = true;
        }

        public void duplicate(Task obj){

            Field[] publicFields = getClass().getFields();
            for (Field field : publicFields) {
                try {
                    field.set((obj), field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //obj.extra = new Random(System.currentTimeMillis()).nextInt();
            obj.extra = new Random(System.nanoTime()).nextInt();
            obj.shown = false;
            obj.isDone = false;
            obj.alreadyReturned = true;
            //Log.d("myLogs", "obj.extra:" + obj.extra);

        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj == this)
                return true;

     /* obj ссылается на null */

            if(obj == null)
                return false;

     /* Удостоверимся, что ссылки имеют тот же самый тип */

            if(!(getClass() == obj.getClass()))
                return false;
            else
            {
                Task tmp = (Task)obj;
                if(tmp.id == this.id) {
                    return true;
                }else {
                    return false;
                }
            }
        }

    }

    public class TaskComparator implements Comparator<Task>
    {
        public int compare(Task left, Task right) {
            return left.clockStartTime.compareTo(right.clockStartTime);
        }
    }










//    public class DateChangedReceiver extends BroadcastReceiver {
//
//        public DateChangedReceiver() {
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            calendar.clear();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            calendar.clear();
//            calendar.set(year, month, day);
//            currDate = new Date(calendar.getTimeInMillis());
//
//            winter.invalidate();
//            spring.invalidate();
//            summer.invalidate();
//            autumn.invalidate();
//        }
//
//    }

    //TODO NumberYearPicker
//    public class NumberYearPicker extends LinearLayout {
//
//
//        private Context context;
//        private int textColor = Color.BLACK;
//        private long repeatDeley = 50;
//
//
//        private int elementHeight = 60;
//
//        private int elementWidth = elementHeight; // you're all squares, yo
//
//
//        private int minimum = 0;
//        private int maximum = 4999;
//
//        private int textSize = 10;
//
//        public Integer value;
//
//        Button decrement;
//        Button increment;
//        public EditText valueText;
//
//        private Handler repeatUpdateHandler = new Handler();
//
//        private boolean autoIncrement = false;
//        private boolean autoDecrement = false;
//
//        class RepetetiveUpdater implements Runnable {
//            public void run() {
//                if( autoIncrement ){
//                    increment();
//                    repeatUpdateHandler.postDelayed( new RepetetiveUpdater(), repeatDeley );
//                } else if( autoDecrement ){
//                    decrement();
//                    repeatUpdateHandler.postDelayed( new RepetetiveUpdater(), repeatDeley );
//                }
//            }
//        }
//        public NumberYearPicker(Context context, AttributeSet attributeSet ) {
//            super(context, attributeSet);
//
//            this.context = context;
//            this.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
//            LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//            initDecrementButton( context );
//            initValueEditText( context );
//            initIncrementButton( context );
//            if( getOrientation() == VERTICAL ){
//                addView( increment, elementParams );
//                addView( valueText, elementParams );
//                addView( decrement, elementParams );
//            } else {
//                addView( decrement, elementParams );
//                addView( valueText, elementParams );
//                addView( increment, elementParams );
//            }
//
//            LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//            valueTextParams.width = (int)(4 * textSize * 2.3f);
//            valueText.setLayoutParams(valueTextParams);
//        }
//
//        public NumberYearPicker(Context context, Integer value) {
//            super(context);
//            this.context = context;
//            this.value = value;
//
//            this.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
//            LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//            initDecrementButton( context );
//            initValueEditText( context );
//            initIncrementButton( context );
//            if( getOrientation() == VERTICAL ){
//                addView( increment, elementParams );
//                addView( valueText, elementParams );
//                addView( decrement, elementParams );
//            } else {
//                addView( decrement, elementParams );
//                addView( valueText, elementParams );
//                addView( increment, elementParams );
//            }
//
//            LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//            valueTextParams.width = (int)(4 * textSize * 2.3f);
//            valueText.setLayoutParams(valueTextParams);
//        }
//
//        public void rebuild(Context context) {
//
//            removeAllViews();
//
////        LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
////        increment.setLayoutParams(elementParams);
////        decrement.setLayoutParams(elementParams);
////
////        LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
////        valueTextParams.width = (int)(4 * textSize * 2.3f);
////        valueText.setLayoutParams(valueTextParams);
//
//            this.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
//            LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//            initDecrementButton( context );
//            initValueEditText( context );
//            initIncrementButton( context );
//            if( getOrientation() == VERTICAL ){
//                addView( increment, elementParams );
//                addView( valueText, elementParams );
//                addView( decrement, elementParams );
//            } else {
//                addView( decrement, elementParams );
//                addView( valueText, elementParams );
//                addView( increment, elementParams );
//            }
//
//            LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//            //valueTextParams.height = textSize;
//            valueTextParams.width = (int)(4 * textSize * 2.1f);
//            valueText.setLayoutParams(valueTextParams);
//
//
//
//        }
//
//        private void initIncrementButton(Context context){
//            increment = new Button( context );
//            increment.setTextSize( textSize );
//            increment.setText( "+" );
//            increment.setTextColor(textColor);
//
//            // Increment once for a click
//            increment.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    increment();
//                }
//            });
//
//            increment.setOnLongClickListener(
//                    new View.OnLongClickListener(){
//                        public boolean onLongClick(View arg0) {
//                            autoIncrement = true;
//                            repeatUpdateHandler.post( new RepetetiveUpdater() );
//                            return false;
//                        }
//                    }
//            );
//
//            increment.setOnTouchListener( new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    if( event.getAction() == MotionEvent.ACTION_UP && autoIncrement ){
//                        autoIncrement = false;
//                    }
//                    return false;
//                }
//            });
//        }
//
//        private void initValueEditText( Context context){
//
//            value = new Integer( 0 );
//
//            valueText = new EditText( context );
//            valueText.setTextSize( textSize );
//            valueText.setTextColor(textColor);
//
//            valueText.setOnKeyListener(new View.OnKeyListener() {
//                public boolean onKey(View v, int arg1, KeyEvent event) {
//                    int backupValue = value;
//                    try {
//                        value = Integer.parseInt( ((EditText)v).getText().toString() );
//                        if( value > maximum ) value = maximum;
//                        if( value < minimum ) value = minimum;
//                    } catch( NumberFormatException nfe ){
//                        value = backupValue;
//                    }
//
//                    //valueText.setText(value.toString());
//
//                    return false;
//                }
//            });
//
//            valueText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if((changedeTasksOfYear || addedTasksOfYear.size() > 0 || destroyedTasksOfYear.size() > 0) && autumn.days.size() > 0 ){
//                        //Log.d("Year", "Year was saved");
//                        saveYear();
//                    }
//
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
////                    if(!charSequence.toString().isEmpty() && yearNumberChanged && Integer.valueOf(charSequence.toString()) > yearNumber){
////                        autumn.addCyclicTasks = true;
////                    }
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    if(!editable.toString().isEmpty()) {
//                        chosenYearNumber = Integer.valueOf(editable.toString());
//                    }
//
//                    //%%C del -
//                    if( curentYearNumber < chosenYearNumber) {
//                        autumn.addCyclicTasks = true;
//                    }
//
//                    if(linLayout != null) {
//                        day = null;
//                        linLayout.removeAllViews();
//                    }
//                    addedTasksOfYear.clear();
//                    destroyedTasksOfYear.clear();
//                    changedeTasksOfYear = false;
//
//                    int width = constraintLayout.getRight() + guideline.getLeft();
//                    int tucherWidth;
//                    int tucherHeight;
//
//                    //Winter
//                    tucherWidth = constraintLayout.getRight();
//                    tucherHeight = width;
//
//                    winter.side = width/2;
//                    winter.x = tucherWidth;
//                    winter.y = tucherHeight;
//
//                    winter.days.clear();
//                    winter.currentDate = null;
//                    //winter.selectedDay = null;
//                    winter.upperLeftCornerX = 0;
//                    winter.firstOccurrence = true;
//                    winter.invalidate();
//
//
//                    //Spring
//                    tucherWidth = width;
//                    tucherHeight = constraintLayout.getBottom()-width*2;
//
//                    spring.side = width/2;
//                    spring.x = tucherWidth - tucherWidth / 2;
//                    spring.y = 0;
//
//                    spring.days.clear();
//                    spring.currentDate = null;
//                    //spring.selectedDay = null;
//                    spring.bottomLeftCornerY = 0;
//                    spring.firstOccurrence = true;
//                    spring.invalidate();
//
//
//                    //Summer
//                    tucherWidth = constraintLayout.getRight();;
//                    tucherHeight = width;
//
//                    summer.side = width/2;
//                    summer.x = 0;
//                    summer.y = tucherHeight - tucherHeight / 2;
//
//                    summer.days.clear();
//                    summer.currentDate = null;
//                    //summer.selectedDay = null;
//                    summer.bottomRightCornerX = 0;
//                    summer.firstOccurrence = true;
//                    summer.invalidate();
//
//
//                    //Autumn
//                    tucherWidth = width;
//                    tucherHeight = constraintLayout.getBottom()-width*2;
//
//                    autumn.side = width/2;
//                    autumn.x = tucherWidth - tucherWidth / 2;
//                    autumn.y = tucherHeight;
//
//                    autumn.days.clear();
//                    autumn.currentDate = null;
//                    //autumn.selectedDay = null;
//                    autumn.upperRightCornerY = 0;
//                    autumn.firstOccurrence = true;
//                    autumn.invalidate();
//
//                    restoreYear(editable.toString());
//
//
//                }
//            });
//
//            valueText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if( hasFocus ){
//                        ((EditText)v).selectAll();
//                    }
//                }
//            });
//            valueText.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL );
//            valueText.setText( value.toString() );
//            valueText.setInputType( InputType.TYPE_CLASS_NUMBER );
//        }
//
//        private void initDecrementButton( Context context){
//            decrement = new Button( context );
//            decrement.setTextSize( textSize );
//            decrement.setText( "-" );
//            decrement.setTextColor(textColor);
//
//
//            decrement.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    decrement();
//                }
//            });
//
//
//            decrement.setOnLongClickListener(
//                    new View.OnLongClickListener(){
//                        public boolean onLongClick(View arg0) {
//                            autoDecrement = true;
//                            repeatUpdateHandler.post( new RepetetiveUpdater() );
//                            return false;
//                        }
//                    }
//            );
//
//            decrement.setOnTouchListener( new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    if( event.getAction() == MotionEvent.ACTION_UP && autoDecrement ){
//                        autoDecrement = false;
//                    }
//                    return false;
//                }
//            });
//        }
//
//        public void increment(){
//            if( value < maximum ){
//                value = value + 1;
//            }else{
//                value = minimum;
//            }
//            String strValue= value.toString();
//            int valueLength = strValue.length();
//
//            if (valueLength < 4){
//                for (int i = 0; i < 4 - valueLength; i++){
//                    strValue = "0" + strValue;
//
//                }
//            }
//            valueText.setText(strValue);
//        }
//
//        public void decrement(){
//            if( value > minimum ){
//                value = value - 1;
//            }else{
//                value = maximum;
//            }
//
//            String strValue= value.toString();
//            int valueLength = strValue.length();
//
//            if (valueLength < 4){
//                for (int i = 0; i < 4 - valueLength; i++){
//                    strValue = "0" + strValue;
//
//                }
//            }
//            valueText.setText(strValue);
//        }
//
//        public int getValue(){
//            return value;
//        }
//
//        public void setValue( int value ){
//            if( value > maximum ) value = maximum;
//            if( value >= minimum ){
//                this.value = value;
//
//                String strValue=  Integer.toString(value);
//                int valueLength = strValue.length();
//
//                if (valueLength < 4){
//                    for (int i = 0; i < 4 - valueLength; i++){
//                        strValue = "0" + strValue;
//
//                    }
//                }
//                valueText.setText(strValue);
//            }
//        }
//
//        public long getRepeatDeley() {
//            return repeatDeley;
//        }
//
//        public int getElementHeight() {
//            return elementHeight;
//        }
//
//        public int getElementWidth() {
//            return elementWidth;
//        }
//
//        public int getMinimum() {
//            return minimum;
//        }
//
//        public int getMaximum() {
//            return maximum;
//        }
//
//        public int getTextSize() {
//            return textSize;
//        }
//
//        public void setRepeatDeley(long repeatDeley) {
//            this.repeatDeley = repeatDeley;
//        }
//
//        public void setElementHeight(int elementHeight) {
//            this.elementHeight = elementHeight;
//        }
//
//        public void setElementWidth(int elementWidth) {
//            this.elementWidth = elementWidth;
//        }
//
//        public void setMinimum(int minimum) {
//            this.minimum = minimum;
//        }
//
//        public void setMaximum(int maximum) {
//            this.maximum = maximum;
//        }
//
//        public void setTextSize(int textSize) {
//            this.textSize = textSize;
//        }
//
//        public void setTextColor(int textColor) {
//            this.textColor = textColor;
//        }
//    }



}





