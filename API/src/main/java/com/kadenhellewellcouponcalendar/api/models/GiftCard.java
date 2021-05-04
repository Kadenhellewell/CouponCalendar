package com.kadenhellewellcouponcalendar.api.models;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class GiftCard {
    public String company;
    public String category;
    public double amount;
    public String expDateString;

    @Exclude
    public String id;
    @Exclude
    public LocalDate expDate;


    public GiftCard() {}

    public GiftCard(String company, String category, double amount, long expDateLong)
    {
        this.company = company;
        this.category = category;
        this.amount = amount;
        Instant instant = Instant.ofEpochMilli(expDateLong); // expDateLong is in milliseconds
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        this.expDateString = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(dateTime);
        this.expDate = dateTime.toLocalDate();
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(obj instanceof GiftCard)
        {
            GiftCard other = (GiftCard) obj;
            return other.id.equals(id);
        }
        return false;
    }
}
