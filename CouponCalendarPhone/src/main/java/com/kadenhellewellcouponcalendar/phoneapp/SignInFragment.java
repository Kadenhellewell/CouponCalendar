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


public class SignInFragment extends Fragment {
    public SignInFragment() {
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText email = view.findViewById(R.id.emailEditText);
        EditText password = view.findViewById(R.id.passwordEditText);
        Button signIn = view.findViewById(R.id.signIn);
        Button signUp = view.findViewById(R.id.signUpLink);
        signIn.setOnClickListener(v -> {
            view.viewModel.signIn(
                    email.getText().toString(),
                    password.getText().toString()
            );
        });

        signUp.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, SignUpFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }
}

