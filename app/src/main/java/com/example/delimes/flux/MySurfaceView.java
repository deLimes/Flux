package com.example.delimes.flux;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by User on 07.10.2017.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }


    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

        @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), getResources());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }
    }


}

class DrawThread extends Thread{
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private Bitmap picture;
    private Matrix matrix;
    private long prevTime;

    Paint p;
    int side = 15;
    int longSide;
    float x;
    float y;
    float upperLeftCornerX = 0;
    float upperRightCornerX = 0;
    float bottomLeftCornerY = 0;
    float upperRightCornerY = 0;
    float bottomRightCornerX = 0;


    public DrawThread(SurfaceHolder surfaceHolder, Resources resources){
        this.surfaceHolder = surfaceHolder;

        // загружаем картинку, которую будем отрисовывать
        //picture = BitmapFactory.decodeResource(resources, R.drawable.icon);

        // формируем матрицу преобразований для картинки
        matrix = new Matrix();
        matrix.postScale(3.0f, 3.0f);
        matrix.postTranslate(100.0f, 100.0f);

        // сохраняем текущее время
        prevTime = System.currentTimeMillis();

        p = new Paint();
        side = 15;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (runFlag) {
            // получаем текущее время и вычисляем разницу с предыдущим
            // сохраненным моментом времени
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            if (elapsedTime > 30){
                // если прошло больше 30 миллисекунд - сохраним текущее время
                // и повернем картинку на 2 градуса.
                // точка вращения - центр картинки
                prevTime = now;
               ////////////////////////// matrix.preRotate(2.0f, picture.getWidth() / 2, picture.getHeight() / 2);
            }
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    //canvas.drawColor(Color.BLACK);
                    //canvas.drawBitmap(picture, matrix, null);
                    canvas.drawColor(Color.WHITE);
                    //drawCalendar(canvas);
                }
            }
            finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void drawCalendar(Canvas canvas){

        int Width = canvas.getWidth();
        int Height = canvas.getHeight();

        canvas.drawColor(Color.WHITE);

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
}
