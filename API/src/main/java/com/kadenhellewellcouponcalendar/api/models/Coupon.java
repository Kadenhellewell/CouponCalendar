package com.kadenhellewellcouponcalendar.api.models;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.time.*;


public class Coupon {
    public String company;
    public String category;
    public String deal;
    public long expDateLong;
    public String address;
    public String uriString;

    @Exclude
    public String id;
    @Exclude
    public Uri imageUri;
    @Exclude
    public LocalDate expDate;


    public Coupon() {}

    public Coupon(String company, String category, String deal, long expDateLong, String address, Uri uri)
    {
        this.company = company;
        this.category = category;
        this.deal = deal;
        this.expDateLong = expDateLong;
        Instant instant = Instant.ofEpochMilli(expDateLong); // expDateLong is in milliseconds
        //this.expDate = ZonedDateTime.ofInstant(instant, ); //TODO convert instant to LocalDate
        this.address = address;
        this.imageUri = uri;
        this.uriString = "";
        if (uri != null)
        {
            this.uriString = uri.toString();
        }

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
