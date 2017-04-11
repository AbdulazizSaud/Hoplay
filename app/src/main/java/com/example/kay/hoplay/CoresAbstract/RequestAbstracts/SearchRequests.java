package com.example.kay.hoplay.CoresAbstract.RequestAbstracts;


import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.util.BitmapOptimizer;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class SearchRequests extends Fragment {

    protected ImageView pcChoice;
    protected ImageView psChoice;
    protected ImageView xboxChoice;

    protected TextView searchGameMessage ;
    protected TextView pcMessage ;
    protected TextView psMessage ;
    protected TextView xboxMessage ;
    protected AutoCompleteTextView searchGame ;
    private  String[] gamesArray;
    private  int layoutItemId;
    private List<String> gamesList;
    private  ArrayAdapter<String> gamesAdapter;

    protected MaterialBetterSpinner countrySpinner;
    protected MaterialBetterSpinner ranksSpinner ;
    protected MaterialBetterSpinner numberOfPlayersSpinner ;

    protected Button searchRequestButton;

    private BitmapOptimizer bitmapOptimizer;

    boolean pcIsChosen , psIsChosen , xboxIsChosen ;

    private Bitmap pcLogoUnPressed, pcLogoPressed,psLogoUnPressed, psLogoPressed,xboxLogoUnPressed, xboxLogoPressed;


    public SearchRequests() {
        // Required empty public constructor
        bitmapOptimizer = new BitmapOptimizer();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_requests_search, container, false);
        initControls(view);
        changePlatformColors();
        changeInputsIcons();



        return view;
    }



    private void setBitmapLogo(Bitmap pcLogo,Bitmap psLogo,Bitmap ps4Logo)
    {
        pcChoice.setImageBitmap(pcLogo);
        psChoice.setImageBitmap(psLogo);
        xboxChoice.setImageBitmap(ps4Logo);

    }
    protected abstract void searchForRequest();


    // Init all controls
    private void initControls(View view)
    {

        final Typeface playregular = Typeface.createFromAsset(getResources().getAssets(), "playregular.ttf");
        final Typeface playbold = Typeface.createFromAsset(getResources().getAssets(), "playbold.ttf");
        pcChoice = (ImageView) view.findViewById(R.id.pc_choice_imageview);
        psChoice = (ImageView) view.findViewById(R.id.ps_choice_imageview);
        xboxChoice = (ImageView) view.findViewById(R.id.xbox_choice_imageview);

        gamesArray = getResources().getStringArray(R.array.games_list);
        layoutItemId = android.R.layout.simple_dropdown_item_1line;
        gamesArray = getResources().getStringArray(R.array.games_list);
        gamesList = Arrays.asList(gamesArray);
        gamesAdapter = new ArrayAdapter<String>(getContext(), layoutItemId, gamesList);
//        pcMessage = (TextView) view.findViewById(R.id.pc_requests_message_textview);
//        psMessage = (TextView) view.findViewById(R.id.ps_requests_message_textview);
//        xboxMessage = (TextView) view.findViewById(R.id.xbox_requests_message_textview);

        searchGame = (AutoCompleteTextView) view.findViewById(R.id.games_autocompletetextview_search_request);
        searchGame.setAdapter(gamesAdapter);

        searchGameMessage = (TextView) view.findViewById(R.id.search_game_message_textview);
        countrySpinner = (MaterialBetterSpinner) view.findViewById(R.id.country_spinner_search_request);
        ranksSpinner=(MaterialBetterSpinner) view.findViewById(R.id.players_rank_spinner_search_request);
        numberOfPlayersSpinner = (MaterialBetterSpinner) view.findViewById(R.id.players_number_spinner_search_request);
        searchRequestButton = (Button) view.findViewById(R.id.search_button_search_request);
        searchGame.setTypeface(playbold);
        ranksSpinner.setTypeface(playregular);
        numberOfPlayersSpinner.setTypeface(playregular);
        countrySpinner.setTypeface(playregular);




        pcLogoUnPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.pc_logo, 100, 100);
        pcLogoPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.pc_colorful_logo, 100, 100);

        psLogoUnPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.ps_logo, 100, 100);
        psLogoPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.ps_colorful_logo, 100, 100);

        xboxLogoUnPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.xbox_logo, 100, 100);
        xboxLogoPressed = bitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.xbox_colorful_logo, 100, 100);


        setBitmapLogo(pcLogoUnPressed,psLogoUnPressed,xboxLogoUnPressed);



        // To remove the focus on the first edittext for the games
        searchRequestButton.requestFocus();

        psIsChosen= false ;
        pcIsChosen=false ;
        xboxIsChosen = false ;

        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets() ,"sansationbold.ttf");
        searchGameMessage.setTypeface(sansation);
//        pcMessage.setTypeface(sansation);
//        psMessage.setTypeface(sansation);
//        xboxMessage.setTypeface(sansation);
        searchRequestButton.setTypeface(sansation);



        ArrayAdapter regionAdapter = new  SpinnerAdapter(getContext() ,R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.countries_array)));

        ArrayAdapter playersRanksAdapter = new  SpinnerAdapter(getContext(),R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.players_ranks)));

        ArrayAdapter playersNumberAdapter = new SpinnerAdapter(getContext(),R.layout.spinnner_item,
                Arrays.asList(getResources().getStringArray(R.array.players_number)));


        playersRanksAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersNumberAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        numberOfPlayersSpinner.setAdapter(playersNumberAdapter);
        ranksSpinner.setAdapter(playersRanksAdapter);
        countrySpinner.setAdapter(regionAdapter);






    }

    // Change platforms colors
    private void changePlatformColors()
    {
        // Coloring platform images after clicking it
        pcChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pcIsChosen==false)
                {
                    setBitmapLogo(pcLogoPressed,psLogoUnPressed,xboxLogoUnPressed);
                    //    pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.pc_color));
                    //    psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    //   xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));

                    pcIsChosen = true;
                    xboxIsChosen= false;
                    psIsChosen = false;
                }


            }
        });
        psChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(psIsChosen==false)
                {

                    setBitmapLogo(pcLogoUnPressed,psLogoPressed,xboxLogoUnPressed);

                    //  psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.ps_color));
                    // pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    //  xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    psIsChosen=true;
                    pcIsChosen=false ;
                    xboxIsChosen=false ;
                }
            }
        });

        xboxChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(xboxIsChosen==false)
                {
                    setBitmapLogo(pcLogoUnPressed,psLogoUnPressed,xboxLogoPressed);

                    //                    xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.xbox_color));
                    // psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    // pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxIsChosen=true;
                    pcIsChosen=false;
                    psIsChosen=false;
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

    private void changeInputsIcons()
    {


        searchGame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_focused_24dp , 0, 0, 0);
                if(s.length() == 0)
                {
                    searchGame.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_games_unfocused_24dp , 0, 0, 0);
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

                countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_focused_24dp , 0, 0, 0);
                if(s.length()==0)
                {
                    countrySpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_unfocused_24dp , 0, 0, 0);

                }
                countrySpinner.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color));
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

                numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_focused_24dp , 0, 0, 0);
                if(s.length() == 0)
                {
                    numberOfPlayersSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_unfocused_24dp , 0, 0, 0);

                }
                numberOfPlayersSpinner.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color));
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

                ranksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_focused_24dp , 0, 0, 0);
                if(s.length()==0)
                {
                    ranksSpinner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_grade_unfocused_24dp , 0, 0, 0);
                }
                ranksSpinner.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color));
                ranksSpinner.setTypeface(playbold);

            }
        });



    }


}
