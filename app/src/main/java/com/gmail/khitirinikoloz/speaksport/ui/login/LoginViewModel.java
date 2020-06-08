package com.gmail.khitirinikoloz.speaksport.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.data.login.LoginRepository;
import com.gmail.khitirinikoloz.speaksport.data.login.Result;
import com.gmail.khitirinikoloz.speaksport.data.login.model.LoggedInUser;
import com.gmail.khitirinikoloz.speaksport.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (isUserNameAndPasswordValid(username, password))
            loginFormState.setValue(new LoginFormState(true));
        else
            loginFormState.setValue(new LoginFormState(false));
    }

    private boolean isUserNameAndPasswordValid(String username, String password) {
        return (username != null && username.trim().length() > 0) &&
                (password != null && password.trim().length() > 0);
    }
}
