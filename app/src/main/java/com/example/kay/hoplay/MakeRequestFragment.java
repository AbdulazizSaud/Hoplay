package com.example.kay.hoplay;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MakeRequestFragment extends Fragment {


    private  TextView makeRequestMessage ;
    private Button makeRequestButton ;
    private Spinner regionSpinner ;
    private Spinner numberOfPlayersSpinner ;
    private Spinner playersRanksSpinner;
    private RadioButton pcRadiobutton ;
    private RadioButton psRadiobutton;
    private RadioButton xboxRadiobutton;

    public MakeRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_make_request_fragment, container, false);
        Typeface sansationbold = Typeface.createFromAsset(getResources().getAssets(), "sansationbold.ttf");
        makeRequestMessage = (TextView) view.findViewById(R.id.make_request_message_textview);
        makeRequestMessage.setTypeface(sansationbold);
        makeRequestButton = (Button) view.findViewById(R.id.make_request_button);
        makeRequestButton.setTypeface(sansationbold);
        regionSpinner = (Spinner) view.findViewById(R.id.region_spinner);
        numberOfPlayersSpinner = (Spinner) view.findViewById(R.id.number_of_players_spinner);
        playersRanksSpinner = (Spinner) view.findViewById(R.id.players_rank_spinner);
        pcRadiobutton = (RadioButton) view.findViewById(R.id.pc_choice_radiobutton);
        pcRadiobutton.setTypeface(sansationbold);
        psRadiobutton = (RadioButton) view.findViewById(R.id.ps_choice_radiobutton);
        psRadiobutton.setTypeface(sansationbold);
        xboxRadiobutton = (RadioButton) view.findViewById(R.id.xbox_choice_radiobutton);
        xboxRadiobutton.setTypeface(sansationbold);


      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinnner_item, areay of values) {

            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);

                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };
        */

        ArrayAdapter regionAdapter = ArrayAdapter.createFromResource(getContext() ,R.array.countries_array,R.layout.spinnner_item);
        ArrayAdapter playersRanksAdapter = ArrayAdapter.createFromResource(getContext() ,R.array.players_ranks,R.layout.spinnner_item);
        ArrayAdapter playersNumberAdapter = ArrayAdapter.createFromResource(getContext() ,R.array.players_number,R.layout.spinnner_item);

        playersRanksAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        playersNumberAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        numberOfPlayersSpinner.setAdapter(playersNumberAdapter);
        regionSpinner.setAdapter(regionAdapter);
        playersRanksSpinner.setAdapter(playersRanksAdapter);

        return view;
    }

}
