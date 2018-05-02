package com.mealsharedev.mealshare;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mealsharedev.mealshare.Models.Meal;

import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by Laura on 02-05-2018.
 */

public class MealService extends Service {

    private final IBinder binder = new MealServiceBinder();
    private ArrayList<Meal> meals;

    public class MealServiceBinder extends Binder {
        public MealService getService() {
            return MealService.this;
        }
    }

    public ArrayList<Meal> GetMeals()
    {
        return meals;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GetDataFromDatabase();

    }

    public void GetDataFromDatabase()
    {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new data item has been added, add it to the list
                Meal meal = dataSnapshot.getValue(Meal.class);
                meals.add(meal);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                //Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A data item has changed
                Meal meal = dataSnapshot.getValue(Meal.class);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
