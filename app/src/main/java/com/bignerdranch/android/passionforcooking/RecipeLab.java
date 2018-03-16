package com.bignerdranch.android.passionforcooking;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeLab {
    private static RecipeLab sRecipeLab;
    private List<Recipe> mRecipes;

    public static RecipeLab get(Context context) {
        if (sRecipeLab == null) {
            sRecipeLab = new RecipeLab(context);
        }
        return sRecipeLab;
    }

    private RecipeLab(Context context) {
        mRecipes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe " + i);
            recipe.setLiked(i % 2 == 0); // Every other one
            mRecipes.add(recipe);
        }
    }

    public List<Recipe> getRecipes() {

        return mRecipes;
    }

    public Recipe getRecipe(UUID id) {
        for (Recipe recipe : mRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }

        return null;
    }

}

