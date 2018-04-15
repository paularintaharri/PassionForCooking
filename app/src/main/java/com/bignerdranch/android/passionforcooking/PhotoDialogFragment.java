package com.bignerdranch.android.passionforcooking;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Paula on 15.4.2018.
 */

public class PhotoDialogFragment extends DialogFragment {

    private static final String ARG_PHOTO_FILE = "photoFile";

    private ImageView mPhotoView;
    private File mPhotoFile;

    public static PhotoDialogFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_FILE, photoFile);

        PhotoDialogFragment fragment = new PhotoDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPhotoFile = (File) getArguments().getSerializable(ARG_PHOTO_FILE);

        View view = inflater.inflate(R.layout.dialog_photo, container, false);

        mPhotoView = (ImageView) view.findViewById(R.id.photo_view_dialog);

        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }

        return view;
    }
}