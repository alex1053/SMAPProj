package com.mealsharedev.mealshare.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.dao.FirebaseDAO;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_ALL_MEALS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_ALL_MEALS;

/**
 * Created by alexb on 07-05-2018.
 */

public class MealUpdateService extends Service {
    public static final String NEW_MEALS_AVAILABLE = "newMealsAvailable";
    public static final String NEW_MEALS_EXTRA = "newMealsExtra";
    public static final long UPDATE_INTERVAL_MILLIS = 120000; //2min
    private FirebaseDAO dao;
    private Timer updateScheduler;
    private final IBinder binder = new MealUpdateServiceBinder();

    public class MealUpdateServiceBinder extends Binder {
        public MealUpdateService getService() {
            return MealUpdateService.this;
        }
    }

    private BroadcastReceiver mealListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tmpList = intent.getParcelableArrayListExtra(DAO_ALL_MEALS_EXTRA);
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(NEW_MEALS_EXTRA, tmpList);
            broadcastIntent.setAction(NEW_MEALS_AVAILABLE);
            LocalBroadcastManager.getInstance(MealUpdateService.this).sendBroadcast(broadcastIntent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new FirebaseDAO(this);
        IntentFilter filter = new IntentFilter(DAO_GET_ALL_MEALS);
        LocalBroadcastManager.getInstance(this).registerReceiver(mealListReceiver, filter);

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
        if (updateScheduler != null) {
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
        dao.getAllMeals();
    }
}
