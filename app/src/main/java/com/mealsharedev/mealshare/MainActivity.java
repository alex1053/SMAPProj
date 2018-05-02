package com.mealsharedev.mealshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference meals = databaseRef.child("meals");

        Query mealsQuery = meals;
        mealsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Object> meals = new ArrayList<>();
                for(DataSnapshot mealSnapshot: dataSnapshot.getChildren()){
                    meals.add(mealSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //InitializaListView();
    }

    public void OpenNewMealActivity()
    {
        Intent intent = new Intent(this, NewMealActivity.class);
        startActivity(intent);
    }

    public void OpenMealDetails(Meal meal)
    {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        startActivity(intent);
    }

    public void InitializaListView()
    {
        MealAdapter mealAdapter = new MealAdapter(this, null);
        MealListView = findViewById(R.id.listView);
        MealListView.setAdapter(mealAdapter);
        MealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //OpenMealDetails(meals.get(position));
            }
        });
    }
}
