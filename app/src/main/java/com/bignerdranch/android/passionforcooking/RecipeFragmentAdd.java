package com.bignerdranch.android.passionforcooking;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeFragmentAdd extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String DIALOG_RATE = "DialogRate";
    private static final int REQUEST_RATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 1;

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private Recipe mRecipe;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mRateButton; //Rate recipe
    private CheckBox mLikedCheckBox;
    private Button mSendButton;
    private EditText mIngredients;
    private EditText mDetails;

    public static RecipeFragmentAdd newInstance(UUID recipeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_ID, recipeId);

        RecipeFragmentAdd fragment = new RecipeFragmentAdd();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID recipeId = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = RecipeLab.get(getActivity()).getRecipe(recipeId);
    }

    @Override
    public void onPause() {
        super.onPause();

        RecipeLab.get(getActivity())
                .updateRecipe(mRecipe);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipe_delet_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_recipe:
                RecipeLab.get(getActivity()).deleteItem(mRecipe);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_add, container, false);

        mTitleField = (EditText) v.findViewById(R.id.recipe_title);
        mTitleField.setText(mRecipe.getTitle());
        mTitleField.addTextChangedListener(textWatcher);

        mDetails = (EditText) v.findViewById(R.id.details_edit);
        mDetails.setText(mRecipe.getDetails());
        mDetails.addTextChangedListener(textWatcher);

        mIngredients = (EditText) v.findViewById(R.id.ingredients_edit);
        mIngredients.setText(mRecipe.getIngredients());
        mIngredients.addTextChangedListener(textWatcher);

        mDateButton = (Button) v.findViewById(R.id.recipe_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mRecipe.getDate());
                dialog.setTargetFragment(RecipeFragmentAdd.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
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
                dialog.setTargetFragment(RecipeFragmentAdd.this, REQUEST_RATE);
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

        mSendButton = (Button) v.findViewById(R.id.recipe_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getRecipeSend());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.recipe_subject));
                i = Intent.createChooser(i, getString(R.string.send_resipe_via));
                startActivity(i);
            }
        });

        return v;
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTitleField.hasFocus()) {
                mRecipe.setTitle(s.toString());
            } else if (mIngredients.hasFocus()) {
                mRecipe.setIngredients(s.toString());
            }else {
                mRecipe.setDetails(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_RATE) {
            //ratebar value
            float rate = (float) data.getSerializableExtra(RateFragment.EXTRA_RATE);

            // rate counts and save them to database
            int count = mRecipe.getRateCount();
            count = count + 1;
            mRecipe.setRateCount(count);

            //counts mean value
            float currentrate = mRecipe.getRate();
            currentrate = currentrate + rate;
            mRecipe.setRate(currentrate);
            float mean  = currentrate / count;
            mRecipe.setMeanRate(mean);
            updateRate();
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mRecipe.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        mDateButton.setText(dateFormat.format(mRecipe.getDate()));}

    private void updateRate() {
        mRateButton.setText(getResources().getString(R.string.rating) + " " + String.valueOf(Math.round(mRecipe.getMeanRate()) + "/5"));
    }

    private String getRecipeSend() {
        String report = getString(R.string.send_recipe, mRecipe.getTitle(),
                mRecipe.getIngredients(), mRecipe.getDetails());
        return report;
    }

}

