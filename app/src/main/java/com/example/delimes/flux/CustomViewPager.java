package com.example.delimes.flux;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import static com.example.delimes.flux.MainActivity.returnToCurrentDate;


public class CustomViewPager extends ViewPager {

    private Context context;
    private GestureDetector gestureDetector;

    public CustomViewPager(Context context) {
        super(context);

        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        this.context = context;
        this.gestureDetector = new GestureDetector(context, new CustomGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        gestureDetector.onTouchEvent(ev);

        return super.onTouchEvent(ev);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(null);
    }

    private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            returnToCurrentDate();

            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);

            returnToCurrentDate();
        }
    }
}
