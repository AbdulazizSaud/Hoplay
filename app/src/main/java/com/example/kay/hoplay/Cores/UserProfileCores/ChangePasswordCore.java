package com.example.kay.hoplay.Cores.UserProfileCores;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.ChangePassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Kay on 6/1/2017.
 */

public class ChangePasswordCore  extends ChangePassword {


    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void OnStartActivity() {
        mAuth = app.getAuth();
        mFirebaseUser = mAuth.getCurrentUser();


    }

    @Override
    protected void validateOldPassword(String oldPassword) {

        String userEmail = app.getUserInformation().getUserEmail();
        if (oldPassword.isEmpty())
            return;

        mAuth.signInWithEmailAndPassword(userEmail, oldPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                        updateUserPassword(newPassword.getText().toString().trim());
                } else {
                    Snackbar snackbar = Snackbar
                            .make(changePasswordRelativeLayout, "Old Password is incorrect", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });

    }

    @Override
    protected void updateUserPassword(String newPassword) {

        mFirebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Your Password is updated successfully",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }


}
