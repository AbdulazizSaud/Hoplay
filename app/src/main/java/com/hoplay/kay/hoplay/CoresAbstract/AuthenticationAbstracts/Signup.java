package com.hoplay.kay.hoplay.CoresAbstract.AuthenticationAbstracts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoplay.kay.hoplay.Adapters.SpinnerAdapter;
import com.hoplay.kay.hoplay.Cores.MainAppMenuCore;
import com.hoplay.kay.hoplay.Activities.TermsAndConditions;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Interfaces.Constants;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.util.Helper;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by azoz-pc on 2/6/2017.
 */

public abstract class Signup extends AppCompatActivity implements Constants {


    /***************************************/

    protected TextView createNewTextView;
    protected TextView accountTextView;
    protected ImageView goBackToMain;
    protected TextInputEditText usernameSignUp;
    protected TextInputEditText passwordSignUp;
    protected TextInputEditText emailSignUp;
    protected TextInputEditText nickNameSignUp;
    protected TextView agrrement;
    protected TextView termsAndConditions;
    protected Button signUpBtn;
    protected TextInputEditText confirmPasswordEdititext;
    protected App app =  App.getInstance();


    protected Timer inputChecker = new Timer();
    protected boolean checkingUsername=false;
    protected int currenCheckingStatus;

    ProgressDialog loadingDialog;



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
        viewListener();


    }


    // Change icon on edittexts
    private void viewListener() {

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

                if(!usernameValidation()) return;


                final String username = s.toString().toLowerCase().trim();
                currenCheckingStatus = USER_SEARCHING;

                inputChecker.cancel();
                inputChecker =new Timer();

                inputChecker.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        if(!checkingUsername) {

                            checkingUsername = true;
                            checkUsername(username);
                        }

                    }
                },1000);





                // NOTICE : This code word but it has problem with entering numeric input , so i commented it .. the next Focus
                // Codes in email and password  .. etc  . is commednted as well

//                    // Next Focus : to the email
//                usernameSignUp.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_FORWARD )
//                            emailSignUp.requestFocus();
//
//                        return true;
//                    }
//                });
//



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


                // Next Focus : confirm password
//                passwordSignUp.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_FORWARD )
//                            confirmPasswordEdititext.requestFocus();
//
//                        return true;
//                    }
//                });
//

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


                // Next Focus : password
//                emailSignUp.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_FORWARD )
//                            passwordSignUp.requestFocus();
//
//                        return true;
//                    }
//                });


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
        usernameSignUp = (TextInputEditText) findViewById(R.id.username_sign_up_edittext);
        usernameSignUp.setTypeface(playregular);
        passwordSignUp = (TextInputEditText) findViewById(R.id.password_sign_up_edittext);
        passwordSignUp.setTypeface(playregular);
        emailSignUp = (TextInputEditText) findViewById(R.id.email_sign_up_edittext);
        emailSignUp.setTypeface(playregular);
      //  nickNameSignUp = (EditText) findViewById(R.id.nickname_sign_up_edittext);
        signUpBtn = (Button) findViewById(R.id.sign_in_button);
        signUpBtn.setTypeface(sansationbold);
        confirmPasswordEdititext = (TextInputEditText) findViewById(R.id.confirm_password_edittext);
        confirmPasswordEdititext.setTypeface(playregular);


        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle(R.string.signup_signin_up);
        loadingDialog.setMessage(Signup.this.getString(R.string.signup_just_a_moment));



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
                final String username  =usernameSignUp.getText().toString().trim().toLowerCase();
                final String password  = passwordSignUp.getText().toString().trim();
                final String nickname = username;

