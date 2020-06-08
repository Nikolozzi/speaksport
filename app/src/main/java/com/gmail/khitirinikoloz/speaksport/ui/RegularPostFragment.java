package com.gmail.khitirinikoloz.speaksport.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.entity.RegularPost;
import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.ui.home.HomeViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegularPostFragment extends Fragment {

    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText topicEditText;
    private PopupWindow popUpWindow;
    private View popUpView;
    private HomeViewModel homeViewModel;
    private NewPostActivity newPostActivity;
    private List<TextInputEditText> requiredFields;

    public RegularPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_regular_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newPostActivity = (NewPostActivity) requireActivity();

        //temporary bridge between this fragment and homeFragment to display posts.(is not correct)
        homeViewModel = new ViewModelProvider(newPostActivity).get(HomeViewModel.class);

        titleEditText = view.findViewById(R.id.regular_title_edittext);
        descriptionEditText = view.findViewById(R.id.regular_description_edittext);
        topicEditText = view.findViewById(R.id.regular_topic_edittext);

        requiredFields = new ArrayList<>(Arrays.asList(titleEditText, topicEditText));

        topicEditText.setOnClickListener(v -> {
            newPostActivity.setUpPopupWindow(topicEditText);
            popUpView = newPostActivity.getPopUpWindowView();
            popUpWindow = newPostActivity.getPopupWindow();
            popUpWindow.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
        });

        final Button saveButton = view.findViewById(R.id.regular_save_post_button);
        saveButton.setOnClickListener(v -> savePost());
    }

    private void savePost() {
        if (!allFieldsSet())
            return;

        String title = String.valueOf(titleEditText.getText());
        String description = String.valueOf(descriptionEditText.getText());
        String topic = String.valueOf(topicEditText.getText());
        Post regularPost = new RegularPost(title, description, topic);
        homeViewModel.setPost(regularPost);
        newPostActivity.finish();
    }

    private boolean allFieldsSet() {
        boolean isSet = true;
        for (TextInputEditText editText : requiredFields) {
            if (TextUtils.getTrimmedLength(editText.getText()) == 0) {
                editText.setError("Field not set");
                isSet = false;
            }
        }
        return isSet;
    }

    @Override
    public void onPause() {
        if (popUpWindow != null && popUpWindow.isShowing())
            popUpWindow.dismiss();
        super.onPause();
    }
}
