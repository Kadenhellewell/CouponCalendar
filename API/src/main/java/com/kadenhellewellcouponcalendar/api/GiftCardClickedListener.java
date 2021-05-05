package com.kadenhellewellcouponcalendar.api;

import com.kadenhellewellcouponcalendar.api.models.GiftCard;

public interface GiftCardClickedListener {
    public void usedOnClick(GiftCard giftCard);

    public void updateOnClick(GiftCard giftCard);
}
