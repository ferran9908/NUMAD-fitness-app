package com.example.fitnessapp;

public class ExerciseModel {
    private String name;

    public ExerciseModel(String name, Boolean isWeighted) {
        this.name = name;
        this.isWeighted = isWeighted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getWeighted() {
        return isWeighted;
    }

    public void setWeighted(Boolean weighted) {
        isWeighted = weighted;
    }

    private Boolean isWeighted;

    @Override
    public String toString() {
        return "ExerciseModel{" +
                "name='" + name + '\'' +
                ", isWeighted=" + isWeighted +
                '}';
    }
}
