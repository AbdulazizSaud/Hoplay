package com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.RequestCore.LobbyFragmentCore;
import com.hoplay.kay.hoplay.CoresAbstract.MainAppMenu;
import com.hoplay.kay.hoplay.Fragments.NewRequestFragment;
import com.hoplay.kay.hoplay.Interfaces.Constants;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.Rank;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.hoplay.kay.hoplay.Adapters.SpinnerAdapter;
import com.hoplay.kay.hoplay.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Kay on 2/12/2017.
 */

public abstract class NewRequest extends AppCompatActivity implements Constants{


    /***************************************/

    // NOTE : Deal with spinners  here as Edittexts because it's not created natively by android studio it's a Dependency/Library.


    private boolean isRequest , isSaveRequest;
    private TextView makeRequestMessage;
    private Button makeRequestButton;
    private Button saveRequestButton;
    private MaterialBetterSpinner countrySpinner;
    private MaterialBetterSpinner numberOfPlayersSpinner;
    private MaterialBetterSpinner playersRanksSpinner;
    private MaterialBetterSpinner matchTypeSpinner;
    private RadioButton pcRadiobutton;
    private RadioButton psRadiobutton;
    private RadioButton xboxRadiobutton;
    private String selectedPlatform;
    private AutoCompleteTextView gamesAutoCompleteTextView;
    private EditText requestDescritopnEdittext;


    private EditText descriptionEdittext;
    protected ArrayList<String> gamesList;
    protected ArrayList<String> regionList;
    protected ArrayList<String> playerNumberList;
    protected ArrayList<String> ranksList;
    protected App app;
    protected ProgressDialog creatingRequestDialog;


    protected ArrayAdapter<String> gameAdapter;
    protected ArrayAdapter regionAdapter;
    protected ArrayAdapter playersNumberAdapter;
    protected ArrayAdapter playersRanksAdapter;
    private ArrayAdapter matchTypeAdapter;




    private Dialog gameProviderDialog;
    private boolean userEnteredGameProviderAcc ;
    private String pcGameProvider;







