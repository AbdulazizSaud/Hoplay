package com.example.kay.hoplay.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.PatternStrategyComponents.DataCommon;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategyInterface;
import com.example.kay.hoplay.PatternStrategyComponents.Strategies.FirebaseSignupStrategy;
import com.example.kay.hoplay.Services.ErrorHandler;
import com.example.kay.hoplay.Services.GetAPI;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.util.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SignUpActivity extends AppCompatActivity {


    private TextView createNewTextView;
    private TextView accountTextView;
    private ImageView goBackToMain;
    private EditText usernameSignUp;
    private EditText passwordSignUp;
    private EditText emailSignUp;
    private EditText nickNameSignUp;
    private TextView agrrement;
    private TextView termsAndConditions;
    private Button signUp;
    private EditText confirmPasswordEdititext;
    private String username , nickname , password , email ;


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
        signUp = (Button) findViewById(R.id.sign_in_button);
        signUp.setTypeface(sansationbold);
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
                checkFullname();
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

    }  // End Of onCreate Method .


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
    protected void onStart() {
        super.onStart();
    }


    public void toMain(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }


    public void signUp(View view) {

//        if (startAPI() !=null) {
//            Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
//            startActivity(i);
//        }

        App app = App.getInstance();

        final String email = emailSignUp.getText().toString().trim();
        final String username  =usernameSignUp.getText().toString().trim();
        final String password  = passwordSignUp.getText().toString().trim();

        if(email.equals(null)|| email.equals("") || username.equals(null)|| password.equals(""))
            return;

        DataCommon<String> passData = new DataCommon<String>(username,email,password);

        app.setPattrenStrargey(this,new FirebaseSignupStrategy());

        String messages = app.excutePattrenStrargey(passData);

        String[] messages_split = messages.split("_Message_");
        Log.i("message-------->",messages_split[0]);


        if(messages_split[0].equals(App.SUCCESSED)){
            Toast.makeText(getApplicationContext(),App.SUCCESSED_CREATED_ACCOUNT,Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(),messages_split[1],Toast.LENGTH_LONG).show();
        }

    }

    public void toTermsAndConditions(View v) {
        Intent i = new Intent(SignUpActivity.this, TermsAndConditions.class);
        startActivity(i);
    }

    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private JSONObject startAPI() {


        JSONObject jsonAPI = null;

        HashMap<String ,String> data = new HashMap<>();
        data.put("username", usernameSignUp.getText().toString().trim());
        data.put("password", passwordSignUp.getText().toString().trim());
        data.put("email", emailSignUp.getText().toString().trim());

        jsonAPI = App.getInstance().getAPI(GetAPI.REGISTER,data);

        return jsonAPI;

    }



    // CHECK USERNAME :
    public Boolean checkUsername() {

        username = usernameSignUp.getText().toString();
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
    public Boolean checkFullname() {

        nickname = nickNameSignUp.getText().toString();

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
    public Boolean checkPassword() {

        password = passwordSignUp.getText().toString();

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
    public Boolean checkEmail() {

        email = emailSignUp.getText().toString();

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

    public Boolean verifyRegForm() {

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




}
