package com.mealsharedev.mealshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mealsharedev.mealshare.Adapters.CommentAdapter;
import com.mealsharedev.mealshare.Adapters.MealAdapter;
import com.mealsharedev.mealshare.Models.Meal;

import java.util.ArrayList;

public class MainActivity extends headerActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
    Button btnNewMeal;
    ListView MealListView;
    ArrayList<Meal> meals = new ArrayList<>();
    DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(LOGOUT_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnNewMeal = findViewById(R.id.btnNewMeal);
        btnNewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenNewMealActivity();
            }
        });

        Intent intent = getIntent();
        setHeadings(intent.getStringExtra("user"));

        InitializaListView();
    }

    public void OpenNewMealActivity()
    {
        Intent intent = new Intent(this, NewMealActivity.class);
        startActivity(intent);
    }

    public void OpenMealDetails(Meal meal)
    {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    public void InitializaListView()
    {
        Meal meal = new Meal("hhh", "Anders Lehman", "Lasagna", "Delicious italian lasagna!", "6", "39", "Finlandsgade 26","8200", "Aarhus N", "22-08-18. 19:00");
        Meal meal2 = new Meal("hhh", "Anders Lehman", "Burger", "Delicious american burger!", "3", "59", "Finlandsgade 26","8200", "Aarhus N", "22-08-18. 19:00");
        Meal meal3 = new Meal("hhh", "Anders Lehman", "Sharwama med kylling", "info info info!", "6", "39", "Edwin Rahrs vej 26","8210", "Brabrand", "22-08-18. 19:00");
        Meal meal4 = new Meal("hhh", "Anders Lehman", "Cokoladekage m. kaffeglasur", "Bedste kage i verden!", "10", "20", "Skanderborgvej 26","8660", "Skanderborg", "22-08-18. 19:00");
        Meal meal5 = new Meal("hhh", "Anders Lehman", "Sushi", "Delicious italian lasagna!", "2", "119", "Søndergade 7","8000", "Aarhus", "22-08-18. 19:00");

        meals.add(meal);
        meals.add(meal2);
        meals.add(meal3);
        meals.add(meal4);
        meals.add(meal5);

        MealAdapter mealAdapter = new MealAdapter(this, meals);
        MealListView = findViewById(R.id.listView);
        MealListView.setAdapter(mealAdapter);
        MealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenMealDetails(meals.get(i));
            }
        });
    }
}
