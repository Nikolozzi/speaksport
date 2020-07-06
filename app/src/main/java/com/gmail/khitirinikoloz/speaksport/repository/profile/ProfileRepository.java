package com.gmail.khitirinikoloz.speaksport.repository.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.UserApi;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.API_BASE_URL;

public class ProfileRepository {

    private static final String LOG_TAG = ProfileRepository.class.getSimpleName();
    public static final int FAILED_REQUEST_CODE = -1;
    private UserApi userApi;
    private static volatile ProfileRepository instance;

    private ProfileRepository() {
        this.initializeUserApi();
    }

    public static ProfileRepository getInstance() {
        if (instance == null) {
            synchronized (ProfileRepository.class) {
                if (instance == null)
                    instance = new ProfileRepository();
            }
        }
        return instance;
    }

    public void updateUser(@NonNull final User user, @NonNull final MutableLiveData<Integer> statusLiveData) {
        Call<Void> call = userApi.updateUser(user.getId(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                statusLiveData.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                statusLiveData.postValue(FAILED_REQUEST_CODE);
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    public void getUser(@NonNull final Long userId, @NonNull final MutableLiveData<UserResponse> userResponse) {
        Call<UserResponse> call = userApi.getUserById(userId);
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

    public void getUserByPassword(@NonNull final User user,
                                  @NonNull final MutableLiveData<UserResponse> userResponse) {
        Call<UserResponse> call = userApi.getUserByPassword(user.getId(), user.getPassword());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse responseData = response.body();
                    responseData.setResponseCode(response.code());
                    userResponse.postValue(responseData);
                    return;
                }

                userResponse.postValue(new UserResponse(false, response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                userResponse.postValue(new UserResponse(true, null));
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    private void initializeUserApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);
    }
}