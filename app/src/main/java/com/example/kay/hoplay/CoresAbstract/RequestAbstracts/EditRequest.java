package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Fragments.NewRequestFragment;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.Rank;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.kay.hoplay.Interfaces.Constants.MAX_SAVED_REQUEST_PREMIUM;
import static com.example.kay.hoplay.Interfaces.Constants.MAX_SAVED_REQUEST_REGULAR;

public abstract class  EditRequest extends AppCompatActivity {




    private TextView editRequestTitle;
    private Button updateRequestButton;

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
    protected ProgressDialog updatigRequestDialog;


    protected ArrayAdapter<String> gameAdapter;
    protected ArrayAdapter regionAdapter;
    protected ArrayAdapter playersNumberAdapter;
    protected ArrayAdapter playersRanksAdapter;


    private RequestModel UpdatedRequest;
    protected int requestModelIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        initControl();
        fieldsListeners();
        OnStartActivity();


        Intent intent = getIntent();


        // Receiving saved request info
        UpdatedRequest = new RequestModel();
        Bundle bundle = intent.getExtras();


        if (bundle !=null)
        {
            UpdatedRequest = (RequestModel) bundle.getParcelable("savedReq");
            Log.e("===>",UpdatedRequest.getGameId());
        }


        // Load  user games
        ArrayList<GameModel> games = app.getGameManager().getAllGamesArrayList();
        for (GameModel gameModel : games)
        {
            gamesList.add(gameModel.getGameName());
        }

