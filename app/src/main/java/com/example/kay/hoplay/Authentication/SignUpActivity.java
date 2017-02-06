package com.example.kay.hoplay.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.MainAppMenu;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Services.GetAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUpActivity extends Signup {

    // Get firebase authentication from App
    private FirebaseAuth appAuth;


    @Override
    public void OnStartActivity() {
        //.
        appAuth = FirebaseAuth.getInstance();
    }


    // this method will do auth to crearte a new user
    // receive:  username , password, email
    // if success, it will switch to a mainApp and give a success message
    // if fail , it will give the user a failed message

    @Override
    protected void signUp(final String email,final String username,final String password,final String nickname) {

//        if (startAPI() !=null) {
//            Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
//            startActivity(i);
//        }

        appAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this
                ,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            JSONObject jsonObject = new JSONObject();
                            try {

                                // here we pass the username and email to emit them via socket io to server
                                jsonObject.put("username",username);
                                jsonObject.put("email",email);
                                app.getSocket().emit("signup_database",jsonObject);


                                // success message
                                Toast.makeText(getApplicationContext(),App.SUCCESSED_CREATED_ACCOUNT,Toast.LENGTH_LONG).show();
                                // switch to main AppMenu
                                toMainMenuApp();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            // failed message
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    // old method .. perhaps we will need it in feature
    @Override
    protected JSONObject startAPI() {


        JSONObject jsonAPI = null;

        HashMap<String ,String> data = new HashMap<>();
        data.put("username", usernameSignUp.getText().toString().trim());
        data.put("password", passwordSignUp.getText().toString().trim());
        data.put("email", emailSignUp.getText().toString().trim());

        jsonAPI = App.getInstance().getAPI(GetAPI.REGISTER,data);

        return jsonAPI;

    }


}
