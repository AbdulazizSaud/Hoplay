package com.example.kay.hoplay.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.kay.hoplay.CoresAbstract.Lobby.Lobby;
import com.example.kay.hoplay.Models.PlayerModel;
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

        lobby = new Lobby(getContext(), view) {
            @Override
            public void addFriend(PlayerModel model) {
                addPlayerToFreind(model);
            }

            @Override
            public void kickPlayer(PlayerModel model) {
                removePlayerFromLobby(model);
            }

            @Override
            public void updateAdminInfo(RequestModel requestModel) {
                updateAdminInformation(requestModel);
            }
        };


        lobby.getCloseRequestButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // show the exit verification dialog
                showLeaveLobbyDialog();
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


    protected void showLeaveLobbyDialog(){

        final Dialog leaveLobbyDialog;
        leaveLobbyDialog = new Dialog(getContext());
        leaveLobbyDialog.setContentView(R.layout.leave_lobby_validation);
        leaveLobbyDialog.show();


        leaveLobbyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView leaveLobbyMessage;
        Button leaveLobbyYes , leaveLobbyNo;

        leaveLobbyMessage = (TextView)  leaveLobbyDialog.findViewById(R.id.leave_lobby_message_textview);

        leaveLobbyYes = ( Button) leaveLobbyDialog.findViewById(R.id.leave_lobby_yes_button);
        leaveLobbyNo = ( Button) leaveLobbyDialog.findViewById(R.id.leave_lobby_no_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        Typeface playbold = Typeface.createFromAsset(getResources().getAssets() ,"playbold.ttf");
        leaveLobbyYes.setTypeface(sansation);
        leaveLobbyNo.setTypeface(sansation);
        leaveLobbyMessage.setTypeface(playbold);



        // Exit Lobby
        leaveLobbyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
                leaveLobbyDialog.dismiss();
            }
        });


        // Remove Dialog
        leaveLobbyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveLobbyDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = leaveLobbyDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    protected abstract void OnStartActivity();

    protected abstract void cancelRequest();
    protected abstract void addPlayerToFreind(PlayerModel model);
    protected abstract void removePlayerFromLobby(PlayerModel model);

    protected abstract void updateAdminInformation(RequestModel model);
}
