package com.example.delimes.flux;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by User on 04.10.2017.
 */

class MyCalendar extends View {
    static Paint p;
    // координаты для рисования квадрата
    static float x = 0;
    static float y = 0;
    static int side = 15;
    static public int longSide = 0;

    boolean firstOccurrence = true;
    static float correctiveX = 0;
    static float correctiveY = 0;

    // переменные для перетаскивания
    boolean drag = false;
    float dragX = 0;
    float dragY = 0;

    float biasX = 0;
    float biasY = 0;

    Bitmap backingBitmap;
    Canvas drawCanvas;
    private Path drawPath;

    Context context;

    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private Scroller scroller;
    static private float scaleFactor = 1;

    ///////////////////////////////////
    public TextView textView1;
    public TextView textView2;
    public TextView textView3;
    public TextView textView4;
    public TextView textView5;
    ///////////////////////////////////
    static float upperLeftCornerX = 0;
    static float upperRightCornerX = 0;
    static float bottomLeftCornerY = 0;
    static float upperRightCornerY = 0;
    static float bottomRightCornerX = 0;

    static float scrollX;
    static float scrollY;
    static float focusX;
    static float focusY;




    public MyCalendar(Context context) {
        super(context);
        init(context);

    }

    public MyCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCalendar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        p = new Paint();
        p.setColor(Color.GREEN);
        backingBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(backingBitmap);
        drawPath = new Path();

        gestureDetector = new GestureDetector(context, new MyGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());


        //invalidate();
        //draw(canvas);
        // init scrollbars

        setHorizontalScrollBarEnabled(true);
        setVerticalScrollBarEnabled(true);

        scroller = new Scroller(context);

