package com.example.kay.hoplay.RequestComponents;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kay.hoplay.Activities.MainMenu.MainAppMenu;
import com.example.kay.hoplay.Adapters.SpinnerAdapter;
import com.example.kay.hoplay.R;
import com.example.kay.hoplay.model.SavedRequestsList;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class SearchRequestsFragment extends Fragment {

    protected ImageView pcChoice;
    protected ImageView psChoice;
    protected ImageView xboxChoice;

    protected TextView searchGameMessage ;
    protected TextView pcMessage ;
    protected TextView psMessage ;
    protected TextView xboxMessage ;
    protected EditText searchGame ;

    protected Spinner regionsSpinner;
    protected Spinner ranksSpinner ;
    protected Spinner numberOfPlayersSpinner ;

    protected Button searchRequestButton;


    boolean pcIsChosen , psIsChosen , xboxIsChosen ;



    public SearchRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_requests_search, container, false);
        pcChoice = (ImageView) view.findViewById(R.id.pc_choice_imageview);
        psChoice = (ImageView) view.findViewById(R.id.ps_choice_imageview);
        xboxChoice = (ImageView) view.findViewById(R.id.xbox_choice_imageview);
        pcMessage = (TextView) view.findViewById(R.id.pc_requests_message_textview);
        psMessage = (TextView) view.findViewById(R.id.ps_requests_message_textview);
        xboxMessage = (TextView) view.findViewById(R.id.xbox_requests_message_textview);
        searchGameMessage = (TextView) view.findViewById(R.id.search_game_message_textview);
        searchGame = (EditText) view.findViewById(R.id.games_search_edittext);
        regionsSpinner = (Spinner) view.findViewById(R.id.region_search_spinner);
        ranksSpinner=(Spinner) view.findViewById(R.id.players_rank_search_spinner);
        numberOfPlayersSpinner = (Spinner) view.findViewById(R.id.number_of_players_search_spinner);
        searchRequestButton = (Button) view.findViewById(R.id.make_request_button);


        // To remove the focus on the first edittext for the games
        searchRequestButton.requestFocus();

        psIsChosen= false ;
        pcIsChosen=false ;
        xboxIsChosen = false ;

        Typeface sansation = Typeface.createFromAsset(getActivity().getAssets() ,"sansationbold.ttf");
        searchGameMessage.setTypeface(sansation);
        pcMessage.setTypeface(sansation);
        psMessage.setTypeface(sansation);
        xboxMessage.setTypeface(sansation);
        searchGame.setTypeface(sansation);





        pcChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pcIsChosen==false)
                {
                    pcChoice.setImageResource(R.drawable.pc_colorful_logo);
                    pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.pc_color));
                    psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxChoice.setImageResource(R.drawable.xbox_logo);
                    psChoice.setImageResource(R.drawable.ps_logo);
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
                    psChoice.setImageResource(R.drawable.ps_colorful_logo);
                    psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.ps_color));
                    pcChoice.setImageResource(R.drawable.pc_logo);
                    pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxChoice.setImageResource(R.drawable.xbox_logo);
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
                    xboxChoice.setImageResource(R.drawable.xbox_colorful_logo);
                    xboxMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.xbox_color));
                    pcChoice.setImageResource(R.drawable.pc_logo);
                    psChoice.setImageResource(R.drawable.ps_logo);
                    psMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    pcMessage.setTextColor(ContextCompat.getColor(getContext(),R.color.invisible_request_color));
                    xboxIsChosen=true;
                    pcIsChosen=false;
                    psIsChosen=false;
                }

            }
        });



        searchGame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    MainAppMenu.getBottomBar().hide();
                }
                MainAppMenu.getBottomBar().show();
            }
        });


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
        regionsSpinner.setAdapter(regionAdapter);

        return view;
    }


    protected abstract void searchForRequest();


}
