package com.example.hikermanagementapp.model;
public class Hike {
    private long id;
    private String hikeName;
    private String location;
    private String date;
    private String time;
    private int numberOfDays;
    private String description;
    private String lengthEditText;
    private int parking;
    private String difficulty;
    private String requiredGear;
    public Hike(long id, String hikeName, String location, String date, String time, int numberOfDays, String description, String lengthEditText, int parking, String difficulty, String requiredGear) {
        this.id = id;
        this.hikeName = hikeName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.numberOfDays = numberOfDays;
        this.description = description;
        this.lengthEditText = lengthEditText;
        this.parking = parking;
        this.difficulty = difficulty;
        this.requiredGear = requiredGear;
    }
    public long getId() {
        return id;
    }
    public String getHikeName() {
        return hikeName;
    }
    public String getLocation() {
        return location;
    }
    public String getDate() {
        return date;
    }
    public String getTime() { return time; }
    public int getNumberOfDays() {
        return numberOfDays;
    }
    public String getLengthText(){return lengthEditText; }
    public int getParking() {
        return parking;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public String getDescription() {
        return description;
    }
    public String getRequiredGear() {
        return requiredGear;
    }
}
