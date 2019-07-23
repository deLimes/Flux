package com.example.delimes.flux;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NumberYearPicker extends LinearLayout {


    private Context context;
    private int textColor = Color.BLACK;
    private long repeatDeley = 50;


    private int elementHeight = 60;

    private int elementWidth = elementHeight; // you're all squares, yo


    private int minimum = 0;
    private int maximum = 4999;

    private int textSize = 10;

    public Integer value;

    Button decrement;
    Button increment;
    public EditText valueText;

    private Handler repeatUpdateHandler = new Handler();

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;

    public boolean extendYear = false;

    public MainActivity mainActivity ;

    class RepetetiveUpdater implements Runnable {
        public void run() {
            if( autoIncrement ){
                increment();
                repeatUpdateHandler.postDelayed( new NumberYearPicker.RepetetiveUpdater(), repeatDeley );
            } else if( autoDecrement ){
                decrement();
                repeatUpdateHandler.postDelayed( new NumberYearPicker.RepetetiveUpdater(), repeatDeley );
            }
        }
    }
    public NumberYearPicker(Context context, AttributeSet attributeSet ) {
        super(context, attributeSet);

        this.mainActivity = (MainActivity)context;
        this.context = context;
        this.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
        initDecrementButton( context );
        initValueEditText( context );
        initIncrementButton( context );
        if( getOrientation() == VERTICAL ){
            addView( increment, elementParams );
            addView( valueText, elementParams );
            addView( decrement, elementParams );
        } else {
            addView( decrement, elementParams );
            addView( valueText, elementParams );
            addView( increment, elementParams );
        }

        LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
        valueTextParams.width = (int)(4 * textSize * 2.3f);
        valueText.setLayoutParams(valueTextParams);
    }

    public NumberYearPicker(Context context, Integer value) {
        super(context);
        this.mainActivity = (MainActivity)context;
        this.context = context;
        this.value = value;

        this.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
        initDecrementButton( context );
        initValueEditText( context );
        initIncrementButton( context );
        if( getOrientation() == VERTICAL ){
            addView( increment, elementParams );
            addView( valueText, elementParams );
            addView( decrement, elementParams );
        } else {
            addView( decrement, elementParams );
            addView( valueText, elementParams );
            addView( increment, elementParams );
        }

        LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
        valueTextParams.width = (int)(4 * textSize * 2.3f);
        valueText.setLayoutParams(valueTextParams);
    }

    public void rebuild(Context context) {

        removeAllViews();

//        LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//        increment.setLayoutParams(elementParams);
//        decrement.setLayoutParams(elementParams);
//
//        LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
//        valueTextParams.width = (int)(4 * textSize * 2.3f);
//        valueText.setLayoutParams(valueTextParams);

        this.setLayoutParams( new LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        LayoutParams elementParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
        initDecrementButton( context );
        initValueEditText( context );
        initIncrementButton( context );
        if( getOrientation() == VERTICAL ){
            addView( increment, elementParams );
            addView( valueText, elementParams );
            addView( decrement, elementParams );
        } else {
            addView( decrement, elementParams );
            addView( valueText, elementParams );
            addView( increment, elementParams );
        }

        LayoutParams valueTextParams = new LinearLayout.LayoutParams( elementHeight, elementWidth );
        //valueTextParams.height = textSize;
        valueTextParams.width = (int)(4 * textSize * 2.1f);
        valueText.setLayoutParams(valueTextParams);



    }

    private void initIncrementButton(Context context){
        increment = new Button( context );
        increment.setTextSize( textSize );
        increment.setText( "+" );
        increment.setTextColor(textColor);

        // Increment once for a click
        increment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                increment();
            }
        });

        increment.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        autoIncrement = true;
                        repeatUpdateHandler.post( new NumberYearPicker.RepetetiveUpdater() );
                        return false;
                    }
                }
        );

        increment.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( event.getAction() == MotionEvent.ACTION_UP && autoIncrement ){
                    autoIncrement = false;
                }
                return false;
            }
        });
    }

    private void initValueEditText(final Context context){

        value = new Integer( 0 );

        valueText = new EditText( context );
        valueText.setTextSize( textSize );
        valueText.setTextColor(textColor);

        valueText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int arg1, KeyEvent event) {
                int backupValue = value;
                try {
                    value = Integer.parseInt( ((EditText)v).getText().toString() );
                    if( value > maximum ) value = maximum;
                    if( value < minimum ) value = minimum;
                } catch( NumberFormatException nfe ){
                    value = backupValue;
                }

                //valueText.setText(value.toString());

                return false;
            }
        });

        valueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((mainActivity.changedeTasksOfYear || mainActivity.addedTasksOfYear.size() > 0 || mainActivity.destroyedTasksOfYear.size() > 0) && mainActivity.autumn.days.size() > 0 ){
                    //Log.d("Year", "Year was saved");
                    mainActivity.saveYear();
                }


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                    if(!charSequence.toString().isEmpty() && yearNumberChanged && Integer.valueOf(charSequence.toString()) > yearNumber){
//                        autumn.addCyclicTasks = true;
//                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!editable.toString().isEmpty()) {
                    mainActivity.previousChosenYearNumber = mainActivity.chosenYearNumber;
                    mainActivity.chosenYearNumber = Integer.valueOf(editable.toString());
                    if (mainActivity.previousChosenYearNumber != mainActivity.chosenYearNumber) {
                        if (extendYear) {
                            mainActivity.yearNumberChangedForMove = true;
                            mainActivity.yearNumberChangedForFling = true;
                        }
                        mainActivity.yearNumberChangedForDraw = true;
                        MainActivity.dateMonth.setText("__.__.____");
                        if (mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber){
                            mainActivity.yearReducedForFling = true;
                        }
                    }else {
                        mainActivity.yearNumberChangedForMove = false;
                        mainActivity.yearNumberChangedForFling = false;
                        mainActivity.yearNumberChangedForDraw = false;
                    }

                }

                //////////////////////////////////////////////////////////////////////////

                //Log.d("vsm", "mainActivity.winter.length: "+ mainActivity.winter.length);

                //%%C del -
                if( mainActivity.curentYearNumber < mainActivity.chosenYearNumber) {
                    mainActivity.autumn.addCyclicTasks = true;
                }

                if(mainActivity.linLayout != null) {
                    mainActivity.day = null;
                    mainActivity.linLayout.removeAllViews();
                }
                mainActivity.addedTasksOfYear.clear();
                mainActivity.destroyedTasksOfYear.clear();
                mainActivity.changedeTasksOfYear = false;

                int width = mainActivity.constraintLayout.getRight() + mainActivity.guideline.getLeft();
                Log.d("123", "width: "+width);
                int tucherWidth;
                int tucherHeight;

                //Winter

                tucherWidth = mainActivity.constraintLayout.getRight();
                tucherHeight = width;

                ////////mainActivity.winter.side = width/2;

                ////////mainActivity.winter.y = tucherHeight;


                mainActivity.winter.currentDate = null;
                mainActivity.winter.firstOccurrence = true;
                mainActivity.winter.days.clear();
                mainActivity.winter.fillInDays(mainActivity.chosenYearNumber);
                mainActivity.winter.x = tucherWidth;
