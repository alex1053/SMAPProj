package com.mealsharedev.mealshare.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Laura on 01-05-2018.
 */

public class Meal {

    public Meal(String user, String mealName, String description, String portions, String price, String address, String zipCode, String city, String timeStamp) {
        this.user = user;
        this.mealName = mealName;
        this.description = description;
        this.portions = portions;
        this.price = price;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.timeStamp = timeStamp;

        ArrayList<String> subscribers = new ArrayList<>();
    }

    public String mealName;
    public String description;
    public String portions;
    public String price;
    public String timeStamp;
    public String address;
    public String zipCode;
    public String city;
    public String user;

    public List<String> subscribers;

    public Meal(){}
}
