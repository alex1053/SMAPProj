package com.mealsharedev.mealshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;

public class NewMealActivity extends AppCompatActivity {

    Button btnCancel, btnSave;
    EditText mealName, mealDescription, mealPrice, mealLocation, mealAmount, mealZipCode, mealCity, mealDay, mealMonth, mealYear, mealHour, mealMinute;
    Spinner spinDay, spinMonth, spinYear, spinHour, spinMinute;
    DatabaseReference mDatabaseRef;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mealName = findViewById(R.id.editMealName);
        mealDescription = findViewById(R.id.editDescription);
        mealLocation = findViewById(R.id.editLocation);
        mealZipCode = findViewById(R.id.editZipCode);
        mealCity = findViewById(R.id.editCity);
        mealPrice = findViewById(R.id.editPrice);
        mealAmount = findViewById(R.id.editAmount);
        InitializeButtons();
        InitializeSpinners();
        InitializeValidate();

        //Meal meal = new Meal("mail","name","meal","desc","portion","price","location","zip","city","time");
        //mDatabaseRef.child("meals").child(meal.mealName).setValue(meal);
    }

    private void writeNewMeal()
    {
        //String UserEmail = mAuth.getCurrentUser().getEmail().toString();
        //String UserName = mAuth.getCurrentUser().getDisplayName().toString();
        String UserEmail = "mail";
        String UserName = "name";

        Meal meal = new Meal(UserEmail,
                             UserName,
                             mealName.getText().toString(),
                             mealDescription.getText().toString(),
                             mealAmount.getText().toString(),
                             mealPrice.getText().toString(),
                             mealLocation.getText().toString(),
                             mealZipCode.getText().toString(),
                             mealCity.getText().toString(),
                             getTimeStamp());

        DatabaseReference mealsRef = mDatabase.getReference("meals");

        String key = mealsRef.push().getKey();
    //    String meals1 = mDatabaseRef.child("meals").child(mealName.getText().toString()).push().getKey();
     //   Task<Void> meals = mDatabaseRef.child("meals").child(key).setValue(meal);
         mealsRef.child(key).setValue(new Comment("what what", "yo!"));
         /*
         addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("FIREBASE", "task complete");
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FIREBASE", "task failed");
            }
        }).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FIREBASE", "task Success");
            }
        }).addOnCanceledListener(this, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d("FIREBASE", "task cancel");
            }
        });
        */
        Log.d("FIREBASE", mealsRef.toString());

    }

    private void writeNewMeal2()
    {
        String UserEmail = mAuth.getCurrentUser().getEmail().toString();
        String UserName = mAuth.getCurrentUser().getDisplayName().toString();
        //String UserEmail = "mail";
        //String UserName = "name";

        Meal meal = new Meal(UserEmail,
                UserName,
                mealName.getText().toString(),
                mealDescription.getText().toString(),
                mealAmount.getText().toString(),
                mealPrice.getText().toString(),
                mealLocation.getText().toString(),
                mealZipCode.getText().toString(),
                mealCity.getText().toString(),
                getTimeStamp());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("meals");

        myRef.setValue(meal);

    }

    private String getTimeStamp()
    {
        return spinDay.getSelectedItem().toString() + " " + spinMonth.getSelectedItem().toString() + " " + spinYear.getSelectedItem().toString()
                + ". " + spinHour.getSelectedItem().toString() + ":" + spinMinute.getSelectedItem().toString();
    }

    private void InitializeButtons()
    {
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewMeal();
                //finish();
                Toast.makeText(NewMealActivity.this, "Your meal is now shared!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitializeSpinners()
    {
        spinDay = findViewById(R.id.spinDay);
        spinMonth = findViewById(R.id.spinMonth);
        spinYear = findViewById(R.id.spinYear);
        spinHour = findViewById(R.id.spinHour);
        spinMinute = findViewById(R.id.spinMinute);

        //Inspired by: https://developer.android.com/guide/topics/ui/controls/spinner
        ArrayAdapter<CharSequence> monthadapter = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonth.setAdapter(monthadapter);

        ArrayAdapter<CharSequence> dayadapter = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinDay.setAdapter(dayadapter);

        ArrayAdapter<CharSequence> yearadapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinYear.setAdapter(yearadapter);

        ArrayAdapter<CharSequence> houradapter = ArrayAdapter.createFromResource(this,
                R.array.hour_array, android.R.layout.simple_spinner_item);
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHour.setAdapter(houradapter);

        ArrayAdapter<CharSequence> minutadapter = ArrayAdapter.createFromResource(this,
                R.array.minute_array, android.R.layout.simple_spinner_item);
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMinute.setAdapter(minutadapter);
    }

    private void InitializeValidate()
    {
        //Inspired by: https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input
        mealName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealName.getText().toString().length() == 0)
                    mealName.setError(getText(R.string.validate_empty_field));
                if(mealName.getText().toString().length() > 30)
                    mealName.setError(getText(R.string.validate_too_many_characters));
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
        mealZipCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealZipCode.getText().toString().length() == 0)
                    mealZipCode.setError(getText(R.string.validate_empty_field));
                if(mealZipCode.getText().toString().length() > 4)
                    mealZipCode.setError(getText(R.string.validate_too_many_characters));
            }
        });
        mealCity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mealCity.getText().toString().length() == 0)
                    mealCity.setError(getText(R.string.validate_empty_field));
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
