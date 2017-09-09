package com.hoplay.kay.hoplay.CoresAbstract.RequestAbstracts;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoplay.kay.hoplay.Adapters.SpinnerAdapter;
import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.RequestCore.SearchResultsCore;
import com.hoplay.kay.hoplay.Models.GameModel;
import com.hoplay.kay.hoplay.Models.Rank;
import com.hoplay.kay.hoplay.Models.RequestModel;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.util.BitmapOptimizer;
import com.google.firebase.database.ServerValue;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public abstract class SearchRequests extends Fragment {

    protected static App app;

    protected ImageView pcChoice;
    protected ImageView psChoice;
    protected ImageView xboxChoice;

    protected TextView searchGameMessage;
    protected TextView pcMessage;
    protected TextView psMessage;
    protected TextView xboxMessage;
    protected static AutoCompleteTextView searchGameAutoCompleteTextView;

    private int layoutItemId;


    private RelativeLayout activityLayout;

    protected ArrayList<String> gamesList;
    protected ArrayList<String> regionList;
    protected ArrayList<String> playerNumberList;
    protected ArrayList<String> ranksList;


    protected ArrayAdapter regionAdapter;
    protected ArrayAdapter playersNumberAdapter;
    protected ArrayAdapter playersRanksAdapter;


    protected MaterialBetterSpinner countrySpinner;
    protected MaterialBetterSpinner ranksSpinner;
    protected MaterialBetterSpinner numberOfPlayersSpinner;
    private MaterialBetterSpinner matchTypeSpinner;

    protected Button searchRequestButton;

    private BitmapOptimizer bitmapOptimizer;

    private String currentPlatform=null;
    boolean pcIsChosen, psIsChosen, xboxIsChosen;

    private Bitmap pcLogoUnPressed, pcLogoPressed, psLogoUnPressed, psLogoPressed, xboxLogoUnPressed, xboxLogoPressed;


    public SearchRequests() {
        // Required empty public constructor
        bitmapOptimizer = new BitmapOptimizer();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = App.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests_search, container, false);




        // Hide keyboard when click anywhre on fragment
        activityLayout = (RelativeLayout) view.findViewById(R.id.search_request_fragment_relativelayout);
        activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        initControls(view);
        changePlatformColors();
        firledsListeners();
        OnStartActivity();





        return view;
    }


    private void setBitmapLogo(Bitmap pcLogo, Bitmap psLogo, Bitmap ps4Logo) {
        pcChoice.setImageBitmap(pcLogo);
        psChoice.setImageBitmap(psLogo);
        xboxChoice.setImageBitmap(ps4Logo);

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





    // Init all controls
    private void initControls(View view) {

        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        pcChoice = (ImageView) view.findViewById(R.id.pc_choice_imageview);
        psChoice = (ImageView) view.findViewById(R.id.ps_choice_imageview);
        xboxChoice = (ImageView) view.findViewById(R.id.xbox_choice_imageview);


        layoutItemId = android.R.layout.simple_dropdown_item_1line;

        searchGameAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.games_autocompletetextview_search_request);


        searchGameMessage = (TextView) view.findViewById(R.id.search_game_message_textview);
        countrySpinner = (MaterialBetterSpinner) view.findViewById(R.id.country_spinner_search_request);
        ranksSpinner = (MaterialBetterSpinner) view.findViewById(R.id.players_rank_spinner_search_request);
        numberOfPlayersSpinner = (MaterialBetterSpinner) view.findViewById(R.id.players_number_spinner_search_request);
        searchRequestButton = (Button) view.findViewById(R.id.search_button_search_request);
        searchGameAutoCompleteTextView.setTypeface(playbold);
        ranksSpinner.setTypeface(playregular);
        numberOfPlayersSpinner.setTypeface(playregular);
        countrySpinner.setTypeface(playregular);
        matchTypeSpinner = (MaterialBetterSpinner) view.findViewById(R.id.match_type_spinner_search_request);
        matchTypeSpinner.setTypeface(playbold);
        matchTypeSpinner.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));


        pcLogoUnPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.pc_logo, 100, 100);
        pcLogoPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.pc_colorful_logo, 100, 100);

        psLogoUnPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.ps_logo, 100, 100);
        psLogoPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.ps_colorful_logo, 100, 100);

        xboxLogoUnPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.xbox_logo, 100, 100);
        xboxLogoPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.xbox_colorful_logo, 100, 100);


        setBitmapLogo(pcLogoUnPressed, psLogoUnPressed, xboxLogoUnPressed);


        // To remove the focus on the first edittext for the games
        searchRequestButton.requestFocus();

        psIsChosen = false;
        pcIsChosen = false;
        xboxIsChosen = false;
        currentPlatform="Nothing";

        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets(), "sansationbold.ttf");
        searchGameMessage.setTypeface(sansation);
