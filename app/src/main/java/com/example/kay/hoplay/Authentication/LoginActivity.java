package com.example.kay.hoplay.Authentication;

//hello


import android.content.Intent;

import android.support.annotation.NonNull;

import android.widget.Toast;


import com.example.kay.hoplay.Activities.MainAppMenu;
import com.example.kay.hoplay.App.App;

import com.example.kay.hoplay.PatternStrategyComponents.Startgies.FirebaseLogin;
import com.example.kay.hoplay.Services.GetAPI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.HashMap;

/*


 */

public class LoginActivity extends Login {

    // Get firebase authentication from App
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    // this method will do some init to check if user is authenticated , if yes .. it will switch to MainAppMenu
    @Override
    public void OnStartActivity() {
        mAuth = app.getAuth();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
                    startActivity(i);
                }
            }
        };

    }


    // this method will do auth to check if the user is existing
    // receive:  username , password

    // if success, it will switch to a mainApp and give a success message
    // if fail , it will give the user a failed message

    @Override
    protected void login(String username, String password) {

    }

    // old method .. perhaps we will need it in feature
    @Override
    public JSONObject startAPI() {
        JSONObject jsonAPI = null;
        HashMap<String, String> data = new HashMap<>();

        data.put("username", usernameSignIn.getText().toString().trim());
        data.put("password", passwordSignIn.getText().toString().trim());
        jsonAPI = App.getInstance().getAPI(GetAPI.LOGIN, data);

        return jsonAPI;
    }


}


