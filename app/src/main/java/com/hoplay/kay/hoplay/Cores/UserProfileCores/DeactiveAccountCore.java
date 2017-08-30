package com.hoplay.kay.hoplay.Cores.UserProfileCores;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.MainAppMenuCore;
import com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts.DeactiveAccount;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Kay on 7/29/2017.
 */

public class DeactiveAccountCore extends DeactiveAccount implements FirebasePaths {





    @Override
    protected void OnStartActivity() {

        app = App.getInstance();
        mAuth = app.getAuth();

    }

    @Override
    protected void authinticateUser(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingDialog(false);
                    showDeactiveAccDialog();
                } else {


                    loadingDialog(false);
                    Snackbar snackbar = Snackbar
                            .make(deactiveLayout, R.string.deactive_account_wrong_password, Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });


    }

    @Override
    protected void deactiveUserAccount() {


                final String userID = mAuth.getCurrentUser().getUid();


                // Delete username
                app.getDatabaseUserNames().child(app.getUserInformation().getUsername()).removeValue();

                // Delete user info
                app.getDatabaseUsersInfo().child(userID).removeValue();

                // Delete user Auth
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference usersInfoRef = app.getDatabaseUsersInfo();
                            usersInfoRef.child(userID).removeValue();
                        }
                    }
                });


    }
}
