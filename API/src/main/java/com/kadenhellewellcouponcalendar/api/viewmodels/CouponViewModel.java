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
import com.kadenhellewellcouponcalendar.api.models.User;

public class CouponViewModel extends ViewModel {
    private ObservableArrayList<Coupon> coupons = new ObservableArrayList<>();
    private ObservableArrayList<Coupon> filteredCoupons = new ObservableArrayList<>();
    public MutableLiveData<String> currentCategory = new MutableLiveData<>();
    public static final String[] categoryOptions = {"Food", "Shopping", "Miscellaneous"};
    private DatabaseReference db;
    MutableLiveData<User> user;

    public CouponViewModel()
    {
        db = FirebaseDatabase.getInstance().getReference();
        currentCategory.setValue("All");
    }

    public void setUser(MutableLiveData<User> user)
    {
        this.user = user;
    }

    public ObservableArrayList<Coupon> getCoupons()
    {
        if (coupons.size() == 0)
        {
            loadCoupons();
        }
        return coupons;
    }

    public ObservableArrayList<Coupon> getFilteredCoupons()
    {
        if (coupons.size() == 0)
        {
            loadCoupons();
        }
        return filteredCoupons;
    }

    public ObservableArrayList<Coupon> setCouponFilter(String filter)
    {
        if (!currentCategory.getValue().equals(filter))
        {
            filteredCoupons.clear();
            for (Coupon coupon : coupons)
            {
                if (coupon.category.equals(filter))
                {
                    filteredCoupons.add(coupon);
                }
            }
        }

        return getFilteredCoupons();
    }

    private void loadCoupons()
    {
        if (user == null)
        {
            Log.d("User error", "Call setUser");
            return;
        }
        db.child("userData").child(user.getValue().uid).child("coupons").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { 
                Coupon coupon = snapshot.getValue(Coupon.class);
                coupon.id = snapshot.getKey();
                coupon.imageUri = Uri.parse(coupon.uriString);
                coupons.add(coupon);
                if (currentCategory.getValue().equals("All") || currentCategory.getValue().equals(coupon.category))
                {
                    filteredCoupons.add(coupon);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Coupon coupon = snapshot.getValue(Coupon.class);
                coupon.id = snapshot.getKey();
                coupon.imageUri = Uri.parse(coupon.uriString);
                int index = coupons.indexOf(coupon);
                coupons.set(index, coupon);
                if (currentCategory.getValue().equals("All") || currentCategory.getValue().equals(coupon.category))
                {
                    int index2 = filteredCoupons.indexOf(coupon);
                    filteredCoupons.set(index2, coupon);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Coupon coupon = snapshot.getValue(Coupon.class);
                coupon.id = snapshot.getKey();
                coupon.imageUri = Uri.parse(coupon.uriString);
                coupons.remove(coupon);
                if (currentCategory.getValue().equals("All") || currentCategory.getValue().equals(coupon.category))
                {
                    filteredCoupons.remove(coupon);
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

    public void addCoupon(Coupon newCoupon)
    {
        if (user.getValue() == null) return;
        db.child("userData").child(user.getValue().uid).child("coupons").push().setValue(newCoupon);
    }

    // Call this when a coupon gets used or expires.
    public void removeCoupon(Coupon usedCoupon)
    {
        if (user.getValue() == null) return;
        db.child("userData").child(user.getValue().uid).child("coupons").child(usedCoupon.id).removeValue();
    }
}
