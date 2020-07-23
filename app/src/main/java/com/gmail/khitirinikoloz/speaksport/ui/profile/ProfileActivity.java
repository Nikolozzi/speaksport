package com.gmail.khitirinikoloz.speaksport.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gmail.khitirinikoloz.speaksport.BuildConfig;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.User;
import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;
import com.gmail.khitirinikoloz.speaksport.ui.login.LoggedInUser;
import com.gmail.khitirinikoloz.speaksport.ui.login.SessionManager;
import com.gmail.khitirinikoloz.speaksport.ui.profile.util.PathUtil;
import com.gmail.khitirinikoloz.speaksport.ui.signup.UserValidator;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.gmail.khitirinikoloz.speaksport.repository.Constants.NOT_FOUND;
import static com.gmail.khitirinikoloz.speaksport.repository.Constants.SUCCESS;
import static com.gmail.khitirinikoloz.speaksport.repository.profile.ProfileRepository.FAILED_REQUEST_CODE;
import static com.gmail.khitirinikoloz.speaksport.repository.signup.error.EmailError.NO_EMAIL_ERROR;
import static com.gmail.khitirinikoloz.speaksport.repository.signup.error.UsernameError.NO_USERNAME_ERROR;
import static com.gmail.khitirinikoloz.speaksport.ui.login.LoginActivity.FAILED_REQUEST_MESSAGE;

