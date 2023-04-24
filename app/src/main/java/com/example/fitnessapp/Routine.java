package com.example.fitnessapp;

import java.util.ArrayList;

public class Routine {
    private String routineName;
    private ArrayList<ExerciseModel> exercises;

    @Override
    public String toString() {
        return "Routine{" +
                "routineName='" + routineName + '\'' +
                ", exercises=" + exercises +
                '}';
    }

    public Routine(String routineName, ArrayList<ExerciseModel> exercises) {
        this.routineName = routineName;
        this.exercises = exercises;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public ArrayList<ExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ExerciseModel> exercises) {
        this.exercises = exercises;
    }
}
