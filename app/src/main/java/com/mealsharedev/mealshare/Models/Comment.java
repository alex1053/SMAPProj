package com.mealsharedev.mealshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class Comment implements Parcelable {
    public String comment;
    public String displayName;
    private String commentId;

    public Comment(String comment) {

        this.comment = comment;
        this.displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        commentId = UUID.randomUUID().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(displayName);
        dest.writeString(commentId);
    }

    protected Comment(Parcel parcel) {
        comment = parcel.readString();
        displayName = parcel.readString();
        commentId = parcel.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getCommentId() {
        return commentId;
    }


}
