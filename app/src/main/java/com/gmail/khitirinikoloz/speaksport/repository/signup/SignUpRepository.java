package com.gmail.khitirinikoloz.speaksport.repository.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.UserApi;
import com.gmail.khitirinikoloz.speaksport.entity.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpRepository {

    public static final int SUCCESS = 200;
    public static final int NOT_FOUND = 404;
    private static final String LOG_TAG = SignUpRepository.class.getSimpleName();
    private static volatile SignUpRepository instance;
    private static final String USER_BASE_URL = "http://10.0.2.2:8200/";
    private UserApi userApi;
    private MutableLiveData<User> registeredUser = new MutableLiveData<>();
    private MutableLiveData<EmailError> emailError = new MutableLiveData<>();
    private MutableLiveData<UsernameError> usernameError = new MutableLiveData<>();

    // private constructor : singleton access
    private SignUpRepository() {
        this.initializeUserApi();
    }

    public synchronized static SignUpRepository getInstance() {
        if (instance == null) {
            instance = new SignUpRepository();
        }
        return instance;
    }

    public void checkEmailAddress(@NonNull final String email) {
        Call<User> call = userApi.getUserByEmail(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.code() == SUCCESS) {
                    emailError.postValue(new EmailError("this email is already taken"));
                    return;
                }

                if (response.code() == NOT_FOUND)
                    emailError.postValue(new EmailError(EmailError.NO_EMAIL_ERROR));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    public void checkUsername(@NonNull final String username) {
        Call<User> call = userApi.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.code() == SUCCESS) {
                    usernameError.postValue(new UsernameError("this username is already taken"));
                    return;
                }
                if (response.code() == NOT_FOUND)
                    usernameError.postValue(new UsernameError(UsernameError.NO_USERNAME_ERROR));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    public void registerUser(@NonNull final User user) {
        Call<User> call = userApi.addUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody() != null) {
                        try {
                            Log.d(LOG_TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

                registeredUser.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    public LiveData<User> getRegisteredUser() {
        return registeredUser;
    }

    public MutableLiveData<EmailError> getEmailError() {
        return emailError;
    }

    public MutableLiveData<UsernameError> getUsernameError() {
        return usernameError;
    }

    private void initializeUserApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(USER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);
    }
}