package com.mealsharedev.mealshare.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Laura on 01-05-2018.
 */
public class Meal implements Parcelable {

    public Meal(String user, String userName, String mealName, String description, String portions, String price, String address, String zipCode, String city, String timeStamp) {
        this.userMail = user;
        this.userName = userName;
        this.mealName = mealName;
        this.description = description;
        this.portions = portions;
        this.price = price;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.timeStamp = timeStamp;
    }

    public Meal(Map<String, Object> hashmap)
    {
        this.userMail = hashmap.get("userMail").toString();
        this.userName = hashmap.get("userName").toString();
        this.mealName = hashmap.get("mealName").toString();
        this.description = hashmap.get("description").toString();
        this.portions = hashmap.get("portions").toString();
        this.price = hashmap.get("price").toString();
        this.address = hashmap.get("address").toString();
        this.zipCode = hashmap.get("zipCode").toString();
        this.city = hashmap.get("city").toString();
        this.timeStamp = hashmap.get("timeStamp").toString();

    }

    public Meal(String mealName) {
        this.mealName = mealName;
    }

    public String userMail;
    public String userName;
    public String mealName;
    public String description;
    public String portions;
    public String price;
    public String address;
    public String zipCode;
    public String city;
    public String timeStamp;
    //public ArrayList<Comment> comments;

    //public List<String> subscribers;
    //Parcing of the cityweatherobjects
    protected Meal(Parcel in) {
        userMail = in.readString();
        userName = in.readString();
        mealName = in.readString();
        description = in.readString();
        portions = in.readString();
        price = in.readString();
        address = in.readString();
        zipCode = in.readString();
        city = in.readString();
        timeStamp = in.readString();

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
        parcel.writeString(userMail);
        parcel.writeString(userName);
        parcel.writeString(mealName);
        parcel.writeString(description);
        parcel.writeString(portions);
        parcel.writeString(price);
        parcel.writeString(address);
        parcel.writeString(zipCode);
        parcel.writeString(city);
        parcel.writeString(timeStamp);
    }


    public Meal(){}

}
