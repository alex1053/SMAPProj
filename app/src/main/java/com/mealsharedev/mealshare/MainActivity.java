package com.mealsharedev.mealshare;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mealsharedev.mealshare.Adapters.MealAdapter;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.services.MealUpdateService;
import com.mealsharedev.mealshare.services.SubscriptionService;

import java.util.ArrayList;

public class MainActivity extends headerActivity {
    MealUpdateService updateService;
    SubscriptionService subscriptionService;
    private static final int NEW_MEAL_REQUEST_CODE = 1;
    private static final int DETAILS_REQUEST_CODE = 2;
    private static final int MY_MEALS_REQUEST_CODE = 3;


    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Meal> tmpList = intent.getParcelableArrayListExtra(MealUpdateService.NEW_MEALS_EXTRA);
            meals.clear();
            meals.addAll(tmpList);
            mealOverviewAdapter.notifyDataSetChanged();
        }
    };

    ServiceConnection updateServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            updateService = ((MealUpdateService.MealUpdateServiceBinder) service).getService();
            updateService.updateMeals();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    ServiceConnection subscriptionServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            subscriptionService = ((SubscriptionService.SubscriptionServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    BroadcastReceiver signOutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.setResult(RESULT_OK);
            finish();
        }
    };
    Button btnNewMeal, btnMyMeals;
    ListView MealListView;
    ArrayList<Meal> meals = new ArrayList<>();
    FirebaseFirestore mDB;
    MealAdapter mealOverviewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializaListView();
        bindToUpdateService();
        bindToSubscriptionService();

        IntentFilter signOutFilter = new IntentFilter(SIGNOUT_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(signOutReceiver, signOutFilter);


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

        setHeadings(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void bindToUpdateService() {
        Intent intent = new Intent(this, MealUpdateService.class);
        bindService(intent, updateServiceConnection, BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter(MealUpdateService.NEW_MEALS_AVAILABLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, filter);
    }

    private void bindToSubscriptionService() {
        Intent intent = new Intent(this, SubscriptionService.class);
        bindService(intent, subscriptionServiceConnection, BIND_AUTO_CREATE);
    }

    public void OpenNewMealActivity() {
        Intent intent = new Intent(this, NewMealActivity.class);
        startActivityForResult(intent, NEW_MEAL_REQUEST_CODE);
    }

    public void OpenMyMealsActivity() {
        Intent intent = new Intent(this, MyMealsActivity.class);
        startActivityForResult(intent, MY_MEALS_REQUEST_CODE);
    }

    public void OpenMealDetails(Meal meal) {
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("meal", meal);
        startActivityForResult(intent, DETAILS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateService.updateMeals();
    }

    @Override
    protected void onDestroy() {
        unbindService(updateServiceConnection);
        unbindService(subscriptionServiceConnection);
        super.onDestroy();
    }

    public void InitializaListView() {
        mealOverviewAdapter = new MealAdapter(this, meals);
        MealListView = findViewById(R.id.listView);
        MealListView.setAdapter(mealOverviewAdapter);
        MealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenMealDetails(meals.get(i));
            }
        });
    }
}
