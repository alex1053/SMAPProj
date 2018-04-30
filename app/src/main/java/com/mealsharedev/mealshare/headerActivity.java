package com.mealsharedev.mealshare;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Frederik on 28/04/2018.
 */

public class headerActivity extends AppCompatActivity {

    ImageView HeaderImg;
    Button  logout;
    TextView userID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        HeaderImg = findViewById(R.id.headerImg);

    }

    protected void setHeadings (String user) {
        logout = findViewById(R.id.headerBtn);
        userID = findViewById(R.id.headerText);

        userID.setText(user);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                setHeadings(null);

                //TODO redirect to LoginActivity (call finish()????)
            }
        });
    }
}
