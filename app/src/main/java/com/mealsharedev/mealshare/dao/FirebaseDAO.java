package com.mealsharedev.mealshare.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;

public class FirebaseDAO {
    public static final String DAO_GET_ALL_MEALS = "getAllMeals";
    public static final String DAO_GET_MY_MEALS = "getMyMeals";
    public static final String DAO_GET_RESERVED_MEALS = "getReservedMeals";
    public static final String DAO_GET_COMMENTED_MEALS = "getCommentedMeals";
    public static final String DAO_GET_COMMENTS = "getCommentsForMeal";

    private FirebaseFirestore mFF;

    public FirebaseDAO() {
        mFF = FirebaseFirestore.getInstance();
    }

    public void getAllMeals() {

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
