package com.gmail.khitirinikoloz.speaksport.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gmail.khitirinikoloz.speaksport.model.Post;

public final class HomeViewModel extends AndroidViewModel {
    //temporary impl to build bridge between homeFragment and eventPostFragment
    private static MutableLiveData<Post> post;

    public HomeViewModel(Application application) {
        super(application);
        if (post == null) {
            post = new MutableLiveData<>();
        }
    }

    public void setPost(Post newPost) {
        post.postValue(newPost);
    }

    public MutableLiveData<Post> getPost() {
        return post;
    }
}