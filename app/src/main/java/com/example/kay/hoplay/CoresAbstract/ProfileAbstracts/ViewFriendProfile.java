package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Activities.SettingsActivity;
import com.example.kay.hoplay.Adapters.CommonAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Adapters.ViewHolders;
import com.example.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.example.kay.hoplay.Cores.UserProfileCores.FriendsListCore;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.Models.RecentGameModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public abstract class ViewFriendProfile extends AppCompatActivity {

    protected App app;
    private ImageView backBtn;
    
    private TextView usernameProfile;
    private TextView userGamesTextView;
    private TextView userRatingsTextView;
    private TextView userFriendsTextView;
    private TextView gamesNumberTextView;
    private TextView ratingsNumberTextView ;
    private TextView friendsNumberTextView;
    private TextView recentActivitiesTextView;
    private TextView nicknameTextView;
    protected CircleImageView userPictureCircleImageView;
    protected ImageView profileSettingsImageView;
    private LinearLayout toUserGamesLinearLayout ;
    private LinearLayout toUserFriendsLinearLayout;
    private LinearLayout toUserRatingsLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RecentGameModel> recentGameModels =new ArrayList<RecentGameModel>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_profile);


        app = App.getInstance();

        backBtn = (ImageView)findViewById(R.id.user_profile_back_btn);
        
        usernameProfile = (TextView) findViewById(R.id.username_profile);
        
        userFriendsTextView = (TextView) findViewById(R.id.friends_profile_textview);
        friendsNumberTextView = (TextView) findViewById(R.id.friends_number_profile_textview);
        userGamesTextView = (TextView) findViewById(R.id.games_profile_textview);
        userRatingsTextView = (TextView) findViewById(R.id.ratings_profile_textview);
        gamesNumberTextView = (TextView) findViewById(R.id.games_number_profile_textview);
        ratingsNumberTextView = (TextView) findViewById(R.id.ratings_number_profile_textview);
        recentActivitiesTextView = (TextView) findViewById(R.id.recent_activities_textview);
        nicknameTextView = (TextView) findViewById(R.id.nickname_profile_textView);
        userPictureCircleImageView = (CircleImageView) findViewById(R.id.user_profile_photo_circleimageview);
        profileSettingsImageView = (ImageView) findViewById(R.id.user_profile_settings_imageview);
        toUserGamesLinearLayout = (LinearLayout) findViewById(R.id.games_user_profile_linearlayout);
        toUserFriendsLinearLayout = (LinearLayout) findViewById(R.id.friends_user_profile_linearlayout);
        toUserRatingsLinearLayout = (LinearLayout) findViewById(R.id.ratings_user_profile_linearlayout);




        mRecyclerView = (RecyclerView) findViewById(R.id.recent_activities_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);

        
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        OnStartActitvty();
    }



    // Recent Activity Recycler View Adapter :
    private CommonAdapter<RecentGameModel> createAdapter(){
        return new CommonAdapter<RecentGameModel>(recentGameModels,R.layout.new_user_activity) {
            @Override
            public ViewHolders OnCreateHolder(View v) {

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                return new ViewHolders.RecentGameHolder(v);
            }

            @Override
            public void OnBindHolder(ViewHolders holder, RecentGameModel model)
            {

                app.loadingImage(getApplicationContext(),holder,model.getGamePhotoUrl());

                holder.setTitle(model.getGameName());
                holder.setSubtitle(model.getActivityDescription());
                holder.setTime(model.getActivityDate());
            }
        };
    }



    public void addRecentGame(String gameID, String gameName , String gamePhoto , String activityDescription , String activityDate)
    {

        RecentGameModel recentActivity = new RecentGameModel(gameID,gameName,gamePhoto,activityDescription,activityDate);
        recentGameModels.add(recentActivity);
        mAdapter.notifyDataSetChanged();
    }


    protected void setGamesNumber(String number){
        gamesNumberTextView.setText(number);
    }

    protected void setFriendsNumber(String number){
        friendsNumberTextView.setText(number);
    }

    protected void setNicknameTextView( String name) {nicknameTextView.setText(name);}
    protected void setUsernameProfile(String name){usernameProfile.setText(name);}


    protected abstract void OnStartActitvty();
}
