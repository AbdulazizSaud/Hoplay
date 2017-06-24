package com.example.kay.hoplay.CoresAbstract.ProfileAbstracts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public abstract class EditProfile extends AppCompatActivity {

    private final  int RESULT_LOAD_IMG=1;

    private TextView changeUserPhotoTextview;
    private EditText userBioEdittext , gameProviderEdittext;
    private  MaterialBetterSpinner gamesProvidersSpinner;
    private ImageView closeButton;
    private Button saveButton;
    private CircleImageView userPictureCircleImageView;

    protected ArrayList<String> gamesProvidersList;
    protected ArrayAdapter<String> gamesProvidersAdapter;
    protected App app;

    protected String oldBio;
    protected HashMap<String,String> newData = new HashMap<>();

    protected int currentPosition =-1;


    protected final String PROVIDOR_PS = "PSN Account";
    protected final String PROVIDOR_XBOX = "XBOX Account";
    protected final String PROVIDOR_PC = "PC Account:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        app = App.getInstance();

        currentPosition=-1;

        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");



        changeUserPhotoTextview = (TextView) findViewById(R.id.change_profile_photo_textview);
        userBioEdittext = (EditText) findViewById(R.id.edit_user_bio_edittext);
        gameProviderEdittext = (EditText) findViewById(R.id.edit_profile_game_provider_edittext);
        gamesProvidersSpinner = (MaterialBetterSpinner) findViewById(R.id.edit_profile_game_providers_spinner);
        closeButton = (ImageView) findViewById(R.id.go_back_to_preferences_from_edit_profile);
        saveButton = (Button) findViewById(R.id.update_edit_profile_button);
        userPictureCircleImageView = (CircleImageView) findViewById(R.id.user_profile_photo_circleimageview);

        changeUserPhotoTextview.setTypeface(playbold);
        userBioEdittext.setTypeface(playregular);
        gameProviderEdittext.setTypeface(playregular);
        gamesProvidersSpinner.setTypeface(playregular);
        saveButton.setTypeface(playregular);


        gamesProvidersSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));

        gamesProvidersList = new ArrayList<>();
        gamesProvidersAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                gamesProvidersList);



        gamesProvidersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        gamesProvidersAdapter.add(PROVIDOR_PS);
        newData.put(PROVIDOR_PS,app.getUserInformation().getPSNAcc());

        gamesProvidersAdapter.add(PROVIDOR_XBOX);
        newData.put(PROVIDOR_XBOX,app.getUserInformation().getXboxLiveAcc());

        ArrayList<String> pcGamesProvidersList = new ArrayList<String>(app.getUserInformation().getPcGamesAcc().keySet());

        if (!pcGamesProvidersList.isEmpty())
        {
            for (int i = 0 ; i < pcGamesProvidersList.size();i++){
                String providor = PROVIDOR_PC+pcGamesProvidersList.get(i);
                newData.put(providor,app.getUserInformation().getPcGamesAcc().get(providor.replace(PROVIDOR_PC,"")));
                gamesProvidersAdapter.add(providor);

            }
        }

        gamesProvidersSpinner.setAdapter(gamesProvidersAdapter);

        gamesProvidersSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String providor = gamesProvidersAdapter.getItem(position);
                gameProviderEdittext.setText(newData.get(providor));
                currentPosition = position;
            }
        });

        userPictureCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Change Profile Picture")
                        .setPositiveButton("Change Picture", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (isAllowedPremmision()) {

                                    userPictureCircleImageView.setClickable(false);
                                    setSaveButtonClickAble(false);

                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    // Start the Intent
                                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                                }
                            }


                        }).setNegativeButton("Change Cover",null)
                        .show();


            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInfo();
                finish();
            }
        });

        setUserProfileInformation();
        OnStartActivity();


    }

    private boolean isAllowedPremmision() {
        int permissionCheck = ContextCompat.checkSelfPermission(EditProfile.this,READ_EXTERNAL_STORAGE);

        if ( permissionCheck != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this,READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            ActivityCompat.requestPermissions(EditProfile.this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    0);

        } else return true;

        return false;
    }


    private synchronized void setUserProfileInformation() {

        oldBio = app.getUserInformation().getBio();

        userBioEdittext.setText(app.getUserInformation().getBio());
        app.loadingImage(userPictureCircleImageView,app.getUserInformation().getPictureURL());


    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                Bitmap imgBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);

                userPictureCircleImageView.setImageBitmap(imgBitmap);

                uploadPicture(userPictureCircleImageView);

            } else {

                userPictureCircleImageView.setClickable(true);
                setSaveButtonClickAble(true);

                Toast.makeText(getApplicationContext(), "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

            userPictureCircleImageView.setClickable(true);
            setSaveButtonClickAble(true);

            Toast.makeText(getApplicationContext(), "Something went wrong" , Toast.LENGTH_LONG)
                    .show();
            Log.e("--->",e.getLocalizedMessage() );
        }

    }



    protected String getBioText()
    {
        return userBioEdittext.getText().toString().trim();
    }

    protected String getProvidorText(){
        return gameProviderEdittext.getText().toString().trim();
    }

    protected void setSaveButtonClickAble(boolean b)
    {
        saveButton.setClickable(b);
    }

    protected abstract void OnStartActivity();
    protected abstract void saveProfileInfo();
    protected abstract void uploadPicture(CircleImageView circleImageView);
}