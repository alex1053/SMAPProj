package com.mealsharedev.mealshare.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mealsharedev.mealshare.Models.Meal;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by alexb on 07-05-2018.
 */

public class MealUpdateService extends Service {
    public static final String NEW_MEALS_AVAILABLE = "newMealsAvailable";
    public static final String NEW_MEALS_EXTRA = "newMealsExtra";
    public static final long UPDATE_INTERVAL_MILLIS = 120000; //2min
    private ArrayList<Meal> mealList = new ArrayList<>();
    private ArrayList<Meal> newMealList = new ArrayList<>();
    private FirebaseFirestore fDb;
    private Timer updateScheduler;


    private final IBinder binder = new MealUpdateServiceBinder();

    public class MealUpdateServiceBinder extends Binder {
        public MealUpdateService getService() {
            return MealUpdateService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fDb = FirebaseFirestore.getInstance();
        TimerTask scheduledUpdate = new TimerTask() {
            @Override
            public void run() {
                updateMeals();
            }
        };
        //Initiate timer with initial delay and update interval
        updateScheduler = new Timer();
        updateScheduler.schedule(scheduledUpdate, UPDATE_INTERVAL_MILLIS, UPDATE_INTERVAL_MILLIS);
    }

    @Override
    public void onDestroy() {
        if(updateScheduler != null) {
            updateScheduler.cancel();
            updateScheduler = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void updateMeals() {
        newMealList = new ArrayList<>();
        fDb.collection("meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> meal = document.getData();
                                Meal tempMeal = new Meal(meal);
                                newMealList.add(tempMeal);
                            }
                            compareAndBroadcast();
                        }
                    }
                });
    }

    private void compareAndBroadcast() {
        boolean newMealsPresent = false;
        if (newMealList.size() != mealList.size()) {
            newMealsPresent = true;
        } else {
            //Compare mealIds to check for changes
            for (Meal newMeal : newMealList) {
                boolean found = false;
                for (Meal oldMeal : mealList) {
                    if (newMeal.getMealId().equals(oldMeal.getMealId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    newMealsPresent = true;
                    break;
                }
            }
        }

        if (newMealsPresent) {
            mealList = newMealList;
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(NEW_MEALS_EXTRA, mealList);
            broadcastIntent.setAction(NEW_MEALS_AVAILABLE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
    }
}
