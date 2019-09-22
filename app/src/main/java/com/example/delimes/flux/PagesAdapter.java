package com.example.delimes.flux;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static com.example.delimes.flux.MainActivity.currDate;
import static com.example.delimes.flux.MainActivity.dateMonth;
import static com.example.delimes.flux.MainActivity.day;
import static com.example.delimes.flux.MainActivity.dayPager;
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
     public int getItemPosition(Object object) {

         //return super.getItemPosition(object);//так не работает notifyDataSetChanged()
         return POSITION_NONE;
     }


     @Override
     public void notifyDataSetChanged() {
         dayPager.startAnimation(dayPager.alphaAnimationFadeIn);
         dayPager.removeAllViews();//для того, чтобы работал notifyDataSetChanged()
         super.notifyDataSetChanged();

         /*
         dayPager.post(new Runnable() {
             @Override
             public void run() {
                 if (day != null) {
                     if (day.date.equals(currDate)) {
                         dateMonth.setTypeface(null, Typeface.BOLD);
                     } else {
                         dateMonth.setTypeface(null, Typeface.NORMAL);
                     }
                 }
             }
         });
         */



     }

     @Override
    public int getCount() {

        return daysOfYear.size();
    }
}
