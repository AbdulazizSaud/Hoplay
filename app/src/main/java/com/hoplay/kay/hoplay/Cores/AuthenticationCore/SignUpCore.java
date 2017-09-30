package com.hoplay.kay.hoplay.Cores.AuthenticationCore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Signup;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;
import com.hoplay.kay.hoplay.Services.SchemaHelper;
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
    private SchemaHelper schemaHelper = new SchemaHelper(this);

    private boolean isDone=false,isExists = false;

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
    protected void signUp(final String email, final String username, final String password,final String promoCode) {


        loadingDialog(true);
        appAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            FirebaseUser user = appAuth.getCurrentUser();
                            insertInfoToDatabase(user.getUid(),email,username,promoCode);

//                            // success message
//                            String strMeatMsg = String.format(getResources().getString(R.string.signup_successful_message), username);
//                            Toast.makeText(getApplicationContext(), strMeatMsg, Toast.LENGTH_LONG).show();
                            // switch to main AppMenu
                            app.sendEmailVerification(user,username);
                            toLogin();


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





    private void setNewPromoCode(final String promoCode,final String username)
    {


        DatabaseReference usernamesRef = app.getDatabaseUserNames();

        usernamesRef.child(promoCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                {

                    DatabaseReference promoRef = app.getDatabasePromoCode();
                    promoRef.child(promoCode+"/users/"+username).setValue(false);

                    HashMap<String,Object> pointStructure = new HashMap<>();

                    pointStructure.put("points",0);
                    pointStructure.put("users","null");
                    promoRef.child(username).setValue(pointStructure);

                    isExists = true;

                }
                isDone = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void insertInfoToDatabase(final String UID,final String email, final String username,final String promoCode) {

        final DatabaseReference usersInfoRef = app.getDatabaseUsersInfo().child(UID);

        final HashMap<String,String> map = new HashMap<>();

        map.put(FIREBASE_USERNAME_ATTR,username.toLowerCase());
        map.put(FIREBASE_BIO_ATTR,"Bio");
        map.put(FIREBASE_ACCOUNT_TYPE_ATTR,"REGULAR");
        map.put(FIREBASE_EMAIL_ATTR,email);
        map.put(FIREBASE_PICTURE_URL_ATTR,"default");

        // promoCode
        if(promoCode != null)
        setNewPromoCode(promoCode,username);
        else {
            isDone = true;
        }
        //-------------------


        CallbackHandlerCondition callbackHandlerCondition = new CallbackHandlerCondition() {
            @Override
            public boolean callBack() {

                if(isExists)
                {
                    map.put(FIREBASE_PROMO_CODE_ATTR,promoCode);
                }

                if(isDone) {
                    usersInfoRef.child(FIREBASE_DETAILS_ATTR).setValue(map);
                    return true;
                }
                return false;
            }
        };


        new HandlerCondition(callbackHandlerCondition,0);

        // user name list
        app.getDatabaseUserNames().child(username).setValue(UID);

    }


}