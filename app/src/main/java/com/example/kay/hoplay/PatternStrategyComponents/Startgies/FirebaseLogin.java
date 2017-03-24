package com.example.kay.hoplay.PatternStrategyComponents.Startgies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Authentication.LoginActivity;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

/**
 * Created by Kay on 2/7/2017.
 */

public  abstract class FirebaseLogin implements PattrenStrategy<Task<AuthResult>>
{

    public  FirebaseLogin(Activity activity, String username, String password)
    {
        exceute(activity,username,password);
    }

    private void exceute(Activity activity,String username,String password)
    {

        FirebaseAuth  mAuth = App.getInstance().getAuth();


        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                afterComplete(task);
            }
        });

    }

    public abstract void afterComplete(Task<AuthResult> results);

}
