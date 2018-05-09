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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mealsharedev.mealshare.Adapters.MyMealsAdapter;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.dao.FirebaseDAO;

import java.util.ArrayList;

import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_MY_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_MY_MEALS_EXTRA;

public class MyMealsActivity extends AppCompatActivity {

    TextView txtMealHeader;
    RadioButton btnMyMeals, btnAssignedMeals;
    Button btnBack, btnDelete;
    ListView MealListView;
    ArrayList<Meal> meals = new ArrayList<>();
    MyMealsAdapter mealOverviewAdapter;
    boolean isDeleteButtonPressed = false;
    NotificationHandler notificationHandler;
    private FirebaseDAO dao;

    BroadcastReceiver myMealsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tmpList = intent.getParcelableArrayListExtra(DAO_MY_MEALS_EXTRA);
            meals.clear();
            meals.addAll(tmpList);
            mealOverviewAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meals);

        txtMealHeader = findViewById(R.id.txtListViewHeader);
        notificationHandler = new NotificationHandler(this);

        IntentFilter filter = new IntentFilter(DAO_GET_MY_MEALS);
        LocalBroadcastManager.getInstance(this).registerReceiver(myMealsReceiver, filter);

        InitializeButtons();
        SetViewByButtons(btnMyMeals.isChecked());
        InitializaListView();
        dao = new FirebaseDAO(this);
        dao.getMyMeals();
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
        dao.deleteMeal(mealToDelete);
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
                    notificationHandler.NotifyCommentOnSameMeal(meals.get(i));
                } else {
                    OpenMealDetails(meals.get(i));
                }
            }
        });
    }
}