public class ProfileActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();
    public static final String ACTION_USER_UPDATE_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_USER_UPDATE_BROADCAST";
    private static final int REQUEST_PICK_IMG = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    public static final int IMAGE_QUALITY = 20;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText fullNameEditText;
    private EditText descriptionEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private TextView editTextView;
    private TextView saveTextView;
    private TextView editPasswordTextView;
    private TextView savePasswordTextView;
    private LoggedInUser loggedInUser;

    private TextInputLayout usernameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout oldPasswordLayout;
    private TextInputLayout newPasswordLayout;
    private ProgressBar progressBar;

    private ProfileViewModel profileViewModel;
    private User updatedUser;
    private User currentUser;
    private Uri imageUri;
    private ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.profile_title));


        SessionManager sessionManager = new SessionManager(this);
        loggedInUser = sessionManager.getLoggedInUser();

        profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory())
                .get(ProfileViewModel.class);

        usernameEditText = findViewById(R.id.profile_username_edittext);
        emailEditText = findViewById(R.id.profile_email_edittext);
        fullNameEditText = findViewById(R.id.profile_full_name_edittext);
        descriptionEditText = findViewById(R.id.profile_description_edittext);
        oldPasswordEditText = findViewById(R.id.profile_old_password_edittext);
        newPasswordEditText = findViewById(R.id.profile_new_password_edittext);

        usernameLayout = findViewById(R.id.profile_username_layout);
        emailLayout = findViewById(R.id.profile_email_layout);
        oldPasswordLayout = findViewById(R.id.profile_old_password_layout);
        newPasswordLayout = findViewById(R.id.profile_new_password_layout);
        progressBar = findViewById(R.id.progress_get_user);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        saveTextView = findViewById(R.id.textview_save);
        editTextView = findViewById(R.id.textview_edit);
        editTextView.setOnClickListener(v -> editFields(v, saveTextView));
        saveTextView.setOnClickListener(v -> saveFields());

        savePasswordTextView = findViewById(R.id.textview_password_save);
        editPasswordTextView = findViewById(R.id.textview_password_edit);
        editPasswordTextView.setOnClickListener(v -> editPasswordFields(v, savePasswordTextView));
        savePasswordTextView.setOnClickListener(v -> savePasswordFields());
        final FrameLayout profileContainer = findViewById(R.id.profile_img_container);
        profileContainer.setOnClickListener(v -> requestPermissions());
        profileImg = findViewById(R.id.profile_img);
        profileViewModel.getUser(loggedInUser.getUserId());

        this.observeUser();
        this.observeUsername();
        this.observeEmail();
        this.observeUpdatedUser();
        this.observeUserPasswordResponse();
        this.observeUploadedImage(sessionManager);
        this.observeUserAvatar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMG && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                final String path = PathUtil.getRealPath(this, imageUri);
                final File photo = new File(path);
                try {
                    final Bitmap bitmap = BitmapFactory.decodeFile(photo.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, new FileOutputStream(photo));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.VISIBLE);
                profileViewModel.uploadUserAvatar(loggedInUser.getUserId(), photo);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            for (int grant : grantResults) {
                if (grant == PackageManager.PERMISSION_GRANTED) {
                    this.selectPhoto();
                    break;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectPhoto() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMG);
    }

    private void requestPermissions() {
        final String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        else
            this.selectPhoto();
    }

    private void observeUploadedImage(final SessionManager sessionManager) {
        profileViewModel.getImageStatusLiveData().observe(this, imgStatusCode -> {
            if (imgStatusCode == SUCCESS) {

                if (imageUri != null) {
                    Glide.with(this)
                            .load(imageUri)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    if (resource != null) {
                                        progressBar.setVisibility(View.GONE);
                                        Bitmap bitmapDrawable = ((BitmapDrawable) resource).getBitmap();
                                        String absPath = savePhotoToStorage(bitmapDrawable);
                                        sessionManager.saveProfileImage(absPath);
                                    }
                                    return false;
                                }
                            }).into(profileImg);
                }
            }
        });
    }

    private String savePhotoToStorage(Bitmap bitmap) {
        final ContextWrapper contextWrapper = new ContextWrapper(this);
        final File directory = contextWrapper.getDir("images", Context.MODE_PRIVATE);
        final File path = new File(directory, "profile.jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void observeUsername() {
        profileViewModel.getUsernameError().observe(this, usernameError -> {
            if (usernameError.isFailedRequest()) {
                showToast(FAILED_REQUEST_MESSAGE, R.color.color_failure);
                return;
            }
            if (updatedUser != null) {
                if (NO_USERNAME_ERROR.equals(usernameError.getUsernameResponse()) ||
                        loggedInUser.getUsername().equals(updatedUser.getUsername())) {
                    profileViewModel.checkEmailAddress(updatedUser);
                    return;
                }

                if (!updatedUser.getUsername().equals(loggedInUser.getUsername()))
                    usernameLayout.setError(usernameError.getUsernameResponse());
            }
        });
    }

    private void observeEmail() {
        profileViewModel.getEmailError().observe(this, emailError -> {
            if (emailError.isFailedRequest()) {
                showToast(FAILED_REQUEST_MESSAGE, R.color.color_failure);
                return;
            }
            if (updatedUser != null) {
                if (NO_EMAIL_ERROR.equals(emailError.getEmailResponse()) ||
                        loggedInUser.getEmail().equals(updatedUser.getEmail())) {
                    updatedUser.setPhoto(currentUser.getPhoto());
                    profileViewModel.updateUser(updatedUser);
                    return;
                }

                if (!updatedUser.getEmail().equals(loggedInUser.getEmail()))
                    emailLayout.setError(emailError.getEmailResponse());
            }
        });
    }

    private void observeUpdatedUser() {
        profileViewModel.getStatusLiveData().observe(this, statusCode -> {
            if (statusCode == SUCCESS && updatedUser != null) {
                showToast("Successfully updated", R.color.color_success);
                saveTextView.setVisibility(View.GONE);
                editTextView.setVisibility(View.VISIBLE);

                //if the observer was triggered by the password update response
                savePasswordTextView.setVisibility(View.GONE);
                editPasswordTextView.setVisibility(View.VISIBLE);

                Intent userUpdateBroadcast = new Intent(ACTION_USER_UPDATE_BROADCAST);
                userUpdateBroadcast.putExtra("user", updatedUser);
                LocalBroadcastManager.getInstance(this)
                        .sendBroadcast(userUpdateBroadcast);

                this.freezeAllFields();
                this.closeKeyboard();
                return;
            }

            //might also check the response code 404 for unauthorized calls when using tokens.
            if (statusCode == FAILED_REQUEST_CODE) {
                showToast(FAILED_REQUEST_MESSAGE, R.color.color_failure);
            }
        });
    }

    private void observeUser() {
        profileViewModel.getUserResponse().observe(this, userResponse -> {
            if (userResponse.isFailedRequest()) {
                showToast(FAILED_REQUEST_MESSAGE, R.color.color_failure);
                return;
            }
            if (userResponse.getResponseCode() == SUCCESS) {
                this.copyUserIntoFields(userResponse);
                currentUser = new User(userResponse);
                if (userResponse.getPhoto() != null)
                    profileViewModel.getUserAvatar(userResponse.getPhoto().getPath());
                else
                    progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void observeUserAvatar() {
        profileViewModel.getAvatarLiveData().observe(this, imgByteArr -> {
            if (imgByteArr != null)
                Glide.with(this).load(imgByteArr).into(profileImg);

            progressBar.setVisibility(View.GONE);
        });
    }

    private void observeUserPasswordResponse() {
        profileViewModel.getUserPasswordResponse().observe(this, userResponse -> {
            if (userResponse.isFailedRequest()) {
                showToast(FAILED_REQUEST_MESSAGE, R.color.color_failure);
                return;
            }
            if (userResponse.getResponseCode() == NOT_FOUND) {
                oldPasswordLayout.setError("password is not correct");
                return;
            }
            if (userResponse.getResponseCode() == SUCCESS) {
                oldPasswordLayout.setErrorEnabled(false);
                final String newPassword = newPasswordEditText.getText().toString();
                if (!UserValidator.isPasswordValid(newPassword)) {
                    newPasswordLayout.setError(getString(R.string.invalid_password));
                    return;
                }
                updatedUser = new User(userResponse);
                updatedUser.setPassword(newPassword);
                updatedUser.setPhoto(currentUser.getPhoto()); //to make sure photo object is set with others as well
                profileViewModel.updateUser(updatedUser);
            }
        });
    }

    private void copyUserIntoFields(UserResponse userResponse) {
        usernameEditText.setText(userResponse.getUsername());
        emailEditText.setText(userResponse.getEmail());
        fullNameEditText.setText(userResponse.getFullName());
        descriptionEditText.setText(userResponse.getDescription());
    }

    private void saveFields() {
        final String username = usernameEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        if (!UserValidator.isUsernameValid(username)) {
            usernameLayout.setError(getString(R.string.invalid_username));
            return;
        }
        if (!UserValidator.isEmailValid(email)) {
            emailLayout.setError(getString(R.string.invalid_email));
            return;
        }

        final String fullName = fullNameEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();

        // TODO: use builder pattern
        updatedUser = new User();
        updatedUser.setId(loggedInUser.getUserId());
        updatedUser.setUsername(username);
        updatedUser.setEmail(email);
        updatedUser.setFullName(fullName);
        updatedUser.setDescription(description);

        profileViewModel.checkUsername(updatedUser);
    }

    private void savePasswordFields() {
        final String oldPassword = oldPasswordEditText.getText().toString();

        final User user = new User();
        user.setId(loggedInUser.getUserId());
        user.setPassword(oldPassword);
        profileViewModel.getUserByPassword(user);
    }

    private void editFields(final View view, final View saveTextView) {
        unFreezeFields();
        view.setVisibility(View.INVISIBLE);
        saveTextView.setVisibility(View.VISIBLE);
    }

    private void editPasswordFields(final View view, final View savePasswordTextView) {
        oldPasswordEditText.setFocusableInTouchMode(true);
        newPasswordEditText.setFocusableInTouchMode(true);
        view.setVisibility(View.INVISIBLE);
        savePasswordTextView.setVisibility(View.VISIBLE);
    }

    private void freezeAllFields() {
        usernameEditText.setFocusable(false);
        emailEditText.setFocusable(false);
        fullNameEditText.setFocusable(false);
        descriptionEditText.setFocusable(false);
        oldPasswordEditText.setFocusable(false);
        newPasswordEditText.setFocusable(false);

        if (emailLayout.isErrorEnabled())
            emailLayout.setErrorEnabled(false);
        if (usernameLayout.isErrorEnabled())
            usernameLayout.setErrorEnabled(false);
        if (newPasswordLayout.isErrorEnabled())
            newPasswordLayout.setErrorEnabled(false);
    }

    private void unFreezeFields() {
        usernameEditText.setFocusableInTouchMode(true);
        emailEditText.setFocusableInTouchMode(true);
        fullNameEditText.setFocusableInTouchMode(true);
        descriptionEditText.setFocusableInTouchMode(true);
    }

    private void showToast(final String message, @ColorRes final int color) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 50);
        View view = toast.getView();
        view.getBackground().setColorFilter(new PorterDuffColorFilter(getResources()
                .getColor(color), PorterDuff.Mode.SRC_IN));
        TextView textView = view.findViewById(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        toast.show();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
    }
}