//                if(!confrimPassword.equals(password))
//                {
//                    Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_LONG).show();
//                    return;
//                }

                boolean userAvailable = !checkingUsername && currenCheckingStatus == USER_NOT_EXIST;
                boolean validated = usernameValidation() && emailValidtion() && passwordValidation() && confirmPasswordvalidation();

                if(validated && userAvailable)
                {
                    showSurveyDialog(email,username.toLowerCase(),password);
                }
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
                usernameValidation();
            }
        });

        passwordSignUp.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                passwordValidation();
            }
        }));

        confirmPasswordEdititext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmPasswordvalidation();
            }
        });

        emailSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                emailValidtion();
            }
        });
    }

    // CHECK USERNAME :
    protected Boolean usernameValidation() {

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


    // CHECK PASSWORD
    protected Boolean passwordValidation() {

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

    //CHECK CONFIRM PASSWORD
    protected  Boolean confirmPasswordvalidation(){

        String confirmPassword = confirmPasswordEdititext.getText().toString().trim();
        if (!confirmPassword.equals(passwordSignUp.getText().toString().trim()))
        {
            confirmPasswordEdititext.setError(getString(R.string.error_confirm_password));
            return false;
        }


        return true;
    }


    // CHECK EMAIL
    protected Boolean emailValidtion() {

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

        finish();
        Intent i = new Intent(getApplicationContext(), MainAppMenuCore.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition( R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);

    }
    public void toTermsAndConditions(View v) {
        Intent i = new Intent(this, TermsAndConditions.class);
        startActivity(i);
    }


    protected void checkUserCallBack(){
        if(currenCheckingStatus == USER_EXIST)
        usernameSignUp.setError(getString(R.string.error_login_taken));
    }


    protected void loadingDialog(boolean show)
    {

        if (show)
            loadingDialog.show();


        else
            loadingDialog.dismiss();
    }


    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        animation.setDuration(200);
        viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }





    private void showSurveyDialog(final String email,final String username,final String password){


        final Dialog surveyDialog = new Dialog(this);
        surveyDialog.setContentView(R.layout.sign_up_survey_dialog);
        surveyDialog.show();
        surveyDialog.setCancelable(false);



        surveyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final MaterialBetterSpinner feedBackSpinner;
         ArrayAdapter feedBacksAdapter;
        feedBacksAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                new ArrayList());

        feedBacksAdapter.add("Ads");
        feedBacksAdapter.add("Friend-Relative");
        feedBacksAdapter.add("Other");




        TextView surveyMessage ;
        final EditText friendSurveyEdittext ;
        final TextInputLayout friendSurveyInputLayout;
        Button doneBtn , skipBtn ;


        friendSurveyInputLayout = (TextInputLayout) surveyDialog.findViewById(R.id.survey_dialog_textinputlayout);
        feedBackSpinner = (MaterialBetterSpinner) surveyDialog.findViewById(R.id.how_you_know_survey_dialog_spinner);
        friendSurveyEdittext = (EditText) surveyDialog.findViewById(R.id.friend_username_survey_dialog_edittext);
        doneBtn = (Button) surveyDialog.findViewById(R.id.done_survey_dialog_button);
        skipBtn = (Button)  surveyDialog.findViewById(R.id.skip_survey_dialog_button);
        surveyMessage = (TextView) surveyDialog.findViewById(R.id.message_survey_dialog_textview);


        feedBackSpinner.setAdapter(feedBacksAdapter);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        doneBtn.setTypeface(sansation);
        skipBtn.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface playReg = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");

        feedBackSpinner.setTypeface(playbold);
        friendSurveyEdittext.setTypeface(playReg);
        surveyMessage.setTypeface(playReg);


        friendSurveyEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                friendSurveyEdittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_outline_focuesed_32dp, 0);
                if(s.length() == 0)
                {
                    friendSurveyEdittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_outline_not_focuesed_32dp, 0);
                }



            }
        });



        feedBackSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                feedBackSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));
                feedBackSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_feedback_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    feedBackSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_feedback_unfocused_24dp, 0, 0, 0);

                }
                else {
                    if (feedBackSpinner.getText().toString().equalsIgnoreCase("Friend-Relative"))
                    {
                        slideInFromLeft(friendSurveyInputLayout);
                        friendSurveyInputLayout.setVisibility(View.VISIBLE);
                    }
                    else{
                        slideOutToRight(friendSurveyInputLayout);
                        friendSurveyInputLayout.setVisibility(View.GONE);
                    }
                }

            }
        });



        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email,username.toLowerCase(),password,null);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String promoCodeText  =  friendSurveyEdittext.getText().toString().trim().toLowerCase();
                String promoCode =promoCodeText.isEmpty() ? "null" : promoCodeText;
                signUp(email,username.toLowerCase(),password,promoCode.toLowerCase());

            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = surveyDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


    }






    // abstract methods
    protected abstract void signUp(String email,String username,String password,String nickname);
    protected abstract void OnStartActivity();
    protected abstract void checkUsername(String value);

}
