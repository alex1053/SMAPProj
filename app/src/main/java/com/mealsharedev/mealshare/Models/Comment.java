package com.mealsharedev.mealshare.Models;

import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class Comment {

    public String comment;
    public String displayName;
    private String commentId;

    public Comment(String comment) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        this.comment = comment;
        if (mAuth.getCurrentUser() != null) {
            this.displayName = mAuth.getCurrentUser().getDisplayName();
        }

        commentId = UUID.randomUUID().toString();
    }

    public String getCommentId() {
        return commentId;
    }

}
