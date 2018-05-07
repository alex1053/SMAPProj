package com.mealsharedev.mealshare.Models;

public class User {
    private String Id;
    private String displayName;
    private  String email;

    public User(String id, String displayName, String email) {
        Id = id;
        this.displayName = displayName;
        this.email = email;
    }

    public String getId() {
        return Id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
