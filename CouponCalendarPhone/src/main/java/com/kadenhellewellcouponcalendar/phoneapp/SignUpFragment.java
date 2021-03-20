package com.kadenhellewellcouponcalendar.phoneapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        super(R.layout.fragment_sign_up);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText email = view.findViewById(R.id.emailEditText);
        EditText password = view.findViewById(R.id.passwordEditText);
        Button signUp = view.findViewById(R.id.signIn);
        signUp.setOnClickListener(v -> {
            view.viewModel.signUp(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });
    }
}