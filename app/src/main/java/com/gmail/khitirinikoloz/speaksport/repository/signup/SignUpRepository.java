package com.gmail.khitirinikoloz.speaksport.repository.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.UserApi;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.API_BASE_URL;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.NOT_FOUND;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.SUCCESS;

public class SignUpRepository {
    private static final String LOG_TAG = SignUpRepository.class.getSimpleName();
    private static volatile SignUpRepository instance;
    private UserApi userApi;
    private MutableLiveData<UserResponse> userResponse = new MutableLiveData<>();
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
                    emailError.postValue(new EmailError
                            ("this email is already taken", false));
                    return;
                }

                if (response.code() == NOT_FOUND) {
                    emailError.postValue(new EmailError(EmailError.NO_EMAIL_ERROR, false));
                    return;
                }

                Log.d(LOG_TAG, "Email request response: \n" + response.raw());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                emailError.postValue(new EmailError(null, true));
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void checkUsername(@NonNull final String username) {
        Call<User> call = userApi.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.code() == SUCCESS) {
                    usernameError.postValue(new UsernameError
                            ("this username is already taken", false));
                    return;
                }
                if (response.code() == NOT_FOUND) {
                    usernameError.postValue(new UsernameError
                            (UsernameError.NO_USERNAME_ERROR, false));
                    return;
                }

                Log.d(LOG_TAG, "Username request response: \n" + response.raw());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                usernameError.postValue(new UsernameError(null, true));
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void registerUser(@NonNull final User user) {
        Call<UserResponse> call = userApi.addUser(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (!response.isSuccessful()) {
                    userResponse.postValue(new UserResponse(false, response.code()));
                    return;
                }

                final UserResponse responseData = response.body();
                if (responseData != null)
                    responseData.setResponseCode(response.code());
                userResponse.postValue(responseData);
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                userResponse.postValue(new UserResponse(true, null));
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    public MutableLiveData<UserResponse> getUserResponse() {
        return userResponse;
    }

    public MutableLiveData<EmailError> getEmailError() {
        return emailError;
    }

    public MutableLiveData<UsernameError> getUsernameError() {
        return usernameError;
    }

    private void initializeUserApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);
    }
}