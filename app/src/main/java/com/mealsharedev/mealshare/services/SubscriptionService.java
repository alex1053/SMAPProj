package com.mealsharedev.mealshare.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.Models.UserSubscription;
import com.mealsharedev.mealshare.NotificationHandler;
import com.mealsharedev.mealshare.dao.FirebaseDAO;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_ALL_MEALS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_COMMENTED_MEALS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_ALL_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_COMMENTED_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_MY_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_RESERVED_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_MY_MEALS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_RESERVED_MEALS_EXTRA;

/**
 * Created by alexb on 07-05-2018.
 */

public class SubscriptionService extends Service {

    private final IBinder binder = new SubscriptionServiceBinder();
    public static final long UPDATE_INTERVAL_MILLIS = 120000/6; //3sek
    private Timer updateScheduler;
    public ArrayList<Meal> subscribedMeals;
    public ArrayList<Meal> myMeals;
    public ArrayList<Meal> commentMeals;
    private FirebaseDAO dao;
    private NotificationHandler notificationHandler;

    public class SubscriptionServiceBinder extends Binder {
        public SubscriptionService getService() {
            return SubscriptionService.this;
        }
    }

    private BroadcastReceiver mymealsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tempMeals = intent.getParcelableArrayListExtra(DAO_MY_MEALS_EXTRA);
            CheckForNewCommentsOnMyMeals(tempMeals);
            CheckForNewAssignments(tempMeals);
            myMeals = tempMeals;
        }
    };

    private BroadcastReceiver subscribedmealsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tempMeals = intent.getParcelableArrayListExtra(DAO_RESERVED_MEALS_EXTRA);
            CheckForRemovedMeals(tempMeals);
        }
    };

    private BroadcastReceiver commentonmealsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tempMeals = intent.getParcelableArrayListExtra(DAO_COMMENTED_MEALS_EXTRA);
            CheckForNewCommentsOnSubscribedMeals(tempMeals);
        }
    };

    @Override
    public void onCreate() {

        notificationHandler = new NotificationHandler(this);
        dao = new FirebaseDAO(this);
        IntentFilter mymealsfilter = new IntentFilter(DAO_GET_MY_MEALS);
        LocalBroadcastManager.getInstance(this).registerReceiver(mymealsReceiver, mymealsfilter);

        IntentFilter reservedfilter = new IntentFilter(DAO_GET_RESERVED_MEALS);
        LocalBroadcastManager.getInstance(this).registerReceiver(subscribedmealsReceiver, reservedfilter);

        IntentFilter commentonfilter = new IntentFilter(DAO_GET_COMMENTED_MEALS);
        LocalBroadcastManager.getInstance(this).registerReceiver(commentonmealsReceiver, commentonfilter);

        dao.getMyMeals();
        dao.getReservedMeals();

        TimerTask scheduledUpdate = new TimerTask() {
            @Override
            public void run() {
                getMyMealLists();
            }
        };
        updateScheduler = new Timer();
        updateScheduler.schedule(scheduledUpdate, UPDATE_INTERVAL_MILLIS, UPDATE_INTERVAL_MILLIS);
    }

    public void getMyMealLists()
    {
        dao.getMyMeals();
        dao.getReservedMeals();
        dao.getCommentedMealsForUser();
    }

    public void CheckForNewCommentsOnMyMeals(ArrayList<Meal> newMyMealList)
    {
        if(myMeals!=null)
        {
        for(Meal newItem : newMyMealList)
        {
            for(Meal item : myMeals)
            {
                if(item.getMealId().equals(newItem.getMealId()))
                {
                    if(item.commentIdList.size() != newItem.commentIdList.size())
                    {
                        notificationHandler.NotifyCommentOnYourMeal(newItem);
                    }
                }
            }
        }
        }
    }

    public void CheckForNewCommentsOnSubscribedMeals(ArrayList<Meal> newMealList)
    {
        if(commentMeals!=null)
        {
        for(Meal newItem : newMealList)
        {
            for(Meal item : commentMeals)
            {
                if(item.getMealId().equals(newItem.getMealId()))
                {
                    if(item.commentIdList.size() != newItem.commentIdList.size())
                    {
                        notificationHandler.NotifyCommentOnSameMeal(newItem);
                    }
                }
            }
        }
        }
        commentMeals = newMealList;
    }

    public void CheckForNewAssignments(ArrayList<Meal> newMyMealList)
    {
        if(myMeals!=null)
        {
            for(Meal newItem : newMyMealList)
            {
                for(Meal item : myMeals)
                {
                    if(item.getMealId().equals(newItem.getMealId()))
                    {
                        int newPortions = Integer.parseInt(newItem.portions);
                        int oldPortions = Integer.parseInt(item.portions);
                        if(newPortions < oldPortions)
                        {
                            notificationHandler.NotifyMealReserved(newItem);
                        }
                    }
                }
            }
        }
    }

    public void CheckForRemovedMeals(ArrayList<Meal> newSubscribedList)
    {
        if(subscribedMeals!=null)
        {
            if (subscribedMeals.size() > newSubscribedList.size()) {
                notificationHandler.NotifyMealRemoved();
            }
        }
        subscribedMeals = newSubscribedList;
    }

    @Override
    public void onDestroy() {
        if (updateScheduler != null) {
            updateScheduler.cancel();
            updateScheduler = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
