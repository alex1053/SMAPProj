package com.mealsharedev.mealshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Meal implements Parcelable {
    public String displayName;
    private String mealId;
    public String mealName;
    public String description;
    public String portions;
    public String price;
    public String address;
    public String zipCode;
    public String city;
    public String timeStamp;
    public ArrayList<String> commentIdList;

    public Meal(String displayName, String mealName, String description, String portions, String price,
                String address, String zipCode, String city, String timeStamp, ArrayList<String> comments) {
        this.displayName = displayName;
        this.mealName = mealName;
        this.description = description;
        this.portions = portions;
        this.price = price;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.timeStamp = timeStamp;
        this.commentIdList = comments;

        this.mealId = UUID.randomUUID().toString();
    }

    public Meal(Map<String, Object> hashmap) {
        this.displayName = hashmap.get("userId") != null ? hashmap.get("userId").toString() : "skrrt";
        this.mealId = hashmap.get("mealId") != null ? hashmap.get("mealId").toString() : "et id";
        this.mealName = hashmap.get("mealName") != null ? hashmap.get("mealName").toString() : "etNavn";
        this.description = hashmap.get("description") != null ? hashmap.get("description").toString() : "";
        this.portions = hashmap.get("portions") != null ? hashmap.get("portions").toString() : "1";
        this.price = hashmap.get("price") != null ? hashmap.get("price").toString() : "firs";
        this.address = hashmap.get("address") != null ? hashmap.get("address").toString() : "her";
        this.zipCode = hashmap.get("zipCode") != null ? hashmap.get("zipCode").toString() : "8888";
        this.city = hashmap.get("city") != null ? hashmap.get("city").toString() : "byenHer";
        this.timeStamp = hashmap.get("timeStamp") != null ? hashmap.get("timeStamp").toString() : "noooo";
        this.commentIdList = hashmap.get("commentIdList") != null ? (ArrayList<String>) hashmap.get("commentIdList") : new ArrayList<>();
    }

    public String getMealId() {
        return mealId;
    }

    protected Meal(Parcel in) {
        displayName = in.readString();
        mealId = in.readString();
        mealName = in.readString();
        description = in.readString();
        portions = in.readString();
        price = in.readString();
        address = in.readString();
        zipCode = in.readString();
        city = in.readString();
        timeStamp = in.readString();
        commentIdList = in.createStringArrayList();

    }

    //inspired by: http://www.androidbook.com/akc/display?url=DisplayNoteIMPURL&reportId=4539&ownerUserId=android
    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
        parcel.writeString(mealId);
        parcel.writeString(mealName);
        parcel.writeString(description);
        parcel.writeString(portions);
        parcel.writeString(price);
        parcel.writeString(address);
        parcel.writeString(zipCode);
        parcel.writeString(city);
        parcel.writeString(timeStamp);
        parcel.writeStringArray(commentIdList.toArray(new String[0]));
    }


    public Meal() {
    }

}
