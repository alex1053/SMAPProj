package com.mealsharedev.mealshare.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;

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
