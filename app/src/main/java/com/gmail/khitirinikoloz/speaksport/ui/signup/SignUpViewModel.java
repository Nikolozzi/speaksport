package com.gmail.khitirinikoloz.speaksport.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.data.signup.SignUpRepository;

class SignUpViewModel extends ViewModel {
    private MutableLiveData<SignUpFormState> signUpFormState = new MutableLiveData<>();
    private SignUpRepository signUpRepository;
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int PASSWORD_MIN_LENGTH = 6;

    SignUpViewModel(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    LiveData<SignUpFormState> getSignUpFormState() {
        return signUpFormState;
    }

    void signUp(String email, String username, String password) {
        signUpRepository.signUp(email, username, password);
    }

    void signUpDataChanged(String email, String username, String password) {
        if (!isEmailValid(email)) {
            signUpFormState.setValue(new SignUpFormState(R.string.invalid_email, null, null));
        } else if (!isUserNameValid(username)) {
            signUpFormState.setValue(new SignUpFormState(null, R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            signUpFormState.setValue(new SignUpFormState(null, null, R.string.invalid_password));
        } else {
            signUpFormState.setValue(new SignUpFormState(true));
        }
    }

    //simple validation for now
    private boolean isEmailValid(String email) {
        return email != null && email.trim().length() > 0 && email.contains("@");
    }

    private boolean isUserNameValid(String username) {
        return username != null && username.trim().length() >= USERNAME_MIN_LENGTH;
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= PASSWORD_MIN_LENGTH;
    }
}
