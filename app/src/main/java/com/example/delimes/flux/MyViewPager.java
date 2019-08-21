package com.example.delimes.flux;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Date;

import static com.example.delimes.flux.MainActivity.autumn;
import static com.example.delimes.flux.MainActivity.spring;
import static com.example.delimes.flux.MainActivity.summer;
import static com.example.delimes.flux.MainActivity.winter;

public class MyViewPager extends ViewPager implements ViewPager.OnPageChangeListener{

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {

        Log.d("1234", "onPageSelected position: "+position);
        Day dayOfYear = new Day(new Date(), 0, 0, 0, 0);

        if (winter.days.size() >= position + 1){

            dayOfYear = winter.days.get(position);
            winter.selectedDay = dayOfYear;
            spring.selectedDay = null;
            summer.selectedDay = null;
            autumn.selectedDay = null;




        }else if(winter.days.size()
                + spring.days.size() >= position + 1){

            dayOfYear = spring.days.get(position
                    - winter.days.size());

            winter.selectedDay = null;
            spring.selectedDay = dayOfYear;
            summer.selectedDay = null;
            autumn.selectedDay = null;



        }else if(winter.days.size()
                + spring.days.size()
                + summer.days.size() >= position + 1){

            dayOfYear = summer.days.get(position
                    - winter.days.size()
                    - spring.days.size());

            winter.selectedDay = null;
            spring.selectedDay = null;
            summer.selectedDay = dayOfYear;
            autumn.selectedDay = null;



        }else if(winter.days.size()
                + spring.days.size()
                + summer.days.size()
                + autumn.days.size() >= position + 1){

            dayOfYear = autumn.days.get(position
                    - winter.days.size()
                    - spring.days.size()
                    - summer.days.size());

            winter.selectedDay = null;
            spring.selectedDay = null;
            summer.selectedDay = null;
            autumn.selectedDay = dayOfYear;


        }

        //MainActivity.setDay(dayOfYear, false);

        winter.invalidate();
        spring.invalidate();
        summer.invalidate();
        autumn.invalidate();


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
