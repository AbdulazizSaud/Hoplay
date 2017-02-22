package com.example.kay.hoplay.Activities.ForgetPasswordActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.kay.hoplay.Activities.ActivityInterface;
import com.example.kay.hoplay.Authentication.LoginActivity;
import com.example.kay.hoplay.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Kay on 2/10/2017.
 */

public abstract class ForgetPassword extends AppCompatActivity implements ActivityInterface {

    protected Button forgetPasswordButton;
    protected EditText forgetPasswordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetPasswordButton = (Button) findViewById(R.id.search_account_button);
        forgetPasswordEditText = (EditText) findViewById(R.id.find_username_edittext);

        // this method call only on create , init mechinisms
        OnStartActivity();
    }



    public void goToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public abstract  void OnStartActivity();

}