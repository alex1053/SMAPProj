package com.mealsharedev.mealshare.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mealsharedev.mealshare.Models.Comment;

/**
 * Created by alexb on 08-05-2018.
 */

public class FirebaseDAO {
    private FirebaseFirestore mFF;

    public FirebaseDAO() {
        mFF = FirebaseFirestore.getInstance();
    }

    public void getMyMeals(String userID) {

    }

    public void getAssignedMeals(String userID) {

    }

    public void putComment(Comment comment) {

    }


}
