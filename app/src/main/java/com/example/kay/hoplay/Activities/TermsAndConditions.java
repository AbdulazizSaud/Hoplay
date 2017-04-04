package com.example.kay.hoplay.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.kay.hoplay.Cores.AuthenticationCore.SignUpCore;
import com.example.kay.hoplay.R;

public class TermsAndConditions extends AppCompatActivity {

    // Declarations :
    private TextView termsAndConditionsTextView ;
    private Button   agreeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

      //  android.app.ActionBar actionBar = getActionBar();
       // actionBar.hide();



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



        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");
        // Initializations :
        termsAndConditionsTextView = (TextView) findViewById(R.id.terms_and_conditions_textview);
        agreeButton = (Button) findViewById(R.id.agree_terms_and_conditions_button);
        termsAndConditionsTextView.setTypeface(sansationbold);
        agreeButton.setTypeface(sansationbold);


        // Make the termsAndConditionsTextView scrollable :
        termsAndConditionsTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void toAppMenu(View view)
    {
        Intent i = new Intent(getApplicationContext() , SignUpCore.class);
        startActivity(i);
    }
}
