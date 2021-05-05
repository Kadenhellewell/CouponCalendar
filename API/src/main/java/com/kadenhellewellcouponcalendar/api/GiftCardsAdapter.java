package com.kadenhellewellcouponcalendar.api;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;

import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.models.GiftCard;

import org.w3c.dom.Text;

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
        GiftCard giftCard = data.get(position);
        TextView company = holder.getItemView().findViewById(R.id.company);
        company.setText(giftCard.company);

        TextView amount = holder.getItemView().findViewById(R.id.amount);
        amount.setText(Double.toString(giftCard.amount));

        TextView exp = holder.getItemView().findViewById(R.id.exp);
        exp.setText(giftCard.expDateString);

        Button useGiftcard = holder.getItemView().findViewById(R.id.useGiftcard);
        useGiftcard.setOnClickListener(e -> {
            listener.usedOnClick(giftCard);
        });

        Button updateGiftcard = holder.getItemView().findViewById(R.id.updateGiftcard);
        updateGiftcard.setOnClickListener(e -> {
            listener.updateOnClick(giftCard);
        });
    }
}
