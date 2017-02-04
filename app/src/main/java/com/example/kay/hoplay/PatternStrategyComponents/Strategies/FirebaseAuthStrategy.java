package com.example.kay.hoplay.PatternStrategyComponents.Strategies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.MainActivity;
import com.example.kay.hoplay.Activities.MainAppMenu;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.PatternStrategyComponents.DataCommon;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategyInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by azoz-pc on 1/25/2017.
 */

public  class FirebaseAuthStrategy implements PattrenStrategyInterface{


    public static final String SIGN_IN = "SIGN_IN";
    public static final String SIGN_UP = "SIGN_UP";

    private String messageState = Constants.FAILED+"_Message_"+Constants.FAILED_MESSAGE;
    private App app = App.getInstance();
    private FirebaseAuth appInstAuth = app.getmAuth();
    private FirebaseAuth.AuthStateListener authStateListener;



    public FirebaseAuthStrategy(){}


    public FirebaseAuthStrategy(Activity activity){
        initFirebaseAuthListener(activity);
    }

    @Override
    public void sendData(DataCommon data) {
    }

    @Override
    public DataCommon receiveData() {
        return null;
    }

    @Override
    public String excute(@NonNull DataCommon... args) {

        DataCommon<String> dataCommon = args[0];
        String[] dataCommonData = dataCommon.getData();

        if(dataCommonData.length != 4) {
            return messageState;
        }

         String auth=dataCommonData[0];
         String username=dataCommonData[1];
         String email=dataCommonData[2];
         String password=dataCommonData[3];

        if(auth.equals(SIGN_UP))
            signUp(email,username,password);
        else if(auth.equals(SIGN_IN))
            signIn(email,username,password);


        return messageState;

    }



    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }



    private void signIn(final String email,final String username,final String password){
        Log.i("---->","signIn");

        final Activity applicationActivity = app.getCurrentActivity();
        final Context applicationContext = applicationActivity.getApplicationContext();

        appInstAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(applicationActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(applicationContext, MainAppMenu.class);
                    applicationActivity.startActivity(i);
                    Toast.makeText(applicationContext, "Welcome "+App.SUCCESSED_LOGIN+" ;)", Toast.LENGTH_LONG).show();
                    setMessageState(Constants.SUCCESSED+"_Message_"+App.SUCCESSED_LOGIN);

                } else {
                    Toast.makeText(applicationContext, "Please check your email or password", Toast.LENGTH_LONG).show();
                    setMessageState(Constants.SUCCESSED+"_Message_"+App.FAILED_LOGIN);
                }
            }
        });

    }

    private void signUp(final String email,final String username,final String password){

        Log.i("---->","signup");

        final Activity applicationActivity = app.getCurrentActivity();
        final Context applicationContext = applicationActivity.getApplicationContext();

        appInstAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(applicationActivity
                ,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("username",username);
                                jsonObject.put("email",email);

                                app.getSocket().emit("signup_database",jsonObject);

                                Toast.makeText(applicationContext,App.SUCCESSED_CREATED_ACCOUNT,Toast.LENGTH_LONG).show();
                                Intent i = new Intent(applicationActivity, MainAppMenu.class);
                                applicationActivity.startActivity(i);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setMessageState(Constants.SUCCESSED+"_Message_"+Constants.SUCCESSED_CREATED_ACCOUNT);

                        }else {

                            Toast.makeText(applicationContext,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            setMessageState(Constants.FAILED+"_Message_"+task.getException().getMessage());
                        }
                    }
                });
    }


    private void initFirebaseAuthListener(final Activity activity){

        final Context applicationContext = activity.getApplicationContext();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(applicationContext, MainAppMenu.class);
                    activity.startActivity(i);
                } else {
                    // User is signed out
                     if(applicationContext != MainActivity.mainActivity.getApplicationContext()) {
                         Intent i = new Intent(applicationContext, MainActivity.class);
                         activity.startActivity(i);
                     }
                }
            }
        };

        app.setmAuthListener(authStateListener);
    }
}
