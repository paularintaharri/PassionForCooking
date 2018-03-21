package com.bignerdranch.android.passionforcooking;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String DIALOG_RATE = "DialogRate";
    private static final int REQUEST_RATE = 0;



    private Recipe mRecipe;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mRateButton; //Rate recipe
    private CheckBox mLikedCheckBox;

    public static RecipeFragment newInstance(UUID recipeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_ID, recipeId);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recipeId = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = RecipeLab.get(getActivity()).getRecipe(recipeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        mTitleField = (EditText) v.findViewById(R.id.recipe_title);
        mTitleField.setText(mRecipe.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mDateButton = (Button) v.findViewById(R.id.recipe_date);
        mDateButton.setText(mRecipe.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                RateFragment dialog = new RateFragment();
                // RateFragment dialog = RateFragment
               //         .newInstance(mRecipe.getDate());
                dialog.show(manager, DIALOG_RATE);
            }
        });

        //Rate button actions
        mRateButton = (Button) v.findViewById(R.id.recipe_rate);
        updateRate();
        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();

                RateFragment dialog = RateFragment.newInstance(mRecipe.getRate());
                dialog.setTargetFragment(RecipeFragment.this, REQUEST_RATE);
                dialog.show(manager, DIALOG_RATE);
            }
        });

        mLikedCheckBox = (CheckBox)v.findViewById(R.id.recipe_liked);
        mLikedCheckBox.setChecked(mRecipe.isLiked());
        mLikedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mRecipe.setLiked(isChecked);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_RATE) {
            float rate = (float) data
                    .getSerializableExtra(RateFragment.EXTRA_RATE);
            mRecipe.setRate(rate);
            updateRate();
        }
    }

    private void updateRate() {
        mRateButton.setText(String.valueOf(mRecipe.getRate()));
    }
}

