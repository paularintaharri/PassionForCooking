package com.bignerdranch.android.passionforcooking;

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


    public Recipe() {
        mId = UUID.randomUUID();
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
}
