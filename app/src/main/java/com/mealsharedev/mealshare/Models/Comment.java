package com.mealsharedev.mealshare.Models;

import java.util.UUID;

public class Comment {

    public String comment;
    private String UserId;
    private String commentId;

    public Comment(String comment, String userId) {
        this.comment = comment;
        UserId = userId;

        commentId = UUID.randomUUID().toString();
    }

    public String getUserId() {
        return UserId;
    }

    public String getCommentId() {
        return commentId;
    }

}
