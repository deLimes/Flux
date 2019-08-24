package com.example.delimes.flux;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
         //dayPager.removeAllViews();//для того, чтобы работал notifyDataSetChanged()
         super.notifyDataSetChanged();
     }

     @Override
    public int getCount() {

        return daysOfYear.size();
    }
}
