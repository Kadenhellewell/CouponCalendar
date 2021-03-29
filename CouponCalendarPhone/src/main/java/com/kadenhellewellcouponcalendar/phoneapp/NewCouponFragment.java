package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;


public class NewCouponFragment extends Fragment {
    public NewCouponFragment() {
        super(R.layout.fragment_new_coupon);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        CouponViewModel couponViewModel = new ViewModelProvider(getActivity()).get(CouponViewModel.class);
        couponViewModel.setUser(userViewModel.getUser());
        Button addCoupon = view.findViewById(R.id.addCouponButton);
        addCoupon.setOnClickListener(v -> {
            TextInputLayout companyEditText = view.findViewById(R.id.companyName);
            TextInputLayout categoryEditText = view.findViewById(R.id.category);
            TextInputLayout dealEditText = view.findViewById(R.id.deal);
            TextInputLayout expEditText = view.findViewById(R.id.expDate);
            TextInputLayout addressEditText = view.findViewById(R.id.address);
            Coupon coupon = new Coupon(
                    companyEditText.getEditText().getText().toString(),
                    categoryEditText.getEditText().getText().toString(),
                    dealEditText.getEditText().getText().toString(),
                    expEditText.getEditText().getText().toString(),
                    addressEditText.getEditText().getText().toString()
            );

            couponViewModel.addCoupon(coupon);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CouponsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
         });
    }
}