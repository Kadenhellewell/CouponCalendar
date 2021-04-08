package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kadenhellewellcouponcalendar.api.CouponsAdapter;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;


public class CouponsFragment extends Fragment {
    public CouponsFragment() {
        super(R.layout.fragment_coupons);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        RecyclerView couponList = view.findViewById(R.id.couponList);
        CouponViewModel couponViewModel = new ViewModelProvider(getActivity()).get(CouponViewModel.class);
        couponViewModel.setUser(userViewModel.getUser());
        CouponsAdapter adapter = new CouponsAdapter(
                couponViewModel.getCoupons(),
                (couponViewModel::removeCoupon)
        );

        couponList.setAdapter(adapter);
        couponList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}