package com.gmail.khitirinikoloz.speaksport.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.repository.login.LoginRepository;
import com.gmail.khitirinikoloz.speaksport.repository.login.request.LoginRequest;
import com.gmail.khitirinikoloz.speaksport.repository.login.response.LoginResponse;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void login(final LoginRequest loginRequest) {
        loginRepository.login(loginRequest);
    }

    public void loginDataChanged(String username, String password) {
        if (isUserNameAndPasswordValid(username, password))
            loginFormState.setValue(new LoginFormState(true));
        else
            loginFormState.setValue(new LoginFormState(false));
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResponse> getLoginResponse() {
        return loginRepository.getLoginResponse();
    }

    private boolean isUserNameAndPasswordValid(String username, String password) {
        return (username != null && username.trim().length() > 0) &&
                (password != null && password.trim().length() > 0);
    }
}
