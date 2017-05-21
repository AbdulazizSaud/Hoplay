package com.example.kay.hoplay.Cores.RequestCore;

import android.util.Log;

import com.example.kay.hoplay.CoresAbstract.RequestAbstracts.RequestLobby;
import com.example.kay.hoplay.Models.RequestModel;



public class RequestLobbyCore extends RequestLobby {
    RequestModel requestModel;

    @Override
    protected void OnStartActivity() {
        requestModel = new RequestModel();

        // here we will retreive the data;

        Log.i("-->",requestModel.getAdmin());
    }
}
