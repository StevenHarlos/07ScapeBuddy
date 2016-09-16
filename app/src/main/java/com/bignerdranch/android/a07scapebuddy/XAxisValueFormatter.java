package com.bignerdranch.android.a07scapebuddy;



import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RedSodeeePop on 2016-08-19.
 */
public class XAxisValueFormatter implements AxisValueFormatter {

    private SimpleDateFormat mDateFormat;

    public XAxisValueFormatter(){
        mDateFormat = new SimpleDateFormat("MMM d");
    }




    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Long date = (long)value;
        String formattedDate;

        formattedDate = mDateFormat.format(new Date(date));

        return formattedDate;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
