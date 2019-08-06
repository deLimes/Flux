package com.example.delimes.flux;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;


public class ExtensibleEditText extends android.support.v7.widget.AppCompatEditText {

    Context context;
    CountDownTimer countDownTimer;
    public boolean showIvLargerImage = true;

    public ExtensibleEditText(Context context) {
        super(context);

        this.context = context;

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
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        showIvLargerImage = false;
    }

    private void showIvLargerImage() {

        if (!showIvLargerImage){
            showIvLargerImage = true;
            return;
        }
        invalidate();
        Bitmap bitmap = getDrawingCache();

        if (bitmap == null){
            return;
        }

        MainActivity.ivLargerImage.setImageBitmap(bitmap);
        MainActivity.ivLargerImage.setVisibility(VISIBLE);

        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                MainActivity.ivLargerImage.setVisibility(GONE);
                MainActivity.ivLargerImage.clearFocus();
            }
        };
        countDownTimer.start();


    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        Log.d("myLogs2", "onSelectionChanged: " + "selStart "+ selStart+ " selEnd "+ selEnd);
        showIvLargerImage();
    }




}
