package com.example.kay.hoplay.PatternStrategyComponents.Startgies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

/**
 * Created by Kay on 2/7/2017.
 */

public class FirebaseLogin extends PattrenStrategy {


    // Get firebase authentication from App
    private FirebaseAuth mAuth;

    private String username,password;

    private Activity activity;

    public FirebaseLogin(Activity activity,String username, String password)
    {
        this.username = username;
        this.password = password;
        this.activity = activity;
    }

    @Override
    public void execute() {
        mAuth = App.getInstance().getAuth();


        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    // results if it's successed
                    results.put(App.RESULTS_STATUS,App.SUCCESSED);
                    results.put(App.RESULTS_MESSAGE, App.SUCCESSED_LOGIN);

                } else {
                    // results if it's failed
                    results.put(App.RESULTS_STATUS,App.FAILED);
                    results.put(App.RESULTS_MESSAGE, App.FAILED_LOGIN);
                }
            }
        });


    }


    @Override
    public HashMap<String ,String> get() {
        if(results.get(App.RESULTS_STATUS) == null) {
            results.put(App.RESULTS_STATUS, App.FAILED);
            results.put(App.RESULTS_MESSAGE, App.FAILED_MESSAGE);
        }
        return results;
    }
}
