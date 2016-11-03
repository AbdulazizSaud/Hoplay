package com.example.kay.hoplay;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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




        // Initializations :
        termsAndConditionsTextView = (TextView) findViewById(R.id.terms_and_conditions_textview);
        agreeButton = (Button) findViewById(R.id.agree_terms_and_conditions_button);


        // Make the termsAndConditionsTextView scrollable :
        termsAndConditionsTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void toAppMenu(View view)
    {
        Intent i = new Intent(getApplicationContext() , MainAppMenu.class);
        startActivity(i);
    }
}
