package com.mealsharedev.mealshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mealsharedev.mealshare.Adapters.CommentAdapter;
import com.mealsharedev.mealshare.Adapters.MealAdapter;
import com.mealsharedev.mealshare.Models.Meal;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends headerActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
    Button btnNewMeal, btnMyMeals;
    ListView MealListView;
    ArrayList<Meal> meals = new ArrayList<>();
    FirebaseFirestore mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(LOGOUT_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);


        mDB = FirebaseFirestore.getInstance();
        btnMyMeals = findViewById(R.id.btnMyMeals);
        btnMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMyMealsActivity();
            }
        });
        btnNewMeal = findViewById(R.id.btnNewMeal);
        btnNewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenNewMealActivity();
            }
        });

        Intent intent = getIntent();
        setHeadings(intent.getStringExtra("user"));
        //GetData();
        InitializaListView();
    }


    public void GetData() {
        mDB.collection("meals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Map<String, Object> meal = document.getData();
                                Meal tempMeal = new Meal(meal);
                                meals.add(tempMeal);
                            }
                        }
                    }
                });
    }

    public void OpenNewMealActivity() {
        Intent intent = new Intent(this, NewMealActivity.class);
        startActivity(intent);
    }

    public void OpenMyMealsActivity() {
        Intent intent = new Intent(this, MyMealsActivity.class);
        startActivity(intent);
    }

    public void OpenMealDetails(Meal meal) {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    public void InitializaListView() {
        /*Meal meal = new Meal("hhh", "Anders Lehman", "Lasagna", "Delicious italian lasagna!", "6", "39", "Finlandsgade 26", "8200", "Aarhus N", "22-08-18. 19:00");
        Meal meal2 = new Meal("hhh", "Anders Lehman", "Burger", "Delicious american burger!", "3", "59", "Finlandsgade 26", "8200", "Aarhus N", "22-08-18. 19:00");
        Meal meal3 = new Meal("hhh", "Anders Lehman", "Sharwama med kylling", "info info info!", "6", "39", "Edwin Rahrs vej 26", "8210", "Brabrand", "22-08-18. 19:00");
        Meal meal4 = new Meal("hhh", "Anders Lehman", "Cokoladekage m. kaffeglasur", "Bedste kage i verden!", "10", "20", "Skanderborgvej 26", "8660", "Skanderborg", "22-08-18. 19:00");
        Meal meal5 = new Meal("hhh", "Anders Lehman", "Sushi", "Delicious italian lasagna!", "2", "119", "Søndergade 7", "8000", "Aarhus", "22-08-18. 19:00");

        meals.add(meal);
        meals.add(meal2);
        meals.add(meal3);
        meals.add(meal4);
        meals.add(meal5);*/

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
