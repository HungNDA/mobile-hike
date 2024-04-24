package com.example.hikermanagementapp.model;

import android.graphics.Bitmap;

public class ObservationModel {

    private long id;
    private String date;
    private String time;
    private String comment;
    private Bitmap profileImage;

    public ObservationModel(String date, String time, String comment, Bitmap profileImage) {
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.profileImage = profileImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }
}
