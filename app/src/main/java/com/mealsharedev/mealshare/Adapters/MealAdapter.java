package com.mealsharedev.mealshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.R;

import java.util.ArrayList;

/**
 * Created by Laura on 02-05-2018.
 */

public class MealAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Meal> meals;

    public MealAdapter(Context context, ArrayList<Meal> meals){
        this.context = context;
        this.meals = meals;
    }

    @Override
    public int getCount() {
        return meals.size();}

    @Override
    public Object getItem(int position) {return meals.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater cityWeatherInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = cityWeatherInflator.inflate(R.layout.list_view_item, null);
        }

        Meal meal = meals.get(position);
        if(meal !=null){
            TextView txtMealName = (TextView) convertView.findViewById(R.id.txtMealName);
            txtMealName.setText(meal.mealName);
            TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            txtPrice.setText(meal.price + " DKK");
        }
        return convertView;
    }


}
