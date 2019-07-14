package com.example.delimes.flux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ConstraintLayoutForSchedule extends ConstraintLayout {

    private static AnalogClock analogClock;
    private ConstraintLayout.LayoutParams params;


    public ConstraintLayoutForSchedule(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();


        params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.width = analogClock.side * 5;
        params.height = params.width;
        //params.leftToLeft = R.id.сonstraintLayoutForSchedule;

        //params.topToBottom = R.id.buttonAddTask;
        params.topToTop = R.id.сonstraintLayoutForSchedule;
        params.leftToLeft =  R.id.сonstraintLayoutForSchedule;
        params.rightToRight = R.id.сonstraintLayoutForSchedule;
        params.bottomToBottom = R.id.сonstraintLayoutForSchedule;

        analogClock.setLayoutParams(params);


        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(loadBitmapFromView((View)analogClock), 0f, 0f, null);
    }

    public void setAnalogClock(AnalogClock analogClock) {
        this.analogClock = analogClock;
    }

    public static Bitmap loadBitmapFromView(View v) {
        //v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        Bitmap b = Bitmap.createBitmap( analogClock.side * 5, analogClock.side * 5, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        //v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}
