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
        Button addCoupon = view.findViewById(R.id.addCouponButton);
        addCoupon.setOnClickListener(v -> {
            EditText companyEditText = view.findViewById(R.id.companyName);
            EditText categoryEditText = view.findViewById(R.id.category);
            EditText dealEditText = view.findViewById(R.id.deal);
            EditText expEditText = view.findViewById(R.id.expDate);
            EditText addressEditText = view.findViewById(R.id.address);
            Coupon coupon = new Coupon(
                    companyEditText.getText().toString(),
                    categoryEditText.getText().toString(),
                    dealEditText.getText().toString(),
                    expEditText.getText().toString(),
                    addressEditText.getText().toString()
            );

            couponViewModel.addCoupon(coupon, userViewModel.getUser());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CouponsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }
}