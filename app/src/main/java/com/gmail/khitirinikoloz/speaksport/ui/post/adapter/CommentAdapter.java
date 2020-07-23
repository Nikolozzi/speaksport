package com.gmail.khitirinikoloz.speaksport.ui.post.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.Comment;
import com.gmail.khitirinikoloz.speaksport.ui.post.util.PostHelper;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private final Context context;

    public CommentAdapter(final Context context) {
        this.comments = new ArrayList<>();
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
        PostHelper.loadUserImage(context, holder.authorImg, currentComment.getUser());
        holder.publicationView.setText(PostHelper.getPublicationTime(currentComment.getCommentedAt()));
        holder.authorView.setText(currentComment.getUser().getUsername());
        holder.commentView.setText(currentComment.getText());
    }

    @Override
    public long getItemId(int position) {
        return comments.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(final List<Comment> comments) {
        this.comments.addAll(comments);
        int startPosition = this.comments.size() - comments.size();
        notifyItemRangeInserted(startPosition, comments.size());
    }

    public void setComment(final Comment comment) {
        if (!comments.contains(comment)) {
            this.comments.add(0, comment);
            notifyItemChanged(0);
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView authorImg;
        private final TextView publicationView;
        private final TextView authorView;
        private final TextView commentView;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            authorImg = itemView.findViewById(R.id.avatar);
            publicationView = itemView.findViewById(R.id.time);
            authorView = itemView.findViewById(R.id.username);
            commentView = itemView.findViewById(R.id.comment_text);
        }
    }
}
