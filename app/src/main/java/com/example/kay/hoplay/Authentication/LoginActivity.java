package com.example.kay.hoplay.Authentication;

//hello


import android.support.annotation.NonNull;

import android.util.Log;
import android.widget.Toast;


import com.example.kay.hoplay.App.App;

import com.example.kay.hoplay.PatternStrategyComponents.Startgies.FirebaseLogin;
import com.example.kay.hoplay.R;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/*


 */

public class LoginActivity extends Login {

    // Get firebase authentication from App
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser mFirebaseUser;

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
        mFirebaseUser = mAuth.getCurrentUser();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    app.getUserInformation().setUID(user.getUid());
                    toMainMenu();
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

        if (!username.equals("") || !password.equals("")) {

            loadingDialog(true);

            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        toMainMenu();
                    } else {
                        // results if it's failed
                        loadingDialog(false);
                        Toast.makeText(getApplicationContext(), "Hi i'm fail message in LoginActivity.class", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }


}


