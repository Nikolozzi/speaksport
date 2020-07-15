package com.gmail.khitirinikoloz.speaksport.repository.post.subscription;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.SubscriptionApi;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscriptionRepository {
    private static final String LOG_TAG = SubscriptionRepository.class.getSimpleName();
    private static volatile SubscriptionRepository instance;
    private SubscriptionApi subscriptionApi;

    private SubscriptionRepository() {
        this.initializeSubscriptionApi();
    }

    public static SubscriptionRepository getInstance() {
        if (instance == null) {
            synchronized (SubscriptionRepository.class) {
                if (instance == null) {
                    instance = new SubscriptionRepository();
                }
            }
        }
        return instance;
    }


    public void subscribeUser(@NonNull final User user, final long postId,
                              @NonNull final MutableLiveData<Integer> subscriptionResponse) {
        Call<Void> call = subscriptionApi.subscribeUser(user, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                subscriptionResponse.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                subscriptionResponse.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void unSubscribeUser(@NonNull final User user, final long postId,
                                @NonNull final MutableLiveData<Integer> subscriptionResponse) {
        Call<Void> call = subscriptionApi.unSubscribeUser(user, postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                subscriptionResponse.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                subscriptionResponse.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    private void initializeSubscriptionApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        subscriptionApi = retrofit.create(SubscriptionApi.class);
    }
}
