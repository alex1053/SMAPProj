package com.mealsharedev.mealshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mealsharedev.mealshare.Adapters.MealAdapter;
import com.mealsharedev.mealshare.Adapters.MyMealsAdapter;
import com.mealsharedev.mealshare.Models.Meal;

import java.util.ArrayList;

public class MyMealsActivity extends AppCompatActivity {

    TextView txtMealHeader;
    RadioButton btnMyMeals, btnAssignedMeals;
    Button btnBack, btnDelete;
    ListView MealListView;
    ArrayList<Meal> meals = new ArrayList<>();
    MyMealsAdapter mealOverviewAdapter;
    boolean isDeleteButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meals);

        txtMealHeader = findViewById(R.id.txtListViewHeader);

        Meal meal = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        Meal meal2 = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        Meal meal3 = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        Meal meal4 = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        Meal meal5 = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        Meal meal6 = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        Meal meal7 = new Meal("Lauraaaaa", "Maad","Besrkivelse","22","150","Skovagervej 21","8990","Fårup","Idag" );
        meals.add(meal);
        meals.add(meal);
        meals.add(meal2);
        meals.add(meal3);
        meals.add(meal4);
        meals.add(meal5);
        meals.add(meal6);
        meals.add(meal7);

        InitializeButtons();
        SetViewByButtons(btnMyMeals.isChecked());
        InitializaListView();
    }

    private void SetViewByButtons(boolean myMeals) {
        if (myMeals) {
            txtMealHeader.setText(getString(R.string.my_meals_header));
        } else {
            txtMealHeader.setText(getString(R.string.shared_meals_header));
        }
    }

    //Inspired by: https://stackoverflow.com/questions/10903754/input-text-dialog-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public void OpenDialogWindow(Meal mealToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reserve This Meal");

        builder.setMessage("Are you sure you want to delete " + mealToDelete.mealName + "?");

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteMeal(mealToDelete);
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

    private void DeleteMeal(Meal mealToDelete) {


        isDeleteButtonPressed = false;
        btnDelete.setText(getString(R.string.delete));
        Toast.makeText(MyMealsActivity.this, "The meal is now deleted", Toast.LENGTH_SHORT).show();
    }

    public void OpenMealDetails(Meal meal) {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    public void InitializeButtons() {

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDeleteButtonPressed) {
                    isDeleteButtonPressed = true;
                    btnDelete.setText(getString(R.string.cancel));
                    Toast.makeText(MyMealsActivity.this, "Click on the meal you want to remove", Toast.LENGTH_SHORT).show();
                } else {
                    isDeleteButtonPressed = false;
                    btnDelete.setText(getString(R.string.delete));
                }
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnMyMeals = findViewById(R.id.btnMyMeals);
        btnMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMyMeals.setChecked(true);
                btnAssignedMeals.setChecked(false);
                SetViewByButtons(btnMyMeals.isChecked());
            }
        });

        btnAssignedMeals = findViewById(R.id.btnAssignedMeals);
        btnAssignedMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAssignedMeals.setChecked(true);
                btnMyMeals.setChecked(false);
                SetViewByButtons(btnMyMeals.isChecked());
            }
        });

        btnMyMeals.setChecked(true);
    }

    public void InitializaListView() {
        mealOverviewAdapter = new MyMealsAdapter(this, meals);
        MealListView = findViewById(R.id.listViewMeals);
        MealListView.setAdapter(mealOverviewAdapter);
        MealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isDeleteButtonPressed) {
                    OpenDialogWindow(meals.get(i));
                }
                else{
                    OpenMealDetails(meals.get(i));
                }
            }
        });
    }
}