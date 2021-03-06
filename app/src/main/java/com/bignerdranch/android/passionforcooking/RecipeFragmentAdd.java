package com.bignerdranch.android.passionforcooking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Paula on 15.3.2018.
 */

public class RecipeFragmentAdd extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String DIALOG_RATE = "DialogRate";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private static final int REQUEST_RATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_PHOTO= 2;

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private Recipe mRecipe;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mRateButton; //Rate recipe
    private CheckBox mLikedCheckBox;
    private Button mSendButton;
    private EditText mIngredients;
    private EditText mDetails;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;
    private int mPhotoWidth;
    private int mPhotoHeight;

    public interface Callbacks {
        void onRecipeUpdated(Recipe recipe);
    }

    public static RecipeFragmentAdd newInstance(UUID recipeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_ID, recipeId);

        RecipeFragmentAdd fragment = new RecipeFragmentAdd();
        fragment.setArguments(args);
        return fragment;
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
        UUID recipeId = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = RecipeLab.get(getActivity()).getRecipe(recipeId);
        mPhotoFile = RecipeLab.get(getActivity()).getPhotoFile(mRecipe);
    }

    @Override
    public void onPause() {
        super.onPause();

        RecipeLab.get(getActivity())
                .updateRecipe(mRecipe);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
                updateRecipe();
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

        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoButton = (ImageButton) v.findViewById(R.id.recipe_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.passionforcooking.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.recipe_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                PhotoDialogFragment dialog = PhotoDialogFragment.newInstance(mPhotoFile);
                dialog.show(manager, DIALOG_PHOTO);
            }
        });
        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mPhotoWidth = mPhotoView.getMeasuredWidth();
                    mPhotoHeight = mPhotoView.getMeasuredHeight();
                    updatePhotoView();
                }
            });
        }

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
                updateRecipe();
            } else if (mIngredients.hasFocus()) {
                mRecipe.setIngredients(s.toString());
                updateRecipe();
            }else {
                mRecipe.setDetails(s.toString());
                updateRecipe();
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
            updateRecipe();
            updateRate();
        }else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mRecipe.setDate(date);
            updateRecipe();
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
        Uri uri = FileProvider.getUriForFile(getActivity(),
                "com.bignerdranch.android.passionforcooking.fileprovider",
                mPhotoFile);

        getActivity().revokeUriPermission(uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        updateRecipe();
        updatePhotoView();
        }
    }

    private void updateRecipe() {
        RecipeLab.get(getActivity()).updateRecipe(mRecipe);
        mCallbacks.onRecipeUpdated(mRecipe);
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

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), mPhotoWidth, mPhotoHeight);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}

