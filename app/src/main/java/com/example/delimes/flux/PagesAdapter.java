package com.example.delimes.flux;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.delimes.flux.MainActivity.dayPager;
import static com.example.delimes.flux.MainActivity.yearNumberChangedForOnPageChangeListener;
import static com.example.delimes.flux.Quarter.daysOfYear;


 class PagesAdapter extends FragmentStatePagerAdapter {

    public PagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return  PageFragment.newInstance(daysOfYear.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        /*
        PageFragment pageFragment = ((PageFragment)getItem(position));
        pageFragment.updateSchedule(pageFragment.day);
        */
        //fragmentList.add(position, PageFragment.newInstance(daysOfYear.get(position)));

        return super.instantiateItem(container, position);
    }

     @Override
     public int getItemPosition(Object object) {

         return POSITION_NONE;
        /*
         if (!yearNumberChangedForOnPageChangeListener) {
             return super.getItemPosition(object);
         }else{
             return POSITION_NONE;
         }
         */
     }


    @Override
    public int getCount() {

        return daysOfYear.size();
    }
}
