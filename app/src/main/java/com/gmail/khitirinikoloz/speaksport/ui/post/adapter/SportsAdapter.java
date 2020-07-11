package com.gmail.khitirinikoloz.speaksport.ui.post.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.khitirinikoloz.speaksport.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportsViewHolder> {
    private final List<String> sportNames;
    private final PopupWindow popupWindow;
    private final TextInputEditText topicEditText;

    public SportsAdapter(List<String> sportNames, PopupWindow popupWindow, TextInputEditText topicEditText) {
        this.sportNames = sportNames;
        this.popupWindow = popupWindow;
        this.topicEditText = topicEditText;
    }

    @NonNull
    @Override
    public SportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sportItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);
        return new SportsViewHolder(sportItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SportsViewHolder holder, int position) {
        holder.sportsText.setText(sportNames.get(position));
        holder.sportsText.setOnClickListener(v -> {
            CharSequence selectedSport = ((TextView) v).getText();
            topicEditText.setText(selectedSport);
            topicEditText.setError(null);
            popupWindow.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return sportNames.size();
    }

     static class SportsViewHolder extends RecyclerView.ViewHolder {
        private TextView sportsText;

        SportsViewHolder(@NonNull View itemView) {
            super(itemView);
            sportsText = itemView.findViewById(R.id.sport_item);
        }
    }
}
