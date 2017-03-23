package com.example.kay.hoplay.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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

import com.example.kay.hoplay.Activities.MainMenu.MainAppMenuActivity;
import com.example.kay.hoplay.Activities.TermsAndConditions;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.util.Helper;

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
        changeIcon();


    }


    // Change icon on edittexts
    private void changeIcon() {
        // username  , email , password , confirm password  icon changing :
        usernameSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                usernameSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_outline_focuesed_32dp, 0);
                if(s.length() == 0)
                {
                    usernameSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_outline_not_focuesed_32dp, 0);
                }

            }
        });
        passwordSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    passwordSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_open_not_focused_32dp, 0);
                }

            }
        });
        confirmPasswordEdititext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                confirmPasswordEdititext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_all_done_focused_32dp, 0);
                if(s.length() == 0)
                {
                    confirmPasswordEdititext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_all_done_not_focused_32dp, 0);
                }
            }
        });
        emailSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mail_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    emailSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mail_outline_not_focused_32dp, 0);
                }
            }
        });
    }

    //---------------------


    // this method will init layout contents
    private void initControls(){


        // init layout contents

        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getAssets(), "playregular.ttf");


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
        usernameSignUp.setTypeface(playregular);
        passwordSignUp = (EditText) findViewById(R.id.password_sign_up_edittext);
        passwordSignUp.setTypeface(playregular);
        emailSignUp = (EditText) findViewById(R.id.email_sign_up_edittext);
        emailSignUp.setTypeface(playregular);
      //  nickNameSignUp = (EditText) findViewById(R.id.nickname_sign_up_edittext);
        signUpBtn = (Button) findViewById(R.id.sign_in_button);
        signUpBtn.setTypeface(sansationbold);
        confirmPasswordEdititext = (EditText) findViewById(R.id.confirm_password_edittext);
        confirmPasswordEdititext.setTypeface(playregular);


        // I've tried the onClick method  but  it  didn't work .
        // or sometimes  it  work but  it will not  make the
        // onItemSelected for the Bottom bar show the activities .

        // Encrypt Password
//        passwordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        confirmPasswordEdititext.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);



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
                final String nickname = username;
                final String confrimPassword = confirmPasswordEdititext.getText().toString().trim();

//                if(!confrimPassword.equals(password))
//                {
//                    Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_LONG).show();
//                    return;
//                }

                if(checkUsername() && checkEmail() && checkPassword())
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

//        nickNameSignUp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void afterTextChanged(Editable editable) {
//                checkNickname();
//            }
//        });

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

        if (username.length() < 3) {

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
//     protected Boolean checkNickname() {
//
//        String nickname = nickNameSignUp.getText().toString().trim();
//
//        if (nickname.length() == 0) {
//
//            nickNameSignUp.setError(getString(R.string.error_field_empty));
//
//            return false;
//        }
//
//        if (nickname.length() < 2) {
//
//            nickNameSignUp.setError(getString(R.string.error_small_fullname));
//
//            return false;
//        }
//
//        nickNameSignUp.setError(null);
//
//        return  true;
//    }

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

        if (username.length() < 3) {

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
       finish();
    }
    public void toMainMenuApp(){

        ProgressDialog loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle(R.string.signup_signin_up);
        loadingDialog.setMessage(Signup.this.getString(R.string.signup_just_a_moment));
        loadingDialog.show();
        Intent i = new Intent(getApplicationContext(), MainAppMenuActivity.class);
        startActivity(i);

    }
    public void toTermsAndConditions(View v) {
        Intent i = new Intent(this, TermsAndConditions.class);
        startActivity(i);
    }

    // abstract methods
    protected abstract void signUp(String email,String username,String password,String nickname);



}
