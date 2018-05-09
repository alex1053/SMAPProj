package com.mealsharedev.mealshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class headerActivity extends AppCompatActivity {

    public static final String SIGNOUT_BROADCAST = "logoutBroadcast";

    ImageView HeaderImg, ProfileImg;
    ImageButton logout;
    TextView userID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
        HeaderImg = findViewById(R.id.headerImg);

    }

    protected void setHeadings(String user) {
        logout = findViewById(R.id.imgSignOut);
        userID = findViewById(R.id.headerText);

        userID.setText(user);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(SIGNOUT_BROADCAST);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        });
    }
}
