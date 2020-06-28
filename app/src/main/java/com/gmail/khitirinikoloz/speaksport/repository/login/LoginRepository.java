package com.gmail.khitirinikoloz.speaksport.repository.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.LoginApi;
import com.gmail.khitirinikoloz.speaksport.repository.Constants;
import com.gmail.khitirinikoloz.speaksport.repository.login.request.LoginRequest;
import com.gmail.khitirinikoloz.speaksport.repository.login.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository {

    private static final String LOG_TAG = LoginRepository.class.getSimpleName();
    private LoginApi loginApi;
    private MutableLiveData<LoginResponse> loginResponse;

    private LoginRepository() {
        this.initializeLoginApi();
        loginResponse = new MutableLiveData<>();
    }

    //intentionally made non-singleton
    public static LoginRepository getInstance() {
        return new LoginRepository();
    }

    public void login(@NonNull final LoginRequest loginRequest) {
        Call<LoginResponse> call = loginApi.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse responseData = response.body();
                    responseData.setResponseCode(response.code());
                    loginResponse.postValue(response.body());
                    return;
                }

                loginResponse.postValue(new LoginResponse(false, response.code()));
                Log.d(LOG_TAG, String.valueOf(response.raw()));
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginResponse.postValue(new LoginResponse(true, null));
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public MutableLiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    private void initializeLoginApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginApi = retrofit.create(LoginApi.class);
    }
}
