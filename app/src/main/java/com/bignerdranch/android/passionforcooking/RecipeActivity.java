package com.bignerdranch.android.passionforcooking;


import android.support.v4.app.Fragment;

public class RecipeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeFragment();
    }
}
