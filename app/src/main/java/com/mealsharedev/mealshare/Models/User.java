package com.mealsharedev.mealshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String Id;
    private String displayName;
    private String email;

    public User(String id, String displayName, String email) {
        this.Id = id;
        this.displayName = displayName;
        this.email = email;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(displayName);
        dest.writeString(email);
    }

    public User(Parcel parcel) {
        Id = parcel.readString();
        displayName = parcel.readString();
        email = parcel.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
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
