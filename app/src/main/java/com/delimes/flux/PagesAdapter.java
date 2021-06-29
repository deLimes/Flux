package com.delimes.flux;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import static com.delimes.flux.MainActivity.currDate;
import static com.delimes.flux.MainActivity.dateMonth;
import static com.delimes.flux.MainActivity.day;
import static com.delimes.flux.MainActivity.dayPager;
import static com.delimes.flux.Quarter.daysOfYear;


 class PagesAdapter extends FragmentStatePagerAdapter {


     public PagesAdapter(@NonNull FragmentManager fm, int behavior) {
         super(fm, behavior);
     }

     public boolean isCurrentDate(int position) {

         return  daysOfYear.get(position).date.equals(currDate);
     }

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
         //////////////dayPager.removeAllViews();//для того, чтобы работал notifyDataSetChanged()
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

//     @NonNull
//     @Override
//     public Object instantiateItem(@NonNull ViewGroup container, int position) {
//         return super.instantiateItem(container, position);
//         //return new Object();
//     }

//     @Override
//     public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//         super.destroyItem(container, position, object);
//     }

//     @Override
//     public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//         return false;
//     }
 }
