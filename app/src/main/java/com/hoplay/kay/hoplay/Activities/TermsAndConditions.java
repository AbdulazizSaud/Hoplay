package com.hoplay.kay.hoplay.Activities;

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

import com.hoplay.kay.hoplay.Cores.AuthenticationCore.SignUpCore;
import com.hoplay.kay.hoplay.R;

public class TermsAndConditions extends AppCompatActivity {

    // Declarations :
    private TextView termsAndConditionsTextView ;
    private TextView intro , term1 , term2 , term3 , term4 , term5 , term6 , term7 , term8 , term8_1 , term8_2,term8_3;
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



        Typeface playregular = Typeface.createFromAsset(getAssets(), "playregular.ttf");
        Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
        Typeface sansationbold = Typeface.createFromAsset(getAssets(), "sansationbold.ttf");

        // Initializations :
        termsAndConditionsTextView = (TextView) findViewById(R.id.terms_and_conditions_title_textview);
        intro = (TextView) findViewById(R.id.terms_and_conditions_intro_textview);
        intro.setTypeface(playbold);
        term1 = (TextView) findViewById(R.id.terms_and_conditions_1_textview);
        term2 = (TextView) findViewById(R.id.terms_and_conditions_2_textview);
        term3 = (TextView) findViewById(R.id.terms_and_conditions_3_textview);
        term4 = (TextView) findViewById(R.id.terms_and_conditions_4_textview);
        term5 = (TextView) findViewById(R.id.terms_and_conditions_5_textview);
        term6 = (TextView) findViewById(R.id.terms_and_conditions_6_textview);
        term7 = (TextView) findViewById(R.id.terms_and_conditions_7_textview);
        term8 = (TextView) findViewById(R.id.terms_and_conditions_8_textview);
        term8_1 = (TextView) findViewById(R.id.terms_and_conditions_8_1_textview);
        term8_2 = (TextView) findViewById(R.id.terms_and_conditions_8_2_textview);
        term8_3 = (TextView) findViewById(R.id.terms_and_conditions_8_3_textview);

        term1.setTypeface(playregular);
        term2.setTypeface(playregular);
        term3.setTypeface(playregular);
        term4.setTypeface(playregular);
        term5.setTypeface(playregular);
        term6.setTypeface(playregular);
        term7.setTypeface(playregular);
        term8.setTypeface(playbold);
        term8_1.setTypeface(playregular);
        term8_2.setTypeface(playregular);
        term8_3.setTypeface(playregular);







        agreeButton = (Button) findViewById(R.id.agree_terms_and_conditions_button);
        termsAndConditionsTextView.setTypeface(playbold);
        agreeButton.setTypeface(sansationbold);



        // For the Agree button visiablity
        String isFromPref = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                isFromPref= null;
            } else {
                isFromPref= extras.getString("FromPref");
            }
        }

        if (isFromPref !=null)
            agreeButton.setVisibility(View.GONE);


            // Make the termsAndConditionsTextView scrollable :
        termsAndConditionsTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void agreeOnTerms(View view)
    {
        finish();
    }
}
