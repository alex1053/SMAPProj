package com.mealsharedev.mealshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mealsharedev.mealshare.Adapters.CommentAdapter;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.Models.UserSubscription;
import com.mealsharedev.mealshare.dao.FirebaseDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.graphics.Color.GRAY;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_COMMENTS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_COMMENTS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_USERSUBSCRIPTIONS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_MEAL_BY_ID;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_MEAL_BY_ID_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_USERSUBSCRIPTIONS_EXTRA;

public class MealDetailsActivity extends AppCompatActivity {

    CommentAdapter mealAdapter;
    FirebaseDAO dao;

    Button btnBuy, btnBack, btnComment;
    TextView txtMeal, txtUser, txtLocation, txtTime, txtDescription, txtPrice, txtPortions;
    Meal meal;
    Meal tmpMeal;
    ListView CommentListView;
    ArrayList<Comment> comments = new ArrayList<>();

    private BroadcastReceiver commentsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Comment> tmpList = intent.getParcelableArrayListExtra(DAO_COMMENTS_EXTRA);
            if (tmpList != null) {
                comments.clear();
                comments.addAll(tmpList);

                Collections.sort(comments, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return o1.getCommentDate().compareTo(o2.getCommentDate());
                    }
                });
                mealAdapter.notifyDataSetChanged();
            }
        }
    };

    private BroadcastReceiver mealReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            meal = intent.getParcelableExtra(DAO_MEAL_BY_ID_EXTRA);
            txtPortions.setText(meal.portions);
            if (Integer.parseInt(meal.portions) < 1) {
                btnBuy.setEnabled(false);
                btnBuy.setBackgroundColor(GRAY);
            }
            dao.getCommentsForMeal(meal.commentIdList);
        }
    };

    private BroadcastReceiver userSubReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UserSubscription subscription = intent.getParcelableExtra(DAO_USERSUBSCRIPTIONS_EXTRA);
            for (String mealID : subscription.myMealList) {
                if (tmpMeal.getMealId().equals(mealID)) {
                    btnBuy.setEnabled(false);
                    btnBuy.setBackgroundColor(GRAY);
                    break;
                }
            }
            for (String mealID : subscription.reservedMealsList) {
                if (tmpMeal.getMealId().equals(mealID)) {
                    btnBuy.setEnabled(false);
                    btnBuy.setBackgroundColor(GRAY);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        LocalBroadcastManager bMn = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter(DAO_GET_COMMENTS);
        bMn.registerReceiver(commentsReceiver, filter);
        IntentFilter filter1 = new IntentFilter(DAO_MEAL_BY_ID);
        bMn.registerReceiver(mealReceiver, filter1);
        IntentFilter filter2 = new IntentFilter(DAO_GET_USERSUBSCRIPTIONS);
        bMn.registerReceiver(userSubReceiver, filter2);

        dao = new FirebaseDAO(this);


        btnBuy = findViewById(R.id.btnBuy);
        btnBack = findViewById(R.id.btnBack);
        btnComment = findViewById(R.id.btnComment);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialogWindow();
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCommentDialogWindow();
            }
        });

        txtMeal = findViewById(R.id.txtMealDetailHeader);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        txtTime = findViewById(R.id.txtTimeStamp);
        txtPrice = findViewById(R.id.txtPrice);
        txtPortions = findViewById(R.id.txtPortions);
        txtUser = findViewById(R.id.txtUser);


        tmpMeal = getIntent().getParcelableExtra("meal");

        txtMeal.setText(tmpMeal.mealName);
        txtDescription.setText(tmpMeal.description);
        txtLocation.setText(getLocationString(tmpMeal));
        txtTime.setText(tmpMeal.timeStamp);
        txtPrice.setText(tmpMeal.price + " " + getResources().getString(R.string.DKK));
        txtUser.setText(tmpMeal.displayName);
        txtPortions.setText("-");
        dao.getMealByID(tmpMeal.getMealId());
        dao.getUsersubscriptionsForUser();

        InitializeListView();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager bMn = LocalBroadcastManager.getInstance(this);
        bMn.unregisterReceiver(mealReceiver);
        bMn.unregisterReceiver(commentsReceiver);
        bMn.unregisterReceiver(userSubReceiver);
        super.onDestroy();
    }

    public String getLocationString(Meal localMeal) {
        return localMeal.address + ", " + localMeal.zipCode + " " + localMeal.city;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    //Inspired by: https://stackoverflow.com/questions/10903754/input-text-dialog-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public void OpenDialogWindow() {
        if (meal != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.reserve);

            builder.setMessage(getResources().getString(R.string.reserve_question) + " " + meal.mealName + " for " + meal.price + " " + getResources().getString(R.string.DKK) + "?");

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Integer.parseInt(meal.portions) < 1) {
                        Toast.makeText(getApplicationContext(), R.string.no_portions, Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        meal.portions = String.valueOf(Integer.parseInt(meal.portions) - 1);
                        dao.reserveMeal(meal);
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.still_updating_meal, Toast.LENGTH_LONG).show();
        }
    }

    public void OpenCommentDialogWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.write_comment);

        final EditText newComment = new EditText(this);
        newComment.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newComment);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Comment comment = new Comment(newComment.getText().toString());
                dao.putComment(comment, meal);
                comments.add(comment);
                mealAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void InitializeListView() {
        mealAdapter = new CommentAdapter(this, comments);
        CommentListView = findViewById(R.id.ListViewComment);
        CommentListView.setAdapter(mealAdapter);
        CommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }
}
