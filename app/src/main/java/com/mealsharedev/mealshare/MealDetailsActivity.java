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

import com.mealsharedev.mealshare.Adapters.CommentAdapter;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.dao.FirebaseDAO;

import java.util.ArrayList;

import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_COMMENTS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_COMMENTS;

public class MealDetailsActivity extends AppCompatActivity {

    CommentAdapter mealAdapter;
    FirebaseDAO DAO;

    Button btnBuy, btnBack, btnComment;
    TextView txtMeal, txtUser, txtLocation, txtTime, txtDescription, txtPrice, txtPortions;
    Meal meal;
    ListView CommentListView;
    ArrayList<Comment> comments = new ArrayList<>();

    private BroadcastReceiver commentsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Comment> tmpList = intent.getParcelableArrayListExtra(DAO_COMMENTS_EXTRA);
            comments.clear();
            comments.addAll(tmpList != null ? tmpList : new ArrayList<>());
            mealAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        IntentFilter filter = new IntentFilter(DAO_GET_COMMENTS);
        LocalBroadcastManager.getInstance(this).registerReceiver(commentsReceiver, filter);

        DAO = new FirebaseDAO(this);

        btnBuy = findViewById(R.id.btnBuy);
        btnBack = findViewById(R.id.btnBack);
        btnComment = findViewById(R.id.btnComment);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        meal = getIntent().getParcelableExtra("meal");
        DAO.getCommentsForMeal(meal.commentIdList);

        txtMeal = findViewById(R.id.txtMealDetailHeader);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        txtTime = findViewById(R.id.txtTimeStamp);
        txtPrice = findViewById(R.id.txtPrice);
        txtPortions = findViewById(R.id.txtPortions);
        txtUser = findViewById(R.id.txtUser);

        txtMeal.setText(meal.mealName);
        txtDescription.setText(meal.description);
        txtLocation.setText(getLocationString());
        txtTime.setText(meal.timeStamp);
        txtPrice.setText(meal.price + " " + getResources().getString(R.string.DKK));
        txtUser.setText(meal.displayName);
        txtPortions.setText(meal.portions);

        InitializeListView();
    }

    public String getLocationString() {
        return meal.address + ", " + meal.zipCode + " " + meal.city;
    }

    //Inspired by: https://stackoverflow.com/questions/10903754/input-text-dialog-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public void OpenDialogWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reserve);

        builder.setMessage(R.string.reserve_question + meal.mealName + " for " + meal.price + " " + R.string.DKK + "?");

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
                DAO.putComment(comment, meal);
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
