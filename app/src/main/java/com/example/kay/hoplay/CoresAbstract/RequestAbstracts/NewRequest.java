package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.CoresAbstract.AuthenticationAbstracts.Login;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.RequestModel;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.description;
import static android.R.attr.scaleHeight;
import static android.R.attr.scaleWidth;

/**
 * Created by Kay on 2/12/2017.
 */

public abstract class NewRequest extends AppCompatActivity {


    /***************************************/

    // NOTE : Deal with spinners  here as Edittexts because it's not created natively by android studio it's a Dependency/Library.


    private TextView makeRequestMessage;
    private Button makeRequestButton;
    private Button saveRequestButton;
    private MaterialBetterSpinner countrySpinner;
    private MaterialBetterSpinner numberOfPlayersSpinner;
    private MaterialBetterSpinner playersRanksSpinner;
    private MaterialBetterSpinner matchTypeSpinner ;
    private RadioButton pcRadiobutton;
    private RadioButton psRadiobutton;
    private RadioButton xboxRadiobutton;
    private String selectedPlatform;
    private AutoCompleteTextView gamesAutoCompleteTextView;
    private EditText requestDescritopnEdittext;
    private CheckedTextView dropDownSpinnerItem;
    private TextView spinnerItem;
    private  String[] gamesArray;
    private  int layoutItemId;
    private EditText descriptionEdittext;
    protected List<String> gamesList;
    protected List<String> regionList;
    protected List<String> playerNumberList ;
    protected List<String>  ranksList ;
    protected App app;
    protected ProgressDialog creatingRequestDialog;


//    private   ArrayAdapter<String> gamesAdapter;

