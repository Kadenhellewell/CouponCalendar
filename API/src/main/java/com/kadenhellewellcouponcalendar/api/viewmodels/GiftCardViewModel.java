package com.kadenhellewellcouponcalendar.api.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.models.GiftCard;
import com.kadenhellewellcouponcalendar.api.models.User;

public class GiftCardViewModel extends ViewModel {
    private ObservableArrayList<GiftCard> giftcards = new ObservableArrayList<>();
    private ObservableArrayList<GiftCard> filteredGiftcards = new ObservableArrayList<>();
    public MutableLiveData<String> currentCategroy = new MutableLiveData<>();
    public static final String[] categoryOptions = {"Food", "Shopping", "Miscellaneous"};
    private DatabaseReference db;
    MutableLiveData<User> user;

    public GiftCardViewModel()
    {
        db = FirebaseDatabase.getInstance().getReference();
        currentCategroy.setValue("All");
    }

    public void setUser(MutableLiveData<User> user)
    {
        this.user = user;
    }

    public ObservableArrayList<GiftCard> getGiftcards()
    {
        if (giftcards.size() == 0)
        {
            loadGiftCards();
        }
        return giftcards;
    }

    public ObservableArrayList<GiftCard> getFilteredGiftcards()
    {
        if (giftcards.size() == 0)
        {
            loadGiftCards();
        }
        return filteredGiftcards;
    }

    public ObservableArrayList<GiftCard> setGiftcardFilter(String filter)
    {
        if(!currentCategroy.getValue().equals(filter))
        {
            filteredGiftcards.clear();
            for (GiftCard giftCard : giftcards)
            {
                if (giftCard.category.equals(filter))
                {
                    filteredGiftcards.add(giftCard);
                }
            }
        }
        return filteredGiftcards;
    }

    private void loadGiftCards()
    {
        if (user == null)
        {
            Log.d("User error", "Call setUser");
            return;
        }
        db.child("userData").child(user.getValue().uid).child("giftcards").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GiftCard giftcard = snapshot.getValue(GiftCard.class);
                giftcard.id = snapshot.getKey();
                giftcards.add(giftcard);
                if (currentCategroy.getValue().equals("All") || currentCategroy.getValue().equals(giftcard.category))
                {
                    filteredGiftcards.add(giftcard);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GiftCard giftcard = snapshot.getValue(GiftCard.class);
                giftcard.id = snapshot.getKey();
                int index = giftcards.indexOf(giftcard);
                giftcards.set(index, giftcard);
                if (currentCategroy.getValue().equals("All") || currentCategroy.getValue().equals(giftcard.category))
                {
                    int index2 = filteredGiftcards.indexOf(giftcard);
                    filteredGiftcards.set(index2, giftcard);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                GiftCard giftcard = snapshot.getValue(GiftCard.class);
                giftcard.id = snapshot.getKey();
                giftcards.remove(giftcard);
                if (currentCategroy.getValue().equals("All") || currentCategroy.getValue().equals(giftcard.category))
                {
                    filteredGiftcards.remove(giftcard);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addGiftCard(GiftCard newGiftCard)
    {
        if (user.getValue() == null) return;
        db.child("userData").child(user.getValue().uid).child("giftcards").push().setValue(newGiftCard);
    }

    public void updateGiftCard(GiftCard giftCard)
    {
        if (user.getValue() == null) return;
        db.child("userData").child(user.getValue().uid).child("giftcards").child(giftCard.id).setValue(giftCard);
    }

    // Call this when a coupon gets used or expires.
    public void removeGiftCard(GiftCard usedGiftCard)
    {
        if (user.getValue() == null) return;
        db.child("userData").child(user.getValue().uid).child("giftcards").child(usedGiftCard.id).removeValue();
    }
}
