package com.bignerdranch.android.passionforcooking;


import android.support.v4.app.Fragment;


/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}
