package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.Models.UserInformation;
import com.example.kay.hoplay.R;


import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestLobby extends AppCompatActivity {


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
    private ArrayList<UserInformation> userModel;
    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lobby);
        initControls();
        setupRecyclerView();
        addPlayer("AB");
        addPlayer("Raktacular");
        addPlayer("Khaledesu");
        addPlayer("Azizs");
        addPlayer("Deathjin");
        addPlayer("MyAss");

    }

    private void initControls()
    {


        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        gamePhoto = (CircleImageView) findViewById(R.id.game_photo_request_lobby);
        matchTypeTextview = (TextView) findViewById(R.id.match_type_request_lobby_textview);
        matchTypeTextview.setTypeface(playbold);
          matchTypeImageView = (ImageView) findViewById(R.id.match_type_request_lobby_imageview);
      adminTextView = (TextView) findViewById(R.id.room_admin_request_lobby_textview);
        adminTextView.setTypeface(playbold);
              adminPhoto = (CircleImageView) findViewById(R.id.room_admin_photo_circleimageview);
        adminUsername = (TextView) findViewById(R.id.admin_username_request_lobby);
        adminUsername.setTypeface(playregular);
       playersTextview = (TextView)findViewById(R.id.players_request_lobby_textview);
        playersTextview.setTypeface(playbold);
         rankTextView = (TextView) findViewById(R.id.rank_request_lobby);
        rankTextView.setTypeface(playbold);
        rankValueTextview = (TextView) findViewById(R.id.rank_value_request_lobby);
        rankValueTextview.setTypeface(playregular);
     regionTextview = (TextView) findViewById(R.id.region_request_lobby);
        regionTextview.setTypeface(playbold);
        regionValueTextview = (TextView) findViewById(R.id.region_value_request_lobby);
        regionValueTextview.setTypeface(playregular);
        joinButton = (Button) findViewById(R.id.join_request_lobby_button);
        joinButton.setTypeface(playbold);
        userModel = new ArrayList<UserInformation>();





    }


    private void setupRecyclerView() {
        playersRecyclerview = (RecyclerView) findViewById(R.id.players_request_lobby_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        playersRecyclerview.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        playersRecyclerview.setLayoutManager(linearLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = createAdapter();
        playersRecyclerview.setAdapter(mAdapter);
    }



    public void addPlayer(String playerUsername)
    {

        UserInformation player = new UserInformation(playerUsername);
        userModel.add(player);
        mAdapter.notifyDataSetChanged();
    }


    private CommonAdapter<UserInformation> createAdapter(){
        return new CommonAdapter<UserInformation>(userModel,R.layout.player_instance) {


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
            public void OnBindHolder(ViewHolders holder, UserInformation model, int position) {

                holder.setTitle(model.getUsername());
            }
        };
    }



}
