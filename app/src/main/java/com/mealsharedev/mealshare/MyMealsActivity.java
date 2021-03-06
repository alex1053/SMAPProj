package com.mealsharedev.mealshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mealsharedev.mealshare.Adapters.MyMealsAdapter;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.dao.FirebaseDAO;

import java.util.ArrayList;
import java.util.Iterator;

import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_MY_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_GET_RESERVED_MEALS;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_MY_MEALS_EXTRA;
import static com.mealsharedev.mealshare.dao.FirebaseDAO.DAO_RESERVED_MEALS_EXTRA;

public class MyMealsActivity extends headerActivity {

    TextView txtMealHeader;
    RadioButton btnMyMeals, btnReservedMeals;
    Button btnBack, btnDelete;
    ListView MealListView;
    ArrayList<Meal> meals = new ArrayList<>();
    MyMealsAdapter mealOverviewAdapter;
    boolean isDeleteButtonPressed = false;
    ArrayList<Meal> deletedMeals = new ArrayList<>();
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

    BroadcastReceiver assignedMealsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tmpList = intent.getParcelableArrayListExtra(DAO_RESERVED_MEALS_EXTRA);
            meals.clear();
            meals.addAll(tmpList);
            mealOverviewAdapter.notifyDataSetChanged();
        }
    };

    BroadcastReceiver signOutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MyMealsActivity.this.setResult(RESULT_OK);
            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meals);

        txtMealHeader = findViewById(R.id.txtListViewHeader);
        notificationHandler = new NotificationHandler(this);

        IntentFilter filter = new IntentFilter(DAO_GET_MY_MEALS);
        LocalBroadcastManager.getInstance(this).registerReceiver(myMealsReceiver, filter);

        IntentFilter filterr = new IntentFilter(DAO_GET_RESERVED_MEALS);
        LocalBroadcastManager.getInstance(MyMealsActivity.this).registerReceiver(assignedMealsReceiver, filterr);

        IntentFilter signOutFilter = new IntentFilter(SIGNOUT_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(signOutReceiver, signOutFilter);

        InitializeButtons();
        SetViewByButtons(btnMyMeals.isChecked());
        InitializaListView();
        dao = new FirebaseDAO(this);
        dao.getMyMeals();

        setHeadings(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void setResultForActivity() {
        if (deletedMeals.size() > 0) {
            Intent intent = new Intent();
            intent.putExtra("deletedMeals", deletedMeals);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
    }

    @Override
    public void onBackPressed() {
        setResultForActivity();
        super.onBackPressed();
    }

    private void SetViewByButtons(boolean myMeals) {
        if (myMeals) {
            txtMealHeader.setText(getString(R.string.my_meals_header));
            btnDelete.setText(getString(R.string.delete));
        } else {
            txtMealHeader.setText(getString(R.string.shared_meals_header));
            btnDelete.setText(getString(R.string.unsubcribe));
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
                Iterator<Meal> i = meals.iterator();
                while (i.hasNext()) {
                    Meal tmp = i.next();
                    if (tmp.getMealId().equals(mealToDelete.getMealId())) {
                        i.remove();
                        mealOverviewAdapter.notifyDataSetChanged();
                        break;
                    }
                }
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
        if (btnMyMeals.isChecked()) {
            dao.deleteMeal(mealToDelete);
            isDeleteButtonPressed = false;
            deletedMeals.add(mealToDelete);
            btnDelete.setText(getString(R.string.delete));
            Toast.makeText(MyMealsActivity.this, getString(R.string.meal_deleted), Toast.LENGTH_SHORT).show();
        } else {
            mealToDelete.portions = String.valueOf(Integer.parseInt(mealToDelete.portions) + 1);
            dao.optOutMeal(mealToDelete);
        }
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
                setResultForActivity();
                finish();
            }
        });

        btnMyMeals = findViewById(R.id.btnMyMeals);
        btnMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMyMeals.setChecked(true);
                btnReservedMeals.setChecked(false);
                dao.getMyMeals();
                SetViewByButtons(btnMyMeals.isChecked());
            }
        });

        btnReservedMeals = findViewById(R.id.btnAssignedMeals);
        btnReservedMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReservedMeals.setChecked(true);
                btnMyMeals.setChecked(false);
                dao.getReservedMeals();
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