package com.gmail.khitirinikoloz.speaksport.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.entity.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final List<Comment> comments;
    private final Context context;

    CommentAdapter(final List<Comment> comments, final Context context) {
        this.comments = new ArrayList<>(comments);
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View commentItem = LayoutInflater.from(context)
                .inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(commentItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);
        Glide.with(context).load(R.drawable.avatar).into(holder.authorImg);
        holder.authorView.setText(currentComment.getUsername());
        holder.commentView.setText(currentComment.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView authorImg;
        private TextView authorView;
        private TextView commentView;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            authorImg = itemView.findViewById(R.id.avatar);
            authorView = itemView.findViewById(R.id.username);
            commentView = itemView.findViewById(R.id.comment_text);
        }
    }
}
