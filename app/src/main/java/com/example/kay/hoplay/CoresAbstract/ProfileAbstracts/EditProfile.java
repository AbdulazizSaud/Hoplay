package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class EditProfile extends AppCompatActivity {


    TextView changeUserPhotoTextview;
    EditText userBioEdittext , gameProviderEdittext;
    MaterialBetterSpinner gamesProvidersSpinner;
    protected ArrayList<String> gamesProvidersList;
    protected ArrayAdapter<String> gamesProvidersAdapter;
    protected App app;
//    protected HashMap<String,String> pcGamesProviders;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        app = App.getInstance();

        OnStartActivtiy();


        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");



        changeUserPhotoTextview = (TextView) findViewById(R.id.change_profile_photo_textview);
        userBioEdittext = (EditText) findViewById(R.id.edit_user_bio_edittext);
        gameProviderEdittext = (EditText) findViewById(R.id.edit_profile_game_provider_edittext);
        gamesProvidersSpinner = (MaterialBetterSpinner) findViewById(R.id.edit_profile_game_providers_spinner);

        changeUserPhotoTextview.setTypeface(playbold);

        userBioEdittext.setTypeface(playregular);
        gameProviderEdittext.setTypeface(playregular);
        gamesProvidersSpinner.setTypeface(playregular);

        gamesProvidersList = new ArrayList<>();


        gamesProvidersAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                gamesProvidersList);



        gamesProvidersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        gamesProvidersAdapter.add("PSN Account");
        gamesProvidersAdapter.add("XBOX Account");
        Set<String> pcGamesProviders = app.getUserInformation().getPcGamesAcc().keySet();
            gamesProvidersAdapter.addAll(pcGamesProviders.toString());


        gamesProvidersSpinner.setAdapter(gamesProvidersAdapter);


    }




    protected abstract void OnStartActivtiy();
}
