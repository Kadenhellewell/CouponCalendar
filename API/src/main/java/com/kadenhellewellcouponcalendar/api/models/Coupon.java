package com.kadenhellewellcouponcalendar.api.models;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

public class Coupon {
    public String company;
    public String category;
    public String deal;
    public String expDate;
    public String address;


    @Exclude
    public String id;

    public Coupon() {}

    public Coupon(String company, String category, String deal, String expDate, String address)
    {
        this.company = company;
        this.category = category;
        this.deal = deal;
        this.expDate = expDate;
        this.address = address;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(obj instanceof Coupon)
        {
            Coupon other = (Coupon) obj;
            return other.id.equals(id);
        }
        return false;
    }
}