    /***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        initControl();
        fieldsListeners();
        loadstandards();








    }

    private void initControl() {
        app = App.getInstance();

        selectedPlatform="Nothing";
        final Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        makeRequestMessage = (TextView) findViewById(R.id.make_request_message_textview);
        makeRequestMessage.setTypeface(sansationbold);
        makeRequestButton = (Button) findViewById(R.id.request_button_new_request);
        makeRequestButton.setTypeface(sansationbold);
        countrySpinner = (MaterialBetterSpinner) findViewById(R.id.country_spinner_new_request);
        countrySpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));
        numberOfPlayersSpinner = (MaterialBetterSpinner) findViewById(R.id.players_number_spinner_new_request);
        countrySpinner.setTypeface(playbold);
        matchTypeSpinner = (MaterialBetterSpinner) findViewById(R.id.match_type_spinner_new_request);
        matchTypeSpinner.setTypeface(playbold);
        matchTypeSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));
        numberOfPlayersSpinner.setTypeface(playbold);
        playersRanksSpinner = (MaterialBetterSpinner) findViewById(R.id.players_rank_spinner_new_request);
        playersRanksSpinner.setTypeface(playbold);
        saveRequestButton = (Button) findViewById(R.id.save_request_button_new_request);
        saveRequestButton.setTypeface(sansationbold);
        pcRadiobutton = (RadioButton) findViewById(R.id.pc_choice_radiobutton);
        pcRadiobutton.setTypeface(sansationbold);
        psRadiobutton = (RadioButton) findViewById(R.id.ps_choice_radiobutton);
        psRadiobutton.setTypeface(sansationbold);
        xboxRadiobutton = (RadioButton) findViewById(R.id.xbox_choice_radiobutton);
        xboxRadiobutton.setTypeface(sansationbold);
        gamesAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.games_autocompletetextview_new_request);
        gamesAutoCompleteTextView.setTypeface(playbold);

        descriptionEdittext = (EditText) findViewById(R.id.description_edittext_new_request);
        descriptionEdittext.setTypeface(playbold);

        creatingRequestDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        creatingRequestDialog.setTitle(R.string.new_request_dialog_title);
        creatingRequestDialog.setMessage(NewRequest.this.getString(R.string.new_request_dialog_message));



   //     gamesArray = getResources().getStringArray(R.array.games_list);
    //    layoutItemId = android.R.layout.simple_dropdown_item_1line;
       // gamesArray = getResources().getStringArray(R.array.games_list);
//        gamesList = Arrays.asList(gamesArray);

        gamesList = new ArrayList<String>();
        regionList = new ArrayList<String>();
        playerNumberList = new ArrayList<String>();
        ranksList = new ArrayList<String>();
//        gamesList.add("Item1");
//        gamesList.add("Item2");
//        gamesList.add("Item3");


        ArrayAdapter<String> gameAdapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,gamesList);

        ArrayAdapter regionAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.spinnner_item, regionList);

        ArrayAdapter playersNumberAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
             playerNumberList);


        ArrayAdapter playersRanksAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                ranksList);




//        gamesAdapter = new ArrayAdapter<>(this, layoutItemId, gamesList);




//        requestDescritopnEdittext = (EditText) findViewById(R.id.request_descritption_edittext);

        makeRequestMessage.requestFocus();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(requestDescritopnEdittext, InputMethodManager.SHOW_IMPLICIT);


        // (this ,R.array.players_ranks,R.layout.spinnner_item);




//
//

//


        ArrayAdapter matchTypeAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.match_types)));





        gameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersRanksAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersNumberAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        matchTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//
//

        gamesAutoCompleteTextView.setThreshold(1);
        gamesAutoCompleteTextView.setAdapter(gameAdapter);
        numberOfPlayersSpinner.setAdapter(playersNumberAdapter);
            countrySpinner.setAdapter(regionAdapter);
        playersRanksSpinner.setAdapter(playersRanksAdapter);
        matchTypeSpinner.setAdapter(matchTypeAdapter);



    }


    // Change icon listener for autocomplete and spinners
    private void fieldsListeners()
    {
        gamesAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                gamesAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_focused_24dp , 0, 0, 0);
                if(s.length() == 0)
                {
                    gamesAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_unfocused_24dp , 0, 0, 0);
                }
                if (checkIfCompetitive(s.toString()))
                {
                    matchTypeSpinner.setVisibility(View.VISIBLE);
                    slideInFromLeft(matchTypeSpinner);

                }
                else
                {
                    if (matchTypeSpinner.isShown())
                    slideOutToRight(matchTypeSpinner);

                    matchTypeSpinner.setVisibility(View.GONE);

                }

                // Load max players
                loadMaxPlayers(s.toString());



            }
        });
        countrySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_focused_24dp , 0, 0, 0);
                if(s.length() ==0 )
                {
                    countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_unfocused_24dp , 0, 0, 0);

                }



            }
        });

        numberOfPlayersSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_focused_24dp , 0, 0, 0);
                if(s.length() == 0 )
                {
                    numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_unfocused_24dp , 0, 0, 0);

                }
                numberOfPlayersSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));


            }
        });

        playersRanksSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                playersRanksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_focused_24dp , 0, 0, 0);
                if(s.length() ==0)
                {
                    playersRanksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_unfocused_24dp , 0, 0, 0);
                }
                playersRanksSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_color));
            }
        });

        matchTypeSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("Competitive"))
                {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp , 0, 0, 0);
                }
                if (s.toString().equalsIgnoreCase("Quick Match"))
                {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp , 0, 0, 0);
                }
                if (s.length() == 0 )
                {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp , 0, 0, 0);
                }
            }
        });


    }


    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            animation.setDuration(200);
            viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }

    public void goBackToMainAppMenu(View v)
    {finish();}


    public void requestButtonListener(View view)
    {



        // the selected platform selected in another method  : onPlatformSelecting
        String selectedGame = gamesAutoCompleteTextView.getText().toString().trim();
        String selectedMatchType = matchTypeSpinner.getText().toString().trim();
        String selectedRegion = countrySpinner.getText().toString().trim();
        String selectedPlayersNumber = numberOfPlayersSpinner.getText().toString().trim();
        String selectedRank = playersRanksSpinner.getText().toString().trim();
        String requestDescription = descriptionEdittext.getText().toString().trim();



        if (selectedRegion.length() == 0)
            selectedRegion="All";
        if (selectedRank.length() ==0)
            selectedRank="All";
        if (requestDescription.length()==0)
            requestDescription = R.string.new_request_default_description_message+selectedGame;





        if (checkIsValidRequest())
        {
            // Start the loading dialog
            creatingRequestDialog.show();
            // Take the user input for the request
            requestInput(selectedPlatform,selectedGame,selectedMatchType , selectedRegion, selectedPlayersNumber , selectedRank , requestDescription);
            finishRequest();
        }

    }
    public   void  onPlatformSelecting(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.pc_choice_radiobutton:
                if (checked)
                    selectedPlatform="PC";
                    break;
            case R.id.ps_choice_radiobutton:
                if (checked)
                    selectedPlatform="PS";
                    break;
            case R.id.xbox_choice_radiobutton:
                if (checked)
                   selectedPlatform="Xbox";
                    break;
            default:
                selectedPlatform="Nothing";
                break;
        }

    }

    private boolean checkIsValidRequest()
    {

        // check the platform
        if (selectedPlatform.equalsIgnoreCase("Nothing"))
        {

            Toast.makeText(getApplicationContext(),R.string.new_request_platform_error,Toast.LENGTH_LONG).show();
            return false;
        }

        // check if the game field is empty
        if (gamesAutoCompleteTextView.getText().toString().length() == 0 )
        {
            Toast.makeText(getApplicationContext(),R.string.new_request_no_game_selected_error , Toast.LENGTH_LONG).show();
            return false;
        }

        // Check selected game is in the user games
        if (!userHasTheGameThenLoadStandards(gamesAutoCompleteTextView.getText().toString().trim()))
        {
            Toast.makeText(getApplicationContext(),R.string.new_request_game_error,Toast.LENGTH_LONG).show();
            return false;
        }



        // check selected match type
        if (matchTypeSpinner.getText().length() == 0)
        {
            Toast.makeText(getApplicationContext(),R.string.new_request_type_match_error, Toast.LENGTH_LONG).show();
            return false;
        }



        // check number of players
        if (numberOfPlayersSpinner.getText().length() == 0)
        {
            Toast.makeText(getApplicationContext(),R.string.new_request_players_number_error, Toast.LENGTH_LONG).show();
            return false;
        }



        return true ;
    }


    // this method check if the user has the game if yes , load game properties
    private boolean userHasTheGameThenLoadStandards(String selectedGame)
    {
        ArrayList<GameModel> userGames = app.getGameManager().getAllGames();
        for (GameModel gameModel : userGames)
            if (gameModel.getGameName().trim().equalsIgnoreCase(selectedGame))
            {
                return true;
            }


        return false;

    }

    private void finishRequest()
    {
        creatingRequestDialog.dismiss();
        finish();
        String msg = String.format(getResources().getString(R.string.new_request_finish_request_message), "");
        Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();
    }

    protected boolean checkIfCompetitive(String selectedGame)
    {

        ArrayList<GameModel> userCompetitiveGames = app.getGameManager().getCompetitiveGames();
        for (GameModel gameModel : userCompetitiveGames)
        {
            if (gameModel.getGameName().trim().equalsIgnoreCase(selectedGame))
                return true;
        }

       return false;
    }


    // This method  load number of  max players of the selected game

    protected abstract void OnStartActivity();

    // This  method  loads requests standards : user games , region
    protected abstract void loadstandards();

    // This method  take the request input from the user and insert it  into the database
    // It should take request model and pass it to the core
    protected abstract void requestInput(String platform , String game, String matchType , String region , String numberOfPlayers , String rank , String description);

    private void loadMaxPlayers(String selectedGame)
    {
        ArrayList<String> gameRanks  = new ArrayList<String>();
        int maxPlayer = 0 ;
        ArrayList<GameModel> userGames = app.getGameManager().getAllGames();
        for (GameModel gameModel : userGames) {
            if (gameModel.getGameName().equalsIgnoreCase(selectedGame))
            {
                maxPlayer = gameModel.getMaxPlayers();
                gameRanks = gameModel.getGameRanks();
            }


        }


        for (int i = 1 ; i <= maxPlayer ; i++)
        {
            playerNumberList.add(Integer.toString(i));
        }

        for (int i = 0 ; i < gameRanks.size() ; i++)
        {
            ranksList.add(gameRanks.get(i));

        }




    }
}