//                if (!mainActivity.decrementYear) {
//                    mainActivity.winter.x = tucherWidth;
//                }
//                if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber ) {
//                    mainActivity.winter.x = mainActivity.winter.length;
//                }
                //mainActivity.winter.invalidate();


                //Spring
                tucherWidth = width;
                tucherHeight = mainActivity.constraintLayout.getBottom()-width*2;

                ////////////////
                //mainActivity.spring.x = tucherWidth - tucherWidth / 2;



                mainActivity.spring.currentDate = null;
                mainActivity.spring.firstOccurrence = true;
                mainActivity.spring.days.clear();
                mainActivity.spring.fillInDays(mainActivity.chosenYearNumber);
                mainActivity.spring.y = 0;
//                if (!mainActivity.decrementYear) {
//                    mainActivity.spring.y = 0;
//                }
//                if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
//                    mainActivity.spring.y = -(mainActivity.spring.length - mainActivity.spring.getHeight());
//                }
                //mainActivity.spring.invalidate();


                //Summer
                tucherWidth = mainActivity.constraintLayout.getRight();;
                tucherHeight = width;

                ////////////////
                //mainActivity.summer.y = tucherHeight - tucherHeight / 2;



                mainActivity.summer.currentDate = null;
                mainActivity.summer.firstOccurrence = true;
                mainActivity.summer.days.clear();
                mainActivity.summer.fillInDays(mainActivity.chosenYearNumber);
                mainActivity.summer.x = 0;
