package com.bignerdranch.android.a07scapebuddy;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by RedSodeeePop on 2016-08-11.
 */

public class Item implements Parcelable {
    private int mID;
    private String mIconUrl;
    private String mPrice;
    private String mDescription;
    private String mName;
    private String mTrendType;
    private String mTrendPrice;
    private String mTrend30DayType;
    private String mTrend30DayPrice;
    private String mTrend90DayType;
    private String mTrend90DayPrice;
    private String mTrend180DayType;
    private String mTrend180DayPrice;
    private List<Date> mGraphDates;
    private List<Integer> mGraphPrices;

    private Drawable mIcon;

    public Item(){

    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmIconUrl() {
        return mIconUrl;
    }

    public void setmIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTrendType() {
        return mTrendType;
    }

    public void setmTrendType(String mTrendType) {
        this.mTrendType = mTrendType;
    }

    public String getmTrendPrice() {
        return mTrendPrice;
    }

    public void setmTrendPrice(String mTrendPrice) {
        this.mTrendPrice = mTrendPrice;
    }

    public String getmTrend30DayType() {
        return mTrend30DayType;
    }

    public void setmTrend30DayType(String mTrend30DayType) {
        this.mTrend30DayType = mTrend30DayType;
    }

    public String getmTrend30DayPrice() {
        return mTrend30DayPrice;
    }

    public void setmTrend30DayPrice(String mTrend30DayPrice) {
        this.mTrend30DayPrice = mTrend30DayPrice;
    }

    public String getmTrend90DayType() {
        return mTrend90DayType;
    }

    public void setmTrend90DayType(String mTrend90DayType) {
        this.mTrend90DayType = mTrend90DayType;
    }

    public String getmTrend90DayPrice() {
        return mTrend90DayPrice;
    }

    public void setmTrend90DayPrice(String mTrend90DayPrice) {
        this.mTrend90DayPrice = mTrend90DayPrice;
    }

    public String getmTrend180DayType() {
        return mTrend180DayType;
    }

    public void setmTrend180DayType(String mTrend180DayType) {
        this.mTrend180DayType = mTrend180DayType;
    }

    public String getmTrend180DayPrice() {
        return mTrend180DayPrice;
    }

    public void setmTrend180DayPrice(String mTrend180DayPrice) {
        this.mTrend180DayPrice = mTrend180DayPrice;
    }

    public List<Date> getmGraphDates() {
        return mGraphDates;
    }

    public void setmGraphDates(List<Date> mGraphDates) {
        this.mGraphDates = mGraphDates;
    }

    public List<Integer> getmGraphPrices() {
        return mGraphPrices;
    }

    public void setmGraphPrices(List<Integer> mGraphPrices) {
        this.mGraphPrices = mGraphPrices;
    }

    public Drawable getmIcon() {
        return mIcon;
    }

    public void setmIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPrice);
        parcel.writeString(mDescription);
        parcel.writeString(mName);
        parcel.writeString(mTrendType);
        parcel.writeString(mTrendPrice);
        parcel.writeString(mIconUrl);
        parcel.writeInt(mID);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Item(Parcel in) {
        mPrice = in.readString();
        mDescription = in.readString();
        mName = in.readString();
        mTrendType = in.readString();
        mTrendPrice = in.readString();
        mIconUrl = in.readString();
        mID = in.readInt();
    }
}
