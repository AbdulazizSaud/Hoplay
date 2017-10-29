package com.hoplay.kay.hoplay.CoresAbstract.ProfileAbstracts;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Activities.SettingsActivity;
import com.hoplay.kay.hoplay.Adapters.CommonAdapter;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Adapters.ViewHolders;
import com.hoplay.kay.hoplay.Cores.ChatCore.FindUserCore;
import com.hoplay.kay.hoplay.Cores.MainAppMenuCore;
import com.hoplay.kay.hoplay.Cores.RequestCore.NewRequestCore;
import com.hoplay.kay.hoplay.Cores.UserProfileCores.AddGameCore;
import com.hoplay.kay.hoplay.Cores.UserProfileCores.FriendsListCore;
import com.hoplay.kay.hoplay.CoresAbstract.MainAppMenu;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Models.RecentGameModel;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class UserProfile extends Fragment {

    private final static int RESULT_LOAD_IMG = 1;

    protected App app;
    private TextView usernameProfile;
    private TextView userGamesTextView;
    private TextView userRatingsTextView;
    private TextView userFriendsTextView;
    private TextView gamesNumberTextView;
    private TextView ratingsNumberTextView;
    private TextView friendsNumberTextView;
    private TextView recentActivitiesTextView;
    private TextView hopyPointsTextview;
    private TextView hopyPointsValueTextview;
    private TextView bioTextView;
    private CircleImageView userPictureCircleImageView;
    private ImageView profileSettingsImageView;
    private LinearLayout toUserGamesLinearLayout;
    private LinearLayout toUserFriendsLinearLayout;
    private LinearLayout toUserRatingsLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    ArrayList<RecentGameModel> recentGameModels = new ArrayList<RecentGameModel>();
    ArrayList<RecentGameModel> tmpRecentGameModels = new ArrayList<RecentGameModel>();



    // No activities section
    private ImageView noActivityImageview;
    private TextView noActivityMessage;
    private Button addActivityButton;

    Handler handler = new Handler();
    Runnable runnable;




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



        handler.postDelayed(runnable = new Runnable(){
            public void run(){
                //do something

                hopyPointsValueTextview.setText(app.getUserInformation().getHopyPoints());

                handler.postDelayed(this, 1000);
            }
        }, 1000);




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


        // Points Variables
        hopyPointsTextview = (TextView) view.findViewById(R.id.hopy_points_textview);
        hopyPointsValueTextview = (TextView) view.findViewById(R.id.hopy_points_value);


        addActivityButton = (Button) view.findViewById(R.id.add_activity_user_fragment_button);
        noActivityMessage = (TextView) view.findViewById(R.id.no_activity_textview);
        noActivityImageview = (ImageView) view.findViewById(R.id.no_activity_imageview);


        Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");
        Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets(), "sansationbold.ttf");

        usernameProfile.setTypeface(playbold);
        friendsNumberTextView.setTypeface(playregular);
        hopyPointsTextview.setTypeface(playbold);
        hopyPointsValueTextview.setTypeface(playregular);
        userFriendsTextView.setTypeface(playregular);
        userGamesTextView.setTypeface(playregular);
        userRatingsTextView.setTypeface(playregular);
        gamesNumberTextView.setTypeface(playregular);
        ratingsNumberTextView.setTypeface(playregular);
        recentActivitiesTextView.setTypeface(playregular);
        bioTextView.setTypeface(playregular);

        addActivityButton.setTypeface(sansation);
        noActivityMessage.setTypeface(playregular);

        setGamesNumber("0");




    }

    private void setClickableControls() {
        // these  methods used to jump to ( games , scores , following , followers ) activity .
        // I've tried the onClick(View view) method  but  it didn't work .


        userPictureCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Alpha
                if (isAllowedPremmision()) {

                    userPictureCircleImageView.setClickable(false);

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

                }

            }
        });

        toUserGamesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);
                //Stop Handler
                handler.removeCallbacks(runnable);
            }
        });

        toUserFriendsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), FriendsListCore.class);
                i.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);
                //Stop Handler
                handler.removeCallbacks(runnable);

            }
        });




        profileSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right_layouts, R.anim.slide_out_right_layouts);
                //Stop Handler
                handler.removeCallbacks(runnable);


            }
        });


        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Check if the user has games or not
                if(app.getGameManager().getUserGamesNumber()<1)
                    showNoGameDialog();
                else{
                    Intent i = new Intent(getContext(), NewRequestCore.class);
                    startActivity(i);
                    getActivity().overridePendingTransition( R.anim.slide_in_left_layout, R.anim.slide_out_left_layout);
                    //Stop Handler
                    handler.removeCallbacks(runnable);
                    }

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

        // Add holders in reverese mode : new holders added on the top
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next hoplay)




        mAdapter = createAdapter();

        // Show or hide no activity elements
        if (mAdapter.getItemCount() < 1)
            showNoActivityElements();
        else
            hideNoActivityElements();




        mRecyclerView.setAdapter(mAdapter);
    }

    private synchronized void setUserProfileInformation() {
        app = App.getInstance();


        app.loadingImage(userPictureCircleImageView, app.getUserInformation().getPictureURL());
        usernameProfile.setText("@" + app.getUserInformation().getUsername());
        bioTextView.setText(app.getUserInformation().getNickName());
        hopyPointsValueTextview.setText(app.getUserInformation().getHopyPoints());
    }


    protected void loadPicture() {
        app.loadingImage(userPictureCircleImageView, app.getUserInformation().getPictureURL());

    }

    public void addRecentGame(String gameID, String gameName, String gamePhoto, String platform, String activityDescription, long timestmap) {

        RecentGameModel recentActivity = new RecentGameModel(gameID, gameName, gamePhoto, platform, activityDescription, timestmap);
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
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);

                userPictureCircleImageView.setImageBitmap(imgBitmap);

                uploadPicture(userPictureCircleImageView);

            } else {

                userPictureCircleImageView.setClickable(true);

                Toast.makeText(getContext(), "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            userPictureCircleImageView.setClickable(true);
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Log.e("--->", e.getLocalizedMessage());
        }

    }



    private CommonAdapter<RecentGameModel> createAdapter() {
        return new CommonAdapter<RecentGameModel>(recentGameModels, R.layout.new_user_activity) {
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
            public void OnBindHolder(final ViewHolders holder, final RecentGameModel model, int position) {

                // Show or hide no activity elements
                if (mAdapter.getItemCount() < 1)
                    showNoActivityElements();
                else
                    hideNoActivityElements();


                app.loadingImage(getContext(), holder, model.getGamePhotoUrl());

                Typeface playbold = Typeface.createFromAsset(getActivity().getAssets(), "playbold.ttf");
                Typeface playregular = Typeface.createFromAsset(getActivity().getAssets(), "playregular.ttf");




                // Capitalize game name letters
                String gameName = model.getGameName();
                String capitlizedGameName = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
                if (gameName.contains(" ")) {
                    // Capitalize game title letters
                    String cpWord = "";
                    for (int i = 0; i < capitlizedGameName.length(); i++) {
                        if (capitlizedGameName.charAt(i) == 32 && capitlizedGameName.charAt(i + 1) != 32) {
                            cpWord = capitlizedGameName.substring(i + 1, i + 2).toUpperCase() + capitlizedGameName.substring(i + 2);
                            capitlizedGameName = capitlizedGameName.replace(capitlizedGameName.charAt(i + 1), cpWord.charAt(0));
                        }
                    }
                    holder.setTitle(capitlizedGameName);
                } else {
                    if (capitlizedGameName.equalsIgnoreCase("cs:go")) {
                        holder.setTitle(capitlizedGameName.toUpperCase());
                    } else
                        holder.setTitle(capitlizedGameName);
                }



                holder.getTitleView().setTypeface(playbold);
                holder.setSubtitle(model.getActivityDescription());
                holder.getSubtitleView().setTypeface(playregular);



                holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));

                new HandlerCondition(new CallbackHandlerCondition() {
                    @Override
                    public boolean callBack() {
                        holder.setTime(app.convertFromTimeStampToTimeAgo(model.getTimeStamp()));
                        return false;
                    }
                }, 20000);


                holder.getTimeView().setTypeface(playregular);


                holder.getPicture().setBorderWidth(6);
                // Changing title color depending on the platform
                if (model.getGamePlatforms().equalsIgnoreCase("PC")) {
                    holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.pc_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.pc_color));
                } else if (model.getGamePlatforms().equalsIgnoreCase("PS")) {
                    holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.ps_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.ps_color));
                } else {
                    holder.getTitleView().setTextColor(ContextCompat.getColor(getContext(), R.color.xbox_color));
                    holder.getPicture().setBorderColor(ContextCompat.getColor(getContext(), R.color.xbox_color));
                }


            }
        };
    }


    protected void setGamesNumber(String number) {
        gamesNumberTextView.setText(number);
    }

    protected void setFriendsNumber(String number) {
        friendsNumberTextView.setText(number);
    }

    protected void setNicknameTextView(String name) {
        bioTextView.setText(name);
    }

    protected void setUsernameProfile(String name) {
        usernameProfile.setText(name);
    }


    private void showNoActivityElements(){

        Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_view);

        recentActivitiesTextView.setVisibility(View.GONE);

        noActivityImageview.setVisibility(View.INVISIBLE);
        noActivityImageview.startAnimation(slideDown);
        noActivityImageview.setVisibility(View.VISIBLE);

        noActivityMessage.setVisibility(View.INVISIBLE);
        noActivityMessage.startAnimation(slideDown);
        noActivityMessage.setVisibility(View.VISIBLE);


        addActivityButton.setVisibility(View.INVISIBLE);
        addActivityButton.startAnimation(slideDown);
        addActivityButton.setVisibility(View.VISIBLE);
    }

    private void hideNoActivityElements() {

        recentActivitiesTextView.setVisibility(View.VISIBLE);
        noActivityImageview.setVisibility(View.GONE);
        noActivityMessage.setVisibility(View.GONE);
        addActivityButton.setVisibility(View.GONE);
    }


    private boolean isAllowedPremmision() {
        int permissionCheck = ContextCompat.checkSelfPermission(app.getMainAppMenuCore(),READ_EXTERNAL_STORAGE);

        if ( permissionCheck != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(app.getMainAppMenuCore(),READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            ActivityCompat.requestPermissions(app.getMainAppMenuCore(),
                    new String[]{READ_EXTERNAL_STORAGE},
                    0);

        } else return true;

        return false;
    }




    private void showNoGameDialog(){


        final Dialog noGameDialog = new Dialog(getContext());
        noGameDialog.setContentView(R.layout.activity_no_game_pop_up);
        noGameDialog.show();


        noGameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView noGameText;
        Button addGameButton;




        noGameText = (TextView) noGameDialog.findViewById(R.id.popup_message_textview);
        addGameButton = (Button) noGameDialog.findViewById(R.id.add_game_button_no_game_popup);




        Typeface sansation = Typeface.createFromAsset(getResources().getAssets() ,"sansationbold.ttf");
        addGameButton.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        final Typeface playReg = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");

        noGameText.setTypeface(playReg);

        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddGameCore.class);
                startActivity(i);
                getActivity().overridePendingTransition( R.anim.slide_in_down_layouts, R.anim.slide_out_down_layouts);
                //Stop Handler
                handler.removeCallbacks(runnable);
                noGameDialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = noGameDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }




    protected abstract void OnStartActitvty();

    protected abstract void uploadPicture(CircleImageView circleImageView);



}