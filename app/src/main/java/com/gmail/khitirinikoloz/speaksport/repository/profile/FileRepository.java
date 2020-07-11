package com.gmail.khitirinikoloz.speaksport.repository.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.api.FileApi;
import com.gmail.khitirinikoloz.speaksport.model.User;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.API_BASE_URL;

public class FileRepository {
    private static final String LOG_TAG = FileRepository.class.getSimpleName();
    private static volatile FileRepository instance;
    private FileApi fileApi;

    private FileRepository() {
        this.initializeFileApi();
    }

    public static FileRepository getInstance() {
        if (instance == null) {
            synchronized (FileRepository.class) {
                if (instance == null)
                    instance = new FileRepository();
            }
        }
        return instance;
    }

    public void uploadUserAvatar(final long userId,
                                 @NonNull final File photo,
                                 @NonNull final MutableLiveData<Integer> imageStatusLiveData) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), photo);
        final MultipartBody.Part part = MultipartBody.Part.createFormData("file",
                photo.getName(), requestBody);

        Call<Void> call = fileApi.uploadUserImage(userId, part);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                imageStatusLiveData.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                imageStatusLiveData.postValue(-1);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    public void getUserAvatar(@NonNull final String path,
                              @NonNull final MutableLiveData<byte[]> avatarLiveData) {
        Call<ResponseBody> call = fileApi.getUserImage(path);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        avatarLiveData.postValue(response.body().bytes());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                avatarLiveData.postValue(null);
                Log.d(LOG_TAG, t.toString());
            }
        });
    }


    private void initializeFileApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fileApi = retrofit.create(FileApi.class);
    }
}
