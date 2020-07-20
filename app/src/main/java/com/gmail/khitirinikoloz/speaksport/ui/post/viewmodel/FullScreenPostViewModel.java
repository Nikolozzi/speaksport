package com.gmail.khitirinikoloz.speaksport.ui.post.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmail.khitirinikoloz.speaksport.model.Comment;
import com.gmail.khitirinikoloz.speaksport.repository.comment.CommentRepository;
import com.gmail.khitirinikoloz.speaksport.repository.comment.CommentRequest;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostRepository;
import com.gmail.khitirinikoloz.speaksport.repository.post.PostResponse;

import java.util.List;

public class FullScreenPostViewModel extends ViewModel {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MutableLiveData<PostResponse> postResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Comment>> commentsResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Comment> commentResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Comment> newCommentResponseLiveData = new MutableLiveData<>();

    public FullScreenPostViewModel(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void getPost(final long postId) {
        postRepository.getPost(postId, postResponseLiveData);
    }

    public void getComments(final long postId, final int page) {
        commentRepository.getComments(postId, page, commentsResponseLiveData);
    }

    public void getComment(final long commentId) {
        commentRepository.getComment(commentId, commentResponseLiveData);
    }

    public void addComment(final CommentRequest commentRequest) {
        commentRepository.addComment(commentRequest, newCommentResponseLiveData);
    }

    public LiveData<PostResponse> getPostResponseLiveData() {
        return postResponseLiveData;
    }

    public LiveData<List<Comment>> getCommentsResponseLiveData() {
        return commentsResponseLiveData;
    }

    public MutableLiveData<Comment> getCommentResponseLiveData() {
        return commentResponseLiveData;
    }

    public LiveData<Comment> getNewCommentResponseLiveData() {
        return newCommentResponseLiveData;
    }
}