//                if (!mainActivity.decrementYear) {
//                    mainActivity.summer.x = 0;
//                }
//                if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
//                    mainActivity.summer.x = -(mainActivity.summer.length - mainActivity.summer.getWidth());
//                }
                //mainActivity.summer.invalidate();


                //Autumn
                tucherWidth = width;
                tucherHeight = mainActivity.constraintLayout.getBottom() - width * 2;

                ////////////////
                //mainActivity.autumn.side = width/2;
                //mainActivity.autumn.x = tucherWidth - tucherWidth / 2;



                mainActivity.autumn.currentDate = null;
                mainActivity.autumn.firstOccurrence = true;
                mainActivity.autumn.days.clear();
                mainActivity.autumn.fillInDays(mainActivity.chosenYearNumber);
                mainActivity.autumn.y = tucherHeight;
//                if (!mainActivity.decrementYear) {
//                    mainActivity.autumn.y = tucherHeight;
//                }

//                if (mainActivity.decrementYear){
//                    mainActivity.decrementYear = false;
//                }
//                if( mainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber) {
//                    mainActivity.autumn.y = mainActivity.autumn.length;
//                }
                //mainActivity.autumn.invalidate();

                mainActivity.restoreYear(editable.toString());

                mainActivity.winter.invalidate();
                mainActivity.spring.invalidate();
                mainActivity.summer.invalidate();
                mainActivity.autumn.invalidate();

                //////////////////////////////////////////////////////////////////////////
            }
        });

        valueText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus ){
                    ((EditText)v).selectAll();
                }
            }
        });
        valueText.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL );
        valueText.setText( value.toString() );
        valueText.setInputType( InputType.TYPE_CLASS_NUMBER );
    }

    private void initDecrementButton( Context context){
        decrement = new Button( context );
        decrement.setTextSize( textSize );
        decrement.setText( "-" );
        decrement.setTextColor(textColor);


        decrement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                decrement();
            }
        });


        decrement.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        autoDecrement = true;
                        repeatUpdateHandler.post( new NumberYearPicker.RepetetiveUpdater() );
                        return false;
                    }
                }
        );

        decrement.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( event.getAction() == MotionEvent.ACTION_UP && autoDecrement){
                    autoDecrement = false;
                }
                return false;
            }
        });

    }

    public void increment(){
        if( value < maximum ){
            value = value + 1;
        }else{
            value = minimum;
        }
        String strValue= value.toString();
        int valueLength = strValue.length();

        if (valueLength < 4){
            for (int i = 0; i < 4 - valueLength; i++){
                strValue = "0" + strValue;

            }
        }
        valueText.setText(strValue);
    }

    public void decrement(){
        if( value > minimum ){
            value = value - 1;
        }else{
            value = maximum;
        }

        String strValue= value.toString();
        int valueLength = strValue.length();

        if (valueLength < 4){
            for (int i = 0; i < 4 - valueLength; i++){
                strValue = "0" + strValue;

            }
        }
        valueText.setText(strValue);

    }

    public int getValue(){
        return value;
    }

    public void setValue( int value ){
        if( value > maximum ) value = maximum;
        if( value >= minimum ){
            this.value = value;

            String strValue=  Integer.toString(value);
            int valueLength = strValue.length();

            if (valueLength < 4){
                for (int i = 0; i < 4 - valueLength; i++){
                    strValue = "0" + strValue;

                }
            }
            valueText.setText(strValue);
        }
    }

    public long getRepeatDeley() {
        return repeatDeley;
    }

    public int getElementHeight() {
        return elementHeight;
    }

    public int getElementWidth() {
        return elementWidth;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setRepeatDeley(long repeatDeley) {
        this.repeatDeley = repeatDeley;
    }

    public void setElementHeight(int elementHeight) {
        this.elementHeight = elementHeight;
    }

    public void setElementWidth(int elementWidth) {
        this.elementWidth = elementWidth;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}

