package com.hoplay.kay.hoplay.Cores.RequestCore;

import android.widget.Toast;

import com.google.firebase.database.Query;
import com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts.NewRequest;
import com.hoplay.kay.hoplay.Interfaces.FirebasePaths;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.Rank;
import com.hoplay.kay.hoplay.Models.Ranks;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.FirebaseControllers.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewRequestCore extends NewRequest implements FirebasePaths{


    @Override
    protected void addGame(GameModel gameModel) {
        // Create game ref for users
        DatabaseReference userFavorGameRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_FAVOR_GAMES_PATH);
        userFavorGameRef.child(gameModel.getGameID()).setValue(gameModel.getGameType());

        // add the game to the game manager
        app.getGameManager().addGame(gameModel);
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
                        getGameInfo(shot.getKey(), gameType, shot);
                        isDone = true;
                        whichDialog = true;

                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        return listener;
    }



    private void getGameInfo(String key, String gametype, DataSnapshot dataSnapshot) {


        String gameId = key;
        String gameName = dataSnapshot.child("name").getValue().toString().trim();

        // Capitlizing the first letter of a game
        String gameNameWithCapitalLetter = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);


        String gamPic = dataSnapshot.child("photo").getValue(String.class);
        int maxPlayer = dataSnapshot.child("max_player").getValue(Integer.class);
        String supportedPlatformes = dataSnapshot.child(FIREBASE_GAME_PLATFORMS_ATTR).getValue(String.class);
        String gameProvider = dataSnapshot.child(FIREBASE_GAME_PC_GAME_PROVIDER).getValue(String.class);

        GameModel gameModel = new GameModel(gameId, gameNameWithCapitalLetter, gamPic, supportedPlatformes, gametype, maxPlayer, gameProvider);

        notAddedGame = gameModel;


    }



    @Override
    protected void searchForGame(String value) {


        // Just to push
        DatabaseReference gamesRef = app.getDatabaseGames();

        Query nameQuery = gamesRef.child("_competitive_").orderByChild("name").startAt(value).endAt(value + "\uf8ff").limitToFirst(10);
        getData(nameQuery, "_competitive_");
        nameQuery = gamesRef.child("_coop_").orderByChild("name").startAt(value).endAt(value + "\uf8ff").limitToFirst(10);
        getData(nameQuery, "_coop_");



        Query tagQuery = gamesRef.child("_competitive_").orderByChild("tags/"+value).equalTo(true);
        getData(tagQuery, "_competitive_");
        tagQuery = gamesRef.child("_coop_").orderByChild("tags/"+value).equalTo(true);
        getData(tagQuery, "_coop_");



    }

    protected void OnStartActivity() {


        // Load regions
        DatabaseReference regionsRef = app.getDatabaseRegions();
        regionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    for (DataSnapshot region : dataSnapshot.getChildren()) {
                        regionList.add(region.getValue(String.class).trim());
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void saveGameProviderAccount(String gameProvider ,String userGameProviderAcc , String platform) {

        // users_info_ -> user id
        DatabaseReference userRef = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID());
        if (platform.equalsIgnoreCase("PS"))
            // users_info_ -> user id - > PSN_account
                userRef.child(FIREBASE_USER_PS_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("XBOX"))
            // users_info_ -> user id - > XBOX_life_account
            userRef.child(FIREBASE_USER_XBOX_GAME_PROVIDER).setValue(userGameProviderAcc);
        else if (platform.equalsIgnoreCase("PC"))
        {
            // users_info_ -> user id - > PC_game_provider_accopunt STWAM , BATTLENET .. etc
            userRef.child(FIREBASE_USER_PC_GAME_PROVIDER).child(gameProvider).setValue(userGameProviderAcc);
        }

    }

    @Override
    protected void addSaveRequestToFirebase() {

        DatabaseReference ref = app.getDatabaseUsersInfo().child(app.getUserInformation().getUID()).child(FIREBASE_SAVED_REQS_PATH);

        ref.getParent().child("count").setValue(app.getSavedRequests().size(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null)
                {
                     // <<
                }else {
                    //
                    String msg = String.format(getResources().getString(R.string.new_request_finish_save_request_message), "");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


        ref.setValue(app.getSavedRequests());



    }

    @Override
    protected void loadRanksForNotAddedGame(final GameModel gameModel) {

        app.getDatabaseGames().child(gameModel.getGameType() + "/" + gameModel.getGameID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Rank> ranks = new ArrayList<>();

                for (DataSnapshot rank : dataSnapshot.child("ranks").getChildren()) {
                    ranks.add(new Rank(rank.getValue().toString(), rank.getValue(String.class)));
                }

                Ranks ranksAsClass = new Ranks();
                ranksAsClass.setRanksList(ranks);
                app.getGameManager().getGameByName(gameModel.getGameName()).setGameRanks(ranksAsClass);
                isDoneForLoadingRanks = true;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void addRequestToFirebase(String platform, String gameName, String matchType, String region, String numberOfPlayers, String rank, String description) {


        Request request = new Request(platform,gameName,matchType,region,numberOfPlayers,rank,description);
        app.switchMainAppMenuFragment(new LobbyFragmentCore(request.getRequestModelReference()),2);

    }



}
