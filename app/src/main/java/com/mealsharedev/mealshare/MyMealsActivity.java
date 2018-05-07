package com.mealsharedev.mealshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyMealsActivity extends AppCompatActivity {

    TextView txtMealHeader;
    RadioButton btnMyMeals, btnAssignedMeals;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meals);

        btnBack = findViewById(R.id.btnBack);
        btnMyMeals = findViewById(R.id.btnMyMeals);
        btnAssignedMeals = findViewById(R.id.btnAssignedMeals);
        txtMealHeader = findViewById(R.id.txtListViewHeader);
        btnMyMeals.setChecked(true);
        SetViewByButtons(btnMyMeals.isChecked());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMyMeals.setChecked(true);
                btnAssignedMeals.setChecked(false);
                SetViewByButtons(btnMyMeals.isChecked());
            }
        });

        btnAssignedMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAssignedMeals.setChecked(true);
                btnMyMeals.setChecked(false);
                SetViewByButtons(btnMyMeals.isChecked());
            }
        });
    }

    private void SetViewByButtons(boolean myMeals)
    {
        if(myMeals) {
            txtMealHeader.setText("My Meals");
        }
        else{
            txtMealHeader.setText("Assigned Meals");}
    }
}