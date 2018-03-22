package com.bignerdranch.android.passionforcooking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bignerdranch.android.passionforcooking.database.RecipeBaseHelper;
import com.bignerdranch.android.passionforcooking.database.RecipeCursorWrapper;
import com.bignerdranch.android.passionforcooking.database.RecipeDbSchema.RecipeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeLab {
    private static RecipeLab sRecipeLab;

   //private List<Recipe> mRecipes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RecipeLab get(Context context) {
        if (sRecipeLab == null) {
            sRecipeLab = new RecipeLab(context);
        }
        return sRecipeLab;
    }

    private RecipeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RecipeBaseHelper(mContext)
                .getWritableDatabase();
        //mRecipes = new ArrayList<>();

       /* for (int i = 0; i < 5; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe " + i);
            recipe.setLiked(i % 2 == 0); // Every other one
            mRecipes.add(recipe);
        } */
    }

    public void addRecipe(Recipe r) {
        ContentValues values = getContentValues(r);
        mDatabase.insert(RecipeTable.NAME, null, values);

        //mRecipes.add(r);
    }

    public void deleteItem(UUID recipeID) {
        Recipe recipe = getRecipe(recipeID);
       // mRecipes.remove(recipe);
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipe = new ArrayList<>();

        RecipeCursorWrapper cursor = queryRecipes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipe.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return recipe;
    }

    public Recipe getRecipe(UUID id) {

        RecipeCursorWrapper cursor = queryRecipes(
                RecipeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRecipe();
        } finally {
            cursor.close();
        }
    }

    public void updateRecipe(Recipe recipe) {
        String uuidString = recipe.getId().toString();
        ContentValues values = getContentValues(recipe);

        mDatabase.update(RecipeTable.NAME, values,
                RecipeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private RecipeCursorWrapper queryRecipes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecipeTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new RecipeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Cols.UUID, recipe.getId().toString());
        values.put(RecipeTable.Cols.TITLE, recipe.getTitle());
        values.put(RecipeTable.Cols.DATE, recipe.getDate().getTime());
        values.put(RecipeTable.Cols.LIKED, recipe.isLiked() ? 1 : 0);
        values.put(RecipeTable.Cols.RATE, recipe.getRate());

        return values;
    }

}