//        pcMessage.setTypeface(sansation);
//        psMessage.setTypeface(sansation);
//        xboxMessage.setTypeface(sansation);
        searchRequestButton.setTypeface(sansation);



        gamesList = new ArrayList<>();
        regionList = new ArrayList<>();
        playerNumberList = new ArrayList<>();
        ranksList = new ArrayList<>();




        regionList.add("All");

        regionAdapter = new SpinnerAdapter(getContext(),
                R.layout.spinnner_item, regionList);


        playersNumberAdapter = new SpinnerAdapter(getContext(), R.layout.spinnner_item,
                playerNumberList);


        playersRanksAdapter = new SpinnerAdapter(getContext(), R.layout.spinnner_item,
                ranksList);

        ArrayAdapter matchTypeAdapter = new SpinnerAdapter(getContext(), R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.match_types)));



        playersRanksAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersNumberAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        matchTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        numberOfPlayersSpinner.setAdapter(playersNumberAdapter);
        ranksSpinner.setAdapter(playersRanksAdapter);
        countrySpinner.setAdapter(regionAdapter);
        searchGameAutoCompleteTextView.setThreshold(1);
        matchTypeSpinner.setAdapter(matchTypeAdapter);




//        // Load  user games
        App.gameAdapter.clear();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                App.loadGames();
                searchGameAutoCompleteTextView.setAdapter(App.gameAdapter);
            }
        }.start();




        searchRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gameName = "";
                String region="";
                int playersNumber=0;
                String rank="";
                String matchType="";


                // default values

                rank = "All Ranks";
                matchType = "All Matches";
//                if (selectedPlayersNumber.length()==0)
//                    selectedPlayersNumber="All Numbers";


                gameName = searchGameAutoCompleteTextView.getText().toString().trim();
                region= countrySpinner.getText().toString().trim();
                if (!numberOfPlayersSpinner.getText().toString().trim().isEmpty()) {
                    int number = numberOfPlayersSpinner.getText().toString().trim().equals("All Numbers") ? 0:Integer.parseInt(numberOfPlayersSpinner.getText().toString().trim());
                    playersNumber = number ;
                }

                if (!ranksSpinner.getText().toString().trim().isEmpty())
                    rank = ranksSpinner.getText().toString().trim();

                if (!matchTypeSpinner.getText().toString().trim().isEmpty())
                    matchType =matchTypeSpinner.getText().toString().trim();

                RequestModel requestModel;

                if(checkIsValidSearch()){
                    requestModel=new RequestModel(currentPlatform,gameName,region,playersNumber,matchType,rank,-1);
                    searchForRequest(requestModel);
                }

            }
        });


    }

    // Change platforms colors
    private void changePlatformColors() {
        // Coloring platform images after clicking it
        pcChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pcIsChosen == false) {
                    setBitmapLogo(pcLogoPressed, psLogoUnPressed, xboxLogoUnPressed);
                    //    pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.pc_color));
                    //    psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    //   xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));

                    pcIsChosen = true;
                    xboxIsChosen = false;
                    psIsChosen = false;
                    currentPlatform = "PC";
                }


            }
        });
        psChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (psIsChosen == false) {

                    setBitmapLogo(pcLogoUnPressed, psLogoPressed, xboxLogoUnPressed);

                    //  psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.ps_color));
                    // pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    //  xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    psIsChosen = true;
                    pcIsChosen = false;
                    xboxIsChosen = false;
                    currentPlatform = "PS";
                }
            }
        });

        xboxChoice.setClickable(false);
        xboxChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (xboxIsChosen == false) {
                    setBitmapLogo(pcLogoUnPressed, psLogoUnPressed, xboxLogoPressed);

                    //  xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.xbox_color));
                    // psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    // pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxIsChosen = true;
                    pcIsChosen = false;
                    psIsChosen = false;
                    currentPlatform = "XBOX";
                }

            }
        });


