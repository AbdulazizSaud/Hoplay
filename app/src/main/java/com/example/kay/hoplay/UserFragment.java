package com.example.kay.hoplay;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        toUserFollowing = (TextView) view.findViewById(R.id.followers_profile_textview);
        games_number = (TextView) view.findViewById(R.id.games_number_textview);
        ratings_number = (TextView) view.findViewById(R.id.ratings_number_textview);
        followings_number = (TextView) view.findViewById(R.id.followeing_number_textview);
        followers_number = (TextView) view.findViewById(R.id.followers_number_textview);


        Typeface commelight = Typeface.createFromAsset(getActivity().getAssets() ,"commelight.ttf");
        usernameProfile.setTypeface(commelight);



        // these  methods used to jump to ( games , scores , following , followers ) activity .
        // I've tried the onClick(View view) method  but  it didn't work .
        toUserGames.setOnClickListener(new View.OnClickListener() {
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


        toUserFollowing.setOnClickListener(new View.OnClickListener() {
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



        return view;
    }



}