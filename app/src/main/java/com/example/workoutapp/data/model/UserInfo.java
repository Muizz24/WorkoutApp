package com.example.workoutapp.data.model;

public class UserInfo {
    private Level lvl;
    private int weight; private String weightType;
    private int height; private String heightType;
    private String username, gender;
    private int age;
    private int coins;

    public void UserInfo(){

    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getHeightType() {
        return heightType;
    }

    public void setHeightType(String heightType) {
        this.heightType = heightType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
