package com.bignerdranch.android.a07scapebuddy;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class Skill {
    private int mRank;
    private int mLevel;
    private int mXP;
    private String mName;

    public Skill(int rank,int level, int xp){
        mRank = rank;
        mLevel = level;
        mXP = xp;
    }


    public int getmRank() {
        return mRank;
    }

    public void setmRank(int mRank) {
        this.mRank = mRank;
    }

    public int getmLevel() {
        return mLevel;
    }

    public void setmLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public int getmXP() {
        return mXP;
    }

    public void setmXP(int mXP) {
        this.mXP = mXP;
    }

    public void setmName(String name){
        mName = name;
    }

    public String getmName(){
        return mName;
    }
}
