package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.Cores.AuthenticationCore.SignUpCore;
import com.example.kay.hoplay.Cores.RequestCore.NewRequestCore;
import com.example.kay.hoplay.Models.GameModel;
import com.example.kay.hoplay.Models.Rank;
import com.example.kay.hoplay.Models.RequestModel;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.util.BitmapOptimizer;
import com.google.firebase.database.ServerValue;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class SearchRequests extends Fragment {

    protected App app;

    protected ImageView pcChoice;
    protected ImageView psChoice;
    protected ImageView xboxChoice;

    protected TextView searchGameMessage;
    protected TextView pcMessage;
    protected TextView psMessage;
    protected TextView xboxMessage;
    protected AutoCompleteTextView searchGameAutoCompleteTextView;

    private int layoutItemId;




    protected ArrayList<String> gamesList;
    protected ArrayList<String> regionList;
    protected ArrayList<String> playerNumberList;
    protected ArrayList<String> ranksList;

    protected ArrayAdapter<String> gameAdapter;
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




        // Load  user games
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                ArrayList<GameModel> games = app.getGameManager().getAllGamesArrayList();
                for (GameModel gameModel : games)
                {
                    gamesList.add(gameModel.getGameName());
                    Log.i("done",gameModel.getGameName());

                }
            }
        }.start();




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


        GameModel gameModel = app.getGameManager().getGameByName(selectedGame);

        for (int i = 1; i <= gameModel.getMaxPlayers(); i++) {
            playerNumberList.add(Integer.toString(i));

        }

        for (Rank rank : gameModel.getGameRanks().getRanksList()) {
            ranksList.add(rank.getRankName());
        }



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



        gameAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, gamesList);

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
        searchGameAutoCompleteTextView.setAdapter(gameAdapter);
        matchTypeSpinner.setAdapter(matchTypeAdapter);

        searchRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gameName = searchGameAutoCompleteTextView.getText().toString().trim();
                String region  = countrySpinner.getText().toString().trim();
                String playersNumber = numberOfPlayersSpinner.getText().toString().trim();
                String rank = ranksSpinner.getText().toString().trim();
                String matchtype=matchTypeSpinner.getText().toString().trim();



                if(currentPlatform == null || gameName.isEmpty()  || region.isEmpty() ||  playersNumber.isEmpty() || rank.isEmpty()) {
                    Toast.makeText(getContext(),"Check Fields",Toast.LENGTH_LONG).show();
                    return;
                }


                RequestModel requestModel =new RequestModel(currentPlatform,gameName,region,Integer.parseInt(playersNumber),matchtype,rank,-1);
                searchForRequest(requestModel);

                Intent i = new Intent(getContext(),SearchResults.class);
                startActivity(i);

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
            }
        });



        searchGameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();


                // Load max players
                String gameKey = loadGameInformation(name);


                if (checkIfCompetitive(gameKey)) {
                    matchTypeSpinner.setVisibility(View.VISIBLE);
                    ranksSpinner.setVisibility(View.VISIBLE);
                    slideInFromLeft(matchTypeSpinner);
                    slideInFromLeft(ranksSpinner);

                } else {
                    if (matchTypeSpinner.isShown())
                        slideOutToRight(matchTypeSpinner);
                    slideOutToRight(ranksSpinner);

                    matchTypeSpinner.setVisibility(View.GONE);
                    ranksSpinner.setVisibility(View.GONE);

                }

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
                }
                if (s.toString().equalsIgnoreCase("Quick Match")) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_quick_match_24dp, 0, 0, 0);
                }
                if (s.length() == 0) {
                    matchTypeSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whatshot_unfocused_24dp, 0, 0, 0);
                }
            }
        });

    }

    protected boolean checkIfCompetitive(String gameKey) {
        return app.getGameManager().isCompetitive(gameKey);
    }


    protected abstract void OnStartActivity();

    protected abstract void searchForRequest(RequestModel requestModel);
}
