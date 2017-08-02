package com.example.kay.hoplay.Cores.Lobby;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.ChatCore.ChatCore;
import com.example.kay.hoplay.CoresAbstract.ProfileAbstracts.UserList;
import com.example.kay.hoplay.Models.FriendCommonModel;
import com.example.kay.hoplay.Models.LobbyInformation;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class Lobby {

    private CircleImageView gamePhoto;
    private TextView matchTypeTextview;
    private ImageView matchTypeImageView;
    private TextView adminTextView;
    private CircleImageView adminPhoto;
    private TextView adminUsername;
    private TextView playersTextview;
    private RecyclerView playersRecyclerview;
    private TextView rankTextView;
    private TextView rankValueTextview;
    private TextView regionTextview;
    private TextView regionValueTextview;
    private Button joinButton;
    private ArrayList<PlayerModel> playerModels;
    private HashMap<String, PlayerModel> playerModelsHashMap;

    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageView closeRequestButton;


    private LobbyInformation lobbyInformation;

    private App app;

    private Context context;

    public Lobby(Context c, View v) {
        app = App.getInstance();
        this.context = c;
        initControls(v);
        setupRecyclerView(v);
    }

    private void initControls(View v) {


        final Typeface playbold = Typeface.createFromAsset(context.getResources().getAssets(), "playbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(context.getResources().getAssets(), "playregular.ttf");
        gamePhoto = (CircleImageView) v.findViewById(R.id.game_photo_request_lobby);
        matchTypeTextview = (TextView) v.findViewById(R.id.match_type_request_lobby_textview);
        matchTypeTextview.setTypeface(playbold);
        matchTypeImageView = (ImageView) v.findViewById(R.id.match_type_request_lobby_imageview);
        adminTextView = (TextView) v.findViewById(R.id.room_admin_request_lobby_textview);
        adminTextView.setTypeface(playbold);
        adminPhoto = (CircleImageView) v.findViewById(R.id.room_admin_photo_circleimageview);
        adminUsername = (TextView) v.findViewById(R.id.admin_username_request_lobby);
        adminUsername.setTypeface(playregular);
        playersTextview = (TextView) v.findViewById(R.id.players_request_lobby_textview);
        playersTextview.setTypeface(playbold);
        rankTextView = (TextView) v.findViewById(R.id.rank_request_lobby);
        rankTextView.setTypeface(playbold);
        rankValueTextview = (TextView) v.findViewById(R.id.rank_value_request_lobby);
        rankValueTextview.setTypeface(playregular);
        regionTextview = (TextView) v.findViewById(R.id.region_request_lobby);
        regionTextview.setTypeface(playbold);
        regionValueTextview = (TextView) v.findViewById(R.id.region_value_request_lobby);
        regionValueTextview.setTypeface(playregular);

        joinButton = (Button) v.findViewById(R.id.join_request_lobby_button);
        joinButton.setTypeface(playbold);

        closeRequestButton = (ImageView) v.findViewById(R.id.close_request_lobby_imageview);


        playerModels = new ArrayList<PlayerModel>();
        playerModelsHashMap = new HashMap<>();


    }


    private void setupRecyclerView(View v) {
        playersRecyclerview = (RecyclerView) v.findViewById(R.id.players_request_lobby_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        playersRecyclerview.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        playersRecyclerview.setLayoutManager(linearLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = createAdapter();
        playersRecyclerview.setAdapter(mAdapter);
    }

    public void addPlayer(PlayerModel playerModel) {

        if (isExsist(playerModel.getUID())) {
            return;
        }

        playerModels.add(playerModel);
        playerModelsHashMap.put(playerModel.getUID(), playerModel);
        mAdapter.notifyDataSetChanged();

    }


    public void removePlayer(PlayerModel playerModel) {

        if (!isExsist(playerModel.getUID())) {
            return;
        }



        for(Iterator<PlayerModel> it = playerModels.iterator(); it.hasNext();) {
            PlayerModel s = it.next();
            if(s.getUID().equals(playerModel.getUID())) {
                it.remove();
            }
        }


        if(playerModels.size() > 0 ) {
            if (playerModels.get(0) != null) {
                PlayerModel pl = playerModels.get(0);
                updateAdminInfo(pl.getUID(), pl.getUsername(), pl.getProfilePicture());
            }
        }

        playerModelsHashMap.remove(playerModel);
        mAdapter.notifyDataSetChanged();


    }





    public boolean isExsist(String uid) {
        return playerModelsHashMap.containsKey(uid);
    }

    private CommonAdapter<PlayerModel> createAdapter() {
        return new CommonAdapter<PlayerModel>(playerModels, R.layout.player_instance) {


            @Override
            public ViewHolders OnCreateHolder(View v) {

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // First check if the user is admin or not to show this dialog
                        // if admin then show the dialog
                        // if not then just view player profile
                        showPlayerDialog(context);

                    }
                });

                return new ViewHolders.PlayerHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, PlayerModel model, int position) {


                app.loadingImage(context,holder, model.getProfilePicture());

                holder.setTitle(model.getUsername());
                holder.setSubtitle(model.getGamePovider());
                holder.setSubtitle2(model.getGameProviderAcc());

            }
        };
    }


    public Button getJoinButton() {
        return joinButton;
    }


    public ImageView getCloseRequestButton() {
        return closeRequestButton;
    }


    public void setLobbyInfo(String adminUid, String adminName, String adminPicture, String lobbyPictureURL, String matchType, String rank, String region) {


        lobbyInformation = new LobbyInformation(adminUid,adminName,adminPicture,lobbyPictureURL,matchType,rank,region);

        app.loadingImage(gamePhoto, lobbyPictureURL);
        app.loadingImage(adminPhoto, adminPicture);
        adminUsername.setText(adminName);

        matchTypeTextview.setText(matchType);
        mAdapter.notifyDataSetChanged();

        regionValueTextview.setText(region);
        rankValueTextview.setText(rank);

    }

    private void updateAdminInfo(String adminUid, String adminName, String adminPicture)
    {
        lobbyInformation.setAdminUid(adminUid);
        lobbyInformation.setAdminName(adminName);
        lobbyInformation.setAdminPicture(adminPicture);

        adminUsername.setText(adminName);
        app.loadingImage(adminPhoto, adminPicture);
        app.loadingImage(adminPhoto, adminPicture);

    }




    protected void showPlayerDialog( Context c ) {


        final Dialog friendLongClickDialog;
        friendLongClickDialog = new Dialog(c);
        friendLongClickDialog.setContentView(R.layout.players_dialog);
        friendLongClickDialog.show();


        friendLongClickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button viewPlayerProfileButton , removePlayerButton ;

        viewPlayerProfileButton = ( Button) friendLongClickDialog.findViewById(R.id.view_player_profile_button);
        removePlayerButton = ( Button) friendLongClickDialog.findViewById(R.id.remove_player_button);

        Typeface sansation = Typeface.createFromAsset(c.getAssets() ,"sansationbold.ttf");
        viewPlayerProfileButton.setTypeface(sansation);
        removePlayerButton.setTypeface(sansation);


        // view player profile
        viewPlayerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // remove player
        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = friendLongClickDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }



    public void setGameBorderColor(int color){
        gamePhoto.setBorderColor(color);
    }
    public void setGameBorderWidth(int width){
        gamePhoto.setBorderWidth(width);
    }
    public void setMatchImage(int resource){
        matchTypeImageView.setImageResource(resource);
    }




}
