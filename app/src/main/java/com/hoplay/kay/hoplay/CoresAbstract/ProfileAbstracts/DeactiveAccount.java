package com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.hoplay.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Login;
import com.hoplay.kay.hoplay.R;
import com.google.firebase.auth.FirebaseAuth;

public abstract class DeactiveAccount extends AppCompatActivity {

    private EditText password;
    private Button authButton;
    protected RelativeLayout deactiveLayout;
    private  TextView activityTitle ;

    protected FirebaseAuth mAuth;
    protected App app;

    protected ProgressDialog loadingDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactive_account);
        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        OnStartActivity();


        activityTitle = (TextView) findViewById(R.id.deactive_account_title_textview) ;
        password = (EditText) findViewById(R.id.deactive_account_password_edittext);
        authButton = (Button) findViewById(R.id.deactive_account_authinticate_button);
        deactiveLayout = (RelativeLayout) findViewById(R.id.deactive_account_relativelayout);



        // Init progress dialog
        loadingDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle(R.string.deactive_account_progress_dialog_title);
        loadingDialog.setMessage(DeactiveAccount.this.getString(R.string.deactive_account_progress_dialog_body));




        Typeface sansation = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
        Typeface playregular = Typeface.createFromAsset(getAssets(), "playregular.ttf");

        password.setTypeface(playregular);
        authButton.setTypeface(sansation);
        activityTitle.setTypeface(sansation);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_open_not_focused_32dp, 0);
                }

            }
        });




    }

    public void authinticateListener(View view)
    {


        // Show loading dialog
        loadingDialog(true);

        String enteredPassword = password.getText().toString().trim();

        // Check if no view has focus:
        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (enteredPassword.length() <1){

            loadingDialog(false);

            Snackbar snackbar = Snackbar
                    .make(deactiveLayout, R.string.deactive_account_password_error, Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }

        authinticateUser(mAuth.getCurrentUser().getEmail(),enteredPassword);



    }


    protected void showDeactiveAccDialog(){

        final Dialog deactiveAccDialog;
        deactiveAccDialog = new Dialog(this);
        deactiveAccDialog.setContentView(R.layout.verification_deactive_account_pop_up);
        deactiveAccDialog.show();


        deactiveAccDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView verificationDeleteText;
        Button deleteYesButton , deleteNoButton;

        verificationDeleteText = (TextView)  deactiveAccDialog.findViewById(R.id.deactive_account_message);

        deleteYesButton = ( Button) deactiveAccDialog.findViewById(R.id.deactive_account_yes_button);
        deleteNoButton = ( Button) deactiveAccDialog.findViewById(R.id.deactive_account_no_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        Typeface playbold = Typeface.createFromAsset(getResources().getAssets() ,"playbold.ttf");
        deleteYesButton.setTypeface(sansation);
        deleteNoButton.setTypeface(sansation);
        verificationDeleteText.setTypeface(playbold);



        // Deactive account
        deleteYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactiveUserAccount();
                deactiveAccDialog.dismiss();
                finish();
                Intent i = new Intent(getApplicationContext(), LoginCore.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        // Remove Dialog
        deleteNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactiveAccDialog.dismiss();
                finish();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = deactiveAccDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }



    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }




    protected void loadingDialog(boolean show)
    {

        if (show)
            loadingDialog.show();


        else
            loadingDialog.dismiss();
    }



    protected  abstract  void OnStartActivity();
    protected abstract void authinticateUser(String email , String password);
    protected abstract void deactiveUserAccount();


}
