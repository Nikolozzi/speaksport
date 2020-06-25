package com.gmail.khitirinikoloz.speaksport.ui.signup;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.entity.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {

    private static final String LOG_TAG = SignUpFragment.class.getSimpleName();
    private SignUpViewModel signUpViewModel;
    private EditText emailEditText;
    private TextInputLayout emailLayout;
    private EditText usernameEditText;
    private TextInputLayout usernameLayout;
    private EditText passwordEditText;

    private OnSignUpCallback signUpCallback;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        signUpCallback = (OnSignUpCallback) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        signUpViewModel = new ViewModelProvider(this, new SignUpViewModelFactory())
                .get(SignUpViewModel.class);

        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.email_sign_up_edittext);
        emailLayout = view.findViewById(R.id.email_sign_up_layout);
        usernameEditText = view.findViewById(R.id.username_sign_up_edittext);
        usernameLayout = view.findViewById(R.id.username_sign_up_layout);
        passwordEditText = view.findViewById(R.id.password_sign_up_edittext);
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

        signUpButton.setOnClickListener(v -> signUpViewModel.checkEmailAddress(getUserFromInput()));

        signUpViewModel.getEmailError().observe(getViewLifecycleOwner(), emailError -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (emailError.getEmailError().equals(EmailError.NO_EMAIL_ERROR)) {
                    //email is unique
                    signUpViewModel.checkUsername(getUserFromInput());
                    return;
                }

                emailLayout.setError(emailError.getEmailError());
            }
        });

        signUpViewModel.getUsernameError().observe(getViewLifecycleOwner(), usernameError -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (usernameError.getUsernameError().equals(UsernameError.NO_USERNAME_ERROR)) {
                    //username is unique. this method is called after the email check call response is returned
                    signUpViewModel.registerUser(getUserFromInput());
                    return;
                }

                usernameLayout.setError(usernameError.getUsernameError());
            }
        });

        signUpViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                signUpCallback.onSignUp(user);
                closeKeyboard(view);
                requireActivity().onBackPressed();
            }
        });

        final TextView loginTextView = view.findViewById(R.id.login);
        loginTextView.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void closeKeyboard(final View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private User getUserFromInput() {
        return new User(usernameEditText.getText().toString(),
                emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    public interface OnSignUpCallback {
        void onSignUp(User user);
    }
}
