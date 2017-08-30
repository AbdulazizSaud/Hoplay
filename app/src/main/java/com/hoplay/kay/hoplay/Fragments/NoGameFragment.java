package com.hoplay.kay.hoplay.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hoplay.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.hoplay.kay.hoplay.R;

/**
 * Created by Kay on 5/27/2017.
 */

public class NoGameFragment extends ParentRequestFragments {

    public NoGameFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // User has no games Layout

        View view = inflater.inflate(R.layout.activity_no_game_pop_up, container, false);
        TextView noGameMessage;
        Button addGameButton;

        noGameMessage = (TextView) view.findViewById(R.id.popup_message_textview);
        addGameButton = (Button) view.findViewById(R.id.add_game_button_no_game_popup);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        addGameButton.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        noGameMessage.setTypeface(playbold);


        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
            }
        });


        return view ;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}
