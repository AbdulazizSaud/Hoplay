package com.example.kay.hoplay.Authentication;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Services.GetAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    protected void signUp(final String email, final String username, final String password, final String nickname) {

//        if (startAPI() !=null) {
//            Intent i = new Intent(getApplicationContext(), MainAppMenu.class);
//            startActivity(i);
//        }

        Log.i("---------->","ASi");

        appAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            FirebaseUser user = appAuth.getCurrentUser();

                            doDatabaseProcess(user.getUid(),email,username,nickname);

                            //doDatabaseProcess(appAuth.getCurrentUser().getUid(),email, username, password, nickname);
                            // success message
                            Toast.makeText(getApplicationContext(), App.SUCCESSED_CREATED_ACCOUNT, Toast.LENGTH_LONG).show();
                            // switch to main AppMenu
                            toMainMenuApp();


                        } else {
                            // failed message
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    // old method .. perhaps we will need it in feature
    @Override
    protected JSONObject startAPI() {


        JSONObject jsonAPI = null;

        HashMap<String, String> data = new HashMap<>();
        data.put("username", usernameSignUp.getText().toString().trim());
        data.put("password", passwordSignUp.getText().toString().trim());
        data.put("email", emailSignUp.getText().toString().trim());

        jsonAPI = App.getInstance().getAPI(GetAPI.REGISTER, data);

        return jsonAPI;

    }


    private void doDatabaseProcess(final String UID,final String email, final String username, final String nickname) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rotRef = app.getDatabaseUsers().child(UID);


        rotRef.child("_chat_refs_").child("_public_").child("test").setValue("test");
        rotRef.child("_chat_refs_").child("_private_").child("test").setValue("test");
        rotRef.child("_friends_list_").child("test").setValue("testchh");
        rotRef.child("_info_").child("_username_").setValue(username);
        rotRef.child("_info_").child("_nickname_").setValue(nickname);
        rotRef.child("_info_").child("_email_").setValue(email);
        rotRef.child("_info_").child("_picUrl_").setValue("default");



    }


}
