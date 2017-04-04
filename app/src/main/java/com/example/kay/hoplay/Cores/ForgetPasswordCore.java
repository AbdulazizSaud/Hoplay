package com.example.kay.hoplay.Cores;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kay.hoplay.CoresAbstract.ForgetPassword;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordCore extends ForgetPassword {



    private FirebaseAuth mAuth;

    public void forgetPassword(View view) {

        if (mAuth != null) {
            final String email = forgetPasswordEditText.getText().toString().trim();
            if(!email.equals(""))
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        String Msg = String.format(getResources().getString( R.string.forget_password_reset_password), email);
                        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();
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