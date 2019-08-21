package com.example.delimes.flux;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AlphaAnimation;

public class ExtensibleTextView extends android.support.v7.widget.AppCompatTextView {

    private AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0f, 1f);

    public ExtensibleTextView(Context context) {
        super(context);

        init();
    }

    public ExtensibleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ExtensibleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        alphaAnimationFadeIn.setDuration(1000);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        /*глючит находясь в ViewPager
        try {
            if (this != null){
                startAnimation(alphaAnimationFadeIn);
            }
        }catch (Exception e){
            Log.e("myLogs2", "Exception: " + e.getMessage());
        }
        */

    }
}
