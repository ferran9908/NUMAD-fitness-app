package com.example.fitnessapp;

import java.io.Serializable;

public class ActiveExercise implements Serializable {
    private String exerciseName;

    Boolean isWeighted;
    private double weight1, weight2, weight3;
    private int rep1, rep2, rep3;

    public ActiveExercise(String exerciseName, double weight1, double weight2, double weight3, int rep1, int rep2, int rep3, Boolean isWeighted) {
        this.exerciseName = exerciseName;
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.weight3 = weight3;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.rep3 = rep3;
        this.isWeighted = isWeighted;
    }

    public ActiveExercise(String exerciseName, Boolean isWeighted) {
        this.exerciseName = exerciseName;
        this.weight1 = 0;
        this.weight2 = 0;
        this.weight3 = 0;
        this.rep1 = 0;
        this.rep2 = 0;
        this.rep3 = 0;
        this.isWeighted = isWeighted;
    }

    public Boolean getWeighted() {
        return isWeighted;
    }

    public ActiveExercise(String exerciseName) {
        this.exerciseName = exerciseName;
        this.weight1 = 0;
        this.weight2 = 0;
        this.weight3 = 0;
        this.rep1 = 0;
        this.rep2 = 0;
        this.rep3 = 0;
        this.isWeighted = true;
    }


    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public double getWeight1() {
        return weight1;
    }

    public void setWeight1(double weight1) {
        this.weight1 = weight1;
    }

    public double getWeight2() {
        return weight2;
    }

    public void setWeight2(double weight2) {
        this.weight2 = weight2;
    }

    public double getWeight3() {
        return weight3;
    }

    public void setWeight3(double weight3) {
        this.weight3 = weight3;
    }

    public int getRep1() {
        return rep1;
    }

    public void setRep1(int rep1) {
        this.rep1 = rep1;
    }

    public int getRep2() {
        return rep2;
    }

    public void setRep2(int rep2) {
        this.rep2 = rep2;
    }

    public int getRep3() {
        return rep3;
    }

    public void setRep3(int rep3) {
        this.rep3 = rep3;
    }
}
