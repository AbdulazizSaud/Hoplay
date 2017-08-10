package com.example.kay.hoplay.Cores.AuthenticationCore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Signup;
import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpCore extends Signup implements FirebasePaths{

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


        loadingDialog(true);
        appAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            FirebaseUser user = appAuth.getCurrentUser();

                            insertInfoToDatabase(user.getUid(),email,username);

//                            // success message
//                            String strMeatMsg = String.format(getResources().getString(R.string.signup_successful_message), username);
//                            Toast.makeText(getApplicationContext(), strMeatMsg, Toast.LENGTH_LONG).show();
                            // switch to main AppMenu
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                               Toast.makeText(getApplicationContext(),"Verificetion sent to your email",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            toMainMenuApp();


                        } else {
                            // failed message
                            loadingDialog(false);
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }


    @Override
    protected void checkUsername(String value) {

        app.getDatabaseUserNames().child(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currenCheckingStatus = (dataSnapshot.exists()) ? USER_EXIST : USER_NOT_EXIST;
                checkingUsername=false;
                checkUserCallBack();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void insertInfoToDatabase(final String UID,final String email, final String username) {

        DatabaseReference usersInfoRef = app.getDatabaseUsersInfo().child(UID);

        HashMap<String,String> map = new HashMap<>();

        map.put(FIREBASE_USERNAME_ATTR,username.toLowerCase());
        map.put(FIREBASE_BIO_ATTR,"Bio");
        map.put(FIREBASE_ACCOUNT_TYPE_ATTR,"REGULAR");
        map.put(FIREBASE_EMAIL_ATTR,email);
        map.put(FIREBASE_PICTURE_URL_ATTR,"default");
        usersInfoRef.child(FIREBASE_DETAILS_ATTR).setValue(map);

        // user name list
        app.getDatabaseUserNames().child(username).setValue(UID);



    }


}