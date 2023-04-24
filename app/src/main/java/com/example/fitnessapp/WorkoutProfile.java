package com.example.fitnessapp;

public class WorkoutProfile {
    private String profileName;
    private String routineType;
    private String exercises;
    private String imageUrl;
    private Integer likes;

    public WorkoutProfile(String profileName, String routineType, String exercises, String imageUrl, Integer likes) {
        this.profileName = profileName;
        this.routineType = routineType;
        this.exercises = exercises;
        this.imageUrl = imageUrl;
        this.likes = likes;
    }

    public WorkoutProfile(String profileName, String routineType, String exercises, String imageUrl) {
        this.profileName = profileName;
        this.routineType = routineType;
        this.exercises = exercises;
        this.imageUrl = imageUrl;
        this.likes = 0;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}

