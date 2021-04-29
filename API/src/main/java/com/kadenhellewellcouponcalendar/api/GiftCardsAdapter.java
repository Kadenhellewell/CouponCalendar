package com.kadenhellewellcouponcalendar.api;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.models.GiftCard;

public class GiftCardsAdapter extends CustomAdapter<GiftCard> {
    GiftCardClickedListener listener;

    public GiftCardsAdapter(ObservableArrayList<GiftCard> data, GiftCardClickedListener listener) {
        super(data);
        this.listener = listener;
    }

    @Override
    protected int getLayout() {
        return R.layout.giftcard_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO set text for text views and on click listeners for buttons

    }
}
