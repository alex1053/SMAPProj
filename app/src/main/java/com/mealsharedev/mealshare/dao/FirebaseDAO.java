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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.Models.UserSubscription;
import com.mealsharedev.mealshare.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FirebaseDAO {
    //Action names
    public static final String DAO_GET_ALL_MEALS = "getAllMeals";
    public static final String DAO_GET_MY_MEALS = "getMyMeals";
    public static final String DAO_GET_USERSUBSCRIPTIONS = "userSubscriptions";
    public static final String DAO_GET_RESERVED_MEALS = "getReservedMeals";
    public static final String DAO_GET_COMMENTED_MEALS = "getCommentedMeals";
    public static final String DAO_GET_COMMENTS = "getCommentsForMeal";
    public static final String DAO_MEAL_BY_ID = "mealById";

    //Extra names
    public static final String DAO_ALL_MEALS_EXTRA = "allMealsExtra";
    public static final String DAO_MY_MEALS_EXTRA = "myMealsExtra";
    public static final String DAO_USERSUBSCRIPTIONS_EXTRA = "userSubscriptions";
    public static final String DAO_RESERVED_MEALS_EXTRA = "reservedMealsExtra";
    public static final String DAO_COMMENTED_MEALS_EXTRA = "commentedMealsExtra";
    public static final String DAO_COMMENTS_EXTRA = "commentsExtra";
    public static final String DAO_MEAL_BY_ID_EXTRA = "mealById";

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
        mFF.collection(context.getString(R.string.mealCollection))
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

    public void getUsersubscriptionsForUser() {
        String userID = getCurrentUserID();
        mFF.collection(context.getString(R.string.subscriptionCollection)).document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserSubscription tmp = new UserSubscription(task.getResult().getData());
                            Intent intent = new Intent(DAO_GET_USERSUBSCRIPTIONS);
                            intent.putExtra(DAO_USERSUBSCRIPTIONS_EXTRA, tmp);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                });
    }

    public void getMyMeals() {
        String userID = getCurrentUserID();
        ArrayList<Meal> mealList = new ArrayList<>();
        mFF.collection(context.getString(R.string.subscriptionCollection)).document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userSub = task.getResult().getData();
                            UserSubscription subscription = new UserSubscription(userSub);
                            mFF.collection(context.getString(R.string.mealCollection)).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    for (String id : subscription.myMealList) {
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

    public void getMealByID(String id) {
        mFF.collection(context.getString(R.string.mealCollection)).document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(DAO_MEAL_BY_ID);
                            intent.putExtra(DAO_MEAL_BY_ID_EXTRA, new Meal(task.getResult().getData()));
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                });
    }

    public void getReservedMeals() {
        String userID = getCurrentUserID();
        ArrayList<Meal> mealList = new ArrayList<>();
        mFF.collection(context.getString(R.string.subscriptionCollection)).document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userSub = task.getResult().getData();
                            UserSubscription subscription = new UserSubscription(userSub);
                            mFF.collection(context.getString(R.string.mealCollection)).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    for (String id : subscription.reservedMealsList) {
                                                        if (document.getData().get("mealId").equals(id)) {
                                                            Meal tempMeal = new Meal(document.getData());
                                                            mealList.add(tempMeal);
                                                            break;
                                                        }
                                                    }
                                                }
                                                Intent intent = new Intent(DAO_GET_RESERVED_MEALS);
                                                intent.putExtra(DAO_RESERVED_MEALS_EXTRA, mealList);
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void getCommentedMealsForUser() {
        String userID = getCurrentUserID();
        ArrayList<Meal> mealList = new ArrayList<>();
        mFF.collection(context.getString(R.string.subscriptionCollection)).document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userSub = task.getResult().getData();
                            UserSubscription subscription = new UserSubscription(userSub);
                            mFF.collection(context.getString(R.string.mealCollection)).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                                Intent intent = new Intent(DAO_GET_COMMENTED_MEALS);
                                                intent.putExtra(DAO_COMMENTED_MEALS_EXTRA, mealList);
                                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void getCommentsForMeal(ArrayList<String> commentList) {
        mFF.collection(context.getString(R.string.commentCollection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Comment> commentsForMeal = new ArrayList<>();
                            for (String commentId : commentList) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (commentId.equals(document.getData().get("commentId"))) {
                                        commentsForMeal.add(new Comment(document.getData()));
                                        break;
                                    }
                                }
                            }
                            Intent intent = new Intent(DAO_GET_COMMENTS);
                            intent.putExtra(DAO_COMMENTS_EXTRA, commentsForMeal);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }
                    }
                });

    }

    public void putComment(Comment comment, Meal meal) {
        mFF.collection(context.getString(R.string.commentCollection)).document(comment.getCommentId())
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
        meal.commentIdList.add(comment.getCommentId());
        mFF.collection(context.getString(R.string.mealCollection)).document(meal.getMealId()).set(meal);

        String userID = getCurrentUserID();
        mFF.collection(context.getString(R.string.subscriptionCollection)).document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserSubscription subscriptions = new UserSubscription(task.getResult().getData());
                            boolean found = false;
                            for (String id : subscriptions.myMealList) {
                                if (id.equals(meal.getMealId())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                found = false;
                                for (String id : subscriptions.commentedList) {
                                    if (id.equals(meal.getMealId())) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    subscriptions.commentedList.add(meal.getMealId());
                                    mFF.collection(context.getString(R.string.subscriptionCollection))
                                            .document(getCurrentUserID()).set(subscriptions);
                                }
                            }
                        }
                    }
                });
    }

    public void updateMeal(Meal meal) {
        mFF.collection(context.getString(R.string.mealCollection)).document(meal.getMealId()).set(meal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Meal reserved!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void putMeal(Meal meal) {
        String userID = getCurrentUserID();
        mFF.collection(context.getString(R.string.subscriptionCollection)).document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserSubscription subscriptions = new UserSubscription(task.getResult().getData());
                            subscriptions.myMealList.add(meal.getMealId());
                            mFF.collection(context.getString(R.string.subscriptionCollection))
                                    .document(getCurrentUserID()).set(subscriptions);
                        }
                    }
                });
        mFF.collection(context.getString(R.string.mealCollection)).document(meal.getMealId())
                .set(meal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), R.string.meal_shared, Toast.LENGTH_SHORT).show();
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

    public void deleteMeal(Meal meal) {
        mFF.collection(context.getString(R.string.mealCollection)).document(meal.getMealId()).delete();
        for (String commentID : meal.commentIdList) {
            mFF.collection(context.getString(R.string.commentCollection)).document(commentID).delete();
        }
        mFF.collection(context.getString(R.string.subscriptionCollection)).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserSubscription subscription = new UserSubscription(document.getData());
                                if (subscription.getUserUid().equals(getCurrentUserID())) {
                                    Iterator<String> i = subscription.myMealList.iterator();
                                    while (i.hasNext()) {
                                        String tmp = i.next();
                                        if (tmp.equals(meal.getMealId())) {
                                            i.remove();
                                            break;
                                        }
                                    }
                                }
                                Iterator<String> k = subscription.commentedList.iterator();
                                while (k.hasNext()) {
                                    String tmp = k.next();
                                    if (tmp.equals(meal.getMealId())) {
                                        k.remove();
                                        break;
                                    }
                                }
                                Iterator<String> r = subscription.reservedMealsList.iterator();
                                while (r.hasNext()) {
                                    String tmp = r.next();
                                    if (tmp.equals(meal.getMealId())) {
                                        r.remove();
                                        break;
                                    }
                                }
                                mFF.collection(context.getString(R.string.subscriptionCollection))
                                        .document(subscription.getUserUid()).set(subscription);
                            }
                        }
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
