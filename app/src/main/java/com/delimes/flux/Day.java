package com.delimes.flux;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 04.05.2018.
 */

public class Day
{
    /**Позиция*/
    public float left;
    public float top;
    public float right;
    public float bottom;
    public ArrayList<MainActivity.Task> tasks = new ArrayList<MainActivity.Task>();
    public boolean dayClosed;

    /**Дата*/
    Date date;

    /**Ширина*/
    public int width;

    /**Ввыоста*/
    public  int height;

    public Day(Date date, float left, float top, float right, float bottom) {
        this.date = date;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.dayClosed = true;

//            tasks.add(new Task(true,"Задача 1", date.getTime(), 1, 6));
//            tasks.add(new Task(false,"Задача 2", date.getTime(), 2, 15));
//            tasks.add(new Task(true,"Задача 3", date.getTime(), 17, 1));

           /* Calendar rightNow = Calendar.getInstance();
            Date d = new Date();
            System.out.println(d);
            rightNow.setTime(d);
            rightNow.add(Calendar.HOUR, yourHours);
            d = rightNow.getTime();
            System.out.println(d);
*/

    }

    public int numberOfTasksPerDay(){
        int numberOfTasksPerDay = 0;

        for (MainActivity.Task task : tasks) {
            numberOfTasksPerDay++;
        }
        return numberOfTasksPerDay;
    };
}

