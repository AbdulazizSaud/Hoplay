package com.example.kay.hoplay.Authentication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kay.hoplay.Activities.MainMenu.MainAppMenu;
import com.example.kay.hoplay.Activities.MainMenu.MainAppMenuActivity;
import com.example.kay.hoplay.Activities.TermsAndConditions;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.util.Helper;

import org.json.JSONObject;

/**
 * Created by azoz-pc on 2/6/2017.
 */

public abstract class Signup extends AppCompatActivity implements Auth {


    /***************************************/

    protected TextView createNewTextView;
    protected TextView accountTextView;
    protected ImageView goBackToMain;
    protected EditText usernameSignUp;
    protected EditText passwordSignUp;
    protected EditText emailSignUp;
    protected EditText nickNameSignUp;
    protected TextView agrrement;
    protected TextView termsAndConditions;
    protected Button signUpBtn;
    protected EditText confirmPasswordEdititext;
    protected App app =  App.getInstance();

    /***************************************/

    // Main methods

    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //  getSupportActionBar().hide();


        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Get the current display info :
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if (display.getWidth() > display.getHeight()) {
            // Landscape mode
            Log.d("Oreintation :", "Landscape  mode");
        } else {
            // Portrait mode
            Log.d("Oreintation : ", "Portrait mode");
        }


        OnStartActivity();

        initControls();


    }

    //---------------------


    // this method will init layout contents
    private void initControls(){


        // init layout contents

        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");

        createNewTextView = (TextView) findViewById(R.id.create_new_textview);
        createNewTextView.setTypeface(sansationbold);
        accountTextView = (TextView) findViewById(R.id.account_textview);
        accountTextView.setTypeface(sansationbold);
        agrrement = (TextView) findViewById(R.id.agreement_textview);
        agrrement.setTypeface(sansationbold);
        termsAndConditions = (TextView) findViewById(R.id.terms_and_conditions_textview);
        termsAndConditions.setTypeface(sansationbold);
        goBackToMain = (ImageView) findViewById(R.id.go_back_to_main_imageview);
        usernameSignUp = (EditText) findViewById(R.id.username_sign_up_edittext);
        usernameSignUp.setTypeface(sansationbold);
        passwordSignUp = (EditText) findViewById(R.id.password_sign_up_edittext);
        passwordSignUp.setTypeface(sansationbold);
        emailSignUp = (EditText) findViewById(R.id.email_sign_up_edittext);
        emailSignUp.setTypeface(sansationbold);
        nickNameSignUp = (EditText) findViewById(R.id.nickname_sign_up_edittext);
        nickNameSignUp.setTypeface(sansationbold);
        signUpBtn = (Button) findViewById(R.id.sign_in_button);
        signUpBtn.setTypeface(sansationbold);
        confirmPasswordEdititext = (EditText) findViewById(R.id.confirm_password_edittext);
        confirmPasswordEdititext.setTypeface(sansationbold);

        // I've tried the onClick method  but  it  didn't work .
        // or sometimes  it  work but  it will not  make the
        // onItemSelected for the Bottom bar show the activities .

        // Encrypt Password
        passwordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //  confirmPasswordEdititext.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);


        usernameSignUp.setTypeface(sansationbold);
        passwordSignUp.setTypeface(sansationbold);


        agrrement.setTypeface(sansationbold);
        termsAndConditions.setTypeface(sansationbold);

        // ERROR DETECTOR :
        errorDetection();


        // it will execute on click sign up button
        // take all requirements and pass to sign up methods
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email = emailSignUp.getText().toString().trim();
                final String username  =usernameSignUp.getText().toString().trim();
                final String password  = passwordSignUp.getText().toString().trim();
                final String nickname = nickNameSignUp.getText().toString().trim();
                if(checkUsername() && checkEmail() && checkPassword() && checkNickname() )
                signUp(email,username,password,nickname);
            }
        });

        goBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });

    }

    //ERROR DETECTION
    private void errorDetection() {
        usernameSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                checkUsername();
            }
        });

        passwordSignUp.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                checkPassword();
            }
        }));

        nickNameSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                checkNickname();
            }
        });

        emailSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                checkEmail();
            }
        });
    }

    // CHECK USERNAME :
    protected Boolean checkUsername() {

        String username = usernameSignUp.getText().toString().trim();
        Helper helper = new Helper();

        if (username.length() == 0) {

            usernameSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (username.length() < 5) {

            usernameSignUp.setError(getString(R.string.error_small_username));

            return false;
        }

        if (!helper.isValidLogin(username)) {

            usernameSignUp.setError(getString(R.string.error_wrong_format));

            return false;
        }

        usernameSignUp.setError(null);

        return  true;
    }

    // CHECK NICKNAME
    protected Boolean checkNickname() {

        String nickname = nickNameSignUp.getText().toString().trim();

        if (nickname.length() == 0) {

            nickNameSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (nickname.length() < 2) {

            nickNameSignUp.setError(getString(R.string.error_small_fullname));

            return false;
        }

        nickNameSignUp.setError(null);

        return  true;
    }

    // CHECK PASSWORD
    protected Boolean checkPassword() {

        String password = passwordSignUp.getText().toString().trim();

        Helper helper = new Helper();

        if (password.length() == 0) {

            passwordSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            passwordSignUp.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            passwordSignUp.setError(getString(R.string.error_wrong_format));

            return false;
        }

        passwordSignUp.setError(null);

        return true;
    }

    // CHECK EMAIL
    protected Boolean checkEmail() {

        String email = emailSignUp.getText().toString().trim();

        Helper helper = new Helper();

        if (email.length() == 0) {

            emailSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            emailSignUp.setError(getString(R.string.error_wrong_format));

            return false;
        }

        emailSignUp.setError(null);

        return true;
    }

    protected Boolean verifyRegForm(String email,String username,String password,String nickname) {

        usernameSignUp.setError(null);
        nickNameSignUp.setError(null);
        passwordSignUp.setError(null);
        emailSignUp.setError(null);

        Helper helper = new Helper();

        if (username.length() == 0) {

            usernameSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (username.length() < 5) {

            usernameSignUp.setError(getString(R.string.error_small_username));

            return false;
        }

        if (!helper.isValidLogin(username)) {

            usernameSignUp.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (nickname.length() == 0) {

            nickNameSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (nickname.length() < 2) {

            nickNameSignUp.setError(getString(R.string.error_small_fullname));

            return false;
        }

        if (password.length() == 0) {

            passwordSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            passwordSignUp.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            passwordSignUp.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (email.length() == 0) {

            emailSignUp.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            emailSignUp.setError(getString(R.string.error_wrong_format));

            return false;
        }

        return true;
    }


    // switch activity methods
    public void toLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void toMainMenuApp(){
        Intent i = new Intent(getApplicationContext(), MainAppMenuActivity.class);
        startActivity(i);

    }
    public void toTermsAndConditions(View v) {
        Intent i = new Intent(this, TermsAndConditions.class);
        startActivity(i);
    }

    // abstract methods
    protected abstract void signUp(String email,String username,String password,String nickname);
    protected abstract JSONObject startAPI();



}
