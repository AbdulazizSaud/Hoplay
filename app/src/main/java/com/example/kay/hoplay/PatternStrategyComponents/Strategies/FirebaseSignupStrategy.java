package com.example.kay.hoplay.PatternStrategyComponents.Strategies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.MainAppMenu;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.PatternStrategyComponents.DataCommon;
import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategyInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by azoz-pc on 1/25/2017.
 */

public  class FirebaseSignupStrategy implements PattrenStrategyInterface{



    private String messageState = Constants.FAILED+"_Message_"+Constants.FAILED_MESSAGE;


    @Override
    public void sendData(DataCommon data) {

    }

    @Override
    public DataCommon receiveData() {
        return null;
    }

    @Override
    public String excute(@NonNull DataCommon... args) {

        DataCommon<String> dataCommon = args[0];
        String[] dataCommonData = dataCommon.getData();

        if(dataCommonData.length != 3) {
            return messageState;
        }


        final String username=dataCommonData[0];
        final String email=dataCommonData[1];
        final String password=dataCommonData[2];


        final App app = App.getInstance();
        FirebaseAuth appInstAuth = app.getmAuth();


        appInstAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(app.getCurrentActivity()
                ,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("xxxxxxxxxxxx--->",username +" "+email);

                        if(task.isSuccessful()){

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("username",username);
                                jsonObject.put("email",email);
                                Log.i("---->",username +" "+email);

                                app.getSocket().emit("signup_database",jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            setMessageState(Constants.SUCCESSED+"_Message_"+Constants.SUCCESSED_CREATED_ACCOUNT);

                        }else {
                            setMessageState(Constants.FAILED_MESSAGE+"_Message_"+task.getException().getMessage());
                        }
                    }
                });


        return messageState;

    }



    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }
}
