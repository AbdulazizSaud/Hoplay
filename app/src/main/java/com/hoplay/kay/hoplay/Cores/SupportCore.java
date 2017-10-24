package com.hoplay.kay.hoplay.Cores;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.AsyncTask;
import android.service.textservice.SpellCheckerService;
import android.util.Log;
import android.widget.Toast;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.CoresAbstract.Support;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.hoplay.kay.hoplay.Services.GMailSender;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by Kay on 7/28/2017.
 */

public class SupportCore extends Support implements FirebasePaths{


    private FirebaseAuth mAuth;
    private App app;
    Session session = null ;


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

        // add time
        Date currentTime = Calendar.getInstance().getTime();
        reportIDRef.child(FIREBASE_SUPPORT_TIME_ATTR).setValue(currentTime+"");


        // Send Notification Email
        sendEmail(reportTitle,reportMessage);




    }


    private  void  sendEmail(final String subject , final String content){


         // THIS WAY TO OPEN EMAIL INTENT :
//        String[] TO = {"khaled.f.alhindi@gmail.com"};
//
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//
//
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
//        } catch (android.content.ActivityNotFoundException ex) {
//
//        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("hoplaymails@gmail.com",
                            "mail@hoplay.code=DSEW#RRVMSL@$^^&$$##@");
                    sender.sendMail("Hoplay Support Email"+app.getUserInformation().getUserEmail(),

                            "Subject is :\n"+subject+"\n"+"Mail :\n"+content,
                            "hoplaymails@gmail.com", "hoplaymails@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();


    }



}
