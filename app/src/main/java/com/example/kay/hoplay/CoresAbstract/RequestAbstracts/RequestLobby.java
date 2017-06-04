package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.CoresAbstract.Lobby.Lobby;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.UserInformation;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Services.CallbackHandlerCondition;
import com.example.kay.hoplay.Services.HandlerCondition;


import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class RequestLobby extends AppCompatActivity {


    protected Lobby lobby;
    protected App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = App.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lobby);

        lobby = new Lobby(getApplication(), getWindow().getDecorView());

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


    protected abstract void OnStartActivity();
    protected abstract void joinToRequest();
    protected abstract void jumpToLobbyChat(RequestModel model);
}
