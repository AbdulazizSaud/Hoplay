package com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Adapters.CommonAdapter;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Adapters.ViewHolders;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Models.RecentGameModel;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class ViewFriendProfile extends AppCompatActivity {

    protected App app;
    private ImageView backBtn;


    protected ImageView addFriendButton;
    private TextView usernameProfile;
    private TextView userBioTextView;
    private TextView userGamesTextView;
    private TextView userRatingsTextView;
    private TextView userFriendsTextView;
    private TextView gamesNumberTextView;
    private TextView ratingsNumberTextView ;
    private TextView friendsNumberTextView;
    private TextView recentActivitiesTextView;
    protected CircleImageView userPictureCircleImageView;
    protected ImageView profileSettingsImageView;
    private LinearLayout toUserGamesLinearLayout ;
    private LinearLayout toUserFriendsLinearLayout;
    private LinearLayout toUserRatingsLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RecentGameModel> recentGameModels =new ArrayList<RecentGameModel>();
    protected String userKey = "";

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
        userPictureCircleImageView = (CircleImageView) findViewById(R.id.user_profile_photo_circleimageview);
        profileSettingsImageView = (ImageView) findViewById(R.id.user_profile_settings_imageview);
        toUserGamesLinearLayout = (LinearLayout) findViewById(R.id.games_user_profile_linearlayout);
        toUserFriendsLinearLayout = (LinearLayout) findViewById(R.id.friends_user_profile_linearlayout);
        toUserRatingsLinearLayout = (LinearLayout) findViewById(R.id.ratings_user_profile_linearlayout);
        userBioTextView = (TextView)  findViewById(R.id.view_user_profile_bio_textView);
        addFriendButton = (ImageView) findViewById(R.id.add_friend_view_user_profile);




        Typeface playregular = Typeface.createFromAsset(getAssets() ,"playregular.ttf");
        Typeface playbold = Typeface.createFromAsset(getAssets() ,"playbold.ttf");

        usernameProfile.setTypeface(playbold);
        friendsNumberTextView.setTypeface(playregular);
        userFriendsTextView.setTypeface(playregular);
        userGamesTextView.setTypeface(playregular);
        userRatingsTextView.setTypeface(playregular);
        gamesNumberTextView.setTypeface(playregular);
        ratingsNumberTextView.setTypeface(playregular);
        recentActivitiesTextView.setTypeface(playregular);
        userBioTextView.setTypeface(playregular);


        mRecyclerView = (RecyclerView) findViewById(R.id.recent_activities_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next hoplay)


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userPictureCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });



        // Load user info
        OnStartActitvty();

        // Add this user as a friend
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userKey!="" && !usernameProfile.getText().toString().isEmpty())
                {
                    addThisUser(userKey);
                    Toast.makeText(getApplicationContext(),usernameProfile.getText().toString()+" added successfully",Toast.LENGTH_LONG).show();
                }

            }
        });


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
            public void OnBindHolder(final ViewHolders holder,final RecentGameModel model , int position)
            {




                Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
                Typeface playregular = Typeface.createFromAsset(getAssets(), "playregular.ttf");




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
                    holder.setTitle(capitlizedGameName);
                }else {
                    if (capitlizedGameName.equalsIgnoreCase("cs:go")){
                        holder.setTitle(capitlizedGameName.toUpperCase());
                    } else
                        holder.setTitle(capitlizedGameName);
                }

                app.loadingImage(getApplicationContext(),holder,model.getGamePhotoUrl());


                holder.setSubtitle(model.getActivityDescription());


                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));

                new HandlerCondition(new CallbackHandlerCondition() {
                    @Override
                    public boolean callBack() {
                        holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
                        return false;
                    }
                },10000);


                holder.getTitleView().setTypeface(playbold);
                holder.getSubtitleView().setTypeface(playregular);



                holder.getPicture().setBorderWidth(6);
//                 Changing title color depending on the platform
                if (model.getGamePlatforms().equalsIgnoreCase("PC"))
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.pc_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.pc_color));}
                else if (model.getGamePlatforms().equalsIgnoreCase("PS"))
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ps_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.ps_color));}
                else
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.xbox_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.xbox_color));}


            }
        };
    }


    protected void setBio(String bio){
        this.userBioTextView.setText(bio);
    }


    public void addRecentGame(String gameID, String gameName , String gamePhoto ,String platform, String activityDescription , Long timestmap)
    {

        RecentGameModel recentActivity = new RecentGameModel(gameID,gameName,gamePhoto,platform,activityDescription,timestmap);
        recentGameModels.add(recentActivity);
        mAdapter.notifyDataSetChanged();
    }


    protected void setGamesNumber(String number){
        gamesNumberTextView.setText(number);
    }
    protected void setFriendsNumber(String number){
        friendsNumberTextView.setText(number);
    }
    protected void setUsernameProfile(String name){usernameProfile.setText(name);}
    protected abstract void addThisUser(String userKey);
    protected abstract void OnStartActitvty();

}
