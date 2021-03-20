package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;


public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        super(R.layout.fragment_sign_up);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        EditText email = view.findViewById(R.id.emailEditText);
        EditText password = view.findViewById(R.id.passwordEditText);
        Button signUp = view.findViewById(R.id.signIn);
        signUp.setOnClickListener(v -> {
            userViewModel.signUp(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });
    }
}