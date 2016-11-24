package com.example.kay.hoplay.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.CommunityAdapter;
import com.example.kay.hoplay.Adapters.RecentActivitiesAdapter;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.ProfileRequires.UserFollowers;
import com.example.kay.hoplay.ProfileRequires.UserFollowing;
import com.example.kay.hoplay.ProfileRequires.UserGames;
import com.example.kay.hoplay.ProfileRequires.UserRatings;
import com.example.kay.hoplay.model.CommunityUserList;
import com.example.kay.hoplay.model.RecentActivityList;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    private TextView usernameProfile;
    private TextView toUserGames;
    private TextView toUserRatings;
    private TextView toUserFollowing;
    private TextView toUserFollowers ;
    private TextView games_number;
    private TextView ratings_number ;
    private TextView followings_number;
    private TextView followers_number;
    private TextView recentActivitiesTextView;
    private  TextView nickname;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        usernameProfile = (TextView) view.findViewById(R.id.username_profile);
        //usernameProfile.setText(" Y O Y O  I'm username Fragment");


        toUserGames = (TextView) view.findViewById(R.id.games_profile_textview);
        toUserFollowers = (TextView) view.findViewById(R.id.followers_profile_textview);
        toUserRatings = (TextView) view.findViewById(R.id.ratings_profile_textview);
        toUserFollowing = (TextView) view.findViewById(R.id.following_profile_textview);
        games_number = (TextView) view.findViewById(R.id.games_number_textview);
        ratings_number = (TextView) view.findViewById(R.id.ratings_number_textview);
        followings_number = (TextView) view.findViewById(R.id.followeing_number_textview);
        followers_number = (TextView) view.findViewById(R.id.followers_number_textview);
        recentActivitiesTextView = (TextView) view.findViewById(R.id.recent_activities_textview);
        nickname = (TextView) view.findViewById(R.id.nickname_profile_textView);


        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets() ,"sansationbold.ttf");
        usernameProfile.setTypeface(sansation);
        toUserGames.setTypeface(sansation);
        toUserFollowers.setTypeface(sansation);
        toUserRatings.setTypeface(sansation);
        toUserFollowing.setTypeface(sansation);
        games_number.setTypeface(sansation);
        ratings_number.setTypeface(sansation);
        followers_number.setTypeface(sansation);
        followings_number.setTypeface(sansation);
        recentActivitiesTextView.setTypeface(sansation);
        nickname.setTypeface(sansation);


        // these  methods used to jump to ( games , scores , following , followers ) activity .
        // I've tried the onClick(View view) method  but  it didn't work .
        toUserGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserGames.class);
                startActivity(intent);
            }
        });

        games_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserGames.class);
                startActivity(intent);
            }
        });


        toUserRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserRatings.class);
                startActivity(intent);
            }
        });

        ratings_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserRatings.class);
                startActivity(intent);
            }
        });


        toUserFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserFollowing.class);
                startActivity(intent);
            }
        });

        followings_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserFollowing.class);
                startActivity(intent);
            }
        });

        toUserFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserFollowers.class);
                startActivity(intent);
            }
        });

        followers_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserFollowers.class);
                startActivity(intent);
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
        ArrayList<RecentActivityList> recentActivityLists=new ArrayList<RecentActivityList>();







        recentActivityLists.add(addRecentActivity("Played Rocket League","http://www.mobygames.com/images/covers/l/307552-rocket-league-playstation-4-front-cover.jpg","Category : PC Games","15 min ago"));
        recentActivityLists.add(addRecentActivity("Played Overwatch","https://b.thumbs.redditmedia.com/ksN39DPM7HFaXTP_tBi-IuYYfWccRBjYykD6VFSePXE.jpg","Category : PC Games","24 min ago"));
        recentActivityLists.add(addRecentActivity("Played Dying Light","http://shinigaming.com/wp-content/uploads/2015/01/dying-light-logo-high-resolution.jpg","Category : PC Games","5 hours ago"));
        recentActivityLists.add(addRecentActivity("Played Tekken 6","http://vignette2.wikia.nocookie.net/tekken/images/0/04/T6-logo-trophy.png/revision/latest?cb=20140330054519&path-prefix=en","Category : Playstation Games","12 hours ago"));
        recentActivityLists.add(addRecentActivity("Played Naruto Shippuden Ultimate Ninja Storm 4","http://1.bp.blogspot.com/-DYU2tIaTLYo/VlyFVdvpy1I/AAAAAAAADbo/zCxfuIm0YlM/s1600/Untitled.png","Category : Playstation Games","17" +
                " hours ago"));
        recentActivityLists.add(addRecentActivity("World of Warcraft","http://www.technologytell.com/gaming/files/2014/02/world-of-warcraft.png","Category : PC Games","1 day ago"));



        mAdapter = new RecentActivitiesAdapter(recentActivityLists);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }


    public RecentActivityList addRecentActivity(String gameName , String gamePhoto , String activityDescription , String activityDate)
    {
        RecentActivityList recentActivity = new RecentActivityList();
        recentActivity.setGameName(gameName);
        recentActivity.setGamePhotoURL(gamePhoto);
        recentActivity.setActivityDescription(activityDescription);
        recentActivity.setActivityDate(activityDate);
        return recentActivity;
    }



}