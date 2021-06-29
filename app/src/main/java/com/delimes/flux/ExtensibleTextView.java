package com.delimes.flux;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ExtensibleTextView extends AppCompatTextView {

    public AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0f, 1f);

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
