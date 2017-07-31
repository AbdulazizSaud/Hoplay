package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Login;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;

import java.util.ArrayList;

public abstract class ChangePassword extends AppCompatActivity {

    protected EditText currrentPassword,newPassword , confirmNewPassword;
    private Button updateButton;
    protected RelativeLayout changePasswordRelativeLayout;
    protected boolean isCorrectPassword;
    private boolean isCorrectNewPassword ;
    protected App app = App.getInstance();
    protected ProgressDialog loadingDialog ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initControls();
        fieldsListeners();
        OnStartActivity();


    }



    private void initControls()
    {

        isCorrectPassword = false;
        isCorrectNewPassword = false;
        currrentPassword = (EditText) findViewById(R.id.current_password_change_password_edittext);
        newPassword = (EditText) findViewById(R.id.new_password_change_password_edittext);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password_change_password_edittext);
        updateButton = (Button) findViewById(R.id.update_password_button);
        changePasswordRelativeLayout = (RelativeLayout) findViewById(R.id.change_password_relativelayout);


        final Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
        final Typeface playReg = Typeface.createFromAsset(getAssets(), "playregular.ttf");
        Typeface sansation = Typeface.createFromAsset(getAssets() ,"sansationbold.ttf");

        currrentPassword.setTypeface(playReg);
        newPassword.setTypeface(playReg);
        confirmNewPassword.setTypeface(playReg);

        updateButton.setTypeface(sansation);


        // Init progress dialog
        loadingDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        loadingDialog.setTitle(R.string.change_password_dialog_title);
        loadingDialog.setMessage(ChangePassword.this.getString(R.string.change_password_dialog_body_message));




    }

    private void fieldsListeners()
    {
        currrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                currrentPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    currrentPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_open_not_focused_32dp, 0);
                }





            }
        });


        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_open_not_focused_32dp, 0);
                }
                if (s.length()<6)
                {
                    newPassword.setError(getText(R.string.error_small_password));
                    isCorrectNewPassword = false;
                }
                else {
                    isCorrectNewPassword= true;
                }



            }
        });



        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                confirmNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_outline_focused_32dp, 0);
                if(s.length() == 0)
                {
                    confirmNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lock_open_not_focused_32dp, 0);
                }





            }
        });

    }


    private boolean validateNewPasswords()
    {
        String newPass = newPassword.getText().toString().trim();
        String confirmNewPass = confirmNewPassword.getText().toString().trim();

        if (newPass.equals(confirmNewPass))
            return true;

        return false;
    }


    // Update password button
    public void updatePassword(View view)
    {




        if (currrentPassword.getText().toString().trim().isEmpty())
        {
            Snackbar snackbar = Snackbar
                    .make(changePasswordRelativeLayout, R.string.change_password_old_password_empty_error, Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }


        if (newPassword.getText().toString().trim().isEmpty())
        {
            Snackbar snackbar = Snackbar
                    .make(changePasswordRelativeLayout, R.string.change_password_new_password_empty_error, Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }


        if (!validateNewPasswords() && isCorrectNewPassword)
        {
            Snackbar snackbar = Snackbar
                    .make(changePasswordRelativeLayout, R.string.change_password_new_password_match_error, Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }

        loadingDialog.show();
        validateOldPassword(currrentPassword.getText().toString().trim());





    }




    protected void showMessage(String message){
        Snackbar snackbar = Snackbar
                .make(changePasswordRelativeLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }



    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }



    protected abstract void OnStartActivity();
    protected abstract void validateOldPassword(String oldPassword);
    protected abstract void updateUserPassword(String newPassword);

}
