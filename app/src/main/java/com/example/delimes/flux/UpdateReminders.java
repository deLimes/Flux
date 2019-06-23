package com.example.delimes.flux;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

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

        updateReminders();

        stopService(new Intent(this, UpdateReminders.class));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void updateReminders(){

        SharedPreferences preference = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        Log.d("myLogs", "updateReminders: year"+year);

        String json = preference.getString(year, "");
        if (json.isEmpty()) {
            return;
        }

        MainActivity.YearStr yearStr = new Gson().fromJson(json, MainActivity.YearStr.class);
        //MainActivity.context = getApplicationContext();
        MainActivity.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Log.v("123", "updateReminders context: "+ this);
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
                MainActivity.setReminder(task, winter.days.get(i).date);
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
                MainActivity.setReminder(task, spring.days.get(i).date);
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
                MainActivity.setReminder(task, summer.days.get(i).date);
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
                MainActivity.setReminder(task, autumn.days.get(i).date);
            }
        }

        Log.d("123", "updateReminders: OK!");

        //Toast.makeText(context, "OK!", Toast.LENGTH_SHORT).show();

    }
}
