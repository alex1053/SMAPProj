package com.mealsharedev.mealshare;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mealsharedev.mealshare.Models.Meal;

public class MealDetailsActivity extends AppCompatActivity {

    Button btnBuy, btnBack;
    TextView txtMeal, txtUser, txtLocation, txtTime, txtDescription, txtPrice, txtPortions;
    Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        btnBuy = findViewById(R.id.btnBuy);
        btnBack = findViewById(R.id.btnBack);
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
        txtUser.setText(meal.userName);
        txtPortions.setText(meal.portions);
    }

    public String getLocationString()
    {
        return meal.address + ", " + meal.zipCode + " " + meal.city;
    }

    //Inspired by: https://stackoverflow.com/questions/10903754/input-text-dialog-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     public void OpenDialogWindow()
     {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("Reserve This Meal");

         builder.setMessage("Are you sure you want to reserve " + meal.mealName + " for " + meal.price + " DKK?");

         builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {finish();}
         });
         builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {dialog.cancel();}
         });

         builder.show();
     }
}
