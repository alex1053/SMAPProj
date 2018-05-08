package com.mealsharedev.mealshare.dao;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.Models.UserSubscription;
import com.mealsharedev.mealshare.R;

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
    private FirebaseAuth mFA;
    private Context context;

    public FirebaseDAO(Context outsideContext) {
        context = outsideContext;
        mFF = FirebaseFirestore.getInstance();
        mFA = FirebaseAuth.getInstance();
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
        ArrayList<Meal> mealList = new ArrayList<>();
        mFF.collection("userSubscriptions").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> userSub = task.getResult().getData();
                    UserSubscription subscription = new UserSubscription(userSub);
                    mFF.collection("meals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    for (String id : subscription.commentedList) {
                                        if (document.getData().get("mealId").equals(id)) {
                                            Meal tempMeal = new Meal(document.getData());
                                            mealList.add(tempMeal);
                                            break;
                                        }
                                    }
                                }
                                Intent intent = new Intent(DAO_GET_MY_MEALS);
                                intent.putExtra(DAO_MY_MEALS_EXTRA, mealList);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            }
                        }
                    });
                }
            }
        });
    }


    public void getReservedMeals(String userID) {

    }

    public void getCommentedMealsForUser(String userID) {

    }

    public void getCommentsForMeal(ArrayList<String> commentList) {
        ArrayList<Comment> tmpList = new ArrayList<>();
        mFF.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Comment tmpComment = new Comment(document.getData());
                                tmpList.add(tmpComment);
                            }
                            Intent intent = new Intent(DAO_GET_ALL_MEALS);
                            intent.putExtra(DAO_ALL_MEALS_EXTRA, tmpList);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                });

    }

    public void putComment(Comment comment, String mealID) {
        mFF.collection("comments").document(comment.getCommentId())
                .set(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("putComment", "Comment saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("putComment", "Error: ", e);
                    }
                });
        mFF.collection("meals").document(mealID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Meal meal = new Meal(task.getResult().getData());
                            meal.commentIdList.add(comment.getCommentId());
                            mFF.collection("meals").document(mealID)
                                    .set(meal)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("putComment:", "Comment added to meal");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("putComment:", "Failed to add comment to meal", e);
                                            mFF.collection("comments").document(comment.getCommentId())
                                                    .delete();
                                            Toast.makeText(context, R.string.put_comment_error, Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });

        mFF.collection("userSubscriptions").document(getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserSubscription subscriptions = new UserSubscription(task.getResult().getData());
                            subscriptions.commentedList.add(comment.getCommentId());
                            mFF.collection("userSubscriptions").document(getCurrentUserID()).set(subscriptions);
                        }
                    }
                });
    }

    public void putMeal(Meal meal) {
        mFF.collection("userSubscriptions").document(getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserSubscription subscriptions = new UserSubscription(task.getResult().getData());
                            subscriptions.myMealList.add(meal.getMealId());
                            mFF.collection("userSubscriptions").document(getCurrentUserID()).set(subscriptions);
                        }
                    }
                });
        mFF.collection("meals").document(meal.getMealId())
                .set(meal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("putMeal", "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("putMeal", "ERROR", e);
                        Toast.makeText(context, R.string.put_meal_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public String getCurrentUserID() {
        return mFA.getCurrentUser().getUid();
    }

    public String getCurrentUserDisplayName() {
        return mFA.getCurrentUser().getDisplayName();
    }
}
