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

import com.google.android.material.textfield.TextInputLayout;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;


public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        super(R.layout.fragment_sign_up);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        TextInputLayout email = view.findViewById(R.id.emailEditText);
        TextInputLayout password = view.findViewById(R.id.passwordEditText);
        Button signUp = view.findViewById(R.id.signIn);
        Button signInLink = view.findViewById(R.id.signInLink);
        signUp.setOnClickListener(v -> {
            userViewModel.signUp(
                    email.getEditText().getText().toString(),
                    password.getEditText().getText().toString()
            );
        });

        signInLink.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, SignInFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }
}