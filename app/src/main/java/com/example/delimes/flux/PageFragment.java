package com.example.delimes.flux;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.text.PrecomputedTextCompat;
import android.text.InputType;
import android.text.PrecomputedText;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import static com.example.delimes.flux.MainActivity.currDate;
import static com.example.delimes.flux.MainActivity.dayPager;
import static com.example.delimes.flux.MainActivity.gestureDetector;
import static com.example.delimes.flux.MainActivity.setChangedeTasksOfYear;
import static com.example.delimes.flux.MainActivity.task;
import static com.example.delimes.flux.MainActivity.analogClock;
import static com.example.delimes.flux.MainActivity.winter;
import static com.example.delimes.flux.MainActivity.spring;
import static com.example.delimes.flux.MainActivity.summer;
import static com.example.delimes.flux.MainActivity.autumn;
import static com.example.delimes.flux.MainActivity.guideline;
import static com.example.delimes.flux.MainActivity.addedTasksOfYear;
import static com.example.delimes.flux.MainActivity.constraintLayout;
import static com.example.delimes.flux.MainActivity.context;
import static com.example.delimes.flux.MainActivity.changedeTasksOfYear;
import static com.example.delimes.flux.MainActivity.cyclicTasks;
import static com.example.delimes.flux.MainActivity.destroyedTasksOfYear;
import static com.example.delimes.flux.MainActivity.fontHeight;
import static com.example.delimes.flux.MainActivity.numberYearPicker;
import static com.example.delimes.flux.MainActivity.refreshCyclicTasks;
import static com.example.delimes.flux.MainActivity.setReminder;


public class PageFragment extends Fragment {


    int tucherWidth = 100;
    int[] colors = new int[2];
    int[] colors2 = new int[2];
    boolean veryFirstLaunch = true;
    ////boolean firstOccurrence = true;
    //public ConstraintLayout constraintLayout;
    //static ViewPager dayPager;
    public static View viewConstraintLayoutForSchedule;
    public ConstraintLayout сonstraintLayoutForSchedule;
    ConstraintLayout сonstraintLayoutTaskParameters;
    View linearLayout;
    FrameLayout frameLayoutOfScroll;

    ////static Quarter winter, spring, summer, autumn;
    /////static MainActivity.YearStr yearStr;
    static boolean yearRestored = false;

    //ImageView ivLargerImage;

    public ExtensibleTextView dateMonth;
    TextView taskTime;
    TextView taskDuration;


    TextView labelStartOfTask, labelEndOfTask, labelRepeatThrough;
    TextView startOfTask, endOfTask;
    TextView taskCopyTo;
    EditText inDays;
    ExtensibleEditText taskDescription;
    CustomButton buttonAddTask, buttonDeleteTask;
    CheckBox everyYear, everyMonth;
    ScrollView scheduleScroll;
    LayoutInflater ltInflater;
    LinearLayout linLayout;
    public Day day;
    //Day previousDay;
    ////static MainActivity.Task task;
    /*
    ArrayList<MainActivity.Task> addedTasksOfYear = new ArrayList<MainActivity.Task>();
    ArrayList<MainActivity.Task> destroyedTasksOfYear = new ArrayList<MainActivity.Task>();
    */
    static boolean processUpdateSchedule;

    View layoutDayOfWeek;
    TextView  monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    static int chosenYearNumber = 0;
    static int previousChosenYearNumber = 0;

    //static Date currDate;
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


    public static TimeChangedReceiver timeChangedReceiver;
    public static Handler tickHandler;
    private Menu menu_main;

    private final int MY_PERMISSIONS_REQUEST_RECEIVE_BOOT_COMPLETED = 101;
    private AlphaAnimation alphaAnimationClick = new AlphaAnimation(1f, 0.2f);
    private static long dateDoomsday = 95617497600000L;//(4999, 11, 31);




