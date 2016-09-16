package com.bignerdranch.android.a07scapebuddy;

import android.content.Context;

import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RedSodeeePop on 2016-08-19.
 */
public class CustomMarkerView extends MarkerView {
    private TextView mDate;
    private TextView mPrice;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        mDate = (TextView) findViewById(R.id.value_date);
        mPrice = (TextView) findViewById(R.id.value_price);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        SimpleDateFormat format = new SimpleDateFormat("MMM d");

        Long date = (long) e.getX();
        String formattedDate;

        formattedDate = format.format(new Date(date));

        mDate.setText(formattedDate); // set the entry-value as the display text

        Long price = (long) e.getY();

        mPrice.setText(price.toString() + " GP");

    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -getWidth();
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}

