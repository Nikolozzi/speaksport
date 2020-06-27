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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import static com.gmail.khitirinikoloz.speaksport.ui.login.LoginActivity.FAILED_REQUEST_MESSAGE;

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
        final ProgressBar progressBar = view.findViewById(R.id.progress_sign_up);

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

        signUpButton.setOnClickListener(v -> {
            signUpViewModel.checkEmailAddress(getUserFromInput());
            progressBar.setVisibility(View.VISIBLE);
        });

        signUpViewModel.getEmailError().observe(getViewLifecycleOwner(), emailError -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                if (emailError.isFailedRequest()) {
                    showErrorSnackBar(view, FAILED_REQUEST_MESSAGE);
                    return;
                }
                if (EmailError.NO_EMAIL_ERROR.equals(emailError.getEmailResponse())) {
                    //email is unique
                    signUpViewModel.checkUsername(getUserFromInput());
                    return;
                }

                emailLayout.setError(emailError.getEmailResponse());
            }
        });

        signUpViewModel.getUsernameError().observe(getViewLifecycleOwner(), usernameError -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                if (usernameError.isFailedRequest()) {
                    showErrorSnackBar(view, FAILED_REQUEST_MESSAGE);
                    return;
                }
                if (UsernameError.NO_USERNAME_ERROR.equals(usernameError.getUsernameResponse())) {
                    //username is unique. this method is called after the email check call response is returned
                    signUpViewModel.registerUser(getUserFromInput());
                    return;
                }

                usernameLayout.setError(usernameError.getUsernameResponse());
            }
        });

        signUpViewModel.getUserData().observe(getViewLifecycleOwner(), userResponse -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                if (userResponse.isFailedRequest()) {
                    showErrorSnackBar(view, FAILED_REQUEST_MESSAGE);
                    return;
                }
                if (userResponse.getResponseCode() < 200 || userResponse.getResponseCode() >= 300) {
                    showErrorSnackBar(view, "Could not sign you up, unexpected error occurred");
                    return;
                }
                signUpCallback.onSignUp(new User(userResponse));
                closeKeyboard(view);
                requireActivity().onBackPressed();
            }
        });

        final TextView loginTextView = view.findViewById(R.id.login);
        loginTextView.setOnClickListener(v -> {
            closeKeyboard(view);
            requireActivity().onBackPressed();
        });
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

    private void showErrorSnackBar(final View rootView, String message) {
        final ScrollView scrollView = rootView.findViewById(R.id.sign_up_scrollview);
        scrollView.fullScroll(View.FOCUS_UP);

        final CoordinatorLayout coordinatorLayout = rootView.findViewById(R.id.snackbar_signup_failure);
        coordinatorLayout.setVisibility(View.VISIBLE);
        coordinatorLayout.bringToFront();
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.color_failure));
        final Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setRotation(180);
        final TextView snackbarText = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(18);
        snackbar.show();
    }

    public interface OnSignUpCallback {
        void onSignUp(User user);
    }
}
