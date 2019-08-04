package com.example.delimes.flux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class ExtensibleEditText extends android.support.v7.widget.AppCompatEditText {

    public Bitmap drawingCache;
    private boolean dragCursor;
    int xCoord = 0;
    int yCoord = 0;

    float dragX = 0;
    float dragY = 0;

    private int side = 50;

    public ExtensibleEditText(Context context) {
        super(context);

        setDrawingCacheEnabled(true);
    }

    public ExtensibleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDrawingCacheEnabled(true);
    }

    public ExtensibleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setDrawingCacheEnabled(true);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        Log.d("myLogs2", "onSelectionChanged: " + "selStart "+ selStart+ " selEnd "+ selEnd);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.performClick();


        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        Log.d("myLogs2", "onTouchEvent: evX " + evX + " evY "+evY);

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:
                //positionOfTouchX = evX;
                //positionOfTouchY = evY;

                //MainActivity.ivLargerImage.bringToFront();
//                MainActivity.сonstraintLayoutForSchedule.removeView(MainActivity.ivLargerImage);
//                MainActivity.сonstraintLayoutForSchedule.addView(MainActivity.ivLargerImage, MainActivity.сonstraintLayoutForSchedule.getChildCount());
                MainActivity.numberYearPicker.setVisibility(GONE);
                MainActivity.ivLargerImage.setVisibility(VISIBLE);

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) MainActivity.dateMonth.getLayoutParams();
                params.topToBottom = R.id.ivLargerImage;
                MainActivity.dateMonth.setLayoutParams(params);

                //invalidate();
                destroyDrawingCache();
                drawingCache = getDrawingCache();
                dragCursor = true;

                dragX = evX - event.getX();
                dragY = evY - event.getY();


            case MotionEvent.ACTION_MOVE:

                // если режим перетаскивания включен
                if (dragCursor) {

                    Bitmap bitmap = getDrawingCache();
                    xCoord = (int)(evX - dragX - side < 0 ? 0 :evX - dragX - side);
                    yCoord = (int)(evY - dragY - side < 0 ? 0 :evY - dragY - side);

                    if (xCoord + side*2 <= bitmap.getWidth() &&
                            yCoord + side*2  <= bitmap.getHeight() &&
                            yCoord > 0 && xCoord > 0) {
                        // определеяем новые координаты
//                        point1.x = evX - dragX;
//                        point1.y = evY - dragY;

                        destroyDrawingCache();
                        drawingCache = getDrawingCache();

                        adapt();
                    }
                }

                if (dragCursor) {
                    //invalidate();
                }

                break;
            // касание завершено
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

//                MainActivity.сonstraintLayoutForSchedule.removeView(MainActivity.ivLargerImage);
//                MainActivity.сonstraintLayoutForSchedule.addView(MainActivity.ivLargerImage, 0);
                MainActivity.numberYearPicker.setVisibility(VISIBLE);
                MainActivity.ivLargerImage.setVisibility(GONE);

                params = (ConstraintLayout.LayoutParams) MainActivity.dateMonth.getLayoutParams();
                params.topToBottom = R.id.numberYearPicker;
                MainActivity.dateMonth.setLayoutParams(params);



                break;

        }

                return super.onTouchEvent(event);

    }

    @Override
    public void setSelection(int index) {
        super.setSelection(index);
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return super.getDefaultMovementMethod();
    }

    public void adapt(){

//        if(ivLargerImage == null) {
//            ivLargerImage = (ImageView) MainActivity2.activity.findViewById(R.id.ivLargerImage);
//            ivPhoto = (ImageView) MainActivity2.activity.findViewById(R.id.ivPhoto);
//            ivPhoto.setDrawingCacheEnabled(true);
//            PixelPicker1.setMinimum(side + 1);
//            PixelPicker1.setMaximumHeight(ivPhoto.getHeight() - side - 1);
//            PixelPicker1.setMaximumWidth(ivPhoto.getWidth() - side - 1);
//            PixelPicker2.setMinimum(side + 1);
//            PixelPicker2.setMaximumHeight(ivPhoto.getHeight() - side - 1);
//            PixelPicker2.setMaximumWidth(ivPhoto.getWidth() - side - 1);
//            ////////////////////////setDrawingCacheEnabled(true);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//                ivLargerImage.animate().scaleX(5.0f).scaleY(5.0f).setDuration(0);
//            }
//        }

        //MainActivity.ivLargerImage.animate().scaleX(5.0f).scaleY(5.0f).setDuration(1000);

        //////////////////////////////////////destroyDrawingCache();
//        //ivPhoto.destroyDrawingCache();
//        MainActivity.ivLargerImage.destroyDrawingCache();
//        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) MainActivity.ivLargerImage.getLayoutParams();
//        //ViewGroup.LayoutParams params = MainActivity.ivLargerImage.getLayoutParams();
//        //params. = R.id.numberYearPicker;
//
//        MainActivity.ivLargerImage.setLayoutParams(params);

        /////////////////////////////////////////////

//        int xCoord = (int)point.x - side;
//        int yCoord = (int)point.y - side;

        //Log.d("MyLogs","xCoord:"+ xCoord);
        //Log.d("MyLogs","point.x:"+ point.x);

        Bitmap bitmap = getDrawingCache();

        Bitmap croppedBitmap = Bitmap.createBitmap(
                bitmap, xCoord, yCoord, side*2, side*2);

//        Bitmap croppedBitmap2 = Bitmap.createBitmap(
//                drawingCache, xCoord, yCoord, doubleSide, doubleSide);

        int bitmap1Width = croppedBitmap.getWidth();
        int bitmap1Height = croppedBitmap.getHeight();
//        int bitmap2Width = croppedBitmap2.getWidth();
//        int bitmap2Height = croppedBitmap2.getHeight();

//        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
//        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);


        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, croppedBitmap.getConfig());
        //создаем canvas
        Canvas canvas = new Canvas(overlayBitmap);
        //наносим на canvas 1-й битмап
        canvas.drawBitmap(croppedBitmap, new Matrix(), null);
        //сверху наносим 2-й битмап (по центру)
//        canvas.drawBitmap(croppedBitmap2, marginLeft, marginTop, null);

        MainActivity.ivLargerImage.setImageBitmap(croppedBitmap);


        ////////////////////////////////////////////
    }
}
