package com.example.kay.hoplay.UserProfileComponents;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.example.kay.hoplay.CommunityComponents.UserListActivities.ViewFriendProfile;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.UserProfileComponents.ProfileRequires.UserGames;
import com.example.kay.hoplay.UserProfileComponents.ProfileRequires.UserRatings;
import com.example.kay.hoplay.Models.RecentGameModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfile extends Fragment {

    private final static int RESULT_LOAD_IMG=1;

    private  App app;
    private TextView usernameProfile;
    private TextView userGamesTextView;
    private TextView userRatingsTextView;
    private TextView userFriendsTextView;
    private TextView gamesNumberTextView;
    private TextView ratingsNumberTextView ;
    private TextView friendsNumberTextView;
    private TextView recentActivitiesTextView;
    private TextView nicknameTextView;
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        app = App.getInstance();

        usernameProfile = (TextView) view.findViewById(R.id.username_profile);
        //usernameProfile.setText(" Y O Y O  I'm username Fragment");


        userFriendsTextView = (TextView) view.findViewById(R.id.friends_profile_textview);
        friendsNumberTextView = (TextView) view.findViewById(R.id.friends_number_profile_textview);
        userGamesTextView = (TextView) view.findViewById(R.id.games_profile_textview);
        userRatingsTextView = (TextView) view.findViewById(R.id.ratings_profile_textview);
        gamesNumberTextView = (TextView) view.findViewById(R.id.games_number_profile_textview);
        ratingsNumberTextView = (TextView) view.findViewById(R.id.ratings_number_profile_textview);
        recentActivitiesTextView = (TextView) view.findViewById(R.id.recent_activities_textview);
        nicknameTextView = (TextView) view.findViewById(R.id.nickname_profile_textView);
        userPictureCircleImageView = (CircleImageView) view.findViewById(R.id.user_profile_photo_circleimageview);
        profileSettingsImageView = (ImageView) view.findViewById(R.id.user_profile_settings_imageview);
        toUserGamesLinearLayout = (LinearLayout) view.findViewById(R.id.games_user_profile_linearlayout);
        toUserFriendsLinearLayout = (LinearLayout) view.findViewById(R.id.friends_user_profile_linearlayout);
        toUserRatingsLinearLayout = (LinearLayout) view.findViewById(R.id.ratings_user_profile_linearlayout);


        Typeface playregular = Typeface.createFromAsset(getActivity().getAssets() ,"playregular.ttf");

        usernameProfile.setTypeface(playregular);
        friendsNumberTextView.setTypeface(playregular);
        userFriendsTextView.setTypeface(playregular);
        userGamesTextView.setTypeface(playregular);
        userRatingsTextView.setTypeface(playregular);
        gamesNumberTextView.setTypeface(playregular);
        ratingsNumberTextView.setTypeface(playregular);
        recentActivitiesTextView.setTypeface(playregular);
        nicknameTextView.setTypeface(playregular);


        // these  methods used to jump to ( games , scores , following , followers ) activity .
        // I've tried the onClick(View view) method  but  it didn't work .

        toUserGamesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),AddGameCore.class);
                startActivity(i);
            }
        });

        toUserFriendsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext() , ViewFriendProfile.class);
                startActivity(i);

            }
        });

        toUserRatingsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext() , UserRatings.class);
                startActivity(i);
            }
        });




        profileSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(i);

            }
        });

//        profileSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), profileSettings);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater()
//                        .inflate(R.menu.settings_in_profile, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(
//                                getActivity().getApplicationContext() ,
//                                "You Clicked : " + item.getTitle(),
//                                Toast.LENGTH_SHORT
//                        ).show();
//                        return true;
//                    }
//                });
//
//                popup.show(); //showing popup menu
//            }
//        }); //closing the setOnClickListener method


        userPictureCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new AlertDialog.Builder(v.getContext())
                        .setTitle("Change Profile Picture")
                        .setPositiveButton("Change Picture", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // Start the Intent
                                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                            }
                        }).setNegativeButton("Change Cover",null)
                        .show();


            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recent_activities_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        recentGameModels.add(addRecentActivity("XXX1","Played Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Category : PS","15 min ago"));
        recentGameModels.add(addRecentActivity("XXX1","Played Overwatch","https://b.thumbs.redditmedia.com/ksN39DPM7HFaXTP_tBi-IuYYfWccRBjYykD6VFSePXE.jpg","Category : PC","24 min ago"));
        recentGameModels.add(addRecentActivity("XXX1","Played Dying Light","http://shinigaming.com/wp-content/uploads/2015/01/dying-light-logo-high-resolution.jpg","Category : Xbox","5 hours ago"));
        recentGameModels.add(addRecentActivity("XXX1","World of Warcraft","http://www.technologytell.com/gaming/files/2014/02/world-of-warcraft.png","Category : PC","1 day ago"));



        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);


        setUserProfileInformation();

        return view;
    }

    private void setUserProfileInformation() {
         app = App.getInstance();

        app.loadingImage(userPictureCircleImageView,app.getUserInformation().getPictureURL());
        usernameProfile.setText("@"+app.getUserInformation().getUsername());
        nicknameTextView.setText(app.getUserInformation().getNickName());
    }


    public RecentGameModel addRecentActivity(String gameID, String gameName , String gamePhoto , String activityDescription , String activityDate)
    {
        RecentGameModel recentActivity = new RecentGameModel(gameID,gameName,gamePhoto,activityDescription,activityDate);

        return recentActivity;
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
                Toast.makeText(getContext(), "You haven't picked Image",
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
            public void OnBindHolder(ViewHolders holder, RecentGameModel model)
            {

               app.loadingImage(holder,model.getGamePhotoUrl());

                holder.setTitle(model.getGameName());
                holder.setSubtitle(model.getActivityDescription());
                holder.setTime(model.getActivityDate());
            }
        };
    }



}