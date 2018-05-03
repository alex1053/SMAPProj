package com.mealsharedev.mealshare.Models;

/**
 * Created by Laura on 03-05-2018.
 */

public class Comment {

    public String username;
    public String comment;

    public Comment(String username, String comment)
    {
        this.username = username;
        this.comment = comment;
    }

    public Comment(){

    }
}
