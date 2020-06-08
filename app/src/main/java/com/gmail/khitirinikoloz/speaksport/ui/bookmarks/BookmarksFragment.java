package com.gmail.khitirinikoloz.speaksport.ui.bookmarks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.ui.MainActivity;
import com.gmail.khitirinikoloz.speaksport.R;

public class BookmarksFragment extends Fragment {

    private BookmarksViewModel bookmarksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarksViewModel =
                new ViewModelProvider(this).get(BookmarksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        final TextView textView = root.findViewById(R.id.text_bookmarks);
        bookmarksViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        @SuppressWarnings("ConstantConditions")
        final TextView actionBarText = ((MainActivity) requireActivity()).getSupportActionBar()
                .getCustomView().findViewById(R.id.action_bar_title);
        actionBarText.setText(R.string.title_bookmarks);

        return root;
    }
}
