package com.bignerdranch.android.a07scapebuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RedSodeeePop on 2016-08-18.
 */
public class ItemPageActivityFragment extends Fragment {
    private Item mItem;

   // private ThumbnailDownloader mThumbnailDownloader;

    private ImageView mItemIcon;
    private TextView mItemDescription;
    private TextView mItemPrice;
    private TextView mItemName;
    private TextView mItemTrendToday;
    private TextView mItemTrend30Day;
    private TextView mItemTrend90Day;
    private TextView mItemTrend180Day;
    private LineChart mLineChart;
   // private GraphView mItemGraph;
    private CustomMarkerView mMarkerView;


    public static ItemPageActivityFragment newInstance(Parcelable item){


        Bundle args = new Bundle();
        args.putParcelable("item", item);
        ItemPageActivityFragment fragment = new ItemPageActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItem = getArguments().getParcelable("item");
        new FetchItemInfoTask(mItem).execute();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.item_page_view, container, false);

        mItemIcon = (ImageView) v.findViewById(R.id.item_icon_view);
        new GetXMLTask().execute(mItem.getmIconUrl());


        mItemDescription = (TextView) v.findViewById(R.id.item_description_textview);
        mItemDescription.setText(mItem.getmDescription());

        mItemPrice = (TextView) v.findViewById(R.id.item_price_textview);
        mItemPrice.setText("Price: " + mItem.getmPrice());

        mItemName = (TextView)  v.findViewById(R.id.item_name_textview);
        mItemName.setText(mItem.getmName());

        mItemTrendToday = (TextView) v.findViewById(R.id.item_trend_textview_today);
        mItemTrendToday.setText("Today: " + mItem.getmTrendPrice());
        mItemTrendToday.setTextColor(setTrendColor(mItem.getmTrendType()));

        mItemTrend30Day = (TextView) v.findViewById(R.id.item_trend_textview_30day);
        mItemTrend90Day = (TextView) v.findViewById(R.id.item_trend_textview_90day);
        mItemTrend180Day = (TextView) v.findViewById(R.id.item_trend_textview_180day);

        mLineChart =  (LineChart) v.findViewById(R.id.item_trend_graph);

        mMarkerView =  new CustomMarkerView(getContext(),R.layout.graph_marker_view);
        mLineChart.setMarkerView(mMarkerView);
        mLineChart.setDescription("Average price per day");


        return v;
    }

    private int setTrendColor(String trendType){
        if(trendType.equals("negative")){
            return Color.RED;
        }else if(trendType.equals("neutral")){
            return Color.BLACK;
        }else return Color.GREEN;
    }


    private class FetchItemInfoTask extends AsyncTask<Void, Void, Void> {
        private Item mFetchItem;

        public FetchItemInfoTask(Item item){
            mFetchItem = item;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            new OSRSServiceParser().fetchItemData(mFetchItem);


            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            mItemTrend30Day.setText("30 Day: " + mItem.getmTrend30DayPrice());
            mItemTrend30Day.setTextColor(setTrendColor(mItem.getmTrend30DayType()));

            mItemTrend90Day.setText("90 Day: " + mItem.getmTrend90DayPrice());
            mItemTrend90Day.setTextColor(setTrendColor(mItem.getmTrend90DayType()));

            mItemTrend180Day.setText("180 Day: " + mItem.getmTrend180DayPrice());
            mItemTrend180Day.setTextColor(setTrendColor(mItem.getmTrend180DayType()));

            List<Entry> entries = new ArrayList<Entry>();

            for(int i = 0; i < mItem.getmGraphDates().size(); i++){
                entries.add(new Entry(mItem.getmGraphDates().get(i).getTime(), mItem.getmGraphPrices().get(i)));

            }

            LineDataSet dataSet = new LineDataSet(entries,"Label" );
            LineData lineData  = new LineData(dataSet);
            mLineChart.setData(lineData);
            mLineChart.setTouchEnabled(true);
            mLineChart.setDragEnabled(true);
            mLineChart.setHighlightPerDragEnabled(true);
            mLineChart.setHighlightPerTapEnabled(true);


            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setPosition(XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new XAxisValueFormatter());
            xAxis.setGranularity(100000);

            YAxis yAxisRight = mLineChart.getAxisRight();
            yAxisRight.setEnabled(false);

            YAxis yAxisLeft = mLineChart.getAxisLeft();
            yAxisLeft.setValueFormatter(new LargeValueFormatter());



            mLineChart.invalidate();











        }
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            mItemIcon.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }



}
