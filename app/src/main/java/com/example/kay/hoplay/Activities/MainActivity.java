package com.example.kay.hoplay.Activities;

//hello


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    public static MainActivity mainActivity;
    private TextView signupTextView;
    private TextView signupQuestion;
    private TextView forgotPassword;
    private EditText usernameSignIn;
    private EditText passwordSignIn;
    private ImageView logo;
    private Animation fadeIn;
    private Animation fadeOut;
    private AnimationSet animation;
    private Button signInButton;
    private RelativeLayout signInRelativeLayout;
    private Toolbar toolbar;
    private Socket socket;
    private FirebaseAuth.AuthStateListener authStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        LayoutInflater inflater = getLayoutInflater();
//        View v = inflater.inflate(R.layout.start_layout, null);
//        Toast toast = new Toast(getApplicationContext());
//        toast.setView(v);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.FILL, 0, 0);
//        toast.show();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
                    startActivity(i);
                } else {
                    // User is signed out
                    Log.d("---> ", "onAuthStateChanged:signed_out");
                }
            }
        };

        App.getInstance().setmAuthListener(authStateListener);

        // getSupportActionBar().hide();

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

        setup();
        socket = App.getInstance().getSocket();
        socket.on(App.RECEIVE_CLIENT_ID, retID);

    }   // End Of onCreate Method


    private void setup() {


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
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    public void signIn(View v) {
//            if(startAPI() !=null) {
//                socket.emit(App.ADD_USER,usernameSignIn.getText().toString().trim());
//
//                Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
//                startActivity(i);
//            }

        String username = usernameSignIn.getText().toString().trim();
        String password = passwordSignIn.getText().toString().trim();

        App.getInstance().getmAuth().signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your email or password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public JSONObject startAPI() {
        JSONObject jsonAPI = null;
        HashMap<String, String> data = new HashMap<>();
//
        data.put("username", usernameSignIn.getText().toString().trim());
        data.put("password", passwordSignIn.getText().toString().trim());
        jsonAPI = App.getInstance().getAPI(GetAPI.LOGIN, data);

        return jsonAPI;
    }


    public void goToForgetPassword(View view) {
        Intent i = new Intent(MainActivity.this, ForgetPassword.class);
        startActivity(i);
    }

    public Emitter.Listener retID = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            App.clientID = String.valueOf(args[0]);
            Log.i("--->", args[0] + "");
        }
    };

}