    /***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        // Set the screen orientation to the portrait mode :
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initControl();
        fieldsListeners();
        OnStartActivity();

        // Load  user games
        ArrayList<GameModel> games = app.getGameManager().getAllGamesArrayList();
        for (GameModel gameModel : games)
        {
            gamesList.add(gameModel.getGameName());
        }


    }

    private void initControl() {



        app = App.getInstance();

        final Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        makeRequestMessage = (TextView) findViewById(R.id.make_request_message_textview);
        makeRequestMessage.setTypeface(sansationbold);
        makeRequestButton = (Button) findViewById(R.id.request_button_new_request);
        makeRequestButton.setTypeface(sansationbold);
        countrySpinner = (MaterialBetterSpinner) findViewById(R.id.country_spinner_new_request);
        countrySpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));
        numberOfPlayersSpinner = (MaterialBetterSpinner) findViewById(R.id.players_number_spinner_new_request);
        countrySpinner.setTypeface(playbold);
        matchTypeSpinner = (MaterialBetterSpinner) findViewById(R.id.match_type_spinner_new_request);
        matchTypeSpinner.setTypeface(playbold);
        matchTypeSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));
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
        pcRadiobutton.setEnabled(true);
        psRadiobutton.setEnabled(true);
        xboxRadiobutton.setEnabled(true);
        gamesAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.games_autocompletetextview_new_request);
        gamesAutoCompleteTextView.setTypeface(playbold);

        descriptionEdittext = (EditText) findViewById(R.id.description_edittext_new_request);
        descriptionEdittext.setTypeface(playbold);

        // Set the default values
        selectedPlatform = "Nothing";

        isRequest = false ;
        isSaveRequest = false ;


        creatingRequestDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        creatingRequestDialog.setTitle(R.string.new_request_dialog_title);
        creatingRequestDialog.setMessage(NewRequest.this.getString(R.string.new_request_dialog_message));


        gamesList = new ArrayList<>();
        regionList = new ArrayList<>();
        playerNumberList = new ArrayList<>();
        ranksList = new ArrayList<>();


        gameAdapter = new ArrayAdapter<String>(this,
                R.layout.auto_complete_text_view_list_item, gamesList);

        regionAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.spinnner_item, regionList);


        playersNumberAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                playerNumberList);
        playersNumberAdapter.add("All Numbers");


        playersRanksAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                ranksList);
        playersRanksAdapter.add("All Ranks");


        makeRequestMessage.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(requestDescritopnEdittext, InputMethodManager.SHOW_IMPLICIT);


        // (this ,R.array.players_ranks,R.layout.spinnner_item);


        matchTypeAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
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




    // Remove keyboard when click anywhere :
    public void removeKeyboard(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }




    // Change icon listener for autocomplete and spinners
    private void fieldsListeners() {
        gamesAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                gamesAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    gamesAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_unfocused_24dp, 0, 0, 0);
                }
                matchTypeSpinner.setVisibility(View.GONE);
                playersRanksSpinner.setVisibility(View.GONE);


                // To clear previous selections
                numberOfPlayersSpinner.setText("");
                playersRanksSpinner.setText("");



                // load game standards
                numberOfPlayersSpinner.setVisibility(View.VISIBLE);

                // Load max players
                String gameKey = loadGameInformation(s.toString());

                if (checkIfCompetitive(gameKey)) {
                    matchTypeSpinner.setVisibility(View.VISIBLE);
                    slideInFromLeft(matchTypeSpinner);

                    // In case the user already selected a match type and he change the game
                    if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Competitive")) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp, 0, 0, 0);

                        if (playersRanksSpinner.getVisibility() != View.VISIBLE)
                            slideInFromLeft(playersRanksSpinner);

                        playersRanksSpinner.setVisibility(View.VISIBLE);
                    }
                    else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Quick Match")) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);

                        if (playersRanksSpinner.getVisibility() == View.VISIBLE)
                            slideOutToRight(playersRanksSpinner);

                        playersRanksSpinner.setVisibility(View.GONE);

                    }
                    else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("All Matches")){
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);

                        if (playersRanksSpinner.getVisibility() == View.VISIBLE)
                            slideOutToRight(playersRanksSpinner);

                        playersRanksSpinner.setVisibility(View.GONE);
                    }
                    if (matchTypeSpinner.length() == 0) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                    }

                } else {
                    if (matchTypeSpinner.isShown())
                        slideOutToRight(matchTypeSpinner);
                    slideOutToRight(playersRanksSpinner);
                    matchTypeSpinner.setVisibility(View.GONE);
                    playersRanksSpinner.setVisibility(View.GONE);

                }



                // Disable not supported platforms
                String gameName = s.toString();
                String  supportedPlatforms = "";
                try {
                    supportedPlatforms = app.getGameManager().getGameByName(gameName).getGamePlatforms();
                    if (!supportedPlatforms.contains("PS") && !supportedPlatforms.contains("XBOX"))
                    {
                        psRadiobutton.setEnabled(false);
                        xboxRadiobutton.setEnabled(false);
                        pcRadiobutton.setChecked(true);
                        selectedPlatform = "PC";
                    }
                    else if(!supportedPlatforms.contains("PS") && !supportedPlatforms.contains("PC")){
                        pcRadiobutton.setEnabled(false);
                        psRadiobutton.setEnabled(false);
                        xboxRadiobutton.setChecked(true);
                        selectedPlatform = "XBOX";
                    }
                    else if(!supportedPlatforms.contains("PC") && !supportedPlatforms.contains("XBOX")){
                        pcRadiobutton.setEnabled(false);
                        xboxRadiobutton.setEnabled(false);
                        psRadiobutton.setChecked(true);
                        selectedPlatform = "PS";
                    }
                    else if (!supportedPlatforms.contains("PS")){
                        psRadiobutton.setEnabled(false);
                        if (selectedPlatform.equalsIgnoreCase("PS")){
                            selectedPlatform="Nothing";
                        }
                    }
                    else if(!supportedPlatforms.contains("PC"))
                    {
                        pcRadiobutton.setEnabled(false);
                        if (selectedPlatform.equalsIgnoreCase("PC")){
                            selectedPlatform="Nothing";
                        }
                    }
                    else if (!supportedPlatforms.contains("XBOX")){
                        xboxRadiobutton.setEnabled(false);
                        if (selectedPlatform.equalsIgnoreCase("XBOX")){
                            selectedPlatform="Nothing";
                        }
                    }

                }catch (Exception e) {

                }




            }
        });

        gamesAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // Disable not supported platforms
                String gameName = gamesAutoCompleteTextView.getText().toString();
                String  supportedPlatforms = "";

                supportedPlatforms = app.getGameManager().getGameByName(gameName).getGamePlatforms();
                if (!supportedPlatforms.contains("PS") && !supportedPlatforms.contains("XBOX"))
                {
                    psRadiobutton.setEnabled(false);
                    xboxRadiobutton.setEnabled(false);
                    pcRadiobutton.setChecked(true);
                    selectedPlatform = "PC";
                }
                else if(!supportedPlatforms.contains("PS") && !supportedPlatforms.contains("PC")){
                    pcRadiobutton.setEnabled(false);
                    psRadiobutton.setEnabled(false);
                    xboxRadiobutton.setChecked(true);
                    selectedPlatform = "XBOX";
                }
                else if(!supportedPlatforms.contains("PC") && !supportedPlatforms.contains("XBOX")){
                    pcRadiobutton.setEnabled(false);
                    xboxRadiobutton.setEnabled(false);
                    psRadiobutton.setChecked(true);
                    selectedPlatform = "PS";
                }
                else if (!supportedPlatforms.contains("PS")){
                    psRadiobutton.setEnabled(false);
                    if (selectedPlatform.equalsIgnoreCase("PS")){
                        selectedPlatform="Nothing";
                    }
                }
                else if(!supportedPlatforms.contains("PC"))
                {
                    pcRadiobutton.setEnabled(false);
                    if (selectedPlatform.equalsIgnoreCase("PC")){
                        selectedPlatform="Nothing";
                    }
                }
                else if (!supportedPlatforms.contains("XBOX")){
                    xboxRadiobutton.setEnabled(false);
                    if (selectedPlatform.equalsIgnoreCase("XBOX")){
                        selectedPlatform="Nothing";
                    }
                }



                // Capitalize game name letters
                String capitlizedGameName = gameName.substring(0,1).toUpperCase() +  gameName.substring(1);
                if (gameName.contains(" "))
                {
                    // Capitalize game title letters
                    String cpWord= "";
                    for (int  i = 0 ; i < capitlizedGameName.length(); i++)
                    {
                        if (capitlizedGameName.charAt(i) == 32 && capitlizedGameName.charAt(i+1) != 32)
                        {
                            cpWord= capitlizedGameName.substring(i+1,i+2).toUpperCase() + capitlizedGameName.substring(i+2);
                            capitlizedGameName = capitlizedGameName.replace(capitlizedGameName.charAt(i+1),cpWord.charAt(0));
                        }
                    }
                    gamesAutoCompleteTextView.setText(capitlizedGameName);
                }else {
                    if (capitlizedGameName.equalsIgnoreCase("cs:go")){
                        gamesAutoCompleteTextView.setText(capitlizedGameName.toUpperCase());
                    } else
                        gamesAutoCompleteTextView.setText(capitlizedGameName);
                }



                numberOfPlayersSpinner.setVisibility(View.VISIBLE);

                String name = parent.getItemAtPosition(position).toString();

                // Load max players
                String gameKey = loadGameInformation(name);

                if (!gameKey.equals("")) {
                    if (checkIfCompetitive(gameKey)) {
                        matchTypeSpinner.setVisibility(View.VISIBLE);
                        slideInFromLeft(matchTypeSpinner);

                        // In case the user already selected a match type and he change the game
                        if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Competitive")) {
                            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp, 0, 0, 0);

                            if (playersRanksSpinner.getVisibility() != View.VISIBLE)
                                slideInFromLeft(playersRanksSpinner);

                            playersRanksSpinner.setVisibility(View.VISIBLE);
                        }
                        else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Quick Match")) {
                            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);

                            if (playersRanksSpinner.getVisibility() == View.VISIBLE)
                                slideOutToRight(playersRanksSpinner);

                            playersRanksSpinner.setVisibility(View.GONE);

                        }
                        else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("All Matches")){
                            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);

                            if (playersRanksSpinner.getVisibility() == View.VISIBLE)
                                slideOutToRight(playersRanksSpinner);

                            playersRanksSpinner.setVisibility(View.GONE);
                        }
                        if (matchTypeSpinner.length() == 0) {
                            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                        }

                    } else {
                        if (matchTypeSpinner.isShown())
                            slideOutToRight(matchTypeSpinner);
                        slideOutToRight(playersRanksSpinner);
                        matchTypeSpinner.setVisibility(View.GONE);
                        playersRanksSpinner.setVisibility(View.GONE);

                    }
                }





                // Next focus
                matchTypeSpinner.requestFocus();
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

                countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_unfocused_24dp, 0, 0, 0);

                }

//                numberOfPlayersSpinner.requestFocus();
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

                numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_unfocused_24dp, 0, 0, 0);

                }
                numberOfPlayersSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));


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

                playersRanksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    playersRanksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_unfocused_24dp, 0, 0, 0);
                }
                playersRanksSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));
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
                if (s.toString().equalsIgnoreCase("Competitive")) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp, 0, 0, 0);

                    if (playersRanksSpinner.getVisibility() != View.VISIBLE)
                        slideInFromLeft(playersRanksSpinner);

                    playersRanksSpinner.setVisibility(View.VISIBLE);
                }
                if (s.toString().equalsIgnoreCase("Quick Match")) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);

                    if (playersRanksSpinner.getVisibility() == View.VISIBLE)
                        slideOutToRight(playersRanksSpinner);

                    playersRanksSpinner.setVisibility(View.GONE);

                }
                if (s.toString().equalsIgnoreCase("All Matches")){
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);

                    if (playersRanksSpinner.getVisibility() == View.VISIBLE)
                        slideOutToRight(playersRanksSpinner);

                    playersRanksSpinner.setVisibility(View.GONE);
                }
                if (s.length() == 0) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                }

//                // Next Focus
//                countrySpinner.requestFocus();
            }



        });


    }


    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        animation.setDuration(200);
        viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }

    public void goBackToMainAppMenu(View v) {
        finish();
    }


    public void requestButtonListener(View view) {


        isRequest = true;

        // the selected platform selected in another method  : onPlatformSelecting
        String selectedGame = gamesAutoCompleteTextView.getText().toString().trim();
        String selectedMatchType = matchTypeSpinner.getText().toString().trim();
        String selectedRegion = countrySpinner.getText().toString().trim();
        String selectedPlayersNumber = numberOfPlayersSpinner.getText().toString().trim();
        String selectedRank = playersRanksSpinner.getText().toString().trim();
        String requestDescription = descriptionEdittext.getText().toString().trim();

        String pcGameProvider="";
        if (app.getGameManager().getGameByName(selectedGame) != null)
            pcGameProvider =  app.getGameManager().getGameByName(selectedGame).getPcGameProvider();


        if (selectedRank.length() == 0)
            selectedRank = "All Ranks";
        if (requestDescription.length() == 0)
            requestDescription = getString(R.string.new_request_default_description_message) + selectedGame +" ?";
        if (selectedMatchType.length() ==0 )
            selectedMatchType = "All Matches";
        if (selectedPlayersNumber.length()==0)
            selectedPlayersNumber="All Numbers";



        if (checkIsValidRequest()) {
            // Start the loading dialog
            //  creatingRequestDialog.show();
            // Take the user input for the request
            if (selectedPlatform.equalsIgnoreCase("PS") && !app.getUserInformation().getPSNAcc().equals("")){
                addRequestToFirebase(selectedPlatform,selectedGame,selectedMatchType,selectedRegion,selectedPlayersNumber,selectedRank,requestDescription);
                finishRequest();
            }
            else if (selectedPlatform.equalsIgnoreCase("XBOX") && !app.getUserInformation().getXboxLiveAcc().equals("")){
                addRequestToFirebase(selectedPlatform,selectedGame,selectedMatchType,selectedRegion,selectedPlayersNumber,selectedRank,requestDescription);
                finishRequest();
            }
            else if (selectedPlatform.equalsIgnoreCase("PC") && app.getUserInformation().getPcGamesAcc().get(pcGameProvider) !=null)
            {
                addRequestToFirebase(selectedPlatform,selectedGame,selectedMatchType,selectedRegion,selectedPlayersNumber,selectedRank,requestDescription);
                finishRequest();
            }

            else
                createGameProviderDialog(selectedPlatform, selectedGame, selectedMatchType, selectedRegion, selectedPlayersNumber, selectedRank, requestDescription);

        }

    }

    public void onPlatformSelecting(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.pc_choice_radiobutton:
                if (checked)
                    selectedPlatform = "PC";
                break;
            case R.id.ps_choice_radiobutton:
                if (checked)
                    selectedPlatform = "PS";
                break;
            case R.id.xbox_choice_radiobutton:
                if (checked)
                    selectedPlatform = "XBOX";
                break;
            default:
                selectedPlatform = "Nothing";
                break;
        }

    }

    private boolean checkIsValidRequest() {

        // check the platform
        if (selectedPlatform.equalsIgnoreCase("Nothing")) {

            Toast.makeText(getApplicationContext(), R.string.new_request_platform_error, Toast.LENGTH_LONG).show();
            return false;
        }

        // check if the game field is empty
        if (gamesAutoCompleteTextView.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.new_request_no_game_selected_error, Toast.LENGTH_LONG).show();
            return false;
        }

        // Check selected game is in the user games
        if (!userHasTheGame(gamesAutoCompleteTextView.getText().toString().trim())) {

            Toast.makeText(getApplicationContext(), R.string.new_request_game_error, Toast.LENGTH_LONG).show();
            return false;
        }

        // check region
        if (countrySpinner.getText().length()==0){
            Toast.makeText(getApplicationContext(), R.string.new_request_region_error, Toast.LENGTH_LONG).show();
            return false ;
        }


        return true;
    }


    private boolean userHasTheGame(String selectedGame) {
        return app.getGameManager().getGameByName(selectedGame) !=null;
    }
    protected boolean checkIfCompetitive(String gameKey) {
        return app.getGameManager().isCompetitive(gameKey);
    }

    private void finishRequest() {
        creatingRequestDialog.dismiss();
        finish();
        String msg = String.format(getResources().getString(R.string.new_request_finish_request_message), "");
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        isRequest = false;
    }

    private String loadGameInformation(String selectedGame) {


        final GameModel gameModel = app.getGameManager().getGameByName(selectedGame);



        playersNumberAdapter.clear();
        playersRanksAdapter.clear();

        if (gameModel!=null) {
            playersNumberAdapter.add("All Numbers");
            for (int i = 2; i <= gameModel.getMaxPlayers(); i++) {
                playersNumberAdapter.add(Integer.toString(i));
            }

            playersNumberAdapter.notifyDataSetChanged();





            playersRanksAdapter.add("All Ranks");
            for (Rank rank : gameModel.getGameRanks().getRanksList()) {
                playersRanksAdapter.add(rank.getRankName());
            }

            playersRanksAdapter.notifyDataSetChanged();
        }

        if (gameModel==null)
            return "";

        return gameModel.getGameID();
    }




//
//    private void updateAdapter(){
//
//
//        playerNumberList.clear();
//        ranksList.clear();
//
//
//        playersRanksSpinner.clearListSelection();
//        numberOfPlayersSpinner.clearListSelection();
//
//        playersNumberAdapter.clear();
//        playersRanksAdapter.clear();
//
//        playersNumberAdapter.notifyDataSetChanged();
//        playersRanksAdapter.notifyDataSetChanged();
//
//    }

    public void createGameProviderDialog(final String platform , final String game , final String match , final String region , final String playersNumber , final String rank ,final String description)
    {

        userEnteredGameProviderAcc = false;
        pcGameProvider ="";
        gameProviderDialog = new Dialog(this);
        gameProviderDialog.setCancelable(false);
        gameProviderDialog.setContentView(R.layout.provider_account_pop_up);
        gameProviderDialog.show();


        gameProviderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView gameProviderMessage;
        Button saveInfoButton;
        String providerAccountType = "";   // Xbox ID , PSN , PC (STEAM , Battlenet..etc)
        final EditText   gameProviderEdittext ;



        gameProviderEdittext = (EditText) gameProviderDialog.findViewById(R.id.game_provider_account_edittext);
        gameProviderMessage = (TextView) gameProviderDialog.findViewById(R.id.popup_message_textview_provide_account);
        saveInfoButton = (Button) gameProviderDialog.findViewById(R.id.save_game_provider_button);


        if (selectedPlatform.equalsIgnoreCase("PS"))
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), "PSN");
        else if (selectedPlatform.equalsIgnoreCase("XBOX"))
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), "Xbox Live");
        else if (selectedPlatform.equalsIgnoreCase("PC"))
        {
            pcGameProvider =  app.getGameManager().getGameByName(gamesAutoCompleteTextView.getText().toString()).getPcGameProvider();
            providerAccountType = String.format(getResources().getString(R.string.provider_account_message), pcGameProvider);
        }


        gameProviderMessage.setText(providerAccountType);

        Typeface sansation = Typeface.createFromAsset(getAssets() ,"sansationbold.ttf");
        saveInfoButton.setTypeface(sansation);

        final Typeface playbold = Typeface.createFromAsset(getAssets(), "playbold.ttf");
        final Typeface playReg = Typeface.createFromAsset(getAssets(), "playregular.ttf");
        gameProviderMessage.setTypeface(playbold);
        gameProviderMessage.setTypeface(playbold);
        gameProviderEdittext.setTypeface(playReg);




        // Changing edittext icon
        gameProviderEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                gameProviderEdittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mood_focused_24dp, 0);
                if (s.length() == 0) {
                    gameProviderEdittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mood_bad_not_focused_24dp, 0);
                }
            }
        });



        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the user entered the game provider id
                if (gameProviderEdittext.length() > 0 && isRequest)
                {
                    saveGameProviderAccount(pcGameProvider,gameProviderEdittext.getText().toString().trim(),selectedPlatform);
                    gameProviderDialog.dismiss();
                    addRequestToFirebase(platform,game,match,region,playersNumber,rank,description);
                    finishRequest();
                }

                else if (gameProviderEdittext.length() > 0 && isSaveRequest)
                {

                    saveGameProviderAccount(pcGameProvider,gameProviderEdittext.getText().toString().trim(),selectedPlatform);
                    gameProviderDialog.dismiss();
                    prepareSaveReq(game,match,region,playersNumber,rank,description);
                }
                else
                {
                    String noGameProviderMsg ="";
                    if (selectedPlatform.equalsIgnoreCase("PS"))
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), "PSN");
                    else if (selectedPlatform.equalsIgnoreCase("XBOX"))
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), "Xbox Live");
                    else if (selectedPlatform.equalsIgnoreCase("PC"))
                    {
                        String pcGameProvider =  app.getGameManager().getGameByName(gamesAutoCompleteTextView.getText().toString()).getPcGameProvider();
                        noGameProviderMsg = String.format(getResources().getString(R.string.new_request_dialog_no_game_provider_error), pcGameProvider);
                    }
                    Toast.makeText(getApplicationContext(),noGameProviderMsg,Toast.LENGTH_LONG).show();
                }



            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = gameProviderDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }



    private void prepareSaveReq(String selectedGame , String selectedMatchType , String selectedRegion , String selectedPlayersNumber , String selectedRank , String requestDescription)
    {


        int max = app.getUserInformation().isPremium() ? MAX_SAVED_REQUEST_PREMIUM : MAX_SAVED_REQUEST_REGULAR;

        if(app.getSavedRequests().size() >= max)
        {
            Toast.makeText(getApplicationContext(),"You cross the limit of requests",Toast.LENGTH_LONG).show();
            return;
        }

        GameModel gameModel = app.getGameManager().getGameByName(selectedGame);

        int numberPlayers = selectedPlayersNumber.equalsIgnoreCase("All Numbers") ? gameModel.getMaxPlayers():Integer.parseInt(selectedPlayersNumber);
        RequestModel requestModel = new RequestModel(selectedPlatform,selectedGame,app.getUserInformation().getUsername(),requestDescription,selectedRegion,numberPlayers,selectedMatchType,selectedRank);
        requestModel.setAdminName(app.getUserInformation().getUsername());
        requestModel.setAdmin(app.getUserInformation().getUID());
        requestModel.setGameId(gameModel.getGameID());
        requestModel.setRequestPicture(gameModel.getGamePhotoUrl());

        // Generate random unique id for saved request
        requestModel.setSavedReqUniqueID(UUID.randomUUID().toString());

        app.getSavedRequests().add(requestModel);
        addSaveRequestToFirebase();
        isSaveRequest = false;
        finish();
    }
    //

    public void saveRequestListener(View view)
    {

        isSaveRequest = true ;

        String selectedGame = gamesAutoCompleteTextView.getText().toString().trim();
        String selectedMatchType = matchTypeSpinner.getText().toString().trim();
        String selectedRegion = countrySpinner.getText().toString().trim();
        String selectedPlayersNumber = numberOfPlayersSpinner.getText().toString().trim();
        String selectedRank = playersRanksSpinner.getText().toString().trim();
        String requestDescription = descriptionEdittext.getText().toString().trim();

        String pcGameProvider="";
        if (app.getGameManager().getGameByName(selectedGame) != null)
            pcGameProvider =  app.getGameManager().getGameByName(selectedGame).getPcGameProvider();



        if (selectedRank.length() == 0)
            selectedRank = "All Ranks";
        if (requestDescription.length() == 0)
            requestDescription = getString(R.string.new_request_default_description_message) + selectedGame + " ?";
        if (selectedMatchType.length() ==0 )
            selectedMatchType = "All Matches";
        if (selectedPlayersNumber.length()==0)
            selectedPlayersNumber="All Numbers";


        //
        if (checkIsValidRequest())
        {

            if (selectedPlatform.equalsIgnoreCase("PS") && !app.getUserInformation().getPSNAcc().equals("")){
                prepareSaveReq( selectedGame ,  selectedMatchType ,  selectedRegion ,  selectedPlayersNumber ,  selectedRank , requestDescription);
            }
            else if (selectedPlatform.equalsIgnoreCase("XBOX") && !app.getUserInformation().getXboxLiveAcc().equals("")){
                prepareSaveReq( selectedGame ,  selectedMatchType ,  selectedRegion ,  selectedPlayersNumber ,  selectedRank , requestDescription);
            }
            else if (selectedPlatform.equalsIgnoreCase("PC") && app.getUserInformation().getPcGamesAcc().get(pcGameProvider) !=null)
            {
                Log.i("--->",selectedGame +" 1 "+  selectedMatchType +"2  "+  selectedRegion +"3 "+  selectedPlayersNumber +" 4 "+  selectedRank +" 5 "+ requestDescription+" 6 ");
                prepareSaveReq( selectedGame ,  selectedMatchType ,  selectedRegion ,  selectedPlayersNumber ,  selectedRank , requestDescription);
            }

            else
                createGameProviderDialog(selectedPlatform, selectedGame, selectedMatchType, selectedRegion, selectedPlayersNumber, selectedRank, requestDescription);

        }



    }













    protected abstract void OnStartActivity();
    protected abstract void saveGameProviderAccount(String gameProvider,String userGameProviderAcc , String platform );
    protected abstract void addSaveRequestToFirebase();

    // This method  take the request input from the user and insert it  into the database
    // It should take request model and pass it to the core
    protected abstract void addRequestToFirebase(String platform, String game, String matchType, String region, String numberOfPlayers, String rank, String description);

}


