package com.bignerdranch.android.passionforcooking.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.passionforcooking.Recipe;
import com.bignerdranch.android.passionforcooking.database.RecipeDbSchema.RecipeTable;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Paula on 22.3.2018.
 */

public class RecipeCursorWrapper extends CursorWrapper {
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() {
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(RecipeTable.Cols.DATE));
        float rate = getFloat(getColumnIndex(RecipeTable.Cols.RATE));
        int ratecount = getInt(getColumnIndex(RecipeTable.Cols.RATECOUNT));
        float meanrate = getFloat(getColumnIndex(RecipeTable.Cols.MEANRATE));
        int isLiked = getInt(getColumnIndex(RecipeTable.Cols.LIKED));

        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setDate(new Date(date));
        recipe.setRate(rate);
        recipe.setRateCount(ratecount);
        recipe.setMeanRate(meanrate);
        recipe.setLiked(isLiked != 0);

        return recipe;

    }

}
