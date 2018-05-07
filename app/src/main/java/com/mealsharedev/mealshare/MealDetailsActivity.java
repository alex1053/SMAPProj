package com.mealsharedev.mealshare;

import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MealDetailsActivity extends AppCompatActivity {

    CommentAdapter mealAdapter;

    Button btnBuy, btnBack, btnComment;
    TextView txtMeal, txtUser, txtLocation, txtTime, txtDescription, txtPrice, txtPortions;
    Meal meal;
    ListView CommentListView;
    ArrayList<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

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
        txtPrice.setText(meal.price + " DKK");
        //txtUser.setText(meal.userName);
        txtPortions.setText(meal.portions);

        //InitializaListView();
    }

    public String getLocationString() {
        return meal.address + ", " + meal.zipCode + " " + meal.city;
    }

    //Inspired by: https://stackoverflow.com/questions/10903754/input-text-dialog-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public void OpenDialogWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reserve This Meal");

        builder.setMessage("Are you sure you want to reserve " + meal.mealName + " for " + meal.price + " DKK?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void OpenCommentDialogWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Write a comment:");

        final EditText newcomment = new EditText(this);
        newcomment.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newcomment);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Comment comment = new Comment(newcomment.getText().toString());
                comments.add(comment);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void InitializaListView() {
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
