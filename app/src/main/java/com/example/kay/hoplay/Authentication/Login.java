package com.example.kay.hoplay.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Activities.ForgetPasswordActivity.ForgetPasswordActivity;
import com.example.kay.hoplay.Activities.MainMenu.MainAppMenuActivity;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;

import org.json.JSONObject;


/*


 */

public abstract class Login extends AppCompatActivity implements View.OnKeyListener, Auth {

    /***************************************/

    protected Button signupBtn;
    protected TextView signupQuestion;
    protected TextView forgotPassword;
    protected EditText usernameSignIn;
    protected EditText passwordSignIn;
    protected ImageView logo;
    protected Animation fadeIn;
    protected Animation fadeOut;
    protected AnimationSet animation;
    protected Button signInButton;
    protected RelativeLayout signInRelativeLayout;
    protected Toolbar toolbar;
    protected App app = App.getInstance();

    /***************************************/

    // Main methods
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

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

    }

//---------------------

    // this method will init layout contents
    private void initControls() {
        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");


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
        passwordSignIn.setTypeface(sansationbold);

        usernameSignIn = (EditText) findViewById(R.id.username_sign_in_edittext);
        usernameSignIn.setTypeface(sansationbold);



        // it execute login method when click on button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameSignIn.getText().toString().trim();
                String password = passwordSignIn.getText().toString().trim();

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

    //this method switching a to signup actitvty
    protected void toSignUp() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }
    protected void toMainMenu() {

        ProgressDialog loadigDialog = new ProgressDialog(this);
        loadigDialog.setTitle("Signing in ..");
        loadigDialog.setMessage("Just one moment");
        loadigDialog.show();
        Intent i = new Intent(getApplicationContext(), MainAppMenuActivity.class);
        startActivity(i);

    }

    //this method switching a to forget password actitvty
    public void goToForgetPassword(View view) {
        Intent i = new Intent(this, ForgetPasswordActivity.class);
        startActivity(i);
    }


    // abstract methods, Note : I made some comment descripe these methods on Login Activity
    protected abstract JSONObject startAPI();
    protected abstract void login(String username, String password);
}