        p.setFilterBitmap(true);
        p.setDither(false);

    }

    private int getScaledWidth() {
        return (int) (drawCanvas.getWidth() * scaleFactor);
    }

    private int getScaledHeight() {
        return (int) (drawCanvas.getHeight() * scaleFactor);
    }

    @Override
    protected int computeHorizontalScrollRange() {
        return getScaledWidth();
    }

    @Override
    protected int computeVerticalScrollRange() {
        return getScaledHeight();
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//
//        canvas.drawColor(Color.GREEN);
//    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (firstOccurrence) {
            drawCanvas = canvas;
            firstOccurrence = false;
        }



        // рисуем квадрат
        //canvas.drawRect(x, y, x + side, y + side, p);

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);

        canvas.drawColor(Color.BLUE);
        //drawCalendar(canvas);

        canvas.restore();


    }

    public static void drawCalendar(Canvas canvas){

        int Width = canvas.getWidth();
        int Height = canvas.getHeight();

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        //I-ый квартал
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        //1-ый месяц
        int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int k = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            upperLeftCornerX = x - k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, upperLeftCornerX, y, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint(upperLeftCornerX, y, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            upperLeftCornerX = x - k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, upperLeftCornerX, y, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint(upperLeftCornerX, y, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            upperLeftCornerX = x - k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, upperLeftCornerX, y, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint(upperLeftCornerX, y, p);

        longSide=k;
        //II-ой квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.APRIL);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        //1-ый месяц
        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        k = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            bottomLeftCornerY = y + k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, upperLeftCornerX, bottomLeftCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint((float) upperLeftCornerX, bottomLeftCornerY, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MAY);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            bottomLeftCornerY = y + k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, upperLeftCornerX, bottomLeftCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint((float) upperLeftCornerX, bottomLeftCornerY, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.JUNE);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            bottomLeftCornerY = y + k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, upperLeftCornerX, bottomLeftCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint((float) upperLeftCornerX, bottomLeftCornerY, p);

        //III-ий квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.JULY);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        //1-ый месяц
        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        k = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            bottomRightCornerX = upperLeftCornerX + k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, bottomRightCornerX, bottomLeftCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint(bottomRightCornerX, bottomLeftCornerY, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            bottomRightCornerX = upperLeftCornerX + k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, bottomRightCornerX, bottomLeftCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint(bottomRightCornerX, bottomLeftCornerY, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            bottomRightCornerX = upperLeftCornerX + k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, bottomRightCornerX, bottomLeftCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint(bottomRightCornerX, bottomLeftCornerY, p);

        //IV-ый квартал
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        //1-ый месяц
        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        k = 0;
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            upperRightCornerY = bottomLeftCornerY - k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, bottomRightCornerX, upperRightCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint((float) bottomRightCornerX, upperRightCornerY, p);

        //2-ой месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            upperRightCornerY = bottomLeftCornerY - k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, bottomRightCornerX, upperRightCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint((float) bottomRightCornerX, upperRightCornerY, p);

        //3-ий месяц
        calendar.clear();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);

        p.setColor(Color.BLACK);
        // настраиваем размер текста = 30
        p.setTextSize(10);

        maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDaysOfMonth; i++) {
            k += side;
            upperRightCornerY = bottomLeftCornerY - k;
            canvas.drawText(("" + i).length() == 1 ? "0" + i : "" + i, bottomRightCornerX, upperRightCornerY, p);
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(side);
        canvas.drawPoint((float) bottomRightCornerX, upperRightCornerY, p);



//        Toast toast = Toast.makeText(getContext(),
//                "Width:"+ canvas.getWidth() + "Height:"+canvas.getHeight(),
//                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 0);
//        toast.show();
//
//        toast = Toast.makeText(getContext(),
//                "upperLeftCornerX:"+ upperLeftCornerX + "upperRightCornerX"+upperRightCornerX,
//                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 0);
//        toast.show();



        p.setColor(Color.BLUE);
        p.setStrokeWidth(10);
        //canvas.drawPoint(0, -250, p);
        //canvas.drawPoint(x-correctiveX-240, y-correctiveY-270, p);//NexusS
        //canvas.drawPoint(x-correctiveX-600, y-correctiveY-720, p);//Nexus7
        //canvas.drawPoint(x-correctiveX, y-correctiveY, p);
        //((getScrollX() + detector.getFocusX()) * detector.getScaleFactor() - detector.getFocusX())
        canvas.drawPoint(x,
                y, p);
        //canvas.drawPoint(dragX - correctiveX, dragY - correctiveY, p);
        //canvas.drawPoint(dragX-350, dragY-350, p);

        canvas.drawText("" + Width, x, y, p);

        // настраиваем выравнивание текста на центр
        p.setTextAlign(Paint.Align.CENTER);
        // рисуем текст в точке (150,525)
        canvas.drawText("" + Height, x + side, y + side, p);

        //invalidate();
        //canvas.drawBitmap(backingBitmap, 20, 20, p);
        //canvas.drawColor(Color.GREEN);

//        scaleFactor = 1;
//
//        // center image on the screen
//        int width = getWidth();
//        int height = getHeight();
//        if ((width != 0) || (height != 0))
//        {
//            int scrollX = (canvas.getWidth() < width ? -(width - canvas.getWidth()) / 2 : canvas.getWidth() / 2);
//            int scrollY = (canvas.getHeight() < height ? -(height - canvas.getHeight()) / 2 : canvas.getHeight() / 2);
//            scrollTo(scrollX, scrollY);
//        }

        //Rect dst = new Rect(0, 0, getScaledWidth(), getScaledHeight());
        //canvas.drawBitmap(backingBitmap, null, dst, p);
        //canvas.scale(getScaledWidth(), getScaledHeight());
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // координаты Touch-события
        float evX = event.getX();
        float evY = event.getY();

        //if(x=){};

//        Toast toast = Toast.makeText(getContext(),
//                "evX:"+ evX +"evY:"+evY,
//                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 0);
//        toast.show();
//
//        toast = Toast.makeText(getContext(),
//                "__X:"+ (evX - correctiveX) +"__Y:"+(evY - correctiveY),
//                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP, 0, 0);
//        toast.show();

        switch (event.getAction()) {
            // касание началось
            case MotionEvent.ACTION_DOWN:
                // если касание было начато в пределах квадрата

                // включаем режим перетаскивания
                drag = true;
                // разница между левым верхним углом квадрата и точкой касания
                dragX = evX;
                dragY = evY;

                                drag = true;
                                // разница между левым верхним углом квадрата и точкой касания
                                dragX = evX/scaleFactor - x;
                                dragY = evY/scaleFactor - y;

                //textView1.setText("evX:" + evX + "evY:" + evY);
                Log.d("textView1", "evX:" + evX + "evY:" + evY);

//                correctiveX = evX;
//                correctiveY = evY;

                //textView3.setText("--X:" + (correctiveX) + "--Y:" + (correctiveY));
                Log.d("textView3", "--X:" + (correctiveX) + "--Y:" + (correctiveY));

                break;
            // тащим
            case MotionEvent.ACTION_MOVE:
                // если режим перетаскивания включен

                //bias

               /* dragX = evX;
                dragY = evY;
                */

               /* p.setColor(Color.BLUE);
                p.setStrokeWidth(20);
                drawCanvas.drawPoint(evX, evY, p);
                */


                if (drag) {
                    // определеяем новые координаты
                    x = evX/scaleFactor - dragX;
                    y = evY/scaleFactor - dragY;
                    invalidate();
                    //Log.d("XY", "X:" + x + "Y:" + y);
                }

                break;
            // касание завершено
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // выключаем режим перетаскивания


                /*dragX = evX;
                dragY = evY;
                x =evX;
                y =evY;*/

                if(drag) {
//                    correctiveX -= dragX - evX;
//                    correctiveY -= dragY - evY;

                }

                drag = false;

                //textView1.setText("evX:" + evX + "evY:" + evY);
                Log.d("textView1", "evX:" + evX + "evY:" + evY);
                //textView2.setText("evX:" + (evX - correctiveX) + "evY:" + (evY - correctiveY));
                Log.d("textView2", "evX:" + (evX - correctiveX) + "evY:" + (evY - correctiveY));
                //textView3.setText("--X:" + (correctiveX) + "--Y:" + (correctiveY));
                Log.d("textView3", "--X:" + (correctiveX) + "--Y:" + (correctiveY));
                //textView5.setText("X:" + (evX - correctiveX-biasX) + "Y:" + (evY - correctiveY-biasY));
                //textView5.setText("X:" + (x-correctiveX) + "Y:" + (y-correctiveY));
                Log.d("textView5", "X:" + (x) + "Y:" + (y));


                Toast toast = Toast.makeText(getContext(),
                        "upperLeftCornerX:"+ upperLeftCornerX + "upperRightCornerX"+upperRightCornerX,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();

                toast = Toast.makeText(getContext(),
                        "Width:"+ drawCanvas.getWidth() + "Height:"+drawCanvas.getHeight(),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();


                //drawCanvas.drawColor(Color.GREEN);

                //
                // invalidate();
                //textView5.setText("X:" + (evX) + "Y:" + (evY));
//                 toast = Toast.makeText(getContext(),
//                    "--X:"+ (correctiveX) +"--Y:"+(correctiveY),
//                    Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();

                break;

        }


//        switch (event.getAction()) {
//            // касание началось
//            case MotionEvent.ACTION_DOWN:
//                // если касание было начато в пределах квадрата
//                if (evX >= x && evX <= x + side && evY >= y && evY <= y + side) {
//                    // включаем режим перетаскивания
//                    drag = true;
//                    // разница между левым верхним углом квадрата и точкой касания
//                    dragX = evX - x;
//                    dragY = evY - y;
//                }
//                break;
//            // тащим
//            case MotionEvent.ACTION_MOVE:
//                // если режим перетаскивания включен
//                if (drag) {
//                    // определеяем новые координаты для рисования
//                    x = evX - dragX;
//                    y = evY - dragY;
//                    // перерисовываем экран
//                    invalidate();
//                }
//                break;
//            // касание завершено
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // выключаем режим перетаскивания
//                drag = false;
//                break;
//
//        }

//        //scaleGestureDetector.onTouchEvent(event);
//        if (scaleGestureDetector.onTouchEvent(event)) return true;
//
//        return true;

        // check for tap and cancel fling
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            if (!scroller.isFinished()) scroller.abortAnimation();
        }

        // handle pinch zoom gesture
        // don't check return value since it is always true
        scaleGestureDetector.onTouchEvent(event);

        // check for scroll gesture
        if (gestureDetector.onTouchEvent(event)) return true;

//        // check for pointer release
//        if ((event.getPointerCount() == 1) && ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP))
//        {
//            int newScrollX = getScrollX();
//            if (getScaledWidth() < getWidth()) newScrollX = -(getWidth() - getScaledWidth()) / 2;
//            else if (getScrollX() < 0) newScrollX = 0;
//            else if (getScrollX() > getScaledWidth() - getWidth()) newScrollX = getScaledWidth() - getWidth();
//
//            int newScrollY = getScrollY();
//            if (getScaledHeight() < getHeight()) newScrollY = -(getHeight() - getScaledHeight()) / 2;
//            else if (getScrollY() < 0) newScrollY = 0;
//            else if (getScrollY() > getScaledHeight() - getHeight()) newScrollY = getScaledHeight() - getHeight();
//
//            if ((newScrollX != getScrollX()) || (newScrollY != getScrollY()))
//            {
//                scroller.startScroll(getScrollX(), getScrollY(), newScrollX - getScrollX(), newScrollY - getScrollY());
//                awakenScrollBars(scroller.getDuration());
//            }
//        }

        //if(!drag){
           // invalidate();
        //}
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            scrollTo(x, y);
            if (oldX != getScrollX() || oldY != getScrollY()) {
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            }

            //postInvalidate();
        }
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
//        if (!firstOccurrence) {
//            correctiveX -= x - oldX;
//            correctiveY -= y - oldY;
////            Toast toast = Toast.makeText(getContext(),
////                    "__X:" + (correctiveX) + "__Y:" + (correctiveY),
////                    Toast.LENGTH_SHORT);
////            toast.setGravity(Gravity.TOP, 0, 0);
////            toast.show();
//        }else {
//            firstOccurrence = false;
//        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        int scrollX = (getScaledWidth() < width ? -(width - getScaledWidth()) / 2 : getScaledWidth() / 2);
        int scrollY = (getScaledHeight() < height ? -(height - getScaledHeight()) / 2 : getScaledHeight() / 2);
        /////////////////////////////////scrollTo(scrollX, scrollY);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
          /////////////////////////////////// scrollBy((int) distanceX, (int) distanceY);
            ////////////////////////////////////////////
            //textView2.setText("evX:" + (dragX - correctiveX) + "evY:" + (dragY - correctiveY));
            Log.d("textView2", "evX:" + (dragX - correctiveX) + "evY:" + (dragY - correctiveY));

            biasX = x - (dragX - correctiveX);
            biasY = y - (dragY - correctiveY);

            //textView4.setText("biasX:" + (biasX) + "biasY:" + (biasY));
            Log.d("textView4", "biasX:" + (biasX) + "biasY:" + (biasY));

            correctiveX -= distanceX;
            correctiveY -= distanceY;
            scrollX = getScaleX();
            scrollY = getScrollY();

            //Log.d("scroll", "X:" + scrollX + "Y:" + scrollY);
//            Toast toast = Toast.makeText(getContext(),
//                    "__X:"+ (correctiveX) +"__Y:"+(correctiveY),
//                    Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();
            ////////////////////////////////////////////
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //return super.onDown(e);
            Log.d("onDown", e.toString());
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
            Log.d("onShowPress", e.toString());
            //drag = false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //super.onLongPress(e);
            Log.d("onLongPress", e.toString());
            drag = false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            int fixedScrollX = 0, fixedScrollY = 0;
//            int maxScrollX = getScaledWidth(), maxScrollY = getScaledHeight();
//
//            if (getScaledWidth() < getWidth()) {
//                fixedScrollX = -(getWidth() - getScaledWidth()) / 2;
//                maxScrollX = fixedScrollX + getScaledWidth();
//            }
//
//            if (getScaledHeight() < getHeight()) {
//                fixedScrollY = -(getHeight() - getScaledHeight()) / 2;
//                maxScrollY = fixedScrollY + getScaledHeight();
//            }
//
//            boolean scrollBeyondImage = (fixedScrollX < 0) || (fixedScrollX > maxScrollX) || (fixedScrollY < 0) || (fixedScrollY > maxScrollY);
//            if (scrollBeyondImage) return false;
//
//            scroller.fling(getScrollX(), getScrollY(), -(int) velocityX, -(int) velocityY, 0, getScaledWidth() - getWidth(), 0, getScaledHeight() - getHeight());
//            awakenScrollBars(scroller.getDuration());

            //Log.d("onFling", e1.toString() + e2.toString());
            return true;
        }

    }

    class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Toast toast = Toast.makeText(getContext(),
                    "ScaleSpan:" + scaleGestureDetector.getCurrentSpan() + "Width:" + getScaledWidth() + "Height:" + getScaledHeight(),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            scaleFactor *= detector.getScaleFactor();

            focusX = detector.getFocusX();
            focusY = detector.getFocusY();

            Log.d("focus", "X:" + focusX + "Y:" + focusY);

            int newScrollX = (int) ((getScrollX() + detector.getFocusX()) * detector.getScaleFactor() - detector.getFocusX());
            int newScrollY = (int) ((getScrollY() + detector.getFocusY()) * detector.getScaleFactor() - detector.getFocusY());
            //////////////////////////////////////////////////////scrollTo(newScrollX, newScrollY);

            Log.d("scaleFactor", Float.toString(scaleFactor));

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    }


}

