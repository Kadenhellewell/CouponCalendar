package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        HomeActivity activity = (HomeActivity) getActivity();

        //TODO add drop down menu for categories.
        TextView categoryTextView = view.findViewById(R.id.category_text_view);
        registerForContextMenu(categoryTextView);

        RecyclerView couponList = view.findViewById(R.id.couponList);
        activity.couponViewModel.setUser(activity.userViewModel.getUser());
        CouponsAdapter adapter = new CouponsAdapter(
                activity.couponViewModel.getFilteredCoupons(),
                (activity.couponViewModel::removeCoupon) //on clicked listener
        );


        couponList.setAdapter(adapter);
        couponList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category_menu, menu);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        HomeActivity activity = (HomeActivity) getActivity();
        TextView contextMenuTextView = (TextView) v;
        menu.add("All").setOnMenuItemClickListener(item -> {
            activity.couponViewModel.setCouponFilter("All");
            return true;
        });

        menu.add("Food").setOnMenuItemClickListener(item -> {
            // Set adapter to food
            activity.couponViewModel.setCouponFilter("Food");
            return true;
        });

        menu.add("Shopping").setOnMenuItemClickListener(item -> {
            // Set adapter for shopping
            activity.couponViewModel.setCouponFilter("Shopping");
            return true;
        });

        menu.add("Miscellaneous").setOnMenuItemClickListener(item -> {
            activity.couponViewModel.setCouponFilter("Miscellaneous");
            return true;
        });
    }
}