package com.kadenhellewellcouponcalendar.api;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.kadenhellewellcouponcalendar.api.models.Coupon;

public class CouponsAdapter extends CustomAdapter<Coupon>{
    CouponClickedListener listener;
    public CouponsAdapter(ObservableArrayList<Coupon> data, CouponClickedListener listener) {
        super(data);
        this.listener = listener;
    }

    @Override
    protected int getLayout() {
        return R.layout.coupon_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coupon coupon = data.get(position);
        TextView company = holder.getItemView().findViewById(R.id.company);
        TextView deal = holder.getItemView().findViewById(R.id.deal);
        TextView expDate = holder.getItemView().findViewById(R.id.exp);
        TextView address = holder.getItemView().findViewById(R.id.address);

        company.setText(coupon.company);
        deal.setText(coupon.deal);
        expDate.setText(coupon.expDate);
        address.setText(coupon.address);

        Button button = holder.getItemView().findViewById(R.id.useCoupon);
        button.setOnClickListener(view -> {
            listener.onClick(coupon);
        });
    }
}