    public static PageFragment newInstance(Day day) {
        Log.d("123", "newInstance: ");
        PageFragment fragment = new PageFragment();
        fragment.day = day;
        /*
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        */



        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        //setRetainInstance(false);
        //setRetainInstance(false);
        //setRetainInstance(false);

        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        colors2[0] = Color.parseColor("#C1FFC1");
        colors2[1] = Color.parseColor("#B4EEB4");

        //gestureDetector = new GestureDetector(context, new CustomGestureListener());

        //////////////////////////////////////
        dateMonth = new ExtensibleTextView(context);
        dateMonth.setId(R.id.dateMonth);
        dateMonth.getPaint().setUnderlineText(true);
        MainActivity.dateMonth = dateMonth;

        ///////сonstraintLayoutForSchedule.addView(dateMonth);

        сonstraintLayoutTaskParameters  = new ConstraintLayout(context);
        сonstraintLayoutTaskParameters.setId(R.id.сonstraintLayoutTaskParameters);
        //сonstraintLayoutTaskParameters.setBackgroundResource(R.drawable.layout_border);
        ///////сonstraintLayoutForSchedule.addView(сonstraintLayoutTaskParameters);


        taskTime = new TextView(context);
        taskTime.setId(R.id.taskTime);
        taskTime.getPaint().setUnderlineText(true);
        ///////сonstraintLayoutTaskParameters.addView(taskTime);

        taskDuration = new TextView(context);
        taskDuration.setId(R.id.taskDuration);
        taskDuration.getPaint().setUnderlineText(true);
        ///////сonstraintLayoutTaskParameters.addView(taskDuration);

        taskDescription = new ExtensibleEditText(context);//Extensible
        taskDescription.setId(R.id.taskDescription);
        taskDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        taskDescription.setSingleLine();
//        taskDescription.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
//        taskDescription.setMaxLines(5);
        taskDescription.setEnabled(false);
        ///////сonstraintLayoutTaskParameters.addView(taskDescription);

        labelRepeatThrough = new TextView(context);
        labelRepeatThrough.setId(R.id.labelRepeatThrough);
        labelRepeatThrough.setText("Повторять через:");
        ///////сonstraintLayoutTaskParameters.addView(labelRepeatThrough);

        labelStartOfTask = new TextView(context);
        labelStartOfTask.setId(R.id.labelStartOfTask);
        labelStartOfTask.setText("Дата начала:");
        ///////сonstraintLayoutTaskParameters.addView(labelStartOfTask);

        startOfTask = new TextView(context);
        startOfTask.setId(R.id.startOfTask);
        startOfTask.getPaint().setUnderlineText(true);
        ///////сonstraintLayoutTaskParameters.addView(startOfTask);

        labelEndOfTask = new TextView(context);
        labelEndOfTask.setId(R.id.labelEndOfTask);
        labelEndOfTask.setText("Дата окончания:");
        ///////сonstraintLayoutTaskParameters.addView(labelEndOfTask);

        endOfTask = new TextView(context);
        endOfTask.setId(R.id.endOfTask);
        endOfTask.getPaint().setUnderlineText(true);
        ///////сonstraintLayoutTaskParameters.addView(endOfTask);


        taskCopyTo = new TextView(context);
        taskCopyTo.setId(R.id.taskCopyTo);
        taskCopyTo.setText("Копировать в дату...");
        taskCopyTo.getPaint().setUnderlineText(true);
        ///////сonstraintLayoutTaskParameters.addView(taskCopyTo);

        buttonAddTask = new CustomButton(context);
        buttonAddTask.setId(R.id.buttonAddTask);
        buttonAddTask.setText( "+" );
        buttonAddTask.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        buttonAddTask.setGravity(Gravity.CENTER);
        buttonAddTask.setTextColor(Color.BLACK);
        ///////сonstraintLayoutForSchedule.addView(buttonAddTask);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (day == null){
                    Toast toast = Toast.makeText(context,
                            "Сначала нужно выбрать день",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    return;
                }
                if (day.numberOfTasksPerDay() >= 99){
                    Toast toast = Toast.makeText(context,
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

                MainActivity.Task newTask = new MainActivity.Task(true, false,"", myCalender.getTimeInMillis(), 0, 0);
                day.tasks.add(newTask);
                addedTasksOfYear.add(newTask);

                MainActivity.task = newTask;
                MainActivity.task.pageFragmentDate = day.date;

                сonstraintLayoutTaskParameters.setVisibility(View.VISIBLE);
                view.post(new Runnable() {
                    @Override
                    public void run() {

                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                        params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                        linearLayout.setLayoutParams(params);

                    }

                });


                day.dayClosed = true;
                for (MainActivity.Task task : day.tasks) {
                    if(!task.isDone && task.isValid){
                        day.dayClosed = false;
                    }
                }

                updateSchedule(day);


                //setTestReminder();
                taskDescription.setEnabled(true);
                taskDescription.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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

        buttonDeleteTask = new CustomButton(context);
        buttonDeleteTask.setId(R.id.buttonDeleteTask);
        buttonDeleteTask.setText( "_" );
        buttonDeleteTask.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        buttonDeleteTask.setGravity(Gravity.CENTER);
        buttonDeleteTask.setTextColor(Color.BLACK);
        ///////сonstraintLayoutForSchedule.addView(buttonDeleteTask);
        buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {

                    day.tasks.remove(task);
                    /*
                    if (previousDay != null){
                        previousDay.tasks.remove(task);
                    }
                    */
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
                            Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
                            while (iter.hasNext()) {
                                MainActivity.Task t = iter.next();

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
                    for (MainActivity.Task task : day.tasks) {
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

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(taskDescription.getWindowToken(), 0);

            }
        });


        ltInflater = getLayoutInflater();
        linearLayout = ltInflater.inflate(R.layout.layout, сonstraintLayoutForSchedule, false);
        ///////сonstraintLayoutForSchedule.addView(linearLayout);
        ///////linLayout = (LinearLayout) linearLayout.findViewById(R.id.linLayout);

        //ivLargerImage = new ImageView(context);
        //ivLargerImage.setId(R.id.ivLargerImage);
        ///////сonstraintLayoutForSchedule.addView(ivLargerImage, сonstraintLayoutForSchedule.getChildCount());



        layoutDayOfWeek = ltInflater.inflate(R.layout.layout_day_of_week, сonstraintLayoutForSchedule, false);
        layoutDayOfWeek.setId(R.id.layoutDayOfWeek);
        ///////сonstraintLayoutTaskParameters.addView(layoutDayOfWeek);

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

                    if (task.monday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    refreshCyclicParameters();
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.tuesday = !task.tuesday;

                    if (task.tuesday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    refreshCyclicParameters();
                }
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.wednesday = !task.wednesday;

                    if (task.wednesday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    refreshCyclicParameters();
                }
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.thursday = !task.thursday;

                    if (task.thursday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    refreshCyclicParameters();
                }
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.friday = !task.friday;

                    if (task.friday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }

                    refreshCyclicParameters();
                }
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(task != null) {
                    task.saturday = !task.saturday;

                    if (task.saturday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        saturday.setTextColor(getResources().getColor(android.R.color.background_light));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                        saturday.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }

                    refreshCyclicParameters();
                }
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (task != null) {
                    task.sunday = !task.sunday;

                    if (task.sunday) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        sunday.setTextColor(getResources().getColor(android.R.color.background_light));
                    } else {
                        view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                        sunday.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }

                    refreshCyclicParameters();
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
                    MainActivity.Task cyclicTaskCopyContent = new MainActivity.Task(true, false, "", 0, 0, 0);
                    //while (cyclicTasks.remove(task));
                    Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        MainActivity.Task t = iter.next();

                        if (t.equals(task)) {
                            //cyclicTasks.remove(task);
                            cyclicTaskCopyContent = new MainActivity.Task(true, false, t.content, 0, 0, 0);
                            iter.remove();
                        }
                    }
                    while (cyclicTasks.remove(task));
                    task.removeFromAM = true;
                    setReminder(context, task, day.date);

                    task.isCyclic = false;

                    if (!task.content.equals(taskDescription.getText().toString())) {
                        setChangedeTasksOfYear(true);
                    }
                    //установить контент
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
                        //MainActivity.Task taskCopy = new MainActivity.Task(true, false,"", 0, 0, 0);
                        //task.duplicate(taskCopy);
                        //cyclicTasks.add(taskCopy);
                    }else if (task.finishTime == dateDoomsday){
                        task.finishTime = task.startTime;
                    }

                    updateSchedule(day);
                    if (task.isCyclic) {
                        MainActivity.Task taskCopy = new MainActivity.Task(true, false,"", 0, 0, 0);
                        task.duplicate(taskCopy);


                        calendar.clear();
                        calendar.setTimeInMillis(task.startTime);
                        final Calendar myCalender = Calendar.getInstance();
                        myCalender.clear();
                        myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                        long dateTaskStartTime = myCalender.getTimeInMillis();


                        if (dateTaskStartTime == day.date.getTime()) {
                            cyclicTasks.add(taskCopy);
                            //напоминание установится в refreshCyclicTasks(task);
                            refreshCyclicTasks(taskCopy);
                        }else{
                            taskCopy.content = cyclicTaskCopyContent.content;
                            cyclicTasks.add(taskCopy);
                            //set new remind
                            calendar.clear();
                            calendar.setTimeInMillis(task.startTime);

                            myCalender.setTimeInMillis(day.date.getTime());
                            myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                            myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                            if (myCalender.getTimeInMillis() > System.currentTimeMillis()) {
                                setReminder(context, task, day.date);
                                Log.d("123", "onEditorAction: "+ task.extra);
                            }
                        }

                    }else{
                        //set new remind
                        calendar.clear();
                        calendar.setTimeInMillis(task.startTime);

                        final Calendar myCalender = Calendar.getInstance();
                        myCalender.setTimeInMillis(day.date.getTime());
                        myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                        myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                        if (myCalender.getTimeInMillis() > System.currentTimeMillis()) {
                            setReminder(context, task, day.date);
                            Log.d("123", "onEditorAction: "+ task.extra);
                        }
                    }

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


        everyYear = new CheckBox(context);
        everyYear.setId(R.id.everyYear);
        everyYear.setText("Год");

        everyYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!processUpdateSchedule && task != null && day != null && day.date.equals(task.pageFragmentDate)) {

                    task.everyYear = b;

                    refreshCyclicParameters();
                }
            }
        });
        ///////сonstraintLayoutTaskParameters.addView(everyYear);

        everyMonth = new CheckBox(context);
        everyMonth.setId(R.id.everyMonth);
        everyMonth.setText("Месяц");
        everyMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!processUpdateSchedule && task != null && day != null && day.date.equals(task.pageFragmentDate)) {

                    task.everyMonth = b;

                    refreshCyclicParameters();
                }
            }
        });

        ///////сonstraintLayoutTaskParameters.addView(everyMonth);

        inDays = new EditText(context);
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
                    refreshCyclicParameters();
                }



                //установить контент
                return false;
            }

        });
        ///////сonstraintLayoutTaskParameters.addView(inDays);


        /////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.d("123", "onCreateView: ");


        //
        View view = inflater.inflate(R.layout.constraint_layout_for_schedule, container, false);
        сonstraintLayoutForSchedule = view.findViewById(R.id.сonstraintLayoutForSchedule);

        сonstraintLayoutForSchedule.removeAllViews();
        сonstraintLayoutForSchedule.addView(dateMonth);
        сonstraintLayoutForSchedule.addView(сonstraintLayoutTaskParameters);
        сonstraintLayoutForSchedule.addView(buttonAddTask);
        сonstraintLayoutForSchedule.addView(buttonDeleteTask);
        сonstraintLayoutForSchedule.addView(linearLayout);
        //сonstraintLayoutForSchedule.addView(ivLargerImage, сonstraintLayoutForSchedule.getChildCount());

        сonstraintLayoutTaskParameters.removeAllViews();
        сonstraintLayoutTaskParameters.addView(taskTime);
        сonstraintLayoutTaskParameters.addView(taskDuration);
        сonstraintLayoutTaskParameters.addView(taskDescription);
        сonstraintLayoutTaskParameters.addView(labelRepeatThrough);
        сonstraintLayoutTaskParameters.addView(labelStartOfTask);
        сonstraintLayoutTaskParameters.addView(startOfTask);
        сonstraintLayoutTaskParameters.addView(labelEndOfTask);
        сonstraintLayoutTaskParameters.addView(endOfTask);
        сonstraintLayoutTaskParameters.addView(taskCopyTo);
        сonstraintLayoutTaskParameters.addView(layoutDayOfWeek);
        сonstraintLayoutTaskParameters.addView(everyYear);
        сonstraintLayoutTaskParameters.addView(everyMonth);
        сonstraintLayoutTaskParameters.addView(inDays);

        linLayout = (LinearLayout) linearLayout.findViewById(R.id.linLayout);

        ScrollView scrollView = linearLayout.findViewById(R.id.scroll);
        //scrollView.setBackgroundColor(Color.GRAY);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.d("1234", "onTouch: ");
                return gestureDetector.onTouchEvent(motionEvent);
            }

        });


        ConstraintLayout.LayoutParams params;
        final int width = constraintLayout.getRight() + guideline.getLeft();
        int tucherWidth;
        int tucherHeight;

        //dateMonth
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToLeft = R.id.сonstraintLayoutForSchedule;
        params.topToTop = R.id.сonstraintLayoutForSchedule;
        params.rightToRight = R.id.сonstraintLayoutForSchedule;

        params.height = numberYearPicker.getHeight();
        dateMonth.setGravity( Gravity.CENTER_VERTICAL);

        dateMonth.setTextSize( TypedValue.COMPLEX_UNIT_SP, fontHeight );
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


        //labelRepeatThrough
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
        params.topToBottom = R.id.taskDuration;
        labelRepeatThrough.setLayoutParams(params);

        //everyYear
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
        params.topToBottom = R.id.labelRepeatThrough;
        everyYear.setTextSize( TypedValue.COMPLEX_UNIT_SP, fontHeight );
        everyYear.setLayoutParams(params);

        //everyMonth
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToRight = R.id.everyYear;
        params.topToBottom = R.id.labelRepeatThrough;
        everyMonth.setTextSize( TypedValue.COMPLEX_UNIT_SP, fontHeight );
        everyMonth.setLayoutParams(params);

        //inDays
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
        params.topToBottom = R.id.everyMonth;
        inDays.setTextSize( TypedValue.COMPLEX_UNIT_SP, fontHeight );
        inDays.setLayoutParams(params);

        //layoutDayOfWeek
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
        params.topToBottom = R.id.inDays;
        layoutDayOfWeek.setLayoutParams(params);


        //labelStartOfTask
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToLeft = R.id.сonstraintLayoutTaskParameters;
        params.topToBottom = R.id.layoutDayOfWeek;
        params.topMargin = 10;
        //params.rightToLeft = R.id.startOfTask;
        labelStartOfTask.setLayoutParams(params);

        //startOfTask
        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftToRight = R.id.labelStartOfTask;
        params.topToBottom = R.id.layoutDayOfWeek;
        //params.rightToRight = R.id.сonstraintLayoutForSchedule;
        params.topMargin = 10;
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
        params.topMargin = 10;
        taskCopyTo.setLayoutParams(params);


        //buttonAddTask
        params = new ConstraintLayout.LayoutParams((int)(width/1.5f),(int)(width/1.5f));
        params.rightToRight = R.id.сonstraintLayoutForSchedule;
        params.topToBottom= R.id.сonstraintLayoutTaskParameters;

        buttonAddTask.setTextSize( TypedValue.COMPLEX_UNIT_SP, fontHeight );
        buttonAddTask.setLayoutParams(params);

        //buttonDeleteTask
        /*
        float dip = (width/1.5f);
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        params = new ConstraintLayout.LayoutParams((int)px,(int)px);
        */
        params = new ConstraintLayout.LayoutParams((int)(width/1.5f),(int)(width/1.5f));
        params.rightToLeft = R.id.buttonAddTask;
        params.topToBottom = R.id.сonstraintLayoutTaskParameters;

        buttonDeleteTask.setTextSize( TypedValue.COMPLEX_UNIT_SP, fontHeight ); // (width/1.5f/5f)
        buttonDeleteTask.setLayoutParams(params);
        buttonDeleteTask.setGravity(Gravity.CENTER_VERTICAL);
        ////////////


        сonstraintLayoutForSchedule.post(new Runnable() {
            @Override
            public void run() {

                //linearLayout
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.leftToLeft = R.id.сonstraintLayoutForSchedule;
                params.rightToRight = R.id.сonstraintLayoutForSchedule;
                params.topToBottom = R.id.buttonAddTask;
                params.bottomToBottom = R.id.сonstraintLayoutForSchedule;

                params.width = сonstraintLayoutForSchedule.getWidth();
                params.height = сonstraintLayoutForSchedule.getHeight() - buttonAddTask.getBottom();
                //linearLayout.setBackgroundColor(Color.RED);
                linearLayout.setLayoutParams(params);

            }
        });


        if (day != null) {
           updateSchedule(day);
        }
        //tvPage.setBackgroundColor(Color.BLUE);

        /*
        view.post(new Runnable() {
            @Override
            public void run() {
                dateMonth.startAnimation(dateMonth.alphaAnimationFadeIn);
            }
        });
        */

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
                            setChangedeTasksOfYear(true);
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
                            MainActivity.Task taskCopy = new MainActivity.Task(true, false,"", 0, 0, 0);
                            task.duplicate(taskCopy);
                            cyclicTasks.add(taskCopy);

                            calendar.clear();
                            calendar.setTimeInMillis(task.startTime);
                            myCalender.clear();
                            myCalender.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                            myCalender.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                            myCalender.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                            long dateTaskStartTime = myCalender.getTimeInMillis();

                            if (dateTaskStartTime == day.date.getTime()) {
                                refreshCyclicTasks(task);
                            }
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
                            setChangedeTasksOfYear(true);
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
                        Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
                        while (iter.hasNext()) {
                            MainActivity.Task t = iter.next();

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
                            MainActivity.Task taskCopy = new MainActivity.Task(true, false, "", 0, 0, 0);
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
                        calendar.clear();
                        calendar.setTimeInMillis(task.startTime);

                        myCalender.setTimeInMillis(day.date.getTime());
                        myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                        myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                        if (myCalender.getTimeInMillis() > System.currentTimeMillis()) {
                            setReminder(context, task, day.date);
                            Log.d("123", "onTimeSet: "+ task.extra);
                        }

                        /*taskTime.setText(((""+ calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY))+
                                ":"+ ((""+ calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE)));
*/
                    }

                    updateSchedule(day);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, duration? 0:hour, duration? 0:minute, true);

        timePickerDialog.setTitle(title);
        //timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, myDateListener, year, month, dayOfMonth);

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
                    setChangedeTasksOfYear(true);
                    day.tasks.remove(task);
                    /*
                    if (previousDay != null){
                        task.removeFromAM = true;
                        setReminder(context, task, previousDay.date);
                        previousDay.tasks.remove(task);
                        previousDay.dayClosed = true;
                        for (MainActivity.Task task : previousDay.tasks) {
                            if(!task.isDone && task.isValid){
                                previousDay.dayClosed = false;
                            }
                        }
                    }
                    */
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
                for (MainActivity.Task task : day.tasks) {
                    if(!task.isDone && task.isValid){
                        day.dayClosed = false;
                    }
                }

                if(task.finishTime < task.startTime){
                    task.finishTime = task.startTime;
                }

                updateSchedule(day);


                if (task.isCyclic) {
                    Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        MainActivity.Task t = iter.next();

                        if (t.equals(task)) {
                            task.duplicate(t);
                        }
                    }
                }else {
                    calendar.clear();
                    calendar.set(year, month, dayOfMonth);
                    task.transferDate = new Date(calendar.getTimeInMillis());
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

                //set new remind
                calendar.clear();
                calendar.setTimeInMillis(task.startTime);

                myCalender.setTimeInMillis(dayOfYear.date.getTime());
                myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                if (myCalender.getTimeInMillis() > System.currentTimeMillis()) {
                    task.shown = false;
                    setReminder(context, task, dayOfYear.date);
                }

                // Доработать не подсвечиваются дни перенесенных незавершенныз задач
                dayOfYear.dayClosed = true;
                for (MainActivity.Task task : dayOfYear.tasks) {
                    if(!task.isDone && task.isValid){
                        dayOfYear.dayClosed = false;
                    }
                }
                //previousDay = dayOfYear;

                setChangedeTasksOfYear(true);
                //saveYearToFile();


                winter.invalidate();
                spring.invalidate();
                summer.invalidate();
                autumn.invalidate();

            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the start date for the task");
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
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
                    setChangedeTasksOfYear(true);
                }
                if(calendar.getTimeInMillis() < task.startTime){
                    task.finishTime = task.startTime;
                }else if(task.isCyclic){
                    task.finishTime = calendar.getTimeInMillis();
                    myCalender.setTimeInMillis(task.startTime);
                    Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
                    while (iter.hasNext()) {
                        MainActivity.Task t = iter.next();

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
                Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
                while (iter.hasNext()) {
                    MainActivity.Task t = iter.next();

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


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the end date for the task");
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

                MainActivity.Task taskCopy = new MainActivity.Task(true, false, task.content, calendar.getTimeInMillis(), task.durationHours, task.durationMinutes);
                task = taskCopy;

                calendar.clear();
                calendar.set(year, month, dayOfMonth);
                task.transferDate = new Date(calendar.getTimeInMillis());
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

                //set new remind
                calendar.clear();
                calendar.setTimeInMillis(task.startTime);

                myCalender.setTimeInMillis(dayOfYear.date.getTime());
                myCalender.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                myCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                if (myCalender.getTimeInMillis() > System.currentTimeMillis()) {
                    setReminder(context, task, dayOfYear.date);
                }
                // Доработать не подсвечиваются дни перенесенных незавершенныз задач
                dayOfYear.dayClosed = true;
                for (MainActivity.Task task : dayOfYear.tasks) {
                    if(!task.isDone && task.isValid){
                        dayOfYear.dayClosed = false;
                    }
                }
                //previousDay = dayOfYear;

                setChangedeTasksOfYear(true);
                //saveYearToFile();


                winter.invalidate();
                spring.invalidate();
                summer.invalidate();
                autumn.invalidate();

            }
        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, myDateListener, year, month, dayOfMonth);

        datePickerDialog.setTitle("Select the date on which you want to copy the task");
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    public void updateSchedule(final Day selectedDay){

        if(selectedDay == null || taskTime == null) {
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

        dateMonth.setText(strDateMonth);
        if (day.date.equals(currDate)) {
            dateMonth.setTypeface(null, Typeface.BOLD);
        } else {
            dateMonth.setTypeface(null, Typeface.NORMAL);
        }


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
            //taskDescription.showIvLargerImage = false;


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

        Collections.sort(selectedDay.tasks,  new MainActivity.TaskComparator());

        linLayout.removeAllViews();
        for (int i = 0; i < selectedDay.tasks.size(); i++) {
            //Log.d("myLogs", "i = " + i);
            final MainActivity.Task task = selectedDay.tasks.get(i);
            task.pageFragmentDate = day.date;

            final View item = ltInflater.inflate(R.layout.item, linLayout, false);
            item.setSaveEnabled(false);
            item.setSaveFromParentEnabled(false);
            //item.setBackgroundColor(Color.BLUE);

            CheckBox checkBox = (CheckBox) item.findViewById(R.id.checkBox);
            checkBox.setChecked(task.isValid);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    setChangedeTasksOfYear(true);
                    task.isValid = b;
                    if(b){
                        setReminder(context, task, day.date);
                    }else{
                        task.removeFromAM = true;
                        setReminder(context, task, day.date);
                    }
                    //%%C del - setReminder(task);

                    day.dayClosed = true;
                    for (MainActivity.Task task : day.tasks) {
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

                        setChangedeTasksOfYear(true);
                        task.isDone = b;

                        if (b) {
                            task.removeFromAM = true;
                            setReminder(context, task, day.date);
                            //%%C del - setReminder(task);
                        }

                        day.dayClosed = true;
                        for (MainActivity.Task task : day.tasks) {
                            if (!task.isDone && task.isValid) {
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


                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(taskDescription.getWindowToken(), 0);

                    }else {
                        MainActivity.task = task;
                        MainActivity.task.pageFragmentDate = day.date;
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

    public void refreshCyclicParameters(){

        while (cyclicTasks.remove(task));
        Iterator<MainActivity.Task> iter = cyclicTasks.iterator();
        while (iter.hasNext()) {
            MainActivity.Task t = iter.next();

            if (t.equals(task)) {
                //cyclicTasks.remove(task);
                iter.remove();
            }
        }
        task.isCyclic = false;

        if (task.monday         ||
                task.tuesday    ||
                task.wednesday  ||
                task.thursday   ||
                task.friday     ||
                task.saturday   ||
                task.sunday     ||
                task.everyYear  ||
                task.everyMonth ||
                task.inDays > 0) {

            if (task.finishTime == task.startTime){
                task.finishTime = dateDoomsday;
            }
            task.isCyclic = true;
            MainActivity.Task taskCopy = new MainActivity.Task(true, false,"", 0, 0, 0);
            task.duplicate(taskCopy);
            cyclicTasks.add(taskCopy);
        }else if (task.finishTime == dateDoomsday){
            task.finishTime = task.startTime;
        }

        setChangedeTasksOfYear(true);
        refreshCyclicTasks(task);
        updateSchedule(day);

        winter.invalidate();
        spring.invalidate();
        summer.invalidate();
        autumn.invalidate();
        //

    }


}