        // Load received saved req and fill the fields
        fillFieldsWithSavedReq(UpdatedRequest);



    }



    private void initControl() {



        app = App.getInstance();
        selectedPlatform = "Nothing";
        final Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        editRequestTitle = (TextView) findViewById(R.id.edit_request_title_textview);
        editRequestTitle.setTypeface(sansationbold);
        updateRequestButton = (Button) findViewById(R.id.update_edit_request_button);
        updateRequestButton.setTypeface(sansationbold);

        countrySpinner = (MaterialBetterSpinner) findViewById(R.id.country_spinner_edit_request);
        countrySpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));

        numberOfPlayersSpinner = (MaterialBetterSpinner) findViewById(R.id.players_number_spinner_edit_request);
        countrySpinner.setTypeface(playbold);

        matchTypeSpinner = (MaterialBetterSpinner) findViewById(R.id.match_type_spinner_edit_request);
        matchTypeSpinner.setTypeface(playbold);

        matchTypeSpinner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color));
        numberOfPlayersSpinner.setTypeface(playbold);

        playersRanksSpinner = (MaterialBetterSpinner) findViewById(R.id.players_rank_spinner_edit_request);
        playersRanksSpinner.setTypeface(playbold);

        pcRadiobutton = (RadioButton) findViewById(R.id.edit_request_pc_choice_radiobutton);
        pcRadiobutton.setTypeface(sansationbold);

        psRadiobutton = (RadioButton) findViewById(R.id.edit_request_ps_choice_radiobutton);
        psRadiobutton.setTypeface(sansationbold);

        xboxRadiobutton = (RadioButton) findViewById(R.id.edit_request_xbox_choice_radiobutton);
        xboxRadiobutton.setTypeface(sansationbold);

        gamesAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.games_autocompletetextview_edit_request);
        gamesAutoCompleteTextView.setTypeface(playbold);

        descriptionEdittext = (EditText) findViewById(R.id.description_edittext_edit_request);
        descriptionEdittext.setTypeface(playbold);



        updatigRequestDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        updatigRequestDialog.setTitle(R.string.edit_request_dialog_title);
        updatigRequestDialog.setMessage(EditRequest.this.getString(R.string.edit_request_dialog_message));


        gamesList = new ArrayList<>();
        regionList = new ArrayList<>();
        playerNumberList = new ArrayList<>();
        ranksList = new ArrayList<>();


        gameAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, gamesList);

        regionAdapter = new SpinnerAdapter(getApplicationContext(),
                R.layout.spinnner_item, regionList);


        playersNumberAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                playerNumberList);
        playersNumberAdapter.add("All Numbers");


        playersRanksAdapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinnner_item,
                ranksList);
        playersRanksAdapter.add("All Ranks");


        editRequestTitle.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(requestDescritopnEdittext, InputMethodManager.SHOW_IMPLICIT);


        // (this ,R.array.players_ranks,R.layout.spinnner_item);


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



        // Valid designs - when load
        if (matchTypeSpinner.toString().equalsIgnoreCase("Competitive")) {
            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp, 0, 0, 0);
            slideInFromLeft(playersRanksSpinner);
            playersRanksSpinner.setVisibility(View.VISIBLE);
        }
        if (matchTypeSpinner.toString().equalsIgnoreCase("Quick Match")) {
            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);
            slideOutToRight(playersRanksSpinner);
            playersRanksSpinner.setVisibility(View.GONE);

        }
        if (matchTypeSpinner.toString().equalsIgnoreCase("All Matches")){
            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
            slideOutToRight(playersRanksSpinner);
            playersRanksSpinner.setVisibility(View.GONE);
        }
        if (matchTypeSpinner.length() == 0) {
            matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
        }


    }




    private void fillFieldsWithSavedReq(RequestModel requestModel)
    {
        // fill platform :
        if (requestModel.getPlatform().equalsIgnoreCase("PC"))
        {
            selectedPlatform="PC";
            pcRadiobutton.setChecked(true);
        }
        else if (requestModel.getPlatform().equalsIgnoreCase("PS"))
        {
            selectedPlatform="PS";
            psRadiobutton.setChecked(true);
        }
        else{
            selectedPlatform="XBOX";
            xboxRadiobutton.setChecked(true);
        }



       try{
           // fill Game field
           gamesAutoCompleteTextView.setText(app.getGameManager().getGameById(requestModel.getGameId()).getGameName());

           // fill match type spinner
           if (requestModel.getMatchType().equalsIgnoreCase("Competitive"))
           {
               matchTypeSpinner.setText("Competitive");
           }
           else {
               matchTypeSpinner.setText("Quick Match");
           }

           // fill country
           countrySpinner.setText(requestModel.getRegion());


           // fil players number
           numberOfPlayersSpinner.setText(String.valueOf(requestModel.getPlayerNumber()));

           // fill rank
           playersRanksSpinner.setText(requestModel.getRank());

           //fill description
           descriptionEdittext.setText(requestModel.getDescription());
       }catch (Exception e){

       }


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


                // Capitalize game name letters
                String gameName = gamesAutoCompleteTextView.getText().toString();
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

                if (!gameKey.equals(""))
                {
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

                // Disable not supported platforms
                 gameName = gamesAutoCompleteTextView.getText().toString();
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

                // next focus
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

                // Next Focus
//                descriptionEdittext.requestFocus();

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


                // next focus
//                countrySpinner.requestFocus();
            }
        });


    }


    private String loadGameInformation(String selectedGame) {


        final GameModel gameModel = app.getGameManager().getGameByName(selectedGame);

        playersNumberAdapter.clear();
        playersRanksAdapter.clear();

        if (gameModel!=null)
        {
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

    protected boolean checkIfCompetitive(String gameKey) {
        return app.getGameManager().isCompetitive(gameKey);
    }


    public void onPlatformSelectingEditReq(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.edit_request_pc_choice_radiobutton:
                if (checked)
                    selectedPlatform = "PC";
                break;
            case R.id.edit_request_ps_choice_radiobutton:
                if (checked)
                    selectedPlatform = "PS";
                break;
            case R.id.edit_request_xbox_choice_radiobutton:
                if (checked)
                    selectedPlatform = "Xbox";
                break;
            default:
                selectedPlatform = "Nothing";
                break;
        }

    }


    private boolean userHasTheGame(String selectedGame) {
        return app.getGameManager().getGameByName(selectedGame) !=null;
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

//
//        // check selected match type
//        String gameKey = app.getGameManager().getGameByName(gamesAutoCompleteTextView.getText().toString().trim()).getGameID();
//        if (matchTypeSpinner.getText().length() == 0 && checkIfCompetitive(gameKey) ) {
//            Toast.makeText(getApplicationContext(), R.string.new_request_type_match_error, Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//
//        // check number of players
//        if (numberOfPlayersSpinner.getText().length() == 0) {
//            Toast.makeText(getApplicationContext(), R.string.new_request_players_number_error, Toast.LENGTH_LONG).show();
//            return false;
//        }


        return true;
    }


    private void updateRequest(RequestModel requestModel)
    {
        updateReqFirebase(requestModel);
        finish();
    }


    public void updateRequestListener(View view)
    {


        String selectedGame = gamesAutoCompleteTextView.getText().toString().trim();
        String selectedMatchType = matchTypeSpinner.getText().toString().trim();
        String selectedRegion = countrySpinner.getText().toString().trim();
        String selectedPlayersNumber = numberOfPlayersSpinner.getText().toString().trim();
        String selectedRank = playersRanksSpinner.getText().toString().trim();
        String requestDescription = descriptionEdittext.getText().toString().trim();
        String chosenPlatform = selectedPlatform;



        if (selectedRank.length() == 0)
            selectedRank = "All Ranks";
        if (requestDescription.length() == 0)
            requestDescription = R.string.new_request_default_description_message + selectedGame +" ?";
        if (selectedMatchType.length() ==0 )
            selectedMatchType = "All Matches";
        if (selectedPlayersNumber.length()==0)
            selectedPlayersNumber="All Numbers";



        if (checkIsValidRequest())
        {
            UpdatedRequest.setGameId(app.getGameManager().getGameByName(selectedGame.trim()).getGameID());
            UpdatedRequest.setMatchType(selectedMatchType);
            UpdatedRequest.setRegion(selectedRegion);
            UpdatedRequest.setRequestTitle(selectedGame);
            UpdatedRequest.setPlatform(chosenPlatform);
            UpdatedRequest.setRequestPicture(app.getGameManager().getGameByName(selectedGame.trim()).getGamePhotoUrl());


            GameModel gameModel = app.getGameManager().getGameByName(selectedGame);
            int numberPlayers = selectedPlayersNumber.equals("All Numbers") ? gameModel.getMaxPlayers():Integer.parseInt(selectedPlayersNumber);

            UpdatedRequest.setPlayerNumber(numberPlayers);
            UpdatedRequest.setRank(selectedRank);
            UpdatedRequest.setDescription(requestDescription);
            updateRequest(UpdatedRequest);
        }

    }



    public void goBackToNewRequestFragment(View v){
        finish();
    }


    // Abstract Methods :
    protected abstract void OnStartActivity();
    protected abstract void updateReqFirebase(RequestModel requestModel);

}
