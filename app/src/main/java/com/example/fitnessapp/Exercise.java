package com.example.fitnessapp;

public class Exercise {
    private String name;
    private int reps;
    private double weights;

    public Exercise() {}

    public Exercise(String name, int reps, double weights) {
        this.name = name;
        this.reps = reps;
        this.weights = weights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeights() {
        return weights;
    }

    public void setWeights(double weights) {
        this.weights = weights;
    }
}
