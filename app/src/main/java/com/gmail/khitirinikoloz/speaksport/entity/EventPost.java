package com.gmail.khitirinikoloz.speaksport.entity;

import androidx.annotation.NonNull;

import com.gmail.khitirinikoloz.speaksport.model.Post;

import java.util.Calendar;

public class EventPost implements Post {
    private String title;
    private String description;
    private Calendar startTime;
    private Calendar endTime;
    private String location;
    private String topic;

    public EventPost(@NonNull String title, String description, @NonNull Calendar startTime,
                     @NonNull Calendar endTime, @NonNull String location, @NonNull String topic) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
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

    public String getDescription() {
        return description;
    }

    public Calendar getStartTime() {
        return (Calendar) startTime.clone();
    }

    public Calendar getEndTime() {
        return (Calendar) endTime.clone();
    }

    public String getLocation() {
        return location;
    }

    @NonNull
    @Override
    public String toString() {
        return "title: " + title + "\n" +
                "description: " + description + "\n" +
                "startTime: " + startTime.getTime() + "\n" +
                "endTime: " + endTime.getTime() + "\n" +
                "location: " + location + "\n" +
                "topic: " + topic;
    }
}
