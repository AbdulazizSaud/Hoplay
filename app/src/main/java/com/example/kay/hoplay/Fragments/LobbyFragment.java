package com.example.kay.hoplay.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.CoresAbstract.Lobby.Lobby;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;


public abstract class LobbyFragment extends ParentRequestFragments {


    protected Lobby lobby;
    public LobbyFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_request_lobby, container, false);

        lobby = new Lobby(getContext(),view);

        lobby.getCloseRequestButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });

        OnStartActivity();
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }


    protected abstract void OnStartActivity();

    protected abstract void cancelRequest();


}
