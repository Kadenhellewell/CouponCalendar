package com.kadenhellewellcouponcalendar.api.viewmodels;

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
import com.kadenhellewellcouponcalendar.api.models.User;

public class CouponViewModel extends ViewModel {
    private ObservableArrayList<Coupon> coupons;
    private DatabaseReference db;
    //MutableLiveData<User> user = new MutableLiveData<>();

    public CouponViewModel()
    {
        db = FirebaseDatabase.getInstance().getReference();
        coupons = new ObservableArrayList<Coupon>();
        loadCoupons();
    }

    public ObservableArrayList<Coupon> getCoupons()
    {
        return coupons;
    }

    private void loadCoupons()
    {
        db.child("/coupons").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Coupon coupon = snapshot.getValue(Coupon.class);
                coupon.id = snapshot.getKey();
                coupons.add(coupon);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Coupon coupon = snapshot.getValue(Coupon.class);
                coupon.id = snapshot.getKey();
                int index = coupons.indexOf(coupon);
                coupons.set(index, coupon);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Coupon coupon = snapshot.getValue(Coupon.class);
                coupon.id = snapshot.getKey();
                coupons.remove(coupon);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addCoupon(Coupon newCoupon, MutableLiveData<User> user)
    {
        //TODO somehow connect this to the user specific stuff in the UserViewModel
        if (user.getValue() == null) return;
        db.child("userData").child(user.getValue().uid).child("coupons").push().setValue(newCoupon);
    }

    // Call this when a coupon gets used or expires.
    public void removeCoupon(Coupon usedCoupon)
    {
        //TODO make sure this also updates firebase
        coupons.remove(usedCoupon);
    }
}
