package com.gmail.khitirinikoloz.speaksport.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.login.request.LoginRequest;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.signup.SignUpFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.UNAUTHORIZED;

public class LoginActivity extends AppCompatActivity implements SignUpFragment.OnSignUpCallback {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    public static final String FAILED_REQUEST_MESSAGE = "Something went wrong, try again later";
    private static final String FAILED_AUTHORIZATION_MESSAGE = "Username or password is incorrect";
    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        sessionManager = new SessionManager(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        final Button loginButton = findViewById(R.id.btn_login);
        final TextView signUpBtn = findViewById(R.id.sign_up);
        final ProgressBar progressBar = findViewById(R.id.progress_log_in);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
        });

        loginViewModel.getLoginResponse().observe(this, loginResponse -> {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                if (loginResponse.isFailedRequest()) {
                    showSnackBar(FAILED_REQUEST_MESSAGE,
                            ContextCompat.getColor(this, R.color.color_failure));
                    return;
                }
                if (loginResponse.getResponseCode() == UNAUTHORIZED) {
                    showSnackBar(FAILED_AUTHORIZATION_MESSAGE,
                            ContextCompat.getColor(this, R.color.color_failure));
                    return;
                }
                //guaranteed successful login
                if (loginResponse.getResponseCode() >= 200 && loginResponse.getResponseCode() < 300) {
                    final LoggedInUser loggedInUser = new LoggedInUser(loginResponse.getUserId(),
                            loginResponse.getUsername(), loginResponse.getEmail(), loginResponse.getToken());
                    sessionManager.createLoginSession(loggedInUser);

                    finish();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> {
            loginViewModel.login(getLoginDetails());
            progressBar.setVisibility(View.VISIBLE);
        });
        signUpBtn.setOnClickListener(v -> openSignUpFragment());
    }

    private void openSignUpFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.login_container, SignUpFragment.class, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private LoginRequest getLoginDetails() {
        return new LoginRequest(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void onSignUp(User user) {
        final String message = String.format("Welcome %s, please Log in", user.getUsername());
        showSnackBar(message, ContextCompat.getColor(this, R.color.color_success));
    }

    private void showSnackBar(final String message, @ColorInt int color) {
        final CoordinatorLayout coordinatorLayout = findViewById(R.id.snackbar_signup_success);
        coordinatorLayout.setVisibility(View.VISIBLE);
        coordinatorLayout.bringToFront();
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setBackgroundTint(color);
        final Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setRotation(180);
        final TextView snackbarText = snackBarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(18);
        snackbar.show();
    }
}
