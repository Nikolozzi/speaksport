package com.gmail.khitirinikoloz.speaksport.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.profile.FileRepository;
import com.gmail.khitirinikoloz.speaksport.repository.profile.ProfileRepository;
import com.gmail.khitirinikoloz.speaksport.repository.signup.SignUpRepository;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

import java.io.File;

class ProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository;
    private SignUpRepository signUpRepository;
    private FileRepository fileRepository;
    private MutableLiveData<EmailError> emailError = new MutableLiveData<>();
    private MutableLiveData<UsernameError> usernameError = new MutableLiveData<>();
    private MutableLiveData<UserResponse> userResponse = new MutableLiveData<>();
    private MutableLiveData<UserResponse> userPasswordResponse = new MutableLiveData<>();
    private MutableLiveData<Integer> statusLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> imageStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<byte[]> avatarLiveData = new MutableLiveData<>();

    ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.signUpRepository = SignUpRepository.getInstance();
        this.fileRepository = FileRepository.getInstance();
    }

    void checkEmailAddress(final User user) {
        signUpRepository.checkEmailAddress(user.getEmail(), emailError);
    }

    void checkUsername(final User user) {
        signUpRepository.checkUsername(user.getUsername(), usernameError);
    }

    LiveData<EmailError> getEmailError() {
        return emailError;
    }

    LiveData<UsernameError> getUsernameError() {
        return usernameError;
    }

    LiveData<Integer> getStatusLiveData() {
        return statusLiveData;
    }

    LiveData<UserResponse> getUserResponse() {
        return userResponse;
    }

    void updateUser(final User user) {
        profileRepository.updateUser(user, statusLiveData);
    }

    void uploadUserAvatar(final long userId, final File photo) {
        fileRepository.uploadUserAvatar(userId, photo, imageStatusLiveData);
    }

    LiveData<Integer> getImageStatusLiveData() {
        return imageStatusLiveData;
    }

    void getUser(final long userId) {
        profileRepository.getUser(userId, userResponse);
    }

    void getUserAvatar(final String path) {
        fileRepository.getUserAvatar(path, avatarLiveData);
    }

    MutableLiveData<byte[]> getAvatarLiveData() {
        return avatarLiveData;
    }

    LiveData<UserResponse> getUserPasswordResponse() {
        return userPasswordResponse;
    }

    void getUserByPassword(final User user) {
        profileRepository.getUserByPassword(user, userPasswordResponse);
    }
}
