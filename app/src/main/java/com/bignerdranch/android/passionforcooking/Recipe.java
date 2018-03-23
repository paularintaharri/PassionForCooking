package com.bignerdranch.android.passionforcooking;

import android.provider.Telephony;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Paula on 15.3.2018.
 */

public class Recipe {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mLiked;
    private float mRate;
    private float mMeanRate;
    private int mRateCount;


    public Recipe() {
        this(UUID.randomUUID());

    }
    public Recipe(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public float getRate() {
        return mRate;
    }

    public void setRate(float rate) {
        mRate = rate;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isLiked() {
        return mLiked;
    }

    public void setLiked(boolean liked) {
        mLiked = liked;
    }

    public int getRateCount() {
        return mRateCount;
    }

    public void setRateCount(int rateCount) {
        mRateCount = rateCount;
    }

    public float getMeanRate() {return mMeanRate;}

    public void setMeanRate(float meanRate) {
        mMeanRate = meanRate;
    }
}
