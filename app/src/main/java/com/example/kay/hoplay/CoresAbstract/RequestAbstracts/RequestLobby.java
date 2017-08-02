package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.ChatCore.ChatCore;
import com.example.kay.hoplay.Cores.Lobby.Lobby;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;

public abstract class RequestLobby extends AppCompatActivity {


    protected Lobby lobby;
    protected App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = App.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lobby);

        lobby = new Lobby(getApplication(), getWindow().getDecorView()) {
            @Override
            public void removePlayer() {
                return;
            }
        };

        lobby.getJoinButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinToRequest();
            }
        });

        lobby.getCloseRequestButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        OnStartActivity();
    }



    protected void jumpToLobbyChat(RequestModel model,String chatType) {
        Intent chatActivity = new Intent(getApplicationContext(), ChatCore.class);

        chatActivity.putExtra("room_key", model.getRequestId());
        chatActivity.putExtra("room_type", chatType);
        chatActivity.putExtra("room_name", model.getRequestTitle());
        chatActivity.putExtra("room_picture", model.getRequestPicture());

        finish();
        startActivity(chatActivity);
    }

    protected abstract void OnStartActivity();
    protected abstract void joinToRequest();
}
