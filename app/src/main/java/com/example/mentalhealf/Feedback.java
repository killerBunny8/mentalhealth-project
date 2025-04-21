package com.example.mentalhealf;

public class Feedback {
    private String email;
    private String category;
    private String message;
    private long timestamp;
    public Feedback() { }
    public Feedback(String email, String category, String message, long timestamp) {
        this.email = email;
        this.category = category;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
