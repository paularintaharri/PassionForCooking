package com.bignerdranch.android.passionforcooking.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.bignerdranch.android.passionforcooking.database.RecipeDbSchema.RecipeTable;

/**
 * Created by Paula on 22.3.2018.
 */

public class RecipeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recipeBase.db";

    public RecipeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecipeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                RecipeTable.Cols.UUID + ", " +
                RecipeTable.Cols.TITLE + ", " +
                RecipeTable.Cols.DATE + ", " +
                RecipeTable.Cols.LIKED + ", " +
                RecipeTable.Cols.RATE +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

