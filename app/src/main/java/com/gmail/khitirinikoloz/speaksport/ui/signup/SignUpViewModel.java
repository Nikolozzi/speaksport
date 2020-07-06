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
    private MutableLiveData<EmailError> emailError = new MutableLiveData<>();
    private MutableLiveData<UsernameError> usernameError = new MutableLiveData<>();

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
        signUpRepository.checkEmailAddress(user.getEmail(), emailError);
    }

    void checkUsername(final User user) {
        signUpRepository.checkUsername(user.getUsername(), usernameError);
    }

    LiveData<EmailError> getEmailError() {
        return emailError;
    }

    LiveData<UsernameError> getUsernameError() {
        return usernameError;
    }

    LiveData<UserResponse> getUserData() {
        return signUpRepository.getUserResponse();
    }

    void signUpDataChanged(final String email, final String username, final String password) {
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

    private boolean isEmailValid(final String email) {
        return UserValidator.isEmailValid(email);
    }

    private boolean isUserNameValid(final String username) {
        return UserValidator.isUsernameValid(username);
    }

    private boolean isPasswordValid(final String password) {
        return UserValidator.isPasswordValid(password);
    }
}
