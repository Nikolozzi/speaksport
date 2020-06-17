package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gmail.khitirinikoloz.speaksport.R;

public class CommentFragment extends Fragment {

    private EditText commentEditText;
    private MainActivity mainActivity;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) requireActivity();
        commentEditText = view.findViewById(R.id.comment_edittext);

        //noinspection ConstantConditions
        mainActivity.getSupportActionBar().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        commentEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mainActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(commentEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onDetach() {
        //noinspection ConstantConditions
        mainActivity.getSupportActionBar().show();
        super.onDetach();
    }
}
