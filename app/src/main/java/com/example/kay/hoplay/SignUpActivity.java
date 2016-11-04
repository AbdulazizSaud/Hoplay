package com.example.kay.hoplay;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
    private EditText nickName;
    private TextView agrrement;
    private TextView termsAndConditions;
    private Button signUp;
    private EditText confirmPasswordEdititext;


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
        nickName = (EditText) findViewById(R.id.nickname_sign_up_edittext);
        nickName.setTypeface(sansationbold);
        signUp = (Button) findViewById(R.id.sign_in_button);
        signUp.setTypeface(sansationbold);
        confirmPasswordEdititext = (EditText) findViewById(R.id.confirm_password_sign_up_edittext);
        confirmPasswordEdititext.setTypeface(sansationbold);

        // I've tried the onClick method  but  it  didn't work .
        // or sometimes  it  work but  it will not  make the
        // onItemSelected for the Bottom bar show the activities .

        // Encrypt Password
        passwordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordEdititext.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);


        usernameSignUp.setTypeface(sansationbold);
        passwordSignUp.setTypeface(sansationbold);


        agrrement.setTypeface(sansationbold);
        termsAndConditions.setTypeface(sansationbold);

        /*Typeface SFNS = Typeface.createFromAsset(getAssets(), "SFNS.ttf");

            createNewTextView.setTypeface(SFNS);
            accountTextView.setTypeface(SFNS);   */

      /*   Typeface timeburnernormal = Typeface.createFromAsset(getAssets(), "timeburnernormal.ttf");
            createNewTextView.setTypeface(timeburnernormal);
            accountTextView.setTypeface(timeburnernormal);
        */


    }  // End Of onCreate Method


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

        if (startAPI().equals("success")) {
            Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
            startActivity(i);
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

    private String startAPI() {


        String resualt_restful_api = null;
        GetAPI api = new GetAPI();
        api.data.put("username", usernameSignUp.getText().toString().trim());
        api.data.put("password", passwordSignUp.getText().toString().trim());
        api.data.put("email", emailSignUp.getText().toString().trim());

        try {

            resualt_restful_api = api.execute(GetAPI.REGISTER).get();
            boolean error = ErrorHandler.isError(resualt_restful_api);

            if (!error) {
                    JSONObject jsonObject = new JSONObject(resualt_restful_api);

                    Toast.makeText(this,jsonObject.getString("text"),Toast.LENGTH_SHORT).show();
                      return jsonObject.getString("type");


            } else if (resualt_restful_api == ErrorHandler.ERROR_CONNECTION)
                      ErrorHandler.showConnectionERROR(getApplicationContext());


        } catch (JSONException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "Failed";
    }

}
