package com.bignerdranch.android.passionforcooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeListFragment extends Fragment {
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private TextView mTextView;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";


    public interface Callbacks {
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = (RecyclerView) view
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextView = view.findViewById(R.id.no_recipe_there);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        if(RecipeLab.get(getActivity()).getRecipes().size() <= 0) {
            mTextView.setVisibility(View.VISIBLE);
        } else {
            updateUI();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_recipe:
                Recipe recipe = new Recipe();
                RecipeLab.get(getActivity()).addRecipe(recipe);
                updateUI();
                mCallbacks.onRecipeSelected(recipe);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.show_webview:
                showWebView();
                return true;
            case R.id.show_map:
                //showMapView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showWebView() {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        startActivity(intent);
    }

    private void updateSubtitle() {
        RecipeLab recipeLab = RecipeLab.get(getActivity());
        int recipeSize = recipeLab.getRecipes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural,recipeSize, recipeSize);

        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {
        RecipeLab recipeLab = RecipeLab.get(getActivity());
        List<Recipe> recipes = recipeLab.getRecipes();

        if (mAdapter == null) {

            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setRecipes(recipes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Recipe mRecipe;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mRateTextView;
        private ImageView mLikedImageView;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.recipe_date);
            mRateTextView = (TextView) itemView.findViewById(R.id.recipe_rate);
            mLikedImageView = (ImageView) itemView.findViewById(R.id.recipe_liked);

        }
        public void bind(Recipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(mRecipe.getTitle());
            mRateTextView.setText(getResources().getString(R.string.rating) + " " + String.valueOf(Math.round(mRecipe.getMeanRate()) + "/5"));
            mLikedImageView.setVisibility(recipe.isLiked() ? View.VISIBLE : View.GONE);
            DateFormat dateFormat = new SimpleDateFormat(RecipeFragmentAdd.DATE_FORMAT);
            mDateTextView.setText(dateFormat.format(mRecipe.getDate()));

        }

        @Override
        public void onClick(View view) {
           mCallbacks.onRecipeSelected((mRecipe));
        }
    }


    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<Recipe> mRecipes;

        public RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }
        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecipeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            Recipe recipe= mRecipes.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        public void setRecipes(List<Recipe> recipes) {
            mRecipes = recipes;
        }
    }
}
