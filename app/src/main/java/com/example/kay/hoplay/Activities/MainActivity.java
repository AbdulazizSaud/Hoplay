package com.example.kay.hoplay.Activities;

//hello


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Services.ErrorHandler;
import com.example.kay.hoplay.Services.GetAPI;
import com.example.kay.hoplay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
    private Socket socket;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            mainActivity = this;
            super.onCreate(savedInstanceState);

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

            setup();
        socket = App.getInstance().getSocket();
        socket.on(App.RECEIVE_CLIENT_ID,retID);

        }   // End Of onCreate Method


    private void setup(){


        logo = (ImageView) findViewById(R.id.logo_imageview);

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

    }
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

    public void toSignUp(View view) {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);

        }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }


    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }


    public void signIn(View v) {
            if(startAPI().equals("success")) {
                socket.emit(App.ADD_USER,usernameSignIn.getText().toString().trim());

                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
            }
    }


    public String startAPI(){
            String resualt_restful_api = null;
            GetAPI api = new GetAPI();
            api.data.put("username", usernameSignIn.getText().toString().trim());
            api.data.put("password", passwordSignIn.getText().toString().trim());

            try {

                resualt_restful_api = api.execute(GetAPI.LOGIN).get();
                boolean error = ErrorHandler.isError(resualt_restful_api);

                if (!error) {
                    try {
                        JSONObject jsonObject = new JSONObject(resualt_restful_api);

                        Toast.makeText(this,jsonObject.getString("text"),Toast.LENGTH_SHORT).show();
                         return jsonObject.getString("type");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (resualt_restful_api == ErrorHandler.ERROR_CONNECTION
                        || resualt_restful_api == ErrorHandler.ERROR_IO_EXP)

                    ErrorHandler.showConnectionERROR(getApplicationContext());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return "Failed";
        }


    public  void goToForgetPassword(View view)
    {
        Intent i = new Intent(MainActivity.this,ForgetPassword.class);
        startActivity(i);
    }

    public Emitter.Listener retID = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            App.clientID = String.valueOf(args[0]);
            Log.i("--->",args[0]+"");
        }
    };

}