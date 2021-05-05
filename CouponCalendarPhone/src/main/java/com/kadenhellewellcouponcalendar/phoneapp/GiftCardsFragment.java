package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kadenhellewellcouponcalendar.api.CouponsAdapter;
import com.kadenhellewellcouponcalendar.api.GiftCardClickedListener;
import com.kadenhellewellcouponcalendar.api.GiftCardsAdapter;
import com.kadenhellewellcouponcalendar.api.models.GiftCard;


public class GiftCardsFragment extends Fragment {

    public GiftCardsFragment() {
        super(R.layout.fragment_gift_cards);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity activity = (HomeActivity) getActivity();

        RecyclerView giftcardList = view.findViewById(R.id.giftcard_List);
        activity.giftCardViewModel.setUser(activity.userViewModel.getUser());
        GiftCardsAdapter adapter = new GiftCardsAdapter(
                activity.giftCardViewModel.getGiftcards(),
                new GiftCardClickedListener() {
                    @Override
                    public void usedOnClick(GiftCard giftCard) {
                        activity.giftCardViewModel.removeGiftCard(giftCard);
                    }

                    @Override
                    public void updateOnClick(GiftCard giftCard) {
                        activity.giftCardViewModel.updateGiftCard(giftCard);
                    }
                }
        );

        giftcardList.setAdapter(adapter);
        giftcardList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}