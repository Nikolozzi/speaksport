package com.gmail.khitirinikoloz.speaksport.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Post implements Serializable {
    @SerializedName("event")
    private final boolean isEvent;
    private final String title;
    private final String description;
    private final Date startTime;
    private final Date endTime;
    private final String location;
    private final String topic;
    private final User user;

    private Post(final PostBuilder postBuilder) {
        this.isEvent = postBuilder.isEvent;
        this.title = postBuilder.title;
        this.description = postBuilder.description;
        this.startTime = postBuilder.startTime;
        this.endTime = postBuilder.endTime;
        this.location = postBuilder.location;
        this.topic = postBuilder.topic;
        this.user = postBuilder.user;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getTopic() {
        return topic;
    }

    public User getUser() {
        return user;
    }

    @NonNull
    @Override
    public String toString() {
        return "Post{" +
                "isEvent=" + isEvent +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", topic='" + topic + '\'' +
                ", user=" + user +
                '}';
    }

    public static class PostBuilder {
        private boolean isEvent;
        private String title;
        private String description;
        private Date startTime;
        private Date endTime;
        private String location;
        private String topic;
        private User user;

        public PostBuilder(final boolean isEvent, @NonNull final String title,
                           @NonNull final String topic, @NonNull final User user) {
            this.isEvent = isEvent;
            this.title = Objects.requireNonNull(title);
            this.topic = Objects.requireNonNull(topic);
            this.user = Objects.requireNonNull(user);
        }

        public PostBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public PostBuilder startTime(final Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public PostBuilder endTime(final Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public PostBuilder location(final String location) {
            this.location = location;
            return this;
        }

        public Post build() {
            final Post post = new Post(this);
            this.validatePost(post);
            return post;
        }

        private void validatePost(final Post post) {
            if (post.isEvent && (post.startTime == null || post.endTime == null || post.location == null))
                throw new IllegalArgumentException("Event fields are not set");

            if (!post.isEvent && (post.startTime != null || post.endTime != null || post.location != null))
                throw new IllegalArgumentException("Regular post fields are not correctly  set");
        }
    }
}
