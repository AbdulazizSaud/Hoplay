package com.example.kay.hoplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class UserFollowing extends AppCompatActivity {

   private ListView followingListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_following);
//        getSupportActionBar().hide();

        followingListview = (ListView) findViewById(R.id.following_listview);
    }



    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy() {super.onDestroy();}

    @Override
    protected void onStop() {super.onStop();}

    @Override
    protected void onStart(){super.onStart();}
}
