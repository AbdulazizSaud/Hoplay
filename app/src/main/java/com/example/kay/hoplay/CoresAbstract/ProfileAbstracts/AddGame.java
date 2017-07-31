package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.example.kay.hoplay.Interfaces.Constants;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AddGame extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    protected EditText searchBar;
    private ArrayList<GameModel> gamesList = new ArrayList<GameModel>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar loadGamesProgressbar;




    protected App app;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        app = App.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_add_game);
        searchBar = (EditText) findViewById(R.id.search_games_edititext_add_game);
        loadGamesProgressbar = (ProgressBar) findViewById(R.id.load_games_progressbar_add_game);
        loadGamesProgressbar.setVisibility(View.GONE);
        searchBar.setTypeface(playbold);
        mAdapter = createAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // remove the keyboard when start activity
        mRecyclerView.requestFocus();


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Just to push
                final String value = s.toString().toLowerCase().trim();
                showLoadingAnimation();

                // chinging search icon when user search for a game
                // search icon changing animation
                if (s.length() == 0) {
                    searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_unfocused_32dp, 0);
                }
                searchBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search_focused_32dp, 0);


                if (isTextValidate(value)) {
                    searchProcess(value);

                } else {
                    reloadGameList();
                }
            }
        });
        OnStartActivity();
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
        }, Constants.COOL_DOWN_TIMER_MIllI_SECOND);
    }


    protected void updateAdapter(String value) {

        ArrayList<GameModel> gameModelLinkedList = new ArrayList<>();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = value.substring(0, 1).toUpperCase() + value.substring(1);


        for (GameModel game : gamesList) {
            if (!game.getGameName().startsWith(gameNameWithCapitalLetter)) {
                gameModelLinkedList.add(game);
            }
        }


        gamesList.removeAll(gameModelLinkedList);
        mAdapter.notifyDataSetChanged();

    }


    protected boolean checkIsInList(String name) {


        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = name.substring(0, 1).toUpperCase() + name.substring(1);

        for (GameModel game : gamesList) {
            if (game.getGameName().startsWith(gameNameWithCapitalLetter))
                return true;
        }
        return false;
    }


    private boolean isTextValidate(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value;
    }


    protected void addGame(GameModel gameModel) {
        gamesList.add(gameModel);
        mAdapter.notifyDataSetChanged();
    }

    private CommonAdapter<GameModel> createAdapter() {

        return new CommonAdapter<GameModel>(gamesList, R.layout.game_instance) {


            private ViewHolders.UserGameHolder gameHolder;

            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.UserGameHolder(v);
            }

            @Override
            public void OnBindHolder(final ViewHolders holder, final GameModel model, final int position) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                gameHolder = (ViewHolders.UserGameHolder) holder;
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model, v);
                    }
                });

                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (app.getGameManager().checkIfHasGameByName(holder.getTitle().getText().toString().trim()))
                        {
                            showOnLongClickDialog(model);
                            removeGameAnimation(v, position);
                        }
                        return true;
                    }
                });


                final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
                app.loadingImage(getApplication(), holder, model.getGamePhotoUrl());

                gameHolder.getPicture().setBorderWidth(6);
                gameHolder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color));

                // Capitalize game name letters
                String gameName = model.getGameName();
                String capitlizedGameName = gameName.substring(0,1).toUpperCase() +  gameName.substring(1);
                if (gameName.contains(" "))
                {
                    // Capitalize game title letters
                    String cpWord= "";
                    for (int  i = 0 ; i < capitlizedGameName.length(); i++)
                    {
                        if (capitlizedGameName.charAt(i) == 32 && capitlizedGameName.charAt(i+1) != 32)
                        {
                            cpWord= capitlizedGameName.substring(i+1,i+2).toUpperCase() + capitlizedGameName.substring(i+2);
                            capitlizedGameName = capitlizedGameName.replace(capitlizedGameName.charAt(i+1),cpWord.charAt(0));
                        }

                    }
                    gameHolder.setTitle(capitlizedGameName);
                }else {
                    if (capitlizedGameName.equalsIgnoreCase("cs:go")){
                        gameHolder.setTitle(capitlizedGameName.toUpperCase());
                    } else
                    gameHolder.setTitle(capitlizedGameName);
                }


                gameHolder.getTitleView().setTypeface(playbold);
                gameHolder.getSubtitleView().setTypeface(playbold);
                gameHolder.getPcTextView().setTypeface(playbold);
                gameHolder.getPsTextView().setTypeface(playbold);
                gameHolder.getXboxTextView().setTypeface(playbold);

                // Animate holders
                setAnimation(gameHolder.getTitleView(), gameHolder.getSubtitleView(), gameHolder.getPcTextView(), gameHolder.getPsTextView(), gameHolder.getXboxTextView(), gameHolder.getPicture(), position);


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

            // animate holders
            public void setAnimation(View viewToAnimate1, View viewToAnimate2, View viewToAnimate3, View viewToAnimate4, View viewToAnimate5, View viewToAnimate6, int position) {
                // If the bound view wasn't previously displayed on screen, it's animated
                if (position > -1) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                    viewToAnimate1.startAnimation(animation);
                    viewToAnimate2.startAnimation(animation);
                    viewToAnimate3.startAnimation(animation);
                    viewToAnimate4.startAnimation(animation);
                    viewToAnimate5.startAnimation(animation);
                    viewToAnimate6.startAnimation(animation);

                }
            }


        };
    }


    protected void addedGameMessage(String gameName) {
        // success message
        // String Msg = String.format(getResources().getString(R.string.signup_successful_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), "Game (" + gameName + ") Added", Toast.LENGTH_LONG).show();
    }





    protected void showLoadingAnimation() {
        mRecyclerView.setVisibility(RecyclerView.INVISIBLE);
        loadGamesProgressbar.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingAnimation() {
        mRecyclerView.setVisibility(RecyclerView.VISIBLE);
        loadGamesProgressbar.setVisibility(View.INVISIBLE);
    }






    protected abstract void searchForGame(String value);

    protected abstract void OnClickHolders(GameModel gameModel, View v);

    protected abstract void OnStartActivity();

    protected abstract void showOnLongClickDialog(GameModel gameModel);

    protected abstract void removeGameAnimation(View holderView,int position);

}
