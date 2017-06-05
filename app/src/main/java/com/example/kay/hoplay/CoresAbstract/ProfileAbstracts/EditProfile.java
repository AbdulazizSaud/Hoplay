package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class EditProfile extends AppCompatActivity {


    TextView changeUserPhotoTextview;
    EditText userBioEdittext , gameProviderEdittext;
    MaterialBetterSpinner gamesProvidersSpinner;
    protected ArrayList<String> gamesProvidersList;
    protected ArrayAdapter<String> gamesProvidersAdapter;
    protected App app;
    Button updateButton;
//    protected HashMap<String,String> pcGamesProviders;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        app = App.getInstance();

        OnStartActivtiy();


        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        Typeface sansation = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");


        changeUserPhotoTextview = (TextView) findViewById(R.id.change_profile_photo_textview);
        userBioEdittext = (EditText) findViewById(R.id.edit_user_bio_edittext);
        gameProviderEdittext = (EditText) findViewById(R.id.edit_profile_game_provider_edittext);
        gamesProvidersSpinner = (MaterialBetterSpinner) findViewById(R.id.edit_profile_game_providers_spinner);
        updateButton = (Button) findViewById(R.id.update_edit_profile_button);

        changeUserPhotoTextview.setTypeface(playbold);

        userBioEdittext.setTypeface(playregular);
        gameProviderEdittext.setTypeface(playregular);
        gamesProvidersSpinner.setTypeface(playregular);
        updateButton.setTypeface(sansation);

        gamesProvidersSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));


        gamesProvidersList = new ArrayList<>();


        gamesProvidersAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                gamesProvidersList);



        gamesProvidersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        gamesProvidersAdapter.add("PSN Account");
        gamesProvidersAdapter.add("XBOX Account");
        Set<String> pcGamesProvidersSet = app.getUserInformation().getPcGamesAcc().keySet();

        List<String> pcGamesProvidersList = new ArrayList<String>(pcGamesProvidersSet);


        if (!pcGamesProvidersList.isEmpty())
        {

            for (int i = 0 ; i < pcGamesProvidersList.size();i++)
            gamesProvidersAdapter.add(pcGamesProvidersList.get(i));
        }




        gamesProvidersSpinner.setAdapter(gamesProvidersAdapter);


    }




    protected abstract void OnStartActivtiy();
}
