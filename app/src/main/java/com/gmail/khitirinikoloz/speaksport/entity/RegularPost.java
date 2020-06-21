package com.gmail.khitirinikoloz.speaksport.entity;

import androidx.annotation.NonNull;

import com.gmail.khitirinikoloz.speaksport.model.Post;

public class RegularPost implements Post {
    private String title;
    private String description;
    private String topic;

    public RegularPost(@NonNull String title, String description, @NonNull String topic) {
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return "title: " + title + "\n" +
                "description: " + description + "\n" +
                "topic: " + topic;
    }
}
