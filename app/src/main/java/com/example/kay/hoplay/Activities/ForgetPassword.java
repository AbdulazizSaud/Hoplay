package com.example.kay.hoplay.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ForgetPassword extends AppCompatActivity {


    private Button forgetPasswordButton;
    private EditText forgetPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetPasswordButton = (Button) findViewById(R.id.search_account_button);
        forgetPasswordEditText = (EditText) findViewById(R.id.find_username_edittext);

    }

    public void forgetPassword(View view) {

        App app = App.getInstance();

        if (app.getmAuth() != null) {

            final String email = forgetPasswordEditText.getText().toString().trim();
            app.getmAuth().sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password rest sent it to your email : " + email + ", Please check your inbox", Toast.LENGTH_LONG).show();
                        goToMMainActivity();
                    }
                }
            });

        } else {
            Log.e("------>", "mauth is null");
        }
    }


    public void goToMMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}