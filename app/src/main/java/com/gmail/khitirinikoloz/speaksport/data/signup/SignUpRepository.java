package com.gmail.khitirinikoloz.speaksport.data.signup;

public class SignUpRepository {
    private static volatile SignUpRepository instance;

    private SignUpDataSource dataSource;

    // private constructor : singleton access
    private SignUpRepository(SignUpDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static SignUpRepository getInstance(SignUpDataSource dataSource) {
        if (instance == null) {
            instance = new SignUpRepository(dataSource);
        }
        return instance;
    }

    public void signUp(String email, String username, String password) {
        dataSource.signUp(email, username, password);
    }
}
