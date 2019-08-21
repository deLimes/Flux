package com.example.delimes.flux;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Calendar;

public class NumberYearPicker extends LinearLayout {


    private Context context;
    private int textColor = Color.BLACK;
    private long repeatDeley = 50;


    private int elementHeight = 60;

    private int elementWidth = elementHeight; // you're all squares, yo


    private int minimum = 0;
    private int maximum = 4999;

    private float textSize = 10;

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
        increment.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
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
        valueText.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
        valueText.setTextColor(textColor);
        valueText.setEnabled(false);

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
                if(MainActivity.yearRestored && (MainActivity.changedeTasksOfYear || mainActivity.addedTasksOfYear.size() > 0 || mainActivity.destroyedTasksOfYear.size() > 0) && MainActivity.autumn.days.size() > 0 ){
                    //Log.d("Year", "Year was saved");
                    //mainActivity.saveYear();
                    mainActivity.saveYearToFile();
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
                    MainActivity.previousChosenYearNumber = MainActivity.chosenYearNumber;
                    MainActivity.chosenYearNumber = Integer.valueOf(editable.toString());
                    if (MainActivity.previousChosenYearNumber != MainActivity.chosenYearNumber) {
                        if (extendYear) {
                            MainActivity.yearNumberChangedForMove = true;
                            MainActivity.yearNumberChangedForFling = true;
                        }
                        MainActivity.yearNumberChangedForDraw = true;
                        ///%%K///MainActivity.dateMonth.setText("__.__.____");
                        if (MainActivity.previousChosenYearNumber > mainActivity.chosenYearNumber){
                            mainActivity.yearReducedForFling = true;
                        }
                    }else {
                        MainActivity.yearNumberChangedForMove = false;
                        MainActivity.yearNumberChangedForFling = false;
                        MainActivity.yearNumberChangedForDraw = false;
                    }

                }

                //////////////////////////////////////////////////////////////////////////

                //Log.d("vsm", "mainActivity.winter.length: "+ mainActivity.winter.length);

                //%%C del -
                //if( mainActivity.curentYearNumber < mainActivity.chosenYearNumber) {
                MainActivity.autumn.addCyclicTasks = true;
                //}

                ////%%K///
                if(mainActivity.linLayout != null) {
                    MainActivity.setDay(null, false);
                    //mainActivity.task = null;
                    mainActivity.linLayout.removeAllViews();
                }


                mainActivity.addedTasksOfYear.clear();
                mainActivity.destroyedTasksOfYear.clear();
                MainActivity.changedeTasksOfYear = false;

                mainActivity.restoreYearFromFile();

                int width = mainActivity.constraintLayout.getRight() + mainActivity.guideline.getLeft();
                Log.d("123", "width: "+width);
                int tucherWidth;
                int tucherHeight;

                //Winter

                tucherWidth = mainActivity.constraintLayout.getRight();
                tucherHeight = width;

                ////////mainActivity.winter.side = width/2;

                ////////mainActivity.winter.y = tucherHeight;


                MainActivity.winter.currentDate = null;
                MainActivity.winter.firstOccurrence = true;
                MainActivity.winter.days.clear();
                MainActivity.winter.fillInDays(MainActivity.chosenYearNumber);
                MainActivity.winter.x = tucherWidth;
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



                MainActivity.spring.currentDate = null;
                MainActivity.spring.firstOccurrence = true;
                MainActivity.spring.days.clear();
                MainActivity.spring.fillInDays(MainActivity.chosenYearNumber);
                MainActivity.spring.y = 0;
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



                MainActivity.summer.currentDate = null;
                MainActivity.summer.firstOccurrence = true;
                MainActivity.summer.days.clear();
                MainActivity.summer.fillInDays(MainActivity.chosenYearNumber);
                MainActivity.summer.x = 0;
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



                MainActivity.autumn.currentDate = null;
                MainActivity.autumn.firstOccurrence = true;
                MainActivity.autumn.days.clear();
                MainActivity.autumn.fillInDays(MainActivity.chosenYearNumber);
                MainActivity.autumn.y = tucherHeight;
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

                //mainActivity.restoreYear(editable.toString());


//                mainActivity.winter.firstOccurrence = true;
//                mainActivity.spring.firstOccurrence = true;
//                mainActivity.summer.firstOccurrence = true;
//                mainActivity.autumn.firstOccurrence = true;

                //не пашит
//                if (mainActivity.buttonDeleteTask !=  null) {
//                    if (mainActivity.day != null && mainActivity.day.date.getYear() != mainActivity.chosenYearNumber) {
//                        mainActivity.buttonDeleteTask.setEnabled(false);
//                    } else {
//                        mainActivity.buttonDeleteTask.setEnabled(true);
//                    }
//                }
                /////////////////////////////////////////////
                /////////////////
                if (mainActivity.сonstraintLayoutTaskParameters !=  null) {

                    mainActivity.сonstraintLayoutTaskParameters.setVisibility(View.GONE);
                    post(new Runnable() {
                        @Override
                        public void run() {

                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mainActivity.linearLayout.getLayoutParams();
                            params.height = mainActivity.сonstraintLayoutForSchedule.getHeight() - mainActivity.buttonAddTask.getBottom();
                            mainActivity.linearLayout.setLayoutParams(params);

                        }

                    });

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainActivity.taskDescription.getWindowToken(), 0);
                }



                /////////////////////////////////////////////

                MainActivity.winter.invalidate();
                MainActivity.spring.invalidate();
                MainActivity.summer.invalidate();
                MainActivity.autumn.invalidate();

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
        //valueText.setText( value.toString() );
        //valueText.setInputType( InputType.TYPE_CLASS_NUMBER );
    }

    private void initDecrementButton( Context context){
        decrement = new Button( context );
        decrement.setTextSize( TypedValue.COMPLEX_UNIT_SP, textSize );
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

    public float getTextSize() {
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

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}

