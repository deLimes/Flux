package com.example.delimes.flux;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;


import static com.example.delimes.flux.Quarter.daysOfYear;


public class PagesAdapter extends FragmentStatePagerAdapter {

    public PagesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return PageFragment.newInstance(daysOfYear.get(position));
    }
/*
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PageFragment pageFragment = ((PageFragment)getItem(position));
        pageFragment.updateSchedule(pageFragment.day);

        return super.instantiateItem(container, position);
    }
*/
    @Override
    public int getCount() {

        return daysOfYear.size();
    }
}
