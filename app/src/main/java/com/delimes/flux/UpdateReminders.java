package com.delimes.flux;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UpdateReminders extends Service {

    public MainActivity mainActivity;

    public UpdateReminders(Context context) {
        this.mainActivity = (MainActivity)context;
    }

    public UpdateReminders() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateReminders(intent);

        //если остановить службу. пропадут выведеные сообщения
        //stopService(new Intent(this, UpdateReminders.class));

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateReminders(Intent intent){

//        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        String strYear = String.valueOf(calendar.get(Calendar.YEAR));
        Log.d("myLogs", "updateReminders: year"+strYear);

        ///////////////////////////////////////
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
        File sdFile = new File(sdPath, "savedTasks"+strYear);
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
        /////////////////////////////////////////

        if (json.isEmpty()) {
            return;
        }

        MainActivity.YearStr yearStr = new Gson().fromJson(json, MainActivity.YearStr.class);
        //MainActivity.context = getApplicationContext();
        MainActivity.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Log.v("123", "updateReminders context: "+ this);

        Quarter winter = new Quarter(this, 1, true);
        Quarter spring = new Quarter(this, 2, true);
        Quarter summer = new Quarter(this, 3, true);
        Quarter autumn = new Quarter(this, 4, true);

        //reschedule tasks
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array;

        Day dayOfYear = new Day(new Date(), 0, 0, 0, 0);
        int numberDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        if (winter.days.size() >= numberDayOfYear){

            dayOfYear = winter.days.get(numberDayOfYear - 1);

            array = parser.parse(yearStr.daysWinter).getAsJsonArray();

            dayOfYear.tasks = (gson.fromJson(array.get(numberDayOfYear - 1), Day.class)).tasks;

        }else if(winter.days.size()
                + spring.days.size() >= numberDayOfYear){

            dayOfYear = spring.days.get(numberDayOfYear - 1 - winter.days.size());

            array = parser.parse(yearStr.daysSpring).getAsJsonArray();

            dayOfYear.tasks = (gson.fromJson(array.get(numberDayOfYear - 1 - winter.days.size()), Day.class)).tasks;

        }else if(winter.days.size()
                + spring.days.size()
                + summer.days.size() >= numberDayOfYear){

            dayOfYear = summer.days.get(numberDayOfYear - 1
                    - winter.days.size()
                    - spring.days.size());

            array = parser.parse(yearStr.daysSummer).getAsJsonArray();

            dayOfYear.tasks = (gson.fromJson(array.get(numberDayOfYear - 1
                    - winter.days.size()
                    - spring.days.size()), Day.class)).tasks;


        }else if(winter.days.size()
                + spring.days.size()
                + summer.days.size()
                + autumn.days.size() >= numberDayOfYear){

            dayOfYear = autumn.days.get(numberDayOfYear - 1
                    - winter.days.size()
                    - spring.days.size()
                    - summer.days.size());

            array = parser.parse(yearStr.daysSummer).getAsJsonArray();

            dayOfYear.tasks = (gson.fromJson(array.get(numberDayOfYear - 1
                    - winter.days.size()
                    - spring.days.size()
                    - summer.days.size()), Day.class)).tasks;

        }

        for (MainActivity.Task task : dayOfYear.tasks) {
            MainActivity.setReminder(this, task, dayOfYear.date);
        }

        stopService(intent);
        /*
        //из-за медленной работы функции refreshCyclicTasks()
        //установка напоминаний устанавливаеться только на каждый день а не на целый год сразу
        //Зима
        Quarter winter = new Quarter(this, 1, true);
        //winter.context = context;
        //winter.fillInDays(Integer.valueOf(year));

        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        JsonArray array = parser.parse(yearStr.daysWinter).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            winter.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

            for (MainActivity.Task task : winter.days.get(i).tasks) {
                MainActivity.setReminder(this, task, winter.days.get(i).date);
            }
        }

        //Весна
        Quarter spring = new Quarter(this, 2, true);
        //spring.context = context;
        //spring.fillInDays(Integer.valueOf(year));

        array = parser.parse(yearStr.daysSpring).getAsJsonArray();
        //Log.d("test", "Reminders: " + new Date(yearStr.daysSpring.get(62).tasks.get(0).startTime));
        for (int i = 0; i < array.size(); i++) {
            spring.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

            for (MainActivity.Task task : spring.days.get(i).tasks) {
                Log.d("myLogs", "updateReminders test1: "+ new Date(task.startTime));
                Log.d("myLogs", "updateReminders2 test2: "+ spring.days.get(i).date);
                MainActivity.setReminder(this, task, spring.days.get(i).date);
            }
        }

        //Лето
        Quarter summer = new Quarter(this, 3, true);
        //summer.context = context;
        //summer.fillInDays(Integer.valueOf(year));

        array = parser.parse(yearStr.daysSummer).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            summer.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

            for (MainActivity.Task task : summer.days.get(i).tasks) {
                MainActivity.setReminder(this, task, summer.days.get(i).date);
            }
        }

        //Осень
        Quarter autumn = new Quarter(this, 4, true);
        //autumn.context = context;
        //autumn.fillInDays(Integer.valueOf(year));

        array = parser.parse(yearStr.daysAutumn).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            autumn.days.get(i).tasks = (gson.fromJson(array.get(i), Day.class)).tasks;

            for (MainActivity.Task task : autumn.days.get(i).tasks) {
                MainActivity.setReminder(this, task, autumn.days.get(i).date);
            }
        }

        */
        Log.d("123", "updateReminders: OK!");

        //Toast.makeText(context, "OK!", Toast.LENGTH_SHORT).show();

    }
}
