package com.mealsharedev.mealshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class userSubscription implements Parcelable {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUid);
        dest.writeStringArray(myMealList.toArray(new String[0]));
        dest.writeStringArray(commentList.toArray(new String[0]));
        dest.writeStringArray(boughtMealsList.toArray(new String[0]));
    }

    protected userSubscription(Parcel parcel) {
        userUid = parcel.readString();
        myMealList = parcel.createStringArrayList();
        commentList = parcel.createStringArrayList();
        boughtMealsList = parcel.createStringArrayList();
    }

    public static final Creator<userSubscription> CREATOR = new Creator<userSubscription>() {
        @Override
        public userSubscription createFromParcel(Parcel source) {
            return new userSubscription(source);
        }

        @Override
        public userSubscription[] newArray(int size) {
            return new userSubscription[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUserUid() {
        return userUid;
    }
}
