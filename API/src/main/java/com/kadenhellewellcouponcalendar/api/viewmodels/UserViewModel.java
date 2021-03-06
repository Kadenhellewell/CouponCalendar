package com.kadenhellewellcouponcalendar.api.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kadenhellewellcouponcalendar.api.models.Coupon;
import com.kadenhellewellcouponcalendar.api.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserViewModel extends ViewModel {
    FirebaseAuth auth;
    DatabaseReference database;
    MutableLiveData<User> user = new MutableLiveData<>();
    //    MutableLiveData<RuntimeException> loginError = new MutableLiveData<>();
    public UserViewModel() {
        this.auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fbUser = auth.getCurrentUser();
        this.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = auth.getCurrentUser();
//                loginError.setValue(null);
                if (fbUser == null) {
                    user.setValue(null);
                } else {
                    user.setValue(new User(fbUser));
                }
            }
        });
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password);
//        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                AuthResult result = task.getResult();
//                if (result.getUser() == null) {
//                    loginError.setValue(new RuntimeException("Signup failed"));
//                }
//            }
//        });
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }

    public void storeNewUserSpecificCoupon(Coupon coupon) {
        if (user.getValue() == null) return;
        database.child("userData").child(user.getValue().uid).child("coupons").push().setValue(coupon);
    }
}

