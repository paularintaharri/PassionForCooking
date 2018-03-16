package com.bignerdranch.android.passionforcooking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeListFragment extends Fragment {
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = (RecyclerView) view
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return view;
    }

    private void updateUI() {
        RecipeLab recipeLab = RecipeLab.get(getActivity());
        List<Recipe> recipes = recipeLab.getRecipes();

        mAdapter = new RecipeAdapter(recipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
    }
    private class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Recipe mRecipe;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mLikedImageView;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.recipe_date);
            mLikedImageView = (ImageView) itemView.findViewById(R.id.recipe_liked);

        }
        public void bind(Recipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(mRecipe.getTitle());
            mDateTextView.setText(mRecipe.getDate().toString());
            mLikedImageView.setVisibility(recipe.isLiked() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),
                    mRecipe.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
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
    }
}
