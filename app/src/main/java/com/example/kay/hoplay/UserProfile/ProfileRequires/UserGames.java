package com.example.kay.hoplay.UserProfile.ProfileRequires;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.kay.hoplay.R;

public class UserGames extends AppCompatActivity {

    private ListView gamesListview ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_games);
      //  getSupportActionBar().hide();




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
