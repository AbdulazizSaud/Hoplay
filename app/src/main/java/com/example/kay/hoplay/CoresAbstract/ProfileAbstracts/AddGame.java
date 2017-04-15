package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Interfaces.CONSTANTS;
import com.example.kay.hoplay.Models.CommunityChatModel;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.Models.GameDetails;
import com.example.kay.hoplay.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AddGame extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private EditText searchBar;
    private ArrayList<GameDetails> gamesList = new ArrayList<GameDetails>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    protected App app;
    private boolean isTyping;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        app = App.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_add_game);
        searchBar = (EditText) findViewById(R.id.search_games_edititext_add_game);
        searchBar.setTypeface(playbold);
        mAdapter = createAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isTyping = true;

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Just to push
                final String value = s.toString().toLowerCase().trim();

                searchAnimation(s);

                if (isTextValidate(value)) {
                    searchProcess(value);

                } else {
                    reloadGameList();
                }
            }
        });
        OnStartActivity();
    }



    private void searchAnimation(Editable s) {
        // search icon changing animation
        if (s.length() == 0) {
            searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_unfocused_32dp, 0);
        }
        searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_focused_32dp, 0);
    }


    private void reloadGameList() {


    }


    private void searchProcess(final String value) {

        timer.cancel();
        timer = new Timer();

        updateAdapter(value);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                searchForGame(value);
            }
        }, CONSTANTS.COOL_DOWN_TIMER_MIllI_SECOND);
    }


    protected void updateAdapter(String value) {

        ArrayList<GameDetails> gameDetailsLinkedList = new ArrayList<>();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = value.substring(0,1).toUpperCase() + value.substring(1);


        for (GameDetails game : gamesList)
        {
            if (!game.getGameName().startsWith(gameNameWithCapitalLetter))
            {
                gameDetailsLinkedList.add(game);
            }
        }

        gamesList.removeAll(gameDetailsLinkedList);
        mAdapter.notifyDataSetChanged();

    }


    protected boolean checkIsInList(String name){


        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = name.substring(0,1).toUpperCase() + name.substring(1);

        for (GameDetails game : gamesList)
        {
            if (game.getGameName().startsWith(gameNameWithCapitalLetter))
                return true;
        }
        return false;
    }


    private boolean isTextValidate(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value;
    }


    protected void addGame(String gameId, String gameName, String gameType, int maxPlayers, String gamePic, String supportedPlatforms) {
        GameDetails gameDetails = new GameDetails(gameId, gameName, maxPlayers, gamePic, supportedPlatforms);
        gameDetails.setGameType(gameType);

        gamesList.add(gameDetails);
        mAdapter.notifyDataSetChanged();
    }

    private CommonAdapter<GameDetails> createAdapter() {

        return new CommonAdapter<GameDetails>(gamesList, R.layout.game_instance) {


            private ViewHolders.UserGameHolder gameHolder;

            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.UserGameHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final GameDetails model, int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                gameHolder = (ViewHolders.UserGameHolder) holder;
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v);
                    }
                });

                final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
                app.loadingImage(getApplication(), holder, model.getGamePhotoUrl());
                gameHolder.setTitle(model.getGameName());
                gameHolder.getTitleView().setTypeface(playbold);
                gameHolder.getSubtitleView().setTypeface(playbold);
                gameHolder.getPcTextView().setTypeface(playbold);
                gameHolder.getPsTextView().setTypeface(playbold);
                gameHolder.getXboxTextView().setTypeface(playbold);



                String platforms = model.getGamePlatforms();


                setPlatformColor(platforms.contains("PC"), ViewHolders.UserGameHolder.Platform.PC, R.color.pc_color);
                setPlatformColor(platforms.contains("PS"), ViewHolders.UserGameHolder.Platform.PS, R.color.ps_color);
                setPlatformColor(platforms.contains("XBOX"), ViewHolders.UserGameHolder.Platform.XBOX, R.color.xbox_color);

            }


            public void setPlatformColor(boolean status, ViewHolders.UserGameHolder.Platform platform, int activeColor) {

                int currentColor = (status) ? activeColor : R.color.hint_color;
                TextView view = gameHolder.getPlatformTextView(platform);
                view.setTextColor(ContextCompat.getColor(getApplicationContext(), currentColor));

            }


        };
    }

    protected void gameAddedMessage(String gameName) {
        // success message
        // String Msg = String.format(getResources().getString(R.string.signup_successful_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), "Game (" + gameName + ") Added", Toast.LENGTH_LONG).show();
    }




    protected abstract void searchForGame(String value);

    protected abstract void OnClickHolders(GameDetails gameDetails, View v);

    protected abstract void OnStartActivity();


}
