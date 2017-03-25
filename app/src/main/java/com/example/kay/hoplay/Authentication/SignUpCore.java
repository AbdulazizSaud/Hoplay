package com.example.kay.hoplay.Authentication;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.GetAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUpCore extends Signup {

    // Get firebase authentication from App
    private FirebaseAuth appAuth;


    @Override
    public void OnStartActivity() {
        //.
        appAuth = FirebaseAuth.getInstance();

    }

    // this method will do auth to crearte a new user
    // receive:  username , password, email
    // if success, it will switch to a mainApp and give a success message
    // if fail , it will give the user a failed message

    @Override
    protected void signUp(final String email, final String username, final String password, final String nickname) {

        appAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            FirebaseUser user = appAuth.getCurrentUser();

                            insertInfoToDatabase(user.getUid(),email,username,nickname);

                            // success message
                            String strMeatMsg = String.format(getResources().getString(R.string.signup_successful_message), username);


                            Toast.makeText(getApplicationContext(), strMeatMsg, Toast.LENGTH_LONG).show();
                            // switch to main AppMenu
                            toMainMenuApp();


                        } else {
                            // failed message
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }


    private void insertInfoToDatabase(final String UID,final String email, final String username, final String nickname) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference usersInfoRef = app.getDatabaseUsers().child(UID);


        usersInfoRef.child(FirebasePaths.FIREBASE_USERNAME_PATH).setValue(username.toLowerCase());
        usersInfoRef.child(FirebasePaths.FIREBASE_NICKNAME_PATH).setValue(nickname);
        usersInfoRef.child(FirebasePaths.FIREBASE_EMAIL_PATH).setValue(email);
        usersInfoRef.child(FirebasePaths.FIREBASE_PICTURE_URL_PATH).setValue("default");



    }


}
