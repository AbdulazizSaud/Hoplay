package com.example.kay.hoplay.Activities.ForgetPasswordActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.ActivityInterface;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Authentication.LoginActivity;
import com.example.kay.hoplay.PatternStrategyComponents.Startgies.FirebaseLogin;
import com.example.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends ForgetPassword{



    private FirebaseAuth mAuth;

    public void forgetPassword(View view) {

        if (mAuth != null) {
            final String email = forgetPasswordEditText.getText().toString().trim();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password rest sent it to your email : " + email + ", Please check your inbox", Toast.LENGTH_LONG).show();
                        goToLogin();
                    }
                }
            });

        } else {
            Log.e("------>", "mauth is null");
        }
    }


    @Override
    public void OnStartActivity() {
        mAuth = App.getInstance().getAuth();
    }
}