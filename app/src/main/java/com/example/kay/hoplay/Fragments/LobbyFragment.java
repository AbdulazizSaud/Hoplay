package com.example.kay.hoplay.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Models.PlayerModel;
import com.example.kay.hoplay.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public abstract class LobbyFragment extends ParentRequestFragments {




    // Req lobby models
    ArrayList<PlayerModel> playerModels;
    RecyclerView.Adapter playersAdapter;


    public LobbyFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.activity_request_lobby, container, false);

        CircleImageView gamePhoto;
        TextView matchTypeTextview;
        ImageView matchTypeImageView;
        TextView adminTextView;
        CircleImageView adminPhoto;
        TextView adminUsername;
        TextView playersTextview;
        RecyclerView playersRecyclerview;
        TextView rankTextView;
        TextView rankValueTextview;
        TextView regionTextview;
        TextView regionValueTextview;
        Button joinButton;


        LinearLayoutManager linearLayoutManager;



        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        gamePhoto = (CircleImageView) view.findViewById(R.id.game_photo_request_lobby);
        matchTypeTextview = (TextView) view.findViewById(R.id.match_type_request_lobby_textview);
        matchTypeTextview.setTypeface(playbold);
        matchTypeImageView = (ImageView) view.findViewById(R.id.match_type_request_lobby_imageview);
        adminTextView = (TextView) view.findViewById(R.id.room_admin_request_lobby_textview);
        adminTextView.setTypeface(playbold);
        adminPhoto = (CircleImageView) view.findViewById(R.id.room_admin_photo_circleimageview);
        adminUsername = (TextView) view.findViewById(R.id.admin_username_request_lobby);
        adminUsername.setTypeface(playregular);
        playersTextview = (TextView) view.findViewById(R.id.players_request_lobby_textview);
        playersTextview.setTypeface(playbold);
        rankTextView = (TextView) view.findViewById(R.id.rank_request_lobby);
        rankTextView.setTypeface(playbold);
        rankValueTextview = (TextView) view.findViewById(R.id.rank_value_request_lobby);
        rankValueTextview.setTypeface(playregular);
        regionTextview = (TextView) view.findViewById(R.id.region_request_lobby);
        regionTextview.setTypeface(playbold);
        regionValueTextview = (TextView) view.findViewById(R.id.region_value_request_lobby);
        regionValueTextview.setTypeface(playregular);
        joinButton = (Button) view.findViewById(R.id.join_request_lobby_button);
        joinButton.setTypeface(playbold);
        joinButton.setText("Delete");





        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);

        // specify an adapter (see also next example)
        playerModels = new ArrayList<PlayerModel>();
        playersAdapter = createReqLobbyAdapter();

        playersRecyclerview = (RecyclerView) view.findViewById(R.id.players_request_lobby_recyclerview);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        playersRecyclerview.setHasFixedSize(true);
        playersRecyclerview.setLayoutManager(linearLayoutManager);
        playersRecyclerview.setAdapter(playersAdapter);


        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }


    private CommonAdapter<PlayerModel> createReqLobbyAdapter() {
        return new CommonAdapter<PlayerModel>(playerModels, R.layout.player_instance) {


            @Override
            public ViewHolders OnCreateHolder(View v) {

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                return new ViewHolders.PlayerHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, PlayerModel model, int position) {

                holder.setTitle(model.getUsername());
            }
        };
    }




    public void addPlayerToList (String playerUid , String playerUsername) {

        PlayerModel player = new PlayerModel(playerUid,playerUsername);
        playerModels.add(player);
        playersAdapter.notifyDataSetChanged();
    }


    protected abstract void OnStartActivity();
    protected abstract void cancelRequest();

}
