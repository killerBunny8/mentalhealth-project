package com.example.mentalhealf;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Moodlog {
    private String id, description, activity;
    private int feeling;
    private Timestamp time;

    private Moodlog() {}

    public Moodlog(String id, int feeling, String description, String activity, Timestamp time){
        this.id = id;
        this.feeling = feeling;
        this.description = description;
        this.activity = activity;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
}
