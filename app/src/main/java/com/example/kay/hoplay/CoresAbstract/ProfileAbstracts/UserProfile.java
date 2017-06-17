package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class UserProfile extends Fragment {

    private final static int RESULT_LOAD_IMG=1;

    protected   App app;
    private TextView usernameProfile;
    private TextView userGamesTextView;
    private TextView userRatingsTextView;
    private TextView userFriendsTextView;
    private TextView gamesNumberTextView;
    private TextView ratingsNumberTextView ;
    private TextView friendsNumberTextView;
    private TextView recentActivitiesTextView;
    private TextView bioTextView;
    private CircleImageView userPictureCircleImageView;
    private ImageView profileSettingsImageView;
    private LinearLayout toUserGamesLinearLayout ;
    private LinearLayout toUserFriendsLinearLayout;
    private LinearLayout toUserRatingsLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RecentGameModel> recentGameModels =new ArrayList<RecentGameModel>();





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        app = App.getInstance();

        initControls(view);
        setClickableControls();
        OnStartActitvty();


        setupRecyclerView(view);


        setUserProfileInformation();

        return view;
    }


    private void initControls(View view) {
        usernameProfile = (TextView) view.findViewById(R.id.username_profile);
        //usernameProfile.setText(" Y O Y O  I'm username Fragment");


        userFriendsTextView = (TextView) view.findViewById(R.id.friends_profile_textview);
        friendsNumberTextView = (TextView) view.findViewById(R.id.friends_number_profile_textview);
        userGamesTextView = (TextView) view.findViewById(R.id.games_profile_textview);
        userRatingsTextView = (TextView) view.findViewById(R.id.ratings_profile_textview);
        gamesNumberTextView = (TextView) view.findViewById(R.id.games_number_profile_textview);
        ratingsNumberTextView = (TextView) view.findViewById(R.id.ratings_number_profile_textview);
        recentActivitiesTextView = (TextView) view.findViewById(R.id.recent_activities_textview);
        bioTextView = (TextView) view.findViewById(R.id.bio_profile_textView);
        userPictureCircleImageView = (CircleImageView) view.findViewById(R.id.user_profile_photo_circleimageview);
        profileSettingsImageView = (ImageView) view.findViewById(R.id.user_profile_settings_imageview);
        toUserGamesLinearLayout = (LinearLayout) view.findViewById(R.id.games_user_profile_linearlayout);
        toUserFriendsLinearLayout = (LinearLayout) view.findViewById(R.id.friends_user_profile_linearlayout);
        toUserRatingsLinearLayout = (LinearLayout) view.findViewById(R.id.ratings_user_profile_linearlayout);


        Typeface playregular = Typeface.createFromAsset(getActivity().getAssets() ,"playregular.ttf");
        Typeface playbold = Typeface.createFromAsset(getActivity().getAssets() ,"playbold.ttf");

        usernameProfile.setTypeface(playbold);
        friendsNumberTextView.setTypeface(playregular);
        userFriendsTextView.setTypeface(playregular);
        userGamesTextView.setTypeface(playregular);
        userRatingsTextView.setTypeface(playregular);
        gamesNumberTextView.setTypeface(playregular);
        ratingsNumberTextView.setTypeface(playregular);
        recentActivitiesTextView.setTypeface(playregular);
        bioTextView.setTypeface(playregular);




        setGamesNumber("0");

    }

    private void setClickableControls() {
        // these  methods used to jump to ( games , scores , following , followers ) activity .
        // I've tried the onClick(View view) method  but  it didn't work .

        toUserGamesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),AddGameCore.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        toUserFriendsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext() , FriendsListCore.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_down, R.anim.slide_out_down);

            }
        });




        profileSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right);


            }
        });



    }



    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recent_activities_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private synchronized void setUserProfileInformation() {
         app = App.getInstance();


        app.loadingImage(userPictureCircleImageView,app.getUserInformation().getPictureURL());
        usernameProfile.setText("@"+app.getUserInformation().getUsername());
        bioTextView.setText(app.getUserInformation().getNickName());
    }




    protected void loadPicture()
    {
        app.loadingImage(userPictureCircleImageView,app.getUserInformation().getPictureURL());

    }
    public void addRecentGame(String gameID, String gameName , String gamePhoto ,String platform, String activityDescription , String activityDate)
    {

        RecentGameModel recentActivity = new RecentGameModel(gameID,gameName,gamePhoto,"",activityDescription,activityDate);
        recentActivity.setReqPlatform(platform);
        recentGameModels.add(recentActivity);
        mAdapter.notifyDataSetChanged();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                userPictureCircleImageView.setImageBitmap(imgBitmap);

            } else {
                Toast.makeText(getContext(), "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }



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
            public void OnBindHolder(ViewHolders holder, RecentGameModel model,int position)
            {

               app.loadingImage(getContext(),holder,model.getGamePhotoUrl());

                Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
                Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");




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
                    holder.setTitle(capitlizedGameName);
                }



                holder.getTitleView().setTypeface(playbold);
                holder.setSubtitle(model.getActivityDescription());
                holder.getSubtitleView().setTypeface(playregular);
                holder.setTime(model.getActivityDate());
                holder.getTimeView().setTypeface(playregular);


                holder.getPicture().setBorderWidth(6);
                // Changing title color depending on the platform
                if (model.getReqPlatform().equalsIgnoreCase("PC"))
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.pc_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.pc_color));}
                else if (model.getReqPlatform().equalsIgnoreCase("PS"))
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.ps_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.ps_color));}
                else
                {holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.xbox_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.xbox_color));}




            }
        };
    }

    protected void setGamesNumber(String number){
        gamesNumberTextView.setText(number);
    }
    protected void setFriendsNumber(String number){
        friendsNumberTextView.setText(number);
    }

    protected void setNicknameTextView( String name) {bioTextView.setText(name);}
    protected void setUsernameProfile(String name){usernameProfile.setText(name);}


    protected abstract void OnStartActitvty();


}