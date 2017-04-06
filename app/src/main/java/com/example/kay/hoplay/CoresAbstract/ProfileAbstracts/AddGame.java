package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Models.CommunityChatModel;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.Models.GameDetails;
import com.example.kay.hoplay.R;

import java.util.ArrayList;

public abstract class AddGame extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private TextInputEditText searchBar;
    private ArrayList<GameDetails> gamesList=new ArrayList<GameDetails>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected App app;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        app = App.getInstance();
        mRecyclerView = (RecyclerView)findViewById(R.id.rec_add_game);
        searchBar = (TextInputEditText)findViewById(R.id.search_games_edititext_add_game) ;
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

            }

            @Override
            public void afterTextChanged(Editable s) {

                String value = s.toString().trim();

                if(isTextValidate(value))
                {
                    updateAdapter(true);
                    searchForGame(value);
                } else
                {
                    reloadGameList();
                }
            }
        });
        OnStartActivity();
    }




    private void reloadGameList() {
        gamesList.clear();

        for(GameDetails gameDetails : gamesList)
        {
            addGame(gameDetails.getGameID(),
                    gameDetails.getGameName(),
                    gameDetails.getGameType()
                    ,gameDetails.getMaxPlayers()
                    ,gameDetails.getGamePhotoUrl());
        }
    }


    protected void updateAdapter(boolean clearList) {
        if(clearList)
            gamesList.clear();

        mAdapter.notifyDataSetChanged();

    }


    private boolean isTextValidate(String value) {
        return !value.equals("") && !value.equals("\\s+") && null != value;
    }


    protected void addGame(String gameId,String gameName,String gameType,int maxPlayers,String gamePic)
    {
        GameDetails gameDetails = new GameDetails(gameId,gameName,maxPlayers,gamePic);
        gameDetails.setGameType(gameType);

        gamesList.add(gameDetails);
        mAdapter.notifyDataSetChanged();
    }

    private CommonAdapter<GameDetails> createAdapter(){
        return new CommonAdapter<GameDetails>(gamesList,R.layout.game_instance) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                return new ViewHolders.UserGameHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, final GameDetails model) {
                // - get element from your dataset at this position
                // - replace the contents of the view with that element
                ViewHolders.UserGameHolder gameHolder = (ViewHolders.UserGameHolder)holder;
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickHolders(model,v);
                    }
                });

                app.loadingImage(getApplication(),holder, model.getGamePhotoUrl());
                holder.setTitle(model.getGameName());

            }
        };
    }

    protected void gameAddedMessage(String gameName)
    {
        // success message
       // String Msg = String.format(getResources().getString(R.string.signup_successful_message), username);

        // results if it's successed
        Toast.makeText(getApplicationContext(), "Game ("+gameName+") Added",Toast.LENGTH_LONG).show();
    }

    protected abstract void searchForGame(String value);
    protected abstract  void OnClickHolders(GameDetails gameDetails, View v);
    protected abstract void OnStartActivity();
}
