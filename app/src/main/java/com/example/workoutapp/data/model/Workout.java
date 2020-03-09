package com.example.workoutapp.data.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Workout {
    private Double sets, reps, weight;
    private String muscleType, date;

    public Workout() {

    }

    public Workout(Double sets, Double reps, Double weight, String muscleType) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.muscleType = muscleType;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date = dtf.format(now);
    }

    public Double getPoints(){
        Double value = (sets * reps * weight);
        return value;
    }

    public Double getSets() {
        return sets;
    }

    public Double getReps() {
        return reps;
    }

    public Double getWeight() {
        return weight;
    }

    public String getMuscleType() {
        return muscleType;
    }

    public String getDate() {
        return date;
    }

    public void setSets(Double sets) {
        this.sets = sets;
    }

    public void setReps(Double reps) {
        this.reps = reps;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setMuscleType(String muscleType) {
        this.muscleType = muscleType;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
