package com.mealsharedev.mealshare.Models;

import java.util.ArrayList;

public class userSubscription {
    private String userUid;
    public ArrayList<String> myMealList;
    public ArrayList<String> commentList;
    public ArrayList<String> boughtMealsList;

    public userSubscription(String userUid) {
        this.userUid = userUid;

        myMealList = new ArrayList<>();
        commentList = new ArrayList<>();
        boughtMealsList = new ArrayList<>();
    }

    public String getUserUid() {
        return userUid;
    }
}
