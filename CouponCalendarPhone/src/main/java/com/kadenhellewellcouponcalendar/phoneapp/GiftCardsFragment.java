package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView categoryTextView = view.findViewById(R.id.category_text_view_giftCard);
        registerForContextMenu(categoryTextView);

        RecyclerView giftcardList = view.findViewById(R.id.giftcard_List);
        activity.giftCardViewModel.setUser(activity.userViewModel.getUser());
        GiftCardsAdapter adapter = new GiftCardsAdapter(
                activity.giftCardViewModel.getFilteredGiftcards(),
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
            activity.giftCardViewModel.setGiftcardFilter("All");
            return true;
        });

        menu.add("Food").setOnMenuItemClickListener(item -> {
            // Set adapter to food
            activity.giftCardViewModel.setGiftcardFilter("Food");
            return true;
        });

        menu.add("Shopping").setOnMenuItemClickListener(item -> {
            // Set adapter for shopping
            activity.giftCardViewModel.setGiftcardFilter("Shopping");
            return true;
        });

        menu.add("Miscellaneous").setOnMenuItemClickListener(item -> {
            activity.giftCardViewModel.setGiftcardFilter("Miscellaneous");
            return true;
        });
    }
}