package com.example.delimes.flux;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;


public class CustomButton extends android.support.v7.widget.AppCompatButton {

    private String text = "";
    private float textSize = 0;


    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        Rect textBounds = new Rect();
        Paint p = new Paint();
        p.setColor(getCurrentTextColor());
        //p.setColor(Color.RED);
        p.setTextSize(textSize);
        p.setStyle(Paint.Style.FILL);
        p.getTextBounds(text, 0, text.length(), textBounds);

        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        canvas.drawColor(getDrawingCacheBackgroundColor());
        canvas.drawText(text,
                getWidth() * 0.50f - textWidth * 0.50f,
                getHeight() * 0.50f + textHeight* 0.75f,
                //getHeight() * 0.50f,
                p);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText("", type);
        this.text = text.toString();
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);

        Resources r = getResources();
        textSize = TypedValue.applyDimension(
                unit,
                size,
                r.getDisplayMetrics()
        );
    }
}