//
//        searchGame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//                    MainAppMenu.getInstance().getBottomBar().hide();
//                }
//                MainAppMenu.getInstance().getBottomBar().show();
//            }
//        });


    }


    public void slideInFromLeft(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated

        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        animation.setDuration(200);
        viewToAnimate.startAnimation(animation);
    }

    public void slideOutToRight(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
    }

    private void firledsListeners() {



        searchGameAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchGameAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    searchGameAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_unfocused_24dp, 0, 0, 0);
                }

                matchTypeSpinner.setVisibility(View.GONE);
                numberOfPlayersSpinner.setVisibility(View.GONE);

                // load game standards
                numberOfPlayersSpinner.setVisibility(View.VISIBLE);


                // To clear previous selections
                numberOfPlayersSpinner.setText("");
                ranksSpinner.setText("");


                // Load max players
                String gameKey = loadGameInformation(s.toString());



                if (checkIfCompetitive(gameKey)) {
                    matchTypeSpinner.setVisibility(View.VISIBLE);
                    slideInFromLeft(matchTypeSpinner);

                    // In case the user already selected a match type and he change the game
                    if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Competitive")) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp, 0, 0, 0);

                        if (ranksSpinner.getVisibility() != View.VISIBLE)
                            slideInFromLeft(ranksSpinner);

                        ranksSpinner.setVisibility(View.VISIBLE);
                    }
                    else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Quick Match")) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);

                        if (ranksSpinner.getVisibility() == View.VISIBLE)
                            slideOutToRight(ranksSpinner);

                        ranksSpinner.setVisibility(View.GONE);

                    }
                    else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("All Matches")){
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);

                        if (ranksSpinner.getVisibility() == View.VISIBLE)
                            slideOutToRight(ranksSpinner);

                        ranksSpinner.setVisibility(View.GONE);
                    }
                    if (matchTypeSpinner.length() == 0) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                    }

                } else {
                    if (matchTypeSpinner.isShown())
                        slideOutToRight(matchTypeSpinner);
                    slideOutToRight(ranksSpinner);
                    matchTypeSpinner.setVisibility(View.GONE);
                    ranksSpinner.setVisibility(View.GONE);

                }




            }
        });



        searchGameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Capitalize game name letters
                String gameName = searchGameAutoCompleteTextView.getText().toString();
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
                    searchGameAutoCompleteTextView.setText(capitlizedGameName);
                }else {
                    if (capitlizedGameName.equalsIgnoreCase("cs:go")){
                        searchGameAutoCompleteTextView.setText(capitlizedGameName.toUpperCase());
                    } else
                        searchGameAutoCompleteTextView.setText(capitlizedGameName);
                }



                String name = parent.getItemAtPosition(position).toString();



                // Load max players
                String gameKey = loadGameInformation(name);

                if (checkIfCompetitive(gameKey)) {
                    matchTypeSpinner.setVisibility(View.VISIBLE);
                    slideInFromLeft(matchTypeSpinner);

                    // In case the user already selected a match type and he change the game
                    if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Competitive")) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_competitive_24dp, 0, 0, 0);

                        if (ranksSpinner.getVisibility() != View.VISIBLE)
                            slideInFromLeft(ranksSpinner);

                        ranksSpinner.setVisibility(View.VISIBLE);
                    }
                    else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("Quick Match")) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);

                        if (ranksSpinner.getVisibility() == View.VISIBLE)
                            slideOutToRight(ranksSpinner);

                        ranksSpinner.setVisibility(View.GONE);

                    }
                    else if (matchTypeSpinner.getText().toString().equalsIgnoreCase("All Matches")){
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);

                        if (ranksSpinner.getVisibility() == View.VISIBLE)
                            slideOutToRight(ranksSpinner);

                        ranksSpinner.setVisibility(View.GONE);
                    }
                    if (matchTypeSpinner.length() == 0) {
                        matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                    }

                } else {
                    if (matchTypeSpinner.isShown())
                        slideOutToRight(matchTypeSpinner);
                    slideOutToRight(ranksSpinner);
                    matchTypeSpinner.setVisibility(View.GONE);
                    ranksSpinner.setVisibility(View.GONE);

                }


                // Next Focus
                matchTypeSpinner.requestFocus();


            }
        });



        countrySpinner.addTextChangedListener(new TextWatcher() {

            final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");

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
                countrySpinner.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                countrySpinner.setTypeface(playbold);

//                // NEXT FOCUS
//                numberOfPlayersSpinner.requestFocus();
            }
        });

        numberOfPlayersSpinner.addTextChangedListener(new TextWatcher() {
            final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");

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
                numberOfPlayersSpinner.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                numberOfPlayersSpinner.setTypeface(playbold);




            }
        });

        ranksSpinner.addTextChangedListener(new TextWatcher() {
            final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ranksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_focused_24dp, 0, 0, 0);
                if (s.length() == 0) {
                    ranksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_unfocused_24dp, 0, 0, 0);
                }
                ranksSpinner.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                ranksSpinner.setTypeface(playbold);

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

                    if (ranksSpinner.getVisibility() != View.VISIBLE)
                        slideInFromLeft(ranksSpinner);

                    ranksSpinner.setVisibility(View.VISIBLE);
                }
                if (s.toString().equalsIgnoreCase("Quick Match")) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);

                    if (ranksSpinner.getVisibility() == View.VISIBLE)
                        slideOutToRight(ranksSpinner);

                    ranksSpinner.setVisibility(View.GONE);

                }
                if (s.toString().equalsIgnoreCase("All Matches")){
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);

                    if (ranksSpinner.getVisibility() == View.VISIBLE)
                        slideOutToRight(ranksSpinner);

                    ranksSpinner.setVisibility(View.GONE);
                }
                if (s.length() == 0) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                }
            }
        });

    }



    private boolean checkIsValidSearch() {

        // check the platform
        if (currentPlatform.equalsIgnoreCase("Nothing")) {

            Toast.makeText(getContext(), R.string.search_request_fragment_platform_error, Toast.LENGTH_LONG).show();
            return false;
        }

        // check if the game field is empty
        if (searchGameAutoCompleteTextView.getText().toString().length() == 0) {
            Toast.makeText(getContext(), R.string.search_request_fragment_select_game_error, Toast.LENGTH_LONG).show();
            return false;
        }

        // Check selected game is in the user games
        if (!userHasTheGame(searchGameAutoCompleteTextView.getText().toString().trim())) {

            Toast.makeText(getContext(), R.string.search_request_fragment_no_game_error, Toast.LENGTH_LONG).show();
            return false;
        }

        // check region
        if (countrySpinner.getText().length()==0){
            Toast.makeText(getContext(), R.string.search_request_region_error, Toast.LENGTH_LONG).show();
            return false ;
        }


        return true;
    }



    protected boolean checkIfCompetitive(String gameKey) {
        return app.getGameManager().isCompetitive(gameKey);
    }

    // this method check if the user has the game if yes , load game properties
    private boolean userHasTheGame(String selectedGame) {
        return app.getGameManager().getGameByName(selectedGame) !=null;
    }

    protected  void goToResultLayout()
    {
        Intent i = new Intent(getContext(),SearchResultsCore.class);
        startActivity(i);
    }



    protected abstract void OnStartActivity();

    protected abstract void searchForRequest(RequestModel requestModel);
}
