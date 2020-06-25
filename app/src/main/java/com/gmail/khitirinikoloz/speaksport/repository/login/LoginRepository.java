package com.gmail.khitirinikoloz.speaksport.repository.login;

import com.gmail.khitirinikoloz.speaksport.repository.login.model.LoggedInUser;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository() {

    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = null;
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
