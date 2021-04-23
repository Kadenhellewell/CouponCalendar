package com.kadenhellewellcouponcalendar.phoneapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NewCouponFragment extends Fragment {
    Uri imageUri = null;
    long expDate = 0;


    public NewCouponFragment() {
        super(R.layout.fragment_new_coupon);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity activity = ((HomeActivity)getActivity());
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                expDate = ((Long) selection);
                System.out.println(expDate);
            }
        });

        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        CouponViewModel couponViewModel = new ViewModelProvider(getActivity()).get(CouponViewModel.class);
        couponViewModel.setUser(userViewModel.getUser());
        Button addCoupon = view.findViewById(R.id.addCouponButton);
        MaterialButton takePicture = view.findViewById(R.id.takePicture);
        MaterialButton expDateButton = view.findViewById(R.id.expDate);


        expDateButton.setOnClickListener(v -> {
            datePicker.show(activity.getSupportFragmentManager(), "date picker");
        });

        //Take picture
        takePicture.setOnClickListener(v -> {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            ContentResolver resolver = activity.getContentResolver();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "my_image_"+timeStamp+".jpg");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


            // tell the camera app to store the image at that file pointer
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 0);
        });

        //Add new coupon
        addCoupon.setOnClickListener(v -> {
            TextInputLayout companyEditText = view.findViewById(R.id.companyName);
            TextInputLayout categoryEditText = view.findViewById(R.id.category);
            TextInputLayout dealEditText = view.findViewById(R.id.deal);
            TextInputLayout streetEditText = view.findViewById(R.id.street);
            TextInputLayout cityEditText = view.findViewById(R.id.city);
            TextInputLayout stateEditText = view.findViewById(R.id.state);
            TextInputLayout zipEditText = view.findViewById(R.id.zip);
            String address = streetEditText.getEditText().getText().toString() + " " +
                             cityEditText.getEditText().getText().toString() + " " +
                             stateEditText.getEditText().getText().toString() + " " +
                             zipEditText.getEditText().getText().toString();

            Coupon coupon = new Coupon(
                    companyEditText.getEditText().getText().toString(),
                    categoryEditText.getEditText().getText().toString(),
                    dealEditText.getEditText().getText().toString(),
                    expDate,
                    address,
                    imageUri
            );

            couponViewModel.addCoupon(coupon);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CouponsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
         });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0  && resultCode == Activity.RESULT_OK) {
            //TODO not sure what should go here
        }
    }
}