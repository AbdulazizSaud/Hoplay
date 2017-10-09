package com.hoplay.kay.hoplay.CoresAbstract;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.R;

public abstract class Support extends AppCompatActivity  {


    private EditText supportTitle ,  supportMessage;
    private Button sendButton ;
    private TextView supportActivityMessage ;
    private RelativeLayout supportRelativelayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        supportTitle = (EditText) findViewById(R.id.support_title_edittext);
        supportMessage = (EditText) findViewById(R.id.support_message_edittext);
        supportActivityMessage = (TextView) findViewById(R.id.activity_title_message);
        sendButton = (Button) findViewById(R.id.send_support_button);
        supportRelativelayout = (RelativeLayout)  findViewById(R.id.support_relativelayout);


        Typeface sansation = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getAssets(), "playregular.ttf");


        supportActivityMessage.setTypeface(sansation);
        supportTitle.setTypeface(playregular);
        supportMessage.setTypeface(playregular);
        sendButton.setTypeface(sansation);


        supportTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                supportTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_title_focused_32dp, 0);
                if (s.length() == 0) {
                    supportTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_title_unfocused_32dp, 0);
                }
            }
        });


        supportMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                supportMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mail_outline_focused_32dp, 0);
                if (s.length() == 0) {
                    supportMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mail_outline_not_focused_32dp, 0);
                }
            }
        });

        OnStartActivity();


    }



    protected abstract void OnStartActivity();
    protected abstract void storeReport(String reportTitle , String reportMessage);


    public void sendReport(View view){

        String reportTitle = supportTitle.getText().toString().trim();
        String reportMessage = supportMessage.getText().toString().trim();

        if (validReport(reportTitle,reportMessage)){
            storeReport(reportTitle,reportMessage);
            finish();
            Toast.makeText(getApplicationContext(),R.string.support_success_message,Toast.LENGTH_LONG).show();
        }


    }

    private boolean validReport(String reportTitle , String reportMessage){

        if (reportTitle.length() <1){

            Snackbar snackbar = Snackbar
                    .make(supportRelativelayout, R.string.support_title_error, Snackbar.LENGTH_LONG);

            snackbar.show();

            return false;
        }

        if (reportMessage.length() <1)
        {
            Snackbar snackbar = Snackbar
                    .make(supportRelativelayout, R.string.support_message_error, Snackbar.LENGTH_LONG);

            snackbar.show();

            return false;
        }


        return true;

    }


    @Override
    protected void onStart() {
        super.onStart();
        App.supportActivityIsActive = true ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.supportActivityIsActive = false;
    }

    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }




}
