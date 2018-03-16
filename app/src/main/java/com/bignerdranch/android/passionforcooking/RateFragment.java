package com.bignerdranch.android.passionforcooking;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paula on 16.3.2018.
 */

public class RateFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rate, null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.rate_recipe_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
