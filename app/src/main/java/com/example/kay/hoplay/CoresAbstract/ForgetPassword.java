package com.example.kay.hoplay.CoresAbstract;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.util.Helper;

/**
 * Created by Kay on 2/10/2017.
 */

public abstract class ForgetPassword extends AppCompatActivity  {

    protected Button forgetPasswordButton;
    protected EditText forgetPasswordEditText;
    protected TextView findAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        forgetPasswordButton = (Button) findViewById(R.id.search_account_button);
        forgetPasswordEditText = (EditText) findViewById(R.id.find_username_edittext);
        findAccountTextView = (TextView) findViewById(R.id.find_username_textview);

        Typeface sansation = Typeface.createFromAsset(getApplicationContext().getAssets() ,"sansationbold.ttf");
        findAccountTextView.setTypeface(sansation);
        forgetPasswordButton.setTypeface(sansation);

        // User Helper to identify emails
        final Helper helper = new Helper();

        forgetPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                forgetPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mail_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    forgetPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mail_outline_not_focused_32dp, 0);
                }

                if (!helper.isValidEmail(s.toString())){
                    forgetPasswordEditText.setError(getString(R.string.error_wrong_format));
                }

            }
        });



        // this method call only on create , init mechinisms
        OnStartActivity();
    }



    public void goToLogin() {
        Intent i = new Intent(this, LoginCore.class);
        startActivity(i);
    }



    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }





    public abstract  void OnStartActivity();

}
