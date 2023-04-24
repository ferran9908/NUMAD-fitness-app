package com.example.fitnessapp;

public class ProfileCard {

    private final String profileName;
    private final String exercise;

    private String imageUrl;

    //Do you want to change to dateTime ?
    private final String time;
    private final String workout1;
    private final String workout2;
    private final String workout3;


    public ProfileCard(String profileName, String exercise, String time, String workout1, String workout2, String workout3, String imageUrl) {

        this.profileName = profileName;
        this.exercise = exercise;
        this.time = time;

        this.workout1 = workout1;
        this.workout2 = workout2;
        this.workout3 = workout3;
        this.imageUrl = imageUrl;
    }


    public String getProfileName() {
        return profileName;
    }

    public String getExercise() {
        return exercise;
    }

    public String getWorkout1() {
        return workout1;
    }

    public String getWorkout2() {
        return workout2;
    }

    public String getWorkout3() {
        return workout3;
    }
}
