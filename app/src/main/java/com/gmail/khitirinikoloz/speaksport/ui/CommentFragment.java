package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gmail.khitirinikoloz.speaksport.R;

public class CommentFragment extends Fragment {

    private EditText commentEditText;
    private MainActivity mainActivity;
    private OnPublishCommentCallBack publishCommentCallBack;

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
        publishCommentCallBack = mainActivity;

        //noinspection ConstantConditions
        mainActivity.getSupportActionBar().hide();
        final TextView dismissView = view.findViewById(R.id.dismiss);
        final TextView postView = view.findViewById(R.id.post);
        postView.setOnClickListener(v -> postComment());
        dismissView.setOnClickListener(v -> closeFragment(view));
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

    private void closeFragment(final View view) {
        commentEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) mainActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        requireActivity().onBackPressed();
    }

    private void postComment() {
        final String comment = commentEditText.getText().toString();
        if (TextUtils.getTrimmedLength(comment) == 0)
            return;

        publishCommentCallBack.onPublishComment(comment);

        //start the background task which writes the comment to the database, then fetch new comments
        //and display them
        //noinspection ConstantConditions
        ((InputMethodManager) mainActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getView().getWindowToken(), 0);
        requireActivity().onBackPressed();
    }

    public interface OnPublishCommentCallBack {
        void onPublishComment(final String comment);
    }
}
