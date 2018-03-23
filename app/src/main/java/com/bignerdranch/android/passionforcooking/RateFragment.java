package com.bignerdranch.android.passionforcooking;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Paula on 16.3.2018.
 */

public class RateFragment extends DialogFragment {
    public static final String EXTRA_RATE =
           "com.bignerdranch.android.passionforcooking.rate";

    private static final String ARG_RATE = "rate";
    private RatingBar mRatingBar;
    float rate;

    public static RateFragment newInstance(float rate){
        Bundle args = new Bundle();
        args.putFloat(ARG_RATE, rate);

        RateFragment fargment = new RateFragment();
        fargment.setArguments(args);
        return fargment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final float rating = getArguments().getFloat(ARG_RATE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rate, null);
        mRatingBar  = (RatingBar)v.findViewById(R.id.dialog_rate_picker);
        mRatingBar.setRating(rating);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.rate_recipe_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rate = (float) mRatingBar.getRating();
                                Toast.makeText(getActivity(), getResources().getString(R.string.star_toast_start) + " "
                                          + String.valueOf(Math.round(rate)) + " " + getResources().getString(R.string.star_toast_end) , Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                sendResult(Activity.RESULT_OK, rate);
                            }
                        })
                .create();
    }


    private void sendResult(int resultCode, float rate) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RATE, rate);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
