package com.mealsharedev.mealshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;

public class UserSubscription implements Parcelable {
    private String userUid;
    public ArrayList<String> myMealList;
    public ArrayList<String> commentedList;
    public ArrayList<String> reservedMealsList;

    public UserSubscription(String userUid) {
        this.userUid = userUid;

        myMealList = new ArrayList<>();
        commentedList = new ArrayList<>();
        reservedMealsList = new ArrayList<>();
    }

    public UserSubscription(Map<String, Object> hashmap) {
        userUid = hashmap.get("userUid") != null ? hashmap.get("userUid").toString() : "user";
        myMealList = hashmap.get("myMealList") != null ? (ArrayList<String>) hashmap.get("myMealList") : new ArrayList<>();
        commentedList = hashmap.get("commentedList") != null ? (ArrayList<String>) hashmap.get("commentedList") : new ArrayList<>();
        reservedMealsList = hashmap.get("reservedMealsList") != null ? (ArrayList<String>) hashmap.get("reservedMealsList") : new ArrayList<>();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUid);
        dest.writeStringArray(myMealList.toArray(new String[0]));
        dest.writeStringArray(commentedList.toArray(new String[0]));
        dest.writeStringArray(reservedMealsList.toArray(new String[0]));
    }

    protected UserSubscription(Parcel parcel) {
        userUid = parcel.readString();
        myMealList = parcel.createStringArrayList();
        commentedList = parcel.createStringArrayList();
        reservedMealsList = parcel.createStringArrayList();
    }

    public static final Creator<UserSubscription> CREATOR = new Creator<UserSubscription>() {
        @Override
        public UserSubscription createFromParcel(Parcel source) {
            return new UserSubscription(source);
        }

        @Override
        public UserSubscription[] newArray(int size) {
            return new UserSubscription[size];
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
