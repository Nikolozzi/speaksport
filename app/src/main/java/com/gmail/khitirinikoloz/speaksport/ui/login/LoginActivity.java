package com.gmail.khitirinikoloz.speaksport.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.data.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.ui.signup.SignUpFragment;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        sessionManager = new SessionManager(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final EditText usernameEditText = findViewById(R.id.username_edittext);
        final EditText passwordEditText = findViewById(R.id.password_edittext);
        final Button loginButton = findViewById(R.id.btn_login);
        final TextView signUpBtn = findViewById(R.id.sign_up);


        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }

            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && loginButton.isEnabled()) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> loginViewModel.login(usernameEditText.getText().toString(),
                passwordEditText.getText().toString()));

        signUpBtn.setOnClickListener(v -> openSignUpFragment());
    }

    private void openSignUpFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.login_container, SignUpFragment.class, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        //after the login is successful start the user session
        sessionManager.createLoginSession(model.getDisplayName());

        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
