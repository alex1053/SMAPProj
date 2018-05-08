package com.mealsharedev.mealshare.dao;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseDAO {
    //Action names
    public static final String DAO_GET_ALL_MEALS = "getAllMeals";
    public static final String DAO_GET_MY_MEALS = "getMyMeals";
    public static final String DAO_GET_RESERVED_MEALS = "getReservedMeals";
    public static final String DAO_GET_COMMENTED_MEALS = "getCommentedMeals";
    public static final String DAO_GET_COMMENTS = "getCommentsForMeal";

    //Extra names
    public static final String DAO_ALL_MEALS_EXTRA = "allMealsExtra";
    public static final String DAO_MY_MEALS_EXTRA = "myMealsExtra";
    public static final String DAO_RESERVED_MEALS_EXTRA = "reservedMealsExtra";
    public static final String DAO_COMMENTED_MEALS_EXTRA = "commentedMealsExtra";
    public static final String DAO_COMMENTS_EXTRA = "commentsExtra";

    private FirebaseFirestore mFF;
    private Context context;

    public FirebaseDAO(Context context) {
        context = context;
        mFF = FirebaseFirestore.getInstance();
    }

    public void getAllMeals() {
        ArrayList<Meal> tmpList = new ArrayList<>();
        mFF.collection("meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> meal = document.getData();
                                Meal tempMeal = new Meal(meal);
                                tmpList.add(tempMeal);
                            }
                            Intent intent = new Intent(DAO_GET_ALL_MEALS);
                            intent.putExtra(DAO_ALL_MEALS_EXTRA, tmpList);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                });
    }

    public void getMyMeals(String userID) {

    }

    public void getReservedMeals(String userID) {

    }

    public void getCommentedMealsForUser(String userID) {

    }

    public void getCommentsForMeal(String MealID) {

    }

    public void putComment(Comment comment) {

    }

    public void putMeal(Meal meal) {

    }
}
