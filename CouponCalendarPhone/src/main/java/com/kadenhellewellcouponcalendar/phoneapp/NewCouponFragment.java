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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.models.GiftCard;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class NewCouponFragment extends Fragment {
    Uri imageUri = null;
    long expDate = 0;
    String state = "New Coupon";


    public NewCouponFragment() {
        super(R.layout.fragment_new_coupon);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity activity = ((HomeActivity)getActivity());


        //Set up drop down menu
        String[] categoryOptions = {"Food", "Shopping", "Miscellaneous"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.category_menu, categoryOptions);
        TextInputLayout category = view.findViewById(R.id.categoryOptions);
        ((AutoCompleteTextView) category.getEditText()).setAdapter(adapter);
        //Set up drop down menu

        //Buttons
        Button addCoupon = view.findViewById(R.id.addCouponButton);
        MaterialButton takePicture = view.findViewById(R.id.takePicture);
        MaterialButton expDateButton = view.findViewById(R.id.expDate);
        //Buttons

        //Other Views
        TextView newSomething = view.findViewById(R.id.newSomething);
        TextInputLayout dealEditText = view.findViewById(R.id.deal);
        //Other Views

        //Add switch capabilities
        SwitchMaterial switchMaterial = view.findViewById(R.id.switchMat);
        switchMaterial.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (state.equals("New Coupon"))
            {
                state = "New Gift Card";
                dealEditText.setHint("Amount");
                addCoupon.setText("Add Gift Card");
                takePicture.setVisibility(View.GONE);
            }
            else
            {
                state = "New Coupon";
                dealEditText.setHint("Deal");
                addCoupon.setText("Add Coupon");
                takePicture.setVisibility(View.VISIBLE);
            }
            newSomething.setText(state);
        }));
        //Add switch capabilities

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            expDate = ((Long) selection);
            Instant instant = Instant.ofEpochMilli(expDate); // expDateLong is in milliseconds
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            expDateButton.setText(DateTimeFormatter.ofPattern("MM-dd-yyyy").format(dateTime));
        });

        activity.couponViewModel.setUser(activity.userViewModel.getUser());


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

            if (state.equals("New Coupon"))
            {
                Coupon coupon = new Coupon(
                        companyEditText.getEditText().getText().toString(),
                        category.getEditText().getText().toString(),
                        dealEditText.getEditText().getText().toString(),
                        expDate,
                        imageUri
                );

                activity.couponViewModel.addCoupon(coupon);
                activity.redirectToFragment(CouponsFragment.class);
            }
            else
            {
                GiftCard giftCard = new GiftCard(
                        companyEditText.getEditText().getText().toString(),
                        category.getEditText().getText().toString(),
                        Double.parseDouble(dealEditText.getEditText().getText().toString()),
                        expDate
                );

                activity.giftCardViewModel.addGiftCard(giftCard);
                activity.redirectToFragment(GiftCardsFragment.class);
            }
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