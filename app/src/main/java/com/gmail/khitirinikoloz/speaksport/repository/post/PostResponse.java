package com.gmail.khitirinikoloz.speaksport.repository.post;

import androidx.annotation.NonNull;

import com.gmail.khitirinikoloz.speaksport.repository.signup.response.UserResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class PostResponse implements Serializable {
    private long id;
    private boolean event;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Double latitude;
    private Double longitude;
    private String location;
    private String topic;
    private UserResponse user;
    private Date postedAt;
    private long commentsNumber;
    private Set<UserResponse> subscribedUsers;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public long getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public Set<UserResponse> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<UserResponse> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }

    @NonNull
    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", event=" + event +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                ", topic='" + topic + '\'' +
                ", user=" + user +
                ", postedAt=" + postedAt +
                ", commentsNumber=" + commentsNumber +
                ", subscribedUsers=" + subscribedUsers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PostResponse that = (PostResponse) o;
        return id == that.id &&
                event == that.event &&
                title.equals(that.title) &&
                topic.equals(that.topic);
    }

    @Override
    public int hashCode() {
        return (int) (17 * id + title.hashCode() + topic.hashCode());
    }
}
