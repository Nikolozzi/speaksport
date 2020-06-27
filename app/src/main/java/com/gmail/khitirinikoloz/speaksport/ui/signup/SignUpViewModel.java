package com.gmail.khitirinikoloz.speaksport.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.SignUpRepository;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

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

    void registerUser(final User user) {
        signUpRepository.registerUser(user);
    }

    void checkEmailAddress(final User user) {
        signUpRepository.checkEmailAddress(user.getEmail());
    }

    void checkUsername(final User user) {
        signUpRepository.checkUsername(user.getUsername());
    }

    LiveData<EmailError> getEmailError() {
        return signUpRepository.getEmailError();
    }

    LiveData<UsernameError> getUsernameError() {
        return signUpRepository.getUsernameError();
    }

    LiveData<UserResponse> getUserData() {
        return signUpRepository.getUserResponse();
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
