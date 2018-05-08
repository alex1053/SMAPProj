package com.mealsharedev.mealshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mealsharedev.mealshare.Models.User;

import java.util.Arrays;


public class LoginActivity extends headerActivity {

    private static final String EMAIL = "email";
    private static final String SUBSCRIPTIONS = "userSubsriptions";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;

    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessToken accessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();

        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        accessToken = AccessToken.getCurrentAccessToken();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
                // App code
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "CANCEL!", Toast.LENGTH_SHORT).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                // App code
            }
        });
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        setHeadings(currentUser.getDisplayName());
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", currentUser.getDisplayName());
            startActivity(intent);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TokenHandler", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signinSuccess", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            User dbUser = new User(user.getUid(), user.getDisplayName(), user.getEmail());
                            addUserToDatabase(dbUser);
                            setUpSubscriptions(dbUser);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user", user.getDisplayName());
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signinFail", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void addUserToDatabase(User user) {
        mDB.collection("users").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("tag", "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "Failed!!!!!!!!!!!!");
                    }
                });
    }

    private void setUpSubscriptions(User user) {
        mDB.collection(SUBSCRIPTIONS).document(user.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult().getData() != null) {
                   mDB.collection(SUBSCRIPTIONS).document(user.getId()).set(user);
                }
            }
        });
    }
}
