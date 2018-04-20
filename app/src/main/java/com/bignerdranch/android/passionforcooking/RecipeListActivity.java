package com.bignerdranch.android.passionforcooking;


import android.content.Intent;
import android.support.v4.app.Fragment;


/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeListActivity extends SingleFragmentActivity
        implements RecipeListFragment.Callbacks, RecipeFragmentAdd.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail; //activity_twopane = tablet, activity_masterdetail = phone
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = RecipePagerActivity.newIntent(this, recipe.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = RecipeFragmentAdd.newInstance(recipe.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
    public void onRecipeUpdated(Recipe recipe) {
        RecipeListFragment listFragment = (RecipeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

}
