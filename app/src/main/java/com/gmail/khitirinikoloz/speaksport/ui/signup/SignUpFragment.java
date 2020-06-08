package com.gmail.khitirinikoloz.speaksport.ui.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {

    private SignUpViewModel signUpViewModel;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpViewModel = new ViewModelProvider(this, new SignUpViewModelFactory())
                .get(SignUpViewModel.class);

        final EditText emailEditText = view.findViewById(R.id.email_sign_up_edittext);
        final TextInputLayout emailLayout = view.findViewById(R.id.email_sign_up_layout);
        final EditText usernameEditText = view.findViewById(R.id.username_sign_up_edittext);
        final TextInputLayout usernameLayout = view.findViewById(R.id.username_sign_up_layout);
        final EditText passwordEditText = view.findViewById(R.id.password_sign_up_edittext);
        final TextInputLayout passwordLayout = view.findViewById(R.id.password_sign_up_layout);
        final Button signUpButton = view.findViewById(R.id.btn_sign_up);

        signUpViewModel.getSignUpFormState().observe(getViewLifecycleOwner(), signUpFormState -> {
            if (signUpFormState == null)
                return;
            signUpButton.setEnabled(signUpFormState.isDataValid());
            if (signUpFormState.getEmailError() != null)
                emailLayout.setError(getString(signUpFormState.getEmailError()));
            else
                emailLayout.setErrorEnabled(false);
            if (signUpFormState.getUsernameError() != null)
                usernameLayout.setError(getString(signUpFormState.getUsernameError()));
            else
                usernameLayout.setErrorEnabled(false);
            if (signUpFormState.getPasswordError() != null)
                passwordLayout.setError(getString(signUpFormState.getPasswordError()));
            else
                passwordLayout.setErrorEnabled(false);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                signUpViewModel.signUpDataChanged(emailEditText.getText().toString(),
                        usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && signUpButton.isEnabled()) {
                signUpViewModel.signUp(
                        emailEditText.getText().toString(), usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }

            return false;
        });

        signUpButton.setOnClickListener(v -> signUpViewModel.signUp(
                emailEditText.getText().toString(), usernameEditText.getText().toString(),
                passwordEditText.getText().toString()
        ));

        final TextView loginTextView = view.findViewById(R.id.login);
        loginTextView.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}
