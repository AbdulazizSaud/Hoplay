package com.example.kay.hoplay.Cores.UserProfileCores;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Interfaces.FirebasePaths;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.AddGame;
import com.example.kay.hoplay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;


public class AddGameCore extends AddGame implements FirebasePaths {

    View holderView;
    private LinkedList<ValueEventListener> searchForGameListeners = new LinkedList<>();


    protected void OnStartActivity() {

//        searchBar.setVisibility(View.GONE);
//        searchBar= null;


        loadFavorGamesList();
    }

    @Override
    protected void showOnLongClickDialog(final GameModel gameModel) {

        final Dialog onLongClickGameDialog;
        onLongClickGameDialog = new Dialog(AddGameCore.this);
        onLongClickGameDialog.setContentView(R.layout.game_long_click_pop_up);
        onLongClickGameDialog.show();


        onLongClickGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView verificationDeleteText;
        Button deleteYesButton , deleteNoButton;

        deleteYesButton = ( Button) onLongClickGameDialog.findViewById(R.id.delete_game_yes_button);
        deleteNoButton = ( Button) onLongClickGameDialog.findViewById(R.id.delete_game_no_button);

        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        deleteYesButton.setTypeface(sansation);
        deleteNoButton.setTypeface(sansation);



        // Delete Game
        deleteYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame(gameModel.getGameID(),gameModel.getGameName());
                slideRightAnimate(getHolderView());
                onLongClickGameDialog.dismiss();
            }
        });


        // Remove Dialog
        deleteNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongClickGameDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = onLongClickGameDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }

    @Override
    protected void removeGameAnimation(View holderView, int position) {

        if (position > -1) {
            setHolderView(holderView);
        }
    }

    public View getHolderView() {
        return holderView;
    }

    public void setHolderView(View holderView) {
        this.holderView = holderView;
    }

    // Animation Deosn't work yet  !
    private void slideRightAnimate(View viewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);


        viewToAnimate.startAnimation(animation);
        viewToAnimate.setVisibility(View.GONE);

    }


    @Override
    protected void OnClickHolders(GameModel gameModel, View v) {



        // Create game ref for users
        DatabaseReference userFavorGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FAVOR_GAMES_PATH);
        userFavorGameRef.child(gameModel.getGameID()).setValue(gameModel.getGameType());
        addedGameMessage(gameModel.getGameName());
    }


    private void deleteGame(String gameKey, String gameName)
    {
        // users_info_ -> user id  -> _games_ -> _favor_games_
        DatabaseReference userGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_GAMES_REFERENCES).child(FIREBASE_FAVOR_GAME_ATTR);

        // access _favor_games_ -> game_key and delete the game from the database
        userGameRef.child(gameKey).removeValue();

        // now delete the game from the GameManager
        app.getGameManager().deleteGame(gameKey,gameName);

    }

    private void loadFavorGamesList() {


        ArrayList<GameModel> favorGames = app.getGameManager().getAllGamesArrayList();
        String name,gameNameWithCapitalLetter;
        for(GameModel gameModel : favorGames) {

             name = gameModel.getGameName();
             gameNameWithCapitalLetter = name.substring(0, 1).toUpperCase() + name.substring(1);

            gameModel.setGameName(gameNameWithCapitalLetter);

            addGame(gameModel);
        }
    }


    @Override
    protected void searchForGame(String value) {

        // Just to push
        DatabaseReference gamesRef = app.getDatabaseGames();
        Query query = gamesRef.child("_competitive_").orderByChild("name").startAt(value).endAt(value + "\uf8ff").limitToFirst(10);
        getData(query, "_competitive_");
        query = gamesRef.child("_coop_").orderByChild("name").startAt(value).endAt(value + "\uf8ff").limitToFirst(10);
        getData(query, "_coop_");
    }

    private void getData(Query query, String gameType) {
        query.addListenerForSingleValueEvent(createSearchForGameListener(gameType));

    }

    public ValueEventListener createSearchForGameListener(final String gameType) {


        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                    for (DataSnapshot shot : iterable) {
                        String gameName = shot.child("name").getValue().toString().trim();

                        if (!checkIsInList(gameName))
                            addGame(shot.getKey(), gameType, shot);
                    }
                    hideLoadingAnimation();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        searchForGameListeners.add(listener);
        return listener;
    }


    private void addGame(String key, String gametype, DataSnapshot dataSnapshot) {


        String gameId = key;
        String gameName = dataSnapshot.child("name").getValue().toString().trim();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);


        String gamPic = dataSnapshot.child("photo").getValue().toString().trim();
        String maxPlayerAsString = dataSnapshot.child("max_player").getValue().toString().trim();
        String supportedPlatformes = dataSnapshot.child(FIREBASE_GAME_PLATFORMS_ATTR).getValue().toString().trim();
        String gameProvider= dataSnapshot.child(FIREBASE_GAME_PC_GAME_PROVIDER).getValue().toString().trim();
        int maxPlayer = Integer.parseInt(maxPlayerAsString);

        GameModel gameModel = new GameModel(gameId, gameNameWithCapitalLetter, gamPic, supportedPlatformes,gametype,maxPlayer,gameProvider);
        addGame(gameModel);

    }


}
