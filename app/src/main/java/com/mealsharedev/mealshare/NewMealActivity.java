package com.mealsharedev.mealshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewMealActivity extends AppCompatActivity {

    Button btnCancel;
    EditText mealName, mealDescription, mealPrice, mealLocation, mealAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mealName = findViewById(R.id.editMealName);
        mealDescription = findViewById(R.id.editDescription);
        mealLocation = findViewById(R.id.editLocation);
        mealPrice = findViewById(R.id.editPrice);
        mealAmount = findViewById(R.id.editAmount);

        InitializeValidate();
    }

    public void InitializeValidate()
    {
        //Inspired by: https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input
        mealName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealName.getText().toString().length() == 0)
                    mealName.setError(getText(R.string.validate_empty_field));
            }
        });
        mealDescription.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealDescription.getText().toString().length() == 0)
                    mealDescription.setError(getText(R.string.validate_empty_field));
            }
        });
        mealLocation.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealLocation.getText().toString().length() == 0)
                    mealLocation.setError(getText(R.string.validate_empty_field));
            }
        });
        mealAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealAmount.getText().toString().length() == 0)
                    mealAmount.setError(getText(R.string.validate_empty_field));
            }
        });
        mealPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealPrice.getText().toString().length() == 0)
                    mealPrice.setError(getText(R.string.validate_empty_field));
            }
        });
    }
}
