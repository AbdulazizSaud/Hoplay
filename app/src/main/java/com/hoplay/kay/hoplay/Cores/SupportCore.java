package com.hoplay.kay.hoplay.Cores;

import android.util.Log;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.CoresAbstract.Support;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

/**
 * Created by Kay on 7/28/2017.
 */

public class SupportCore extends Support implements FirebasePaths{


    private FirebaseAuth mAuth;
    private App app;

    @Override
    protected void OnStartActivity() {

        app = App.getInstance();
        mAuth = app.getAuth();
    }

    @Override
    protected void storeReport(String reportTitle, String reportMessage) {


        // Get the support ref
       DatabaseReference supportRef = app.getDatabaseSupport();

        // Generate unique id for the message
        String uniqeID = UUID.randomUUID().toString();


        // add a parent with the user id and add the title with
       DatabaseReference reportIDRef = supportRef.child(uniqeID);

        // add the title
        reportIDRef.child(FIREBASE_SUPPORT_TITLE_ATTR).setValue(reportTitle);

        // add the message
        reportIDRef.child(FIREBASE_SUPPORT_MESSAGE_ATTR).setValue(reportMessage);

        // add the user email
        reportIDRef.child(FIREBASE_SUPPORT_EMAIL_ATTR).setValue(mAuth.getCurrentUser().getEmail());

        // add the username
        reportIDRef.child(FIREBASE_SUPPORT_USERNAME).setValue(app.getUserInformation().getUsername());

        // add the user ID
        reportIDRef.child(FIREBASE_SUPPORT_UID).setValue(mAuth.getCurrentUser().getUid());


    }
}
