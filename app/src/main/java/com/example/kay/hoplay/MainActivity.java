package com.example.kay.hoplay;

//hello


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.webkit.WebHistoryItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener  {

    public static MainActivity mainActivity;
    private TextView signupTextView ;
    private TextView signupQuestion ;
    private TextView forgotPassword ;
    private EditText usernameSignIn ;
    private EditText passwordSignIn ;
    private ImageView logo ;
    private Animation fadeIn;
    private Animation fadeOut;
    private AnimationSet animation;
    private Button signInButton;
    private RelativeLayout signInRelativeLayout;
    private Toolbar toolbar;

    // Code changed here bitch ..
    //hello
    //ss
    //..

    void bitch_suck_it(){

    }

    int shit(int ass)
    {
        return ass=100;
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            mainActivity = this;
            super.onCreate(savedInstanceState);
            // Set up firebase

            setContentView(R.layout.activity_main);
            // getSupportActionBar().hide();


            // Set the screen orientation to the portrait mode :
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



            // Get the current display info :
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            if(display.getWidth() > display.getHeight())
            {
                // Landscape mode
                Log.d("Oreintation :","Landscape  mode");
            }
            else
            {
                // Portrait mode
                Log.d("Oreintation : ", "Portrait mode");
            }





         //   Firebase alanRef = ref.child("users").child("alanisawesome");
          //  User alan = new User("Alan Turing", 1912);
          //  alanRef.setValue(alan);





            logo = (ImageView) findViewById(R.id.logo_imageview);

//        // THIS IS HOW TO USE THE TOOLBAR :
//                 toolbar = (Toolbar) findViewById(R.id.app_bar);
//             setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle("YO HOPS!");
         //   toolbar.setTitleTextColor(16711936);



            signupTextView = (TextView) findViewById(R.id.sign_up_textview);
            signupQuestion = (TextView) findViewById(R.id.sign_up_question_textview);
            forgotPassword = (TextView) findViewById(R.id.forgot_password_textview);

            Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
             signupTextView.setTypeface(sansationbold);
            signupQuestion.setTypeface(sansationbold);
            forgotPassword.setTypeface(sansationbold);
            signInButton = (Button) findViewById(R.id.sign_in_button);
            signInButton.setTypeface(sansationbold);
            passwordSignIn = (EditText) findViewById(R.id.password_edittext);
            // Encrypt password
            passwordSignIn.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            usernameSignIn = (EditText) findViewById(R.id.username_sign_in_edittext);
            usernameSignIn.setTypeface(sansationbold);
            passwordSignIn.setTypeface(sansationbold);
            ///sanimateLogo1();

        }   // End Of onCreate Method


    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy() {super.onDestroy();}

    @Override
    protected void onStop() {super.onStop();}

    @Override
    protected void onStart(){super.onStart();}

         public void toSignUp(View view)
        {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }


    public void signIn(View v){
        String resualt_restful_api = "nothing";
        GetAPI api = new GetAPI();
        api.data.put("username", usernameSignIn.getText().toString().trim());
        api.data.put("password", passwordSignIn.getText().toString().trim());

        try {

            resualt_restful_api = api.execute(GetAPI.LOGIN).get();

            if (resualt_restful_api != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resualt_restful_api);

                    Toast.makeText(this,jsonObject.getString("text"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("type").equals("success")) {
                        Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
                        startActivity(i);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else
                Toast.makeText(this, getResources().getString(R.string.signup_failed), Toast.LENGTH_SHORT).show();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    // Animate logo - fade in fade out - :

    /*
        public void animateLogo1()
    {

        for(int i = 1 ; i >=0 ; i-=0.00001)
        {
            logo.animate().alpha(i);
            if(i==0)
                break;
        }
          animateLogo0();
    }

    public void animateLogo0()
    {
         for (int i = 0 ; i <=1 ; i+=0.00001)
            logo.animate().alpha(i);
        animateLogo1();
    }

*/
    public  void goToForgetPassword(View view)
    {
        Intent i = new Intent(MainActivity.this,ForgetPassword.class);
        startActivity(i);
    }

}
