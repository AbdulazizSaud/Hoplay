package com.example.kay.hoplay.CoresAbstract.AuthenticationAbstracts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Cores.AuthenticationCore.SignUpCore;
import com.example.kay.hoplay.Cores.ForgetPasswordCore;
import com.example.kay.hoplay.Cores.MainAppMenuCore;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;


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
    ProgressDialog loadigDialog ;




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
        loadigDialog.dismiss();
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
        changeIconListener();


    }


    // Listener for editexts to change icons whenever the user type anything
    private void changeIconListener() {
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






        // Init progress dialog
        loadigDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        loadigDialog.setTitle(R.string.login_signing_in);
        loadigDialog.setMessage(Login.this.getString(R.string.login_just_a_moment));


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
        Intent i = new Intent(this, SignUpCore.class);
        startActivity(i);

    }
    protected void toMainMenu() {


        Intent i = new Intent(getApplicationContext(), MainAppMenuCore.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    //this method switching a to forget password actitvty
    public void goToForgetPassword(View view) {
        Intent i = new Intent(this, ForgetPasswordCore.class);
        startActivity(i);
    }


    // abstract methods, Note : I made some comment descripe these methods on Login Activity
    protected abstract void login(String username, String password);
    protected abstract void OnStartActivity();

    protected void loadingDialog(boolean show)
    {

        if (show)
            loadigDialog.show();


        else
            loadigDialog.dismiss();
    }
}

