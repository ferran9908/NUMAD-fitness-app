package com.example.fitnessapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String email;
    private String displayName;
    private String imageUrl;
    private List<String> followers;
    private List<String> following;

    public User() {}

    public User(String email, String displayName, String imageUrl) {
        this.email = email;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.followers = new ArrayList<String>();
        this.following = new ArrayList<String>();
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
        this.imageUrl = "https://cdn3.iconfinder.com/data/icons/stay-home-activities-2/512/gym-_workout-_man-_avatar-_wellness-_exercise-_dumbbell-512.png";
        this.followers = new ArrayList<String>();
        this.following = new ArrayList<String>();
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("displayName", displayName);
        result.put("followers", followers);
        result.put("following", following);
        result.put("imageUrl", imageUrl);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
