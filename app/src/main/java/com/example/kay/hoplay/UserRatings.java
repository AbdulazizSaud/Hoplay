package com.example.kay.hoplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class UserRatings extends AppCompatActivity {


    private ListView ratingsListview ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ratings);
      //  getSupportActionBar().hide();

        ratingsListview = (ListView) findViewById(R.id.ratings_listview);




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
