package com.bignerdranch.android.passionforcooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Created by Paula on 16.3.2018.
 */

public class RecipePagerActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID =
            "com.bignerdranch.android.passionforcooking.recipe_id";

    private ViewPager mViewPager;
    private List<Recipe> mRecipes;
    private Button mNextButton;
    private Button mPrevButton;

    public static Intent newIntent(Context packageContext, UUID recipeId) {
        Intent intent = new Intent(packageContext, RecipePagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recipe_pager);

            final UUID recipeId = (UUID) getIntent()
                    .getSerializableExtra(EXTRA_RECIPE_ID);

            mViewPager = (ViewPager) findViewById(R.id.recipe_view_pager);

            mRecipes = RecipeLab.get(this).getRecipes();
            FragmentManager fragmentManager = getSupportFragmentManager();
            mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

                @Override
                public Fragment getItem(int position) {
                    Recipe recipe = mRecipes.get(position);
                    return RecipeFragment.newInstance(recipe.getId());
                }

                @Override
                public int getCount() {
                    return mRecipes.size();
                }
            });

            for (int i = 0; i < mRecipes.size(); i++) {
                if (mRecipes.get(i).getId().equals(recipeId)) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }

            //Next and Prev Button actions
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if (mViewPager.getCurrentItem() == 0) {
                        mPrevButton.setVisibility(View.INVISIBLE);
                    } else {
                        mPrevButton.setVisibility(View.VISIBLE);
                    }

                    if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
                        mNextButton.setVisibility(View.INVISIBLE);
                    } else {
                        mNextButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mPrevButton = (Button) findViewById(R.id.prev_recipe);
            mPrevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
            });

            mNextButton = (Button) findViewById(R.id.next_recipe);
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() +1 );
                }
            });



        }
}
