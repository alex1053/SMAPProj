package com.mealsharedev.mealshare.Models;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

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
@IgnoreExtraProperties
public class Meal {

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

        //ArrayList<String> subscribers = new ArrayList<>();
    }

    public String mealName;
    public String description;
    public String portions;
    public String price;
    public String timeStamp;
    public String address;
    public String zipCode;
    public String city;
    public String userMail;
    public String userName;

    //public List<String> subscribers;

    public Meal(){}

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("mealName", mealName);
        result.put("price", price);

        return result;
    }
}
