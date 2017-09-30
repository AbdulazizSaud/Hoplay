package com.hoplay.kay.hoplay.CoresAbstract.AuthenticationAbstracts;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Cores.AuthenticationCore.SignUpCore;
import com.hoplay.kay.hoplay.Cores.ForgetPasswordCore;
import com.hoplay.kay.hoplay.Cores.MainAppMenuCore;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.hoplay.kay.hoplay.CoresAbstract.MainAppMenu;
import com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts.AddGame;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.SchemaHelper;
import com.hoplay.kay.hoplay.util.Helper;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


/*


 */

public abstract class Login extends AppCompatActivity implements View.OnKeyListener {

    /***************************************/

    protected Button signupBtn;
    protected TextView signupQuestion;
    protected TextView forgotPassword;
    protected EditText usernameSignIn;
    protected EditText passwordSignIn;
    protected ImageView logo;
    protected Button signInButton;
    protected RelativeLayout signInRelativeLayout;
    protected Toolbar toolbar;
    protected App app = App.getInstance();
    ProgressDialog loadingDialog ;
    private LinearLayoutCompat signinBlock;



    // For displaying the tour activities
    protected boolean firstTime = false;

    /***************************************/

    // Main methods
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    // Remove keyboard when click anywhere (the relative layout) :
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
        loadingDialog.dismiss();
        super.onDestroy();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

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






        //this abstract method will do some init things
        OnStartActivity();

        initControls();
        animateViews();
        actionControls();




    }

    private void animateViews() {
        // Animate Views
        logo.setVisibility(View.INVISIBLE);
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_view);
        logo.startAnimation(slideDown);
        logo.setVisibility(View.VISIBLE);

        signinBlock.setVisibility(View.INVISIBLE);
        Animation slideLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left_view);
        signinBlock.startAnimation(slideLeft);
        signinBlock.setVisibility(View.VISIBLE);

        forgotPassword.setVisibility(View.INVISIBLE);
        Animation slideRight = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_right_view);
        forgotPassword.startAnimation(slideRight);
        forgotPassword.setVisibility(View.VISIBLE);

        signupQuestion.setVisibility(View.INVISIBLE);
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.slide_up_view);
        signupQuestion.startAnimation(slideUp);
        signupQuestion.setVisibility(View.VISIBLE);


        signInButton.setVisibility(View.INVISIBLE);
        signInButton.startAnimation(slideUp);
        signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




    }

    // Listener for editexts to change icons whenever the user type anything
    private void actionControls() {
        // Update username and password field icon
        usernameSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    usernameSignIn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_outline_focuesed_32dp, 0);

                    if(s.length() == 0)
                    {
                        usernameSignIn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_person_outline_not_focuesed_32dp, 0);
                    }

            }
        });



        // NOTE : I commented this code a, it work but it has some problem on some numeric inputs


        // Next Focus : to the password
//         usernameSignIn.setOnKeyListener(new View.OnKeyListener() {
//             @Override
//             public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                 if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_FORWARD )
//                    passwordSignIn.requestFocus();
//
//                 return true;
//             }
//         });


        passwordSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                passwordSignIn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    passwordSignIn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_open_not_focused_32dp, 0);
                }

            }
        });
    }


//---------------------

    // this method will init layout contents
    private void initControls() {
        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getAssets(),"playregular.ttf");



        logo = (ImageView) findViewById(R.id.logo_imageview);



        signupBtn = (Button) findViewById(R.id.sign_up_btn);
        signupBtn.setTypeface(sansationbold);

        signupQuestion = (TextView) findViewById(R.id.sign_up_question_textview);
        signupQuestion.setTypeface(sansationbold);

        forgotPassword = (TextView) findViewById(R.id.forgot_password_textview);
        forgotPassword.setTypeface(sansationbold);



        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setTypeface(sansationbold);

        passwordSignIn = (EditText) findViewById(R.id.password_edittext);
        // Encrypt password
        passwordSignIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordSignIn.setTypeface(playregular);


        usernameSignIn = (EditText) findViewById(R.id.username_sign_in_edittext);
        usernameSignIn.setTypeface(playregular);


        signinBlock = (LinearLayoutCompat) findViewById(R.id.signin_blcok_linearlayout);









        // Init progress dialog
        loadingDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle(R.string.login_signing_in);
        loadingDialog.setMessage(Login.this.getString(R.string.login_just_a_moment));


        // it execute login method when click on button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameSignIn.getText().toString().trim();
                String password = passwordSignIn.getText().toString().trim();

                if (validUsernameAndPassword(username,password))
                login(username, password);
            }
        });


        // it will switch to signup click on button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSignUp();
            }
        });

    }


    private boolean validUsernameAndPassword(String username , String password)
    {
        Helper helper = new Helper();


        if (username.length()<1)
        {

            Toast.makeText(getApplicationContext(),getString(R.string.error_empty_email_field),Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.length()<1)
        {

            Toast.makeText(getApplicationContext(),getString(R.string.error_empty_password),Toast.LENGTH_LONG).show();
            return false;
        }

        if (!helper.isValidEmail(username))
        {

            Toast.makeText(getApplicationContext(),getString(R.string.error_email),Toast.LENGTH_LONG).show();
            return false;
        }


        return true;


    }



    //this method switching a to signup actitvty
    protected void toSignUp() {
        Intent i = new Intent(this, SignUpCore.class);
        startActivity(i);
        overridePendingTransition( R.anim.slide_in_up_layouts, R.anim.slide_out_up_layouts);

    }


    protected void toMainMenu() {

        finish();

        if (firstTime)
        {
            Intent i = new Intent(getApplicationContext(), AddGameCore.class);
            i.putExtra("CameFrom","Login");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition( R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);

        }else {
            Intent i = new Intent(getApplicationContext(), MainAppMenuCore.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition( R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);

        }

    }

    //this method switching a to forget password actitvty
    public void goToForgetPassword(View view) {
        Intent i = new Intent(this, ForgetPasswordCore.class);
        startActivity(i);
        overridePendingTransition( R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Kill the application
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);

    }


    protected void showVerificationEmailDialog(final FirebaseUser firebaseUser , final String username) {

        final Dialog verificationEmailDialog;
        verificationEmailDialog = new Dialog(this);
        verificationEmailDialog.setContentView(R.layout.verification_email_dialog);
        verificationEmailDialog.show();

        verificationEmailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView resendEmailMessage;
        Button resendEmailButton;


        resendEmailMessage = (TextView) verificationEmailDialog.findViewById(R.id.email_verification_message);
        resendEmailButton = (Button) verificationEmailDialog.findViewById(R.id.resend_verfication_email_button);

        Typeface playregular = Typeface.createFromAsset(getResources().getAssets() ,"playregular.ttf");
        Typeface playbold = Typeface.createFromAsset(getResources().getAssets() ,"playbold.ttf");

        resendEmailMessage.setTypeface(playregular);
        resendEmailButton.setTypeface(playbold);


        // Resend Verification Email
        resendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.sendEmailVerification(firebaseUser,username);

            }
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = verificationEmailDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }


    // abstract methods, Note : I made some comment descripe these methods on Login Activity
    protected abstract void login(String username, String password);
    protected abstract void OnStartActivity();

    protected void loadingDialog(boolean show)
    {

        if (show)
            loadingDialog.show();
        else
            loadingDialog.dismiss();
    }


